package com.skymobi.market.commons.validate.internal;

import java.lang.reflect.Field;

import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.internal.NumericValidator;
import org.junit.Test;

import com.skymobi.market.commons.validate.ValidateTestSupport;
import com.skymobi.market.commons.validate.internal.bean.ValidateExampleBean;

public class NumericValidatorTest extends ValidateTestSupport {

	@Test
	public void test() {
		Field f0 = null;
		try {
			f0 = ValidateExampleBean.class.getDeclaredField("v1");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Field f1 = null;
		try {
			f1 = ValidateExampleBean.class.getDeclaredField("v5");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Validator validator = new NumericValidator(f0);
		ValidateExampleBean bean = new ValidateExampleBean();
		bean.setV1("hh");
		validateViolateConstraint(validator, bean);

		bean.setV1("12");
		validatePass(validator, bean);

		validator = new NumericValidator(f1);
		validatePass(validator, bean);
	}
}
