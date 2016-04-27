package org.bigmouth.nvwa.servicelogic.handler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;

public class BaseResponse {

	private static final int DEFAULT_BIZCODE = CommonBizCode.SUCCESS;

	@TLVAttribute(tag = 1)
	private int bizCode = DEFAULT_BIZCODE;
	@TLVAttribute(tag = 2)
	private String desc;

	public int getBizCode() {
		return bizCode;
	}

	public void setBizCode(int bizCode) {
		this.bizCode = bizCode;
	}
	
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
