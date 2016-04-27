package org.bigmouth.nvwa.dpl.hotswap;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PlugInDirMonitor {
	private static final Logger LOGGER = LoggerFactory.getLogger(PlugInDirMonitor.class);
	private static final long DEFAULT_CHECK_TIMEOUT = 1000L;

	private final DirChangeListener listener;
	private final String dirName;
	private final long checkTimeout;

	private final ScheduledExecutorService executorService = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "PlugInDirMonitor-thread");
				}
			});
	private Snapshot lastSnapshot = null;

	public PlugInDirMonitor(String dir) {
		this(dir, DirChangeListener.ONLY_LOGGING, DEFAULT_CHECK_TIMEOUT);
	}

	public PlugInDirMonitor(String dir, DirChangeListener listener) {
		this(dir, listener, DEFAULT_CHECK_TIMEOUT);
	}

	public PlugInDirMonitor(String dir, DirChangeListener listener, long checkTimeout) {
		if (StringUtils.isBlank(dir))
			throw new IllegalArgumentException("dir is blank.");
		File directory = new File(dir);
        if (!directory.exists()) {
            if (! directory.mkdirs()) {
                throw new IllegalArgumentException("dir[" + dir + "] is not exists.");
            }
        }
		if (null == listener)
			throw new NullPointerException("listener");
		if (0 >= checkTimeout)
			throw new IllegalArgumentException("0 >= checkTimeout");
		this.dirName = dir;
		this.listener = listener;
		this.checkTimeout = checkTimeout;
	}

	private void checkDir() {
		Snapshot newSnapshot = new Snapshot(dirName);
		if (isModified(newSnapshot)) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("checkDir: " + dirName + " is changed.");

			Snapshot oldSnapshot = lastSnapshot;
			lastSnapshot = newSnapshot;
			listener.onDirChanged(new DirChangeEvent(dirName, oldSnapshot, newSnapshot));
		}
	}

	private boolean isModified(Snapshot newSnapshot) {
		if (null == lastSnapshot) {
			return true;
		} else {
			if (lastSnapshot.hashCode() != newSnapshot.hashCode()) {
				return true;
			} else {
				if (!lastSnapshot.equals(newSnapshot))
					return true;
			}
		}
		return false;
	}

	private void doCheckDir() {
		executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					checkDir();
				} catch (RuntimeException e) {
					LOGGER.error("checkDir:", e);
				}
			}
		}, 0, checkTimeout, TimeUnit.MILLISECONDS);
	}

	public void start() {
		doCheckDir();
	}

	public void destroy() {
		if (null != executorService)
			executorService.shutdownNow();
	}

	public String getDir() {
		return this.dirName;
	}

	public long getCheckTimeout() {
		return checkTimeout;
	}

	final static class Snapshot {
		private static final Logger LOGGER = LoggerFactory.getLogger(Snapshot.class);
		private static final String DEFAULT_INCLUDE_SUFFIX = ".jar";

		private final Map<String, Long> fileInfos = new HashMap<String, Long>();

		public Snapshot(String dirName) {
			File dir = new File(dirName);
			if (dir.isDirectory()) {
				File[] includes = dir.listFiles(new FilenameFilter() {

					public boolean accept(File dir, String name) {
						return isPlugInFile(name);
					}
				});

				for (File file : includes)
					fileInfos.put(file.getAbsolutePath(), new Long(file.lastModified()));
			} else {
				LOGGER.error("Dir Snapshot: [" + dirName + "] is !NOT! directory.");
				throw new RuntimeException("[" + dirName + "] is !NOT! directory.");
			}
		}

		public Map<String, Long> getFileInfos() {
			return fileInfos;
		}

		private boolean isPlugInFile(String fileName) {
			return fileName.endsWith(DEFAULT_INCLUDE_SUFFIX);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fileInfos == null) ? 0 : fileInfos.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Snapshot))
				return false;
			final Snapshot other = (Snapshot) obj;

			if (fileInfos == null) {
				if (other.fileInfos != null)
					return false;
			} else if (!fileInfos.equals(other.fileInfos))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}
	}
}
