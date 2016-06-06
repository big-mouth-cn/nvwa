package com.skymobi.market.commons.validate.internal;

import java.lang.reflect.Field;

import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.internal.NotNullValidator;
import org.junit.Test;

import com.skymobi.market.commons.validate.ValidateTestSupport;
import com.skymobi.market.commons.validate.internal.bean.ValidateExampleBean;

public class NotNullValidatorTest extends ValidateTestSupport {

	@Test
	public void test() {
		Field f = null;
		try {
			f = ValidateExampleBean.class.getDeclaredField("v0");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Validator validator = new NotNullValidator(f);

		ValidateExampleBean bean = new ValidateExampleBean();
		validateViolateConstraint(validator, bean);

		bean.setV0(new Object());
		validatePass(validator, bean);
	}
}
