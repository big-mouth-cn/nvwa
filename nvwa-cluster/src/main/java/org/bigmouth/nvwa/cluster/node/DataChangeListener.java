package org.bigmouth.nvwa.cluster.node;

public interface DataChangeListener {

	public void onDataChanged(String path, byte[] content);
}
