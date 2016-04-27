package org.bigmouth.nvwa.servicelogic.interceptor;

import org.bigmouth.nvwa.codec.tlv.encoders.TLVBooleanArrayEncoder;
import org.bigmouth.nvwa.servicelogic.TransactionInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Interceptor {

	void intercept(TransactionInvocation invocation);

	public static final Interceptor END = new Interceptor() {

		private final Logger LOGGER = LoggerFactory.getLogger(TLVBooleanArrayEncoder.class);

		@Override
		public void intercept(TransactionInvocation invocation) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Interceptor execution chain is ended.");
		}
	};
}
