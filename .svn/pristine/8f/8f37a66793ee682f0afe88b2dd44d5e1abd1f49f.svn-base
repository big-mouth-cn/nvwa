package org.bigmouth.nvwa.validate.internal;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bigmouth.nvwa.validate.CompareValidator;
import org.bigmouth.nvwa.validate.ConstraintViolationException;
import org.bigmouth.nvwa.validate.ValidateException;


public class NotLaterThanValidator extends CompareValidator {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public NotLaterThanValidator(Field field, Field compareField) {
		super(field, compareField);
	}

	@Override
	protected void doValidate(Object input) throws Exception {
		Object _v0 = getValue(input);
		if (!(_v0 instanceof Date))
			throw new ValidateException("Expect Date,but " + _v0);
		Date v0 = (Date) _v0;

		Object _v1 = getCompareValue(input);
		if (!(_v1 instanceof Date))
			throw new ValidateException("Expect Date,but " + _v1);
		Date v1 = (Date) _v1;

		if (v0.getTime() > v1.getTime()) {
			throw new ConstraintViolationException(this, FORMAT.format(v0) + ","
					+ FORMAT.format(v1));
		}
	}

	@Override
	public String getConstraintDesc() {
		return new StringBuilder(64).append(getFieldDesc()).append(" must not be later than ")
				.append(getCompareField()).toString();
	}
}
