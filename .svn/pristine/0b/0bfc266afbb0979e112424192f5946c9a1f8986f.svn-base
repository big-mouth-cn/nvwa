package org.bigmouth.nvwa.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BroadcastClosure implements Closure {

	private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastClosure.class);

	private List<Closure> children;
	private BroadcastStrategy strategy = BroadcastStrategy.CONTINUE;

	public BroadcastClosure(List<Closure> children) {
		super();
		if (null == children)
			throw new NullPointerException("children");

		this.children = children;
	}

	public BroadcastClosure(List<Closure> children, BroadcastStrategy strategy) {
		this(children);
		this.strategy = strategy;
	}

	@Override
	public void execute(Object input) {
		if (null == input)
			throw new NullPointerException("input");

		for (Closure child : children) {
			try {
				child.execute(input);
			} catch (Exception e) {
				if (BroadcastStrategy.INTERRUPT == strategy) {
					LOGGER.error("child.execute occur error,strategy is INTERRUPT;", e);
					break;
				} else {
					LOGGER.error("child.execute occur error,strategy is CONTINUE;", e);
					continue;
				}
			}
		}

	}

	public List<Closure> getChildren() {
		return children;
	}

	public BroadcastStrategy getStrategy() {
		return strategy;
	}

}
