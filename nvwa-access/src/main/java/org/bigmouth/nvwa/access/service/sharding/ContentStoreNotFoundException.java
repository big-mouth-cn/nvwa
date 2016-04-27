package org.bigmouth.nvwa.access.service.sharding;

import org.apache.commons.lang.StringUtils;

public final class ContentStoreNotFoundException extends Exception {

	private static final long serialVersionUID = 4840517425186878334L;

	private final String bizDesc;
	private final int contentFlag;

	public ContentStoreNotFoundException(String bizDesc, int contentFlag, Throwable cause) {
		super("Transaction[" + bizDesc + "] contentFlag[" + contentFlag
				+ "] can not found any ContentStore.", cause);
		if (StringUtils.isBlank("bizDesc"))
			throw new IllegalArgumentException("bizDesc:" + bizDesc);
		if (contentFlag < 0)
			throw new IllegalArgumentException("contentFlag:" + contentFlag);
		this.bizDesc = bizDesc;
		this.contentFlag = contentFlag;
	}

	public ContentStoreNotFoundException(String bizDesc, int contentFlag) {
		super("Transaction[" + bizDesc + "] contentFlag[" + contentFlag
				+ "] can not found any ContentStore.");
		if (StringUtils.isBlank("bizDesc"))
			throw new IllegalArgumentException("bizDesc:" + bizDesc);
		if (contentFlag < 0)
			throw new IllegalArgumentException("contentFlag:" + contentFlag);
		this.bizDesc = bizDesc;
		this.contentFlag = contentFlag;
	}

	public String getBizDesc() {
		return bizDesc;
	}

	public int getContentFlag() {
		return contentFlag;
	}
}
