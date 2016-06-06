package org.bigmouth.nvwa.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Increment id generator.
 * 
 * @author nada
 * 
 */
public final class IncIdGenerator {

	private static final long DEFAULT_STEP = 1;
	private static final long DEFAULT_INIT_VALUE = 0;
	private static final long DEFAULT_MAX_VALUE = Long.MAX_VALUE;

	private final long step;
	private final long maxValue;

	private final AtomicLong v;

	public IncIdGenerator() {
		this(DEFAULT_STEP, DEFAULT_INIT_VALUE, DEFAULT_MAX_VALUE);
	}

	public IncIdGenerator(long step, long initValue, long maxValue) {
		this.step = step;
		this.v = new AtomicLong(initValue);
		this.maxValue = maxValue;
	}

	public long generate() {
		long curVal = v.get();
		if (curVal > maxValue)
			throw new IndexOutOfBoundsException("exceeded maxValue");
		return v.getAndAdd(step);
	}
}
