package com.skymobi.market.commons.validate.internal;

import java.lang.reflect.Field;
import java.util.List;

import org.bigmouth.nvwa.validate.ConstraintViolationException;
import org.bigmouth.nvwa.validate.ValidateException;
import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.internal.CollectionValidator;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.skymobi.market.commons.validate.ValidateTestSupport;
import com.skymobi.market.commons.validate.internal.bean.ValidateExampleBean;

public class CollectionValidatorTest extends ValidateTestSupport {

	@Test
	public void test() {

		Field f = null;
		try {
			f = ValidateExampleBean.class.getDeclaredField("v2");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ValidateExampleBean bean = new ValidateExampleBean();

		Validator validator = new CollectionValidator(f, true, 0, 1);
		validatePass(validator, bean);

		validator = new CollectionValidator(f, false, 0, 1);
		validateViolateConstraint(validator, bean);

		validator = new CollectionValidator(f, false, 3, 5);
		bean.setV2(new String[0]);
		validateViolateConstraint(validator, bean);

		bean.setV2(new String[] { "0", "1", "2" });
		validatePass(validator, bean);

		bean.setV2(new String[] { "0", "1", "2", "3", "4" });
		validatePass(validator, bean);

		bean.setV2(new String[] { "0", "1", "2", "3", "4", "5" });
		validateViolateConstraint(validator, bean);

		validator = new CollectionValidator(f, new StringElementValidator(), false, 3, 5);
		bean.setV2(new String[] { "0", "1", "2", "3", "error" });
		validateViolateConstraint(validator, bean);

		bean.setV2(new String[] { "0", "1", "2", "3", "4" });
		validatePass(validator, bean);

		f = null;
		try {
			f = ValidateExampleBean.class.getDeclaredField("v3");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		validator = new CollectionValidator(f, new StringElementValidator(), false, 3, 5);
		List<String> arr = Lists.newArrayList("0", "1", "2", "3", "4");
		bean.setV3(arr);
		validatePass(validator, bean);

		arr = Lists.newArrayList("0", "1", "error", "3", "4");
		bean.setV3(arr);
		validateViolateConstraint(validator, bean);
	}

	private static final class StringElementValidator implements Validator {

		@Override
		public String getConstraintDesc() {
			return "String Validator";
		}

		@Override
		public void validate(Object input) throws ValidateException, ConstraintViolationException {
			String v = (String) input;
			if ("error".equals(v))
				throw new ConstraintViolationException(this);
		}
	}
}
