package com.skymobi.market.commons.validate.internal;

import java.lang.reflect.Field;

import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.internal.LengthValidator;
import org.junit.Test;

import com.skymobi.market.commons.validate.ValidateTestSupport;
import com.skymobi.market.commons.validate.internal.bean.ValidateExampleBean;

public class LengthValidatorTest extends ValidateTestSupport {

	@Test
	public void test() {

		Field f = null;
		try {
			f = ValidateExampleBean.class.getDeclaredField("v1");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Validator validator = new LengthValidator(f, 0, 1);

		ValidateExampleBean bean = new ValidateExampleBean();
		bean.setV1("hhhh");
		validateViolateConstraint(validator, bean);

		validator = new LengthValidator(f, 0, 4);
		validatePass(validator, bean);

		bean.setV1("hhhhh");
		validateViolateConstraint(validator, bean);

		bean.setV1(null);
		validateViolateConstraint(validator, bean);
	}
}
