package com.skymobi.market.commons.validate.internal;

import java.lang.reflect.Field;

import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.internal.PatternValidator;
import org.junit.Test;

import com.skymobi.market.commons.validate.ValidateTestSupport;
import com.skymobi.market.commons.validate.internal.bean.ValidateExampleBean;

public class PatternValidatorTest extends ValidateTestSupport {

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

		Validator validator = new PatternValidator(f0, ".{1}");
		ValidateExampleBean bean = new ValidateExampleBean();
		bean.setV1("3");
		validatePass(validator, bean);

		bean.setV1("34");
		validateViolateConstraint(validator, bean);
	}
}
