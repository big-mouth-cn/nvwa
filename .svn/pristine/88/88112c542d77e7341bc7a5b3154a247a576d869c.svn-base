package org.bigmouth.nvwa.servicelogic.factory;

import java.util.concurrent.ExecutorService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.dpl.status.StatusHolder;
import org.bigmouth.nvwa.servicelogic.codec.ContentDecoder;
import org.bigmouth.nvwa.servicelogic.codec.ContentEncoder;
import org.bigmouth.nvwa.servicelogic.handler.TransactionExecutor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServiceDeployConfig implements ApplicationContextAware {

	private final String transactionHandlerBeanName;
	private final ContentEncoder contentEncoder;
	private final ContentDecoder contentDecoder;

	private StatusHolder statusHolder;
	private ExecutorService serviceExecutorService;

	private ApplicationContext context;

	public ServiceDeployConfig(String transactionHandlerBeanName, ContentEncoder contentEncoder,
			ContentDecoder contentDecoder) {
		super();
		if (StringUtils.isBlank(transactionHandlerBeanName))
			throw new IllegalArgumentException("transactionHandlerBeanName");
		if (null == contentEncoder)
			throw new NullPointerException("contentEncoder");
		if (null == contentDecoder)
			throw new NullPointerException("contentDecoder");
		this.transactionHandlerBeanName = transactionHandlerBeanName;
		this.contentEncoder = contentEncoder;
		this.contentDecoder = contentDecoder;
	}

	public TransactionExecutor getTransactionHandler() {
		return (TransactionExecutor) context.getBean(transactionHandlerBeanName);
	}

	public ContentEncoder getContentEncoder() {
		return contentEncoder;
	}

	public ContentDecoder getContentDecoder() {
		return contentDecoder;
	}

	public ApplicationContext getApplicationContext() {
		return this.context;
	}

	public StatusHolder getStatusHolder() {
		return statusHolder;
	}

	public void setStatusHolder(StatusHolder statusHolder) {
		this.statusHolder = statusHolder;
	}

	public ExecutorService getServiceExecutorService() {
		return serviceExecutorService;
	}

	public void setServiceExecutorService(ExecutorService serviceExecutorService) {
		this.serviceExecutorService = serviceExecutorService;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
