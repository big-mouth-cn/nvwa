package org.bigmouth.nvwa.servicelogic.interceptor;

import org.bigmouth.nvwa.sap.SapResponse;
import org.bigmouth.nvwa.sap.SapResponseStatus;
import org.bigmouth.nvwa.sap.SapResponseUtils;
import org.bigmouth.nvwa.servicelogic.TransactionInvocation;
import org.bigmouth.nvwa.validate.ConstraintViolationException;
import org.bigmouth.nvwa.validate.ValidateException;
import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.factory.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RequestValidateInterceptor extends AbstractInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestValidateInterceptor.class);

	private final ValidatorFactory validatorFactory;

	public RequestValidateInterceptor(Interceptor next, ValidatorFactory validatorFactory) {
		super(next);
		if (null == validatorFactory)
			throw new NullPointerException("validatorFactory");
		this.validatorFactory = validatorFactory;
	}

	@Override
	public void intercept(TransactionInvocation invocation) {
		Object request = invocation.getRequestModel();
		Validator validator = validatorFactory.create(request.getClass());
		if (null == validator) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Do not need to validate request parameters,ignore.");
		} else {
			try {
				validator.validate(request);
			} catch (ConstraintViolationException e) {
				// illegal parameter
				replyException(invocation, SapResponseStatus.ILLEGAL_PARAMETER);
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Illegal request parameters:", e);
				return;
			} catch (ValidateException e) {
				// server error
				replyException(invocation, SapResponseStatus.INTERNAL_SERVER_ERROR);
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Validate error:", e);
				return;
			}
		}
		fireNextInterceptor(invocation);
	}

	// TODO:bad request record
	private void replyException(TransactionInvocation invocation, SapResponseStatus status) {
		SapResponse sapResponse = SapResponseUtils.createEmptySapResponse(invocation.getRequest()
				.getIdentification(), status);
		invocation.getContext().getReplier().reply(sapResponse);
	}
}
