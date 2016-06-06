package com.skymobi.market.commons.validate.internal;

import java.lang.reflect.Field;

import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.internal.BeanValidator;
import org.bigmouth.nvwa.validate.internal.BeanValidatorWrapper;
import org.bigmouth.nvwa.validate.internal.NotNullValidator;
import org.bigmouth.nvwa.validate.internal.NumericValidator;
import org.junit.Test;

import com.skymobi.market.commons.validate.ValidateTestSupport;
import com.skymobi.market.commons.validate.internal.bean.ExampleBean1;
import com.skymobi.market.commons.validate.internal.bean.ExampleBean2;

public class BeanValidatorTest extends ValidateTestSupport {

	@Test
	public void testMultiField() {
		Field f0 = null;
		try {
			f0 = ExampleBean1.class.getDeclaredField("v0");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Validator v0Validator = new NumericValidator(f0, 0, 10);

		Field f1 = null;
		try {
			f1 = ExampleBean1.class.getDeclaredField("v1");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Validator v1Validator_0 = new NotNullValidator(f1);

		BeanValidator beanValidator = new BeanValidator();
		beanValidator.register(v0Validator);
		beanValidator.register(v1Validator_0);

		ExampleBean1 bean = new ExampleBean1();

		validateViolateConstraint(beanValidator, bean);

		bean.setV0(10);
		bean.setV1("hh");
		validatePass(beanValidator, bean);

		bean.setV0(11);
		validateViolateConstraint(beanValidator, bean);
	}

	@Test
	public void testWrapObject() {
		BeanValidator bean2Validator = createBean2Validator();

		Field f0 = null;
		try {
			f0 = ExampleBean1.class.getDeclaredField("v0");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Validator v0Validator = new NumericValidator(f0, 0, 10);

		Field f1 = null;
		try {
			f1 = ExampleBean1.class.getDeclaredField("v1");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Validator v1Validator_0 = new NotNullValidator(f1);

		Field f2 = null;
		try {
			f2 = ExampleBean1.class.getDeclaredField("bean2");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Validator v2Validator = new BeanValidatorWrapper(f2, bean2Validator);

		BeanValidator beanValidator = new BeanValidator();
		beanValidator.register(v0Validator);
		beanValidator.register(v1Validator_0);
		beanValidator.register(v2Validator);

		// //
		ExampleBean2 bean2 = new ExampleBean2();

		ExampleBean1 bean1 = new ExampleBean1();
		bean1.setV0(5);
		bean1.setV1("gg");

		bean1.setBean2(bean2);
		validateViolateConstraint(beanValidator, bean1);

		// //
		bean2.setV0(4);
		bean2.setV1("ggg");
		validatePass(beanValidator, bean1);
	}

	private BeanValidator createBean2Validator() {
		Field f0 = null;
		try {
			f0 = ExampleBean2.class.getDeclaredField("v0");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Validator v0Validator = new NumericValidator(f0, 0, 10);

		Field f1 = null;
		try {
			f1 = ExampleBean2.class.getDeclaredField("v1");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Validator v1Validator_0 = new NotNullValidator(f1);

		BeanValidator beanValidator = new BeanValidator();
		beanValidator.register(v0Validator);
		beanValidator.register(v1Validator_0);

		return beanValidator;
	}
}
