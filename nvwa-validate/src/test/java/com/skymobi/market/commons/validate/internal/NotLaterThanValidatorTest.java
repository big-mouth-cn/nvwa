package com.skymobi.market.commons.validate.internal;

import java.lang.reflect.Field;
import java.util.Date;

import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.internal.NotLaterThanValidator;
import org.junit.Test;

import com.skymobi.market.commons.validate.ValidateTestSupport;
import com.skymobi.market.commons.validate.internal.bean.ValidateExampleBean;

public class NotLaterThanValidatorTest extends ValidateTestSupport {

	@Test
	public void test() {
		Field f0 = null;
		try {
			f0 = ValidateExampleBean.class.getDeclaredField("v7");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Field f1 = null;
		try {
			f1 = ValidateExampleBean.class.getDeclaredField("v8");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Validator validator = new NotLaterThanValidator(f0, f1);

		ValidateExampleBean bean = new ValidateExampleBean();
		bean.setV7(new Date());
		bean.setV8(new Date());

		validatePass(validator, bean);

		bean.setV8(new Date());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bean.setV7(new Date());
		validateViolateConstraint(validator, bean);
	}
}
