package org.bigmouth.nvwa.servicelogic.interceptor;

import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.sap.SapRequest;
import org.bigmouth.nvwa.servicelogic.MutableTransactionInvocation;
import org.bigmouth.nvwa.servicelogic.TransactionInvocation;
import org.bigmouth.nvwa.servicelogic.codec.CodecSelector;
import org.bigmouth.nvwa.servicelogic.codec.ContentDecoder;


public abstract class ContentDecodeInterceptor extends AbstractInterceptor {

	private final CodecSelector codecSelector;

	public ContentDecodeInterceptor(Interceptor next, CodecSelector codecSelector) {
		super(next);
		if (null == codecSelector)
			throw new NullPointerException("codecSelector");
		this.codecSelector = codecSelector;
	}

	@Override
	public void intercept(TransactionInvocation invocation) {
		MutableTransactionInvocation mutableInvocation = (MutableTransactionInvocation) invocation;
		SapRequest sapRequest = mutableInvocation.getRequest();
		IoBuffer buffer = sapRequest.getContent();
		byte[] content = new byte[sapRequest.getContentLength()];
		buffer.get(content);
		Class<?> requestTemplate = getRequestTemplate();
		if (null == requestTemplate)
			throw new NullPointerException("requestTemplate");

		ContentDecoder contentDecoder = codecSelector.selectDecoder(sapRequest.getContentType());
		Object requestModel = contentDecoder.decode(content, requestTemplate);
		mutableInvocation.setRequestModel(requestModel);

		Object responseModel = createResponseModel();
		if (null == responseModel)
			throw new NullPointerException("responseModel");
		mutableInvocation.setResponseModel(responseModel);

		fireNextInterceptor(mutableInvocation);
	}

	public abstract Class<?> getRequestTemplate();

	public abstract Object createResponseModel();
}
