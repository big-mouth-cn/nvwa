package com.skymobi.market.commons.validate.factory;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.validate.annotation.ValidateNumeric;


public class AnnoBean {

	@ValidateNumeric(min = 1, max = 10)
	private int v0;

	public int getV0() {
		return v0;
	}

	public void setV0(int v0) {
		this.v0 = v0;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
