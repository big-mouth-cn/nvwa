package org.bigmouth.nvwa.servicelogic.handler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;

public class MultiClientRequest extends BaseRequest {

	private static final short TYPE_APPSTORE = 0;
	private static final short TYPE_PCSUITE = 1;

	@TLVAttribute(tag = 80)
	private short clientType = TYPE_APPSTORE;

	public boolean isFromAppStore() {
		return TYPE_APPSTORE == clientType;
	}

	public boolean isFromPcSuite() {
		return TYPE_PCSUITE == clientType;
	}

	public short getClientType() {
		return clientType;
	}

	public void setClientType(short clientType) {
		this.clientType = clientType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
