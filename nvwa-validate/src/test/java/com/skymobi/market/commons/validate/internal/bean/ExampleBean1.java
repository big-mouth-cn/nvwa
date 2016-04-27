package com.skymobi.market.commons.validate.internal.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ExampleBean1 {

	private int v0;

	private String v1;

	private ExampleBean2 bean2;

	public ExampleBean2 getBean2() {
		return bean2;
	}

	public void setBean2(ExampleBean2 bean2) {
		this.bean2 = bean2;
	}

	public int getV0() {
		return v0;
	}

	public void setV0(int v0) {
		this.v0 = v0;
	}

	public String getV1() {
		return v1;
	}

	public void setV1(String v1) {
		this.v1 = v1;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
