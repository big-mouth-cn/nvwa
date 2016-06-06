package com.skymobi.market.commons.validate.factory;

import static junit.framework.Assert.assertNotNull;

import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.factory.AnnotationValidatorFactory;
import org.junit.Test;

import com.skymobi.market.commons.validate.ValidateTestSupport;

public class AnnotationValidatorFactoryTest extends ValidateTestSupport {

	@Test
	public void test() {
		AnnotationValidatorFactory factory = new AnnotationValidatorFactory();
		Validator validator = factory.create(AnnoBean.class);
		assertNotNull(validator);

		AnnoBean annoBean = new AnnoBean();
		annoBean.setV0(2);
		validatePass(validator, annoBean);

		annoBean.setV0(11);
		validateViolateConstraint(validator, annoBean);
	}

	@Test
	public void testMultiField() {
		AnnotationValidatorFactory factory = new AnnotationValidatorFactory();
		Validator validator = factory.create(AnnoBeanMulti.class);
		assertNotNull(validator);

		AnnoBeanMulti bean = new AnnoBeanMulti();
		validateViolateConstraint(validator, bean);

		bean.setV0(2);
		bean.setV1("hhh");
		validatePass(validator, bean);

		bean.setV0(-2);
		bean.setV1("hhh");
		validateViolateConstraint(validator, bean);

		bean.setV0(2);
		bean.setV1("h");
		validateViolateConstraint(validator, bean);
	}

	@Test
	public void testWrapObject() {
		AnnotationValidatorFactory factory = new AnnotationValidatorFactory();
		Validator validator = factory.create(AnnoBeanWrap.class);
		assertNotNull(validator);

		AnnoBeanWrap bean = new AnnoBeanWrap();
		validateViolateConstraint(validator, bean);

		bean.setV0(2);
		validatePass(validator, bean);

		AnnoBean1 child = new AnnoBean1();
		bean.setV1(child);
		validateViolateConstraint(validator, bean);

		child.setV10("");
		child.setV11(4);
		validatePass(validator, bean);

		child.setV10("");
		child.setV11(2);
		validateViolateConstraint(validator, bean);

		child.setV10(null);
		child.setV11(4);
		validateViolateConstraint(validator, bean);
	}
}
