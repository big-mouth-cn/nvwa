package org.bigmouth.nvwa.validate.internal;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.validate.AbstractValidator;
import org.bigmouth.nvwa.validate.ConstraintViolationException;
import org.bigmouth.nvwa.validate.ValidateException;


public class PatternValidator extends AbstractValidator {

	private final Pattern pattern;

	public PatternValidator(Field field, String regex) {
		super(field);
		if (StringUtils.isBlank(regex))
			throw new IllegalArgumentException("regex is blank.");
		pattern = Pattern.compile(regex);
	}

	@Override
	protected void doValidate(Object input) throws Exception {
		Object _v = getValue(input);
		if (!(_v instanceof String))
			throw new ValidateException("Expect String,but " + _v);
		String v = (String) _v;
		Matcher m = pattern.matcher(v);
		if (!m.matches())
			throw new ConstraintViolationException(this, v);
	}

	@Override
	public String getConstraintDesc() {
		return getFieldDesc() + " match Pattern:" + pattern;
	}
}
