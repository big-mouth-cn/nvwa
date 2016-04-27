package com.skymobi.market.commons.validate.factory;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.validate.annotation.ValidateNumeric;


public class AnnoBeanWrap {

	@ValidateNumeric(min = 1, max = 3)
	private int v0;

	private AnnoBean1 v1;

	public int getV0() {
		return v0;
	}

	public void setV0(int v0) {
		this.v0 = v0;
	}

	public AnnoBean1 getV1() {
		return v1;
	}

	public void setV1(AnnoBean1 v1) {
		this.v1 = v1;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
