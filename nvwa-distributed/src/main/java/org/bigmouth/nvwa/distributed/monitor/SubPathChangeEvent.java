package org.bigmouth.nvwa.distributed.monitor;

import java.util.EventObject;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.utils.Pair;

public class SubPathChangeEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	public enum SubEventType {
		CHILD_ADDED, CHILD_REMOVED, CHILD_UPDATED;
	}

	private final SubEventType type;
	private final List<Pair<String, byte[]>> subPaths;

	public static SubPathChangeEvent of(String path, SubEventType type,
			List<Pair<String, byte[]>> subPaths) {
		return new SubPathChangeEvent(path, type, subPaths);
	}

	private SubPathChangeEvent(Object source, SubEventType type, List<Pair<String, byte[]>> subPaths) {
		super(source);
		if (!(source instanceof String))
			throw new IllegalArgumentException("path expect String,but " + source.getClass());
		this.type = type;
		this.subPaths = subPaths;
	}

	public String getPath() {
		return (String) getSource();
	}

	public SubEventType getType() {
		return this.type;
	}

	public List<Pair<String, byte[]>> getSubPaths() {
		return this.subPaths;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
