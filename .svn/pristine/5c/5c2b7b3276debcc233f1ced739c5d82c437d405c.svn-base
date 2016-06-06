package org.bigmouth.nvwa.cluster.node;

import org.apache.commons.lang.ArrayUtils;

public abstract class DataDetector<T> {

    private static final String NODE_INIT_VALUE = "[{\"plugInName\":\"access\",\"plugInCode\":0,\"serviceName\":\"default\",\"serviceCode\":0}]";
	private Wrapper zk;
	private String watchPath;
	private Updatable<T> updater;

	public void detect() {
		byte[] content = zk.getData(watchPath, NODE_INIT_VALUE.getBytes());
		
		update(content);

		zk.registerDataChangeListener(watchPath, new DataChangeListener() {

			@Override
			public void onDataChanged(String path, byte[] content) {
				update(content);
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

	
    public void setZk(Wrapper zk) {
        this.zk = zk;
    }

    public void setWatchPath(String watchPath) {
		this.watchPath = watchPath;
	}

	public void setUpdater(Updatable<T> updater) {
		this.updater = updater;
	}
}
