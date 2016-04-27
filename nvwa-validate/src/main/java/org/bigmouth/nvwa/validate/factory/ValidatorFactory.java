package org.bigmouth.nvwa.validate.factory;

import org.bigmouth.nvwa.validate.Validator;

public interface ValidatorFactory {

	// TODO:
	Validator create(Class<?> beanClass);
}
