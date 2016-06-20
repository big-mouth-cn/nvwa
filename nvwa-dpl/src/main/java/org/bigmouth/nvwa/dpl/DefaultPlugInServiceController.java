package org.bigmouth.nvwa.dpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.dpl.factory.PlugInDiscover;
import org.bigmouth.nvwa.dpl.hotswap.DirChangeEvent;
import org.bigmouth.nvwa.dpl.hotswap.DirChangeListener;
import org.bigmouth.nvwa.dpl.hotswap.PlugInClassLoader;
import org.bigmouth.nvwa.dpl.hotswap.PlugInDirMonitor;
import org.bigmouth.nvwa.dpl.plugin.MutablePlugIn;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.bigmouth.nvwa.dpl.service.MutableService;
import org.bigmouth.nvwa.dpl.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DefaultPlugInServiceController implements PlugInServiceController, DirChangeListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultPlugInServiceController.class);

	private static final String CONTROL_THREAD_NAME = "DPL-Controller-thread";
	private static final ThreadFactory threadFactory = new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, CONTROL_THREAD_NAME);
		}
	};

	private final Map<String, String> filePathAndCodeMap = Maps.newHashMap();
	private final PlugInServiceBus bus;
	private final String plugInDir;
	private final List<PlugInDiscover> plugInDiscovers = Lists.newArrayList();

	private final ExecutorService controlExecutor = Executors
			.newSingleThreadExecutor(threadFactory);

	private PlugInDirMonitor plugInDirMonitor;

	public DefaultPlugInServiceController(PlugInServiceBus bus, String plugInDir,
			List<PlugInDiscover> plugInDiscovers) {
		if (null == bus)
			throw new NullPointerException("bus");
		if (null == plugInDiscovers || 0 == plugInDiscovers.size())
			throw new IllegalArgumentException("plugInDiscovers is blank.");
		this.bus = bus;
		this.plugInDir = plugInDir;
		this.plugInDiscovers.addAll(plugInDiscovers);
	}

	public void start() {
		plugInDirMonitor = new PlugInDirMonitor(plugInDir, this);
		plugInDirMonitor.start();
	}

	public void destroy() {
		if (null != plugInDirMonitor)
			plugInDirMonitor.destroy();
	}

	@Override
	public void onDirChanged(DirChangeEvent event) {
		List<String> addedFiles = event.getAddedFiles();
		List<String> updatedFiles = event.getUpdatedFiles();
		List<String> removedFiles = event.getRemovedFiles();

		if (0 < addedFiles.size()) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("plugin file[{}] has added.", addedFiles);
			for (String f : addedFiles) {
				addPlugIn(f);
			}
		}
		if (0 < updatedFiles.size()) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("plugin file[{}] has updated.", updatedFiles);
			for (String f : updatedFiles) {
				String plugInCode = filePathAndCodeMap.get(f);
				if (StringUtils.isBlank(plugInCode)) {
					// TODO:LOGGER
					if (LOGGER.isDebugEnabled())
						LOGGER
								.debug("plugin file[{}] load has failed previously,add plugin directly.");

					addPlugIn(f);
					continue;
				}
				updatePlugIn(f, plugInCode);
			}
		}
		if (0 < removedFiles.size()) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("plugin file[{}] has removed.", removedFiles);
			for (String f : removedFiles) {
				String plugInCode = filePathAndCodeMap.get(f);
				if (StringUtils.isBlank(plugInCode)) {
					// TODO:LOGGER
					continue;
				}
				removePlugIn(plugInCode);
			}
		}
	}

	private void removePlugIn(String plugInCode) {
		uninstallPlugIn(plugInCode);
		filePathAndCodeMap.remove(plugInCode);
	}

	private void updatePlugIn(String f, String plugInCode) {
		uninstallPlugIn(plugInCode);
		installPlugIn(f);
	}

	private void addPlugIn(String f) {
		String plugInCode = installPlugIn(f);
		if (StringUtils.isBlank(plugInCode))
		    return;
		filePathAndCodeMap.put(f, plugInCode);
	}

	private String installPlugIn(final String plugInPath) {
		if (null == plugInPath)
			throw new NullPointerException("plugInPath");

		Future<String> f = controlExecutor.submit(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return doInstallPlugIn(plugInPath);
			}
		});
		try {
			return f.get();
		} catch (InterruptedException e) {
			f.cancel(true);
			throw new RuntimeException("installPlugIn:", e);
		} catch (ExecutionException e) {
			f.cancel(true);
			Throwable cause = e.getCause();
			throw launderThrowable("installPlugIn[" + plugInPath + "]:", cause);
		}
	}

	private RuntimeException launderThrowable(String message, Throwable t) {
		if (t instanceof RuntimeException) {
			// return (RuntimeException) t;
			return new RuntimeException(message, t);
		} else if (t instanceof Error) {
			throw (Error) t;
		} else {
			throw new RuntimeException(message, t);
		}
	}

	private String doInstallPlugIn(String plugInPath) {
		PlugIn pg = createPlugIn(plugInPath);
		if (null == pg)
		    return null;
        MutablePlugIn plugIn = (MutablePlugIn) pg;
		plugIn.setInstallPath(plugInPath);
		plugIn.init();

		doRegisterPlugIn(plugIn);
		return plugIn.getConfig().getKey();
	}

	private PlugIn createPlugIn(String plugInPath) throws RuntimeException {

		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			// TODO:
			PlugInClassLoader pcl = new PlugInClassLoader(plugInPath, currentClassLoader);
			Thread.currentThread().setContextClassLoader(pcl);

			PlugIn plugIn = null;

			for (PlugInDiscover discover : getPlugInDiscovers()) {
			    try {
			        plugIn = discover.discover(pcl);
			    } catch (Exception e) {
			        LOGGER.error("discover:", e);
			    }
				if (null != plugIn)
					break;
			}
			if (null == plugIn) {
//				throw new RuntimeException("PlugIn format error.");
			    if (LOGGER.isErrorEnabled()) {
			        LOGGER.error("PlugIn[" + pcl.getPlugInJarPath() + "] format error.");
			    }
			    return null;
			}
			plugIn.setClassLoader(pcl);

			return plugIn;
		} finally {
			Thread.currentThread().setContextClassLoader(currentClassLoader);
		}
	}

	private List<PlugInDiscover> getPlugInDiscovers() {
		return this.plugInDiscovers;
	}

	private void doRegisterPlugIn(PlugIn plugIn) {
		bus.register(plugIn);
	}

	private void uninstallPlugIn(final String plugInCode) {
		if (StringUtils.isBlank(plugInCode))
			throw new IllegalArgumentException("PlugInCode is blank.");

		controlExecutor.execute(new Runnable() {

			@Override
			public void run() {
				doUninstallPlugin(plugInCode);
			}
		});
	}

	private void doUninstallPlugin(String plugInCode) {
		PlugIn plugIn = bus.lookupPlugIn(plugInCode);

		if (null == plugIn) {
			LOGGER.error("Code[" + plugInCode + "] is not mapping any plugIn.");
			return;
		}

		bus.unregister(plugInCode);
		plugIn.destroy();
	}

	@Override
	public void activePlugIn(final String plugInKey) {
		Future<?> f = controlExecutor.submit(new Runnable() {

			@Override
			public void run() {
				updatePlugInRunningStatus(plugInKey, true);
			}
		});
		try {
			f.get();
		} catch (InterruptedException e) {
			f.cancel(true);
			throw new RuntimeException("activePlugIn:", e);
		} catch (ExecutionException e) {
			f.cancel(true);
			Throwable cause = e.getCause();
			throw launderThrowable("activePlugIn:", cause);
		}
	}

	private void updatePlugInRunningStatus(String plugInCode, boolean isRunning) {
		if (StringUtils.isBlank(plugInCode))
			throw new IllegalArgumentException("PlugInCode is blank.");

		MutablePlugIn plugIn = (MutablePlugIn) bus.lookupPlugIn(plugInCode);
		if (null == plugIn) {
			LOGGER.error("Code[" + plugInCode + "] is not mapping any plugIn.");
			return;
		}

		plugIn.setRunning(isRunning);
	}

	@Override
	public void activeService(final String plugInKey, final String serviceKey) {
		Future<?> f = controlExecutor.submit(new Runnable() {

			@Override
			public void run() {
				updateServiceRunningStatus(plugInKey, serviceKey, true);
			}
		});

		try {
			f.get();
		} catch (InterruptedException e) {
			f.cancel(true);
			throw new RuntimeException("activeService:", e);
		} catch (ExecutionException e) {
			f.cancel(true);
			Throwable cause = e.getCause();
			throw launderThrowable("activeService:", cause);
		}
	}

	private void updateServiceRunningStatus(String plugInCode, String serviceCode, boolean isRunning) {
		if (StringUtils.isBlank(plugInCode) || StringUtils.isBlank(serviceCode))
			throw new IllegalArgumentException("plugInCode is blank or serviceCode is blank.");

		PlugIn plugIn = bus.lookupPlugIn(plugInCode);
		if (null == plugIn) {
			LOGGER.error("Code[" + plugInCode + "] is not mapping any plugIn,ignore.");
			return;
		}

		MutableService service = (MutableService) plugIn.lookupService(serviceCode);
		if (null == service) {
			LOGGER.error("Code[" + service + "] is not mapping any Service,ignore.");
			return;
		}

		service.setRunning(isRunning);
	}

	@Override
	public void deActivePlugIn(final String plugInKey) {

		Future<?> f = controlExecutor.submit(new Runnable() {

			@Override
			public void run() {
				updatePlugInRunningStatus(plugInKey, false);
			}
		});

		try {
			f.get();
		} catch (InterruptedException e) {
			f.cancel(true);
			throw new RuntimeException("deActivePlugIn:", e);
		} catch (ExecutionException e) {
			f.cancel(true);
			Throwable cause = e.getCause();
			throw launderThrowable("deActivePlugIn:", cause);
		}
	}

	@Override
	public void deActiveService(final String plugInKey, final String serviceKey) {

		Future<?> f = controlExecutor.submit(new Runnable() {

			@Override
			public void run() {
				updateServiceRunningStatus(plugInKey, serviceKey, false);
			}
		});

		try {
			f.get();
		} catch (InterruptedException e) {
			f.cancel(true);
			throw new RuntimeException("deActiveService:", e);
		} catch (ExecutionException e) {
			f.cancel(true);
			Throwable cause = e.getCause();
			throw launderThrowable("deActiveService:", cause);
		}
	}

	@Override
	public List<String> getAllPlugInsDesc() {
		Future<List<String>> f = controlExecutor.submit(new Callable<List<String>>() {

			@Override
			public List<String> call() throws Exception {
				List<String> result = new ArrayList<String>();
				for (Iterator<PlugIn> it = bus.getAllPlugIns(); it.hasNext();) {
					PlugIn plugIn = it.next();
					String plugInCode = plugIn.getConfig().getKey();
					for (Iterator<Service> sit = plugIn.getAllServices(); sit.hasNext();) {
						Service service = sit.next();
						String serviceCode = service.getConfig().getKey();

						StringBuilder sb = new StringBuilder(128);
						sb.append(plugInCode).append("/").append(serviceCode);
						sb.append(",");

						sb.append("isRunning:");
						sb.append(service.getStatus().isRunning());
						// sb.append(",");

						result.add(sb.toString());
					}
				}
				return result;
			}
		});

		try {
			return f.get();
		} catch (InterruptedException e) {
			f.cancel(true);
			throw new RuntimeException("getAllPlugInsDesc:", e);
		} catch (ExecutionException e) {
			f.cancel(true);
			Throwable cause = e.getCause();
			throw launderThrowable("getAllPlugInsDesc:", cause);
		}
	}
}
