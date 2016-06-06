package org.bigmouth.nvwa.servicelogic.handler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;

public class StandardDownloadResponse extends BaseResponse {

	@TLVAttribute(tag = 0, ignoreTagLen = true)
	private int totalLength;
	@TLVAttribute(tag = 0, ignoreTagLen = true)
	private int beginOffset;
	@TLVAttribute(tag = 0, ignoreTagLen = true)
	private int length;
	@TLVAttribute(tag = 0, ignoreTagLen = true)
	private byte[] dynData;

	public int getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

	public int getBeginOffset() {
		return beginOffset;
	}

	public void setBeginOffset(int beginOffset) {
		this.beginOffset = beginOffset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte[] getDynData() {
		return dynData;
	}

	public void setDynData(byte[] dynData) {
		this.dynData = dynData;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
