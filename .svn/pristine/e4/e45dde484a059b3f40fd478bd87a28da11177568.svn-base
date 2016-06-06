package org.bigmouth.nvwa.validate.internal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bigmouth.nvwa.validate.ConstraintViolationException;
import org.bigmouth.nvwa.validate.ValidateException;
import org.bigmouth.nvwa.validate.Validator;


public class BeanValidator implements Validator {

	private final List<Validator> elementValidator = new CopyOnWriteArrayList<Validator>();

	@Override
	public void validate(Object input) throws ValidateException, ConstraintViolationException {
		if (null == input)
			throw new NullPointerException("input");
		for (Validator ev : elementValidator)
			ev.validate(input);
	}

	@Override
	public String getConstraintDesc() {
		return "Bean Validator";
	}

	@Override
	public String toString() {
		return "Bean Validator";
	}

	public void register(List<Validator> validators) {
		if (null == validators)
			throw new NullPointerException("validators");
		for (Validator v : validators)
			register(v);
	}

	public void register(Validator validator) {
		if (null == validator)
			throw new NullPointerException("validator");
		elementValidator.add(validator);
	}

	public void clear() {
		elementValidator.clear();
	}
}
