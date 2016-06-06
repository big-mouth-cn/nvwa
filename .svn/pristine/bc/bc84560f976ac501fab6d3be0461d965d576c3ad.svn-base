package org.bigmouth.nvwa.distributed.monitor;

import org.apache.commons.lang.ArrayUtils;

public abstract class DataDetector<T> {

	private ChangeMonitor zk;
	private String watchPath;
	private Updatable<T> updater;

	public void detect() {
		byte[] content = zk.getDataOf(watchPath);
		update(content);

		zk.addListener(watchPath, new DataChangeListener() {

			@Override
			public void onChanged(DataChangeEvent event) {
				update(event.getContent());
			}
		});
	}

	protected abstract T raw2Model(byte[] content);

	private void update(byte[] content) {
		if (ArrayUtils.isEmpty(content))
			throw new RuntimeException("DataDetector content data is empty.");

		T model = raw2Model(content);
		this.updater.update(model);
	}

	public void setZk(ChangeMonitor zk) {
		this.zk = zk;
	}

	public void setWatchPath(String watchPath) {
		this.watchPath = watchPath;
	}

	public void setUpdater(Updatable<T> updater) {
		this.updater = updater;
	}
}
