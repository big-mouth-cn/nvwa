package org.bigmouth.nvwa.dpl.plugin;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.dpl.GenericLifeCycle;
import org.bigmouth.nvwa.dpl.event.listener.PlugInListener;
import org.bigmouth.nvwa.dpl.service.MutableService;
import org.bigmouth.nvwa.dpl.service.Service;
import org.bigmouth.nvwa.dpl.service.ServiceConfig;
import org.bigmouth.nvwa.dpl.status.ImmutableStatus;
import org.bigmouth.nvwa.dpl.status.Status;
import org.bigmouth.nvwa.dpl.status.StatusSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GenericPlugIn extends GenericLifeCycle<PlugInConfig, PlugInListener> implements
		MutablePlugIn {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericPlugIn.class);

	private final Map<String, Service> services = new ConcurrentHashMap<String, Service>();
	private volatile ClassLoader classloader;
	private volatile String installPath;

	public GenericPlugIn() {
		super();
	}

	public GenericPlugIn(PlugInConfig config, Status status) {
		super(config, status);
	}

	public GenericPlugIn(PlugInConfig config, Status status, List<PlugInListener> listeners) {
		super(config, status, listeners);
	}

	@Override
	public void init() {
		for (Entry<String, Service> e : services.entrySet()) {
			Service s = e.getValue();
			s.init();
		}
		super.init();
	}

	@Override
	public void destroy() {
		for (Entry<String, Service> e : services.entrySet()) {
			Service s = e.getValue();
			s.destroy();
		}
		super.destroy();
	}

	@Override
	public Iterator<Service> getAllServices() {
		return services.values().iterator();
	}

	@Override
	public Service lookupService(String serviceKey) {
		if (StringUtils.isBlank(serviceKey)) {
			LOGGER.error("serviceKey is null or empty.");
			return null;
		}

		Service service = services.get(serviceKey);
		if (null == service)
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Service is not exists handle key [" + serviceKey + "]");

		return service;
	}

	@Override
	public long getLastModifiedTime() {
		return getStatus().getLastModifiedTime();
	}

	@Override
	public boolean isRunning() {
		return getStatus().isRunning();
	}

	@Override
	public void setRunning(boolean running) {
		Status status = new ImmutableStatus(running);
		setStatus(status);

		// set all service status
		for (Iterator<Service> it = getAllServices(); it.hasNext();) {
			MutableService service = (MutableService) it.next();
			service.setRunning(running);
		}
	}

	@Override
	public String getStatusKey() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("status");
		sb.append(StatusSource.SPLIT);
		sb.append("plugin");
		sb.append(StatusSource.SPLIT);
		sb.append(getKey());
		return sb.toString();
	}

	@Override
	public String getDesc() {
		return getConfig().getDesc();
	}

	@Override
	public String getKey() {
		return getConfig().getKey();
	}

	@Override
	public String getName() {
		return getConfig().getName();
	}

	@Override
	public Iterator<ServiceConfig> getServiceConfigs() {
		// TODO Auto-generated method stub
		// TODO:
		return null;
	}

	@Override
	public String getInstallPath() {
		return this.installPath;
	}

	@Override
	public ClassLoader getClassLoader() {
		return classloader;
	}

	@Override
	public void setClassLoader(ClassLoader classloader) {
		if (null == classloader)
			throw new NullPointerException("classloader");
		this.classloader = classloader;
		setClassLoaderOfAllServices(classloader);
	}

	private void setClassLoaderOfAllServices(ClassLoader classloader) {
		for (Entry<String, Service> e : this.services.entrySet()) {
			e.getValue().setClassLoader(classloader);
		}
	}

	@Override
	public void setInstallPath(String installPath) {
		if (StringUtils.isBlank(installPath))
			throw new IllegalArgumentException("installPath is blank.");
		this.installPath = installPath;
	}

	@Override
	public void addService(Service service) {
		if (null == service)
			throw new NullPointerException("service");
		this.services.put(service.getKey(), service);
	}

	@Override
	public void clearServices() {
		this.services.clear();
	}

	@Override
	public void setServices(List<Service> services) {
		if (null == services || 0 == services.size())
			throw new IllegalArgumentException("services is blank.");
		for (Service s : services) {
			this.services.put(s.getKey(), s);
		}
	}
}
