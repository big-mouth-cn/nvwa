package org.bigmouth.nvwa.utils.change;

import java.util.EventObject;

public class ChangeEvent<S> extends EventObject {

	private static final long serialVersionUID = 1L;

	private final S oldSnapshot;
	private final S newSnapshot;

	public ChangeEvent(Object source, S oldSnapshot, S newSnapshot) {
		super(source);
		this.oldSnapshot = oldSnapshot;
		this.newSnapshot = newSnapshot;
	}

	protected S getNewSnapshot() {
		return newSnapshot;
	}

	protected S getOldSnapshot() {
		return oldSnapshot;
	}
}
