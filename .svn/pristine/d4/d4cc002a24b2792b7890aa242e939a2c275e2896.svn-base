package org.bigmouth.nvwa.access.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IllegalRequestHandler {

	public static final Logger LOGGER = LoggerFactory.getLogger(IllegalRequestHandler.class);

	public static final IllegalRequestHandler DEFAULT = new IllegalRequestHandler() {

		@Override
		public void handle(HttpRequestExt httpRequest) {
			LOGGER.error("Illegal http request:" + httpRequest);
		}
	};

	void handle(HttpRequestExt httpRequest);
}
