package org.bigmouth.nvwa.dpl.status;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public final class ImmutableStatus implements Status, Serializable {

	private static final long serialVersionUID = -6356707487685241510L;

	private static final boolean DEFAULT_RUNNING_STATUS = true;

	private final boolean running;
	private final long lastModifiedTime = new Date().getTime();

	public ImmutableStatus() {
		super();
		this.running = DEFAULT_RUNNING_STATUS;
	}

	public ImmutableStatus(final boolean running) {
		super();
		this.running = running;
	}

	@Override
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (running ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmutableStatus other = (ImmutableStatus) obj;
		if (running != other.running)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
