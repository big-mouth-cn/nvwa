package com.skymobi.market.commons.validate.factory;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.validate.ConstraintViolationException;
import org.bigmouth.nvwa.validate.ValidateException;
import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.annotation.ValidateCustom;
import org.bigmouth.nvwa.validate.annotation.ValidateNotNull;


public class AnnoBean1 {

	@ValidateNotNull
	private String v10;
	@ValidateCustom(validator = V11Validator.class)
	private int v11;

	public static final class V11Validator implements Validator {

		@Override
		public String getConstraintDesc() {
			return "V11Validator - value must be great than 3";
		}

		@Override
		public void validate(Object input) throws ValidateException, ConstraintViolationException {
			int v = (Integer) input;
			if (v < 3)
				throw new ConstraintViolationException(this, String.valueOf(v));
		}
	}

	public String getV10() {
		return v10;
	}

	public void setV10(String v10) {
		this.v10 = v10;
	}

	public int getV11() {
		return v11;
	}

	public void setV11(int v11) {
		this.v11 = v11;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
