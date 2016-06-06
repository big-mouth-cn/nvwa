package org.bigmouth.nvwa.utils;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Preconditions;

public class CyclicCounter {

	private final AtomicInteger idx = new AtomicInteger(0);
	private final int initalValue;
	private final int maxValue;
	private final int step;

	public CyclicCounter() {
		this(0, Integer.MAX_VALUE, 1);
	}

	public CyclicCounter(int initalValue, int maxValue, int step) {
		Preconditions.checkArgument(initalValue >= 0, "initalValue must >= 0,but initalValue:"
				+ initalValue);
		Preconditions.checkArgument(maxValue > 0, "maxValue must > 0,but maxValue:" + maxValue);
		Preconditions.checkArgument(step > 0, "step must > 0,but step:" + step);
		Preconditions.checkArgument(maxValue > (initalValue + step),
				"maxValue > (initalValue + step),but initalValue:" + initalValue + ",maxValue:"
						+ maxValue + ",step:" + step);

		this.initalValue = initalValue;
		idx.set(initalValue);
		this.maxValue = maxValue;
		this.step = step;
	}

	/**
	 * reset initalValue = 0.
	 */
	public void reset() {
		idx.set(initalValue);
	}

	public int get() {
		int ret = idx.getAndAdd(step);
		if (ret >= maxValue || ret < 0) {
			reset();
			return get();
		}
		return ret;
	}
}
