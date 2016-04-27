package org.bigmouth.nvwa.sap.namecode;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;


public class DefaultNameCodePair implements NameCodePair {

	@TLVAttribute(tag = 10006)
	private short plugInCode;
	@TLVAttribute(tag = 10005)
	private String plugInName;
	@TLVAttribute(tag = 10008)
	private short serviceCode;
	@TLVAttribute(tag = 10007)
	private String serviceName;

	@Override
	public short getPlugInCode() {
		return plugInCode;
	}

	@Override
	public String getPlugInName() {
		return plugInName;
	}

	@Override
	public short getServiceCode() {
		return serviceCode;
	}

	@Override
	public String getServiceName() {
		return serviceName;
	}

	public void setPlugInCode(short plugInCode) {
		this.plugInCode = plugInCode;
	}

	public void setPlugInName(String plugInName) {
		this.plugInName = plugInName;
	}

	public void setServiceCode(short serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
