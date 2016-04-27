package org.bigmouth.nvwa.distributed.monitor;

import java.util.List;

import org.bigmouth.nvwa.utils.Pair;

/**
 * 封装两类典型场景：<br>
 * 1) 监听某一路径(path)的内容变化；<br>
 * 2) 监听某一路径(path)的子路径、及子路径内容 变化；<br>
 * 
 * @author nada
 * 
 */
public interface ChangeMonitor {

	byte[] getDataOf(String path);

	void addListener(String path, DataChangeListener listener);

	List<Pair<String, byte[]>> getSubPathsOf(String path);

	void addListener(String path, SubPathChangeListener listener);
}
