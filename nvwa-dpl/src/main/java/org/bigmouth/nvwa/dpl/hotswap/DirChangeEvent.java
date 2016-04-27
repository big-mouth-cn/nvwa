package org.bigmouth.nvwa.dpl.hotswap;

import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.dpl.hotswap.PlugInDirMonitor.Snapshot;

import com.google.common.collect.Lists;

public final class DirChangeEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	private final List<String> addedFiles = Lists.newArrayList();
	private final List<String> updatedFiles = Lists.newArrayList();
	private final List<String> removedFiles = Lists.newArrayList();
	private final List<String> currentAllFiles = Lists.newArrayList();

	public DirChangeEvent(Object source, Snapshot oldSnapshot, Snapshot newSnapshot) {
		super(source);
		if (null == newSnapshot)
			throw new NullPointerException("newSnapshot");
		init(oldSnapshot, newSnapshot);
	}

	private void init(Snapshot oldSnapshot, Snapshot newSnapshot) {
		if (null == oldSnapshot) {
			addedFiles.addAll(newSnapshot.getFileInfos().keySet());
		} else {// oldSnapshot is not null,newSnapshot is not null.
			Map<String, Long> oldFileInfos = oldSnapshot.getFileInfos();
			Map<String, Long> newFileInfos = newSnapshot.getFileInfos();

			for (Entry<String, Long> e : oldFileInfos.entrySet()) {
				String file = e.getKey();
				long mf = e.getValue();
				if (!newFileInfos.containsKey(file)) {
					removedFiles.add(file);
				} else {
					if (mf != newFileInfos.get(file))
						updatedFiles.add(file);
				}
			}

			for (Entry<String, Long> e : newFileInfos.entrySet()) {
				String file = e.getKey();
				if (!oldFileInfos.containsKey(file))
					addedFiles.add(file);
			}
		}
		currentAllFiles.addAll(newSnapshot.getFileInfos().keySet());
	}

	public List<String> getUpdatedFiles() {
		return updatedFiles;
	}

	public List<String> getAddedFiles() {
		return addedFiles;
	}

	public List<String> getRemovedFiles() {
		return removedFiles;
	}

	public List<String> getCurrentAllFiles() {
		return currentAllFiles;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
