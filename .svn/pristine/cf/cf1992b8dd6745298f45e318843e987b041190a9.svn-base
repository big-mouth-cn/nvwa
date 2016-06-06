package org.bigmouth.nvwa.servicelogic.interceptor;

import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.sap.ExtendedItem;
import org.bigmouth.nvwa.sap.MutableSapResponse;
import org.bigmouth.nvwa.sap.SapResponseStatus;
import org.bigmouth.nvwa.servicelogic.TransactionInvocation;
import org.bigmouth.nvwa.servicelogic.codec.CodecSelector;
import org.bigmouth.nvwa.servicelogic.codec.ContentEncoder;
import org.bigmouth.nvwa.servicelogic.handler.TransactionExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class SendbackInterceptor extends AbstractInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendbackInterceptor.class);

	private final CodecSelector codecSelector;

	public SendbackInterceptor(CodecSelector codecSelector) {
		super();
		this.codecSelector = codecSelector;
	}

	public SendbackInterceptor(Interceptor next, CodecSelector codecSelector) {
		super(next);
		this.codecSelector = codecSelector;
	}

	@Override
	public void intercept(TransactionInvocation invocation) {

		MutableSapResponse sapResponse = (MutableSapResponse) invocation.getResponse();
		SapResponseStatus status = sapResponse.getStatus();
		if (null != status && SapResponseStatus.OK != status) {
			sendback(invocation, sapResponse, status);
		} else {
			TransactionExecutor transactionHandler = invocation.getTransactionHandler();

			// extended items
			if (isExtandable(transactionHandler)) {
				addExtendedItems(sapResponse, transactionHandler);
			}

			// responseModel content
			Object responseModel = invocation.getResponseModel();
			if (null == responseModel) {
				// TODO:
				throw new RuntimeException("responseModel is null.");
			}

			sendback(invocation, sapResponse, SapResponseStatus.OK);
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Reply response:" + sapResponse);
		fireNextInterceptor(invocation);
	}

	private void addExtendedItems(MutableSapResponse sapResponse,
			TransactionExecutor transactionHandler) {
		Extendable extendable = (Extendable) transactionHandler;
		List<ExtendedItem> extendedItems = getExtendedItems(extendable);
		if (null == extendedItems || 0 == extendedItems.size()) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("TransactionHandler[" + transactionHandler
						+ "] is Extendable,but extendedItems is blank,ignore.");
		} else {
			sapResponse.addExtendedItems(extendedItems);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("TransactionHandler[" + transactionHandler
						+ "] is Extendable,extendedItems:" + extendedItems);
		}
	}

	private List<ExtendedItem> getExtendedItems(Extendable extendable) {
		List<ExtendedItem> extendedItems = extendable.getExtendedItems();
		List<ExtendedItem> copy = Lists.newArrayList();
		for (ExtendedItem item : extendedItems) {
			if (null == item)
				continue;
			copy.add(item);
		}
		return copy;
	}

	private boolean isExtandable(TransactionExecutor transactionHandler) {
		if (!(transactionHandler instanceof Extendable))
			return false;
		Extendable extendable = (Extendable) transactionHandler;
		return null != extendable.getExtendedItems();
	}

	private void sendback(TransactionInvocation invocation, MutableSapResponse sapResponse,
			SapResponseStatus status) {
		// status
		sapResponse.setStatus(status);

		// content
		Object responseModel = invocation.getResponseModel();
//		ContentEncoder contentEncoder = codecSelector.selectEncoder(responseModel.getClass());
		ContentEncoder contentEncoder = codecSelector.selectEncoder(invocation.getRequest().getContentType());
		byte[] bytes = contentEncoder.encode(responseModel);

		// content type
		sapResponse.setContentType(codecSelector.selectContentType(responseModel.getClass()));

		IoBuffer content = IoBuffer.wrap(bytes);
		sapResponse.setContent(content);

		invocation.getContext().getReplier().reply(sapResponse);

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Send back ResponseModel:" + invocation.getResponseModel());
	}
}
