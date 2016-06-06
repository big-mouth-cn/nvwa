package org.bigmouth.nvwa.utils.concurrent;

import java.util.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface FutureListener<F extends Future> extends EventListener {

	static final Logger LOGGER = LoggerFactory.getLogger(FutureListener.class);

	final static FutureListener<Future> DEFAULT = new FutureListener<Future>() {

		@Override
		public void operationComplete(Future future) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("operation complete.");
		}
	};

	void operationComplete(F future);
}
