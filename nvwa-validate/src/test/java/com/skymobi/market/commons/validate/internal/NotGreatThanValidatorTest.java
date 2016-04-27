package com.skymobi.market.commons.validate.internal;

import java.lang.reflect.Field;

import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.internal.NotGreatThanValidator;
import org.junit.Test;

import com.skymobi.market.commons.validate.ValidateTestSupport;
import com.skymobi.market.commons.validate.internal.bean.ValidateExampleBean;

public class NotGreatThanValidatorTest extends ValidateTestSupport {

	@Test
	public void test() {
		Field f0 = null;
		try {
			f0 = ValidateExampleBean.class.getDeclaredField("v5");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Field f1 = null;
		try {
			f1 = ValidateExampleBean.class.getDeclaredField("v6");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Validator validator = new NotGreatThanValidator(f0, f1);
		ValidateExampleBean bean = new ValidateExampleBean();
		bean.setV5(5);
		bean.setV6(5);
		validatePass(validator, bean);

		bean.setV5(5);
		bean.setV6(6);
		validatePass(validator, bean);

		bean.setV5(7);
		bean.setV6(6);
		validateViolateConstraint(validator, bean);
	}
}
