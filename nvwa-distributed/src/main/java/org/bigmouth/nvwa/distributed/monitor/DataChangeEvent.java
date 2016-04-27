package org.bigmouth.nvwa.distributed.monitor;

import java.util.EventObject;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DataChangeEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	private final byte[] content;

	public static DataChangeEvent of(String path, byte[] content) {
		return new DataChangeEvent(path, content);
	}

	private DataChangeEvent(Object source, byte[] content) {
		super(source);
		if (!(source instanceof String))
			throw new IllegalArgumentException("path expect String,but " + source.getClass());
		this.content = content;
	}

	public String getPath() {
		return (String) getSource();
	}

	public byte[] getContent() {
		return this.content;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
