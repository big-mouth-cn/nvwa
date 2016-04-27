package org.bigmouth.nvwa.validate.internal;

import java.lang.reflect.Field;
import java.util.Collection;

import org.bigmouth.nvwa.validate.AbstractValidator;
import org.bigmouth.nvwa.validate.ConstraintViolationException;
import org.bigmouth.nvwa.validate.NoopValidator;
import org.bigmouth.nvwa.validate.ValidateException;
import org.bigmouth.nvwa.validate.Validator;


/**
 * Array or Collection.
 * 
 * @author nada
 * 
 */
public class CollectionValidator extends AbstractValidator {

	private static final Validator DEFAULT_ELEMENT_VALIDATOR = new NoopValidator();
	private static final boolean DEFAULT_ALLOW_EMPTY = true;
	private static final int DEFAULT_MIN_SIZE = 0;
	private static final int DEFAULT_MAX_SIZE = Integer.MAX_VALUE;

	private final boolean allowEmpty;
	private final int minSize;
	private final int maxSize;
	private final Validator elementValidator;

	public CollectionValidator(Field field, Validator elementValidator) {
		this(field, elementValidator, DEFAULT_ALLOW_EMPTY, DEFAULT_MIN_SIZE, DEFAULT_MAX_SIZE);
	}

	public CollectionValidator(Field field, boolean allowEmpty, int minSize, int maxSize) {
		this(field, DEFAULT_ELEMENT_VALIDATOR, allowEmpty, minSize, maxSize);
	}

	public CollectionValidator(Field field, Validator elementValidator, boolean allowEmpty,
			int minSize, int maxSize) {
		super(field);
		if (null == elementValidator)
			throw new NullPointerException("elementValidator");
		if (minSize < 0)
			throw new IllegalArgumentException("minSize:" + minSize);
		if (minSize > maxSize)
			throw new IllegalArgumentException("minSize:" + minSize + " maxSize:" + maxSize);

		this.elementValidator = elementValidator;
		this.allowEmpty = allowEmpty;
		this.minSize = minSize;
		this.maxSize = maxSize;
	}

	@Override
	protected void doValidate(Object input) throws Exception {
		Class<?> fieldType = getField().getType();
		if (isCollection(fieldType)) {
			Collection<?> collection = (Collection<?>) getValue(input);
			if (null == collection || 0 == collection.size()) {
				if (!allowEmpty)
					throw new ConstraintViolationException(this);
			} else {
				int l = collection.size();
				if (l < minSize || l > maxSize)
					throw new ConstraintViolationException(this, "size:" + l);
			}

			validateElement(collection);
		} else if (isArray(fieldType)) {
			Object[] collection = (Object[]) getValue(input);
			if (null == collection || 0 == collection.length) {
				if (!allowEmpty)
					throw new ConstraintViolationException(this);
			} else {
				int l = collection.length;
				if (l < minSize || l > maxSize)
					throw new ConstraintViolationException(this, "size:" + l);
			}

			validateElement(collection);
		} else {
			throw new ValidateException("Unkown input:" + input);
		}
	}

	private void validateElement(Object[] collection) {
		if (elementValidator instanceof NoopValidator)
			return;
		for (Object element : collection) {
			elementValidator.validate(element);
		}
	}

	private void validateElement(Iterable<?> collection) {
		if (elementValidator instanceof NoopValidator)
			return;
		for (Object element : collection) {
			elementValidator.validate(element);
		}
	}

	private boolean isCollection(Class<?> fieldType) {
		return Collection.class.isAssignableFrom(fieldType);
	}

	private boolean isArray(Class<?> fieldType) {
		return Object[].class.isAssignableFrom(fieldType);
	}

	@Override
	public String getConstraintDesc() {
		return new StringBuilder(128).append(getFieldDesc()).append(
				(allowEmpty ? " collection allow Empty" : " collection not allow empty")).append(
				" size range:[" + minSize + "," + maxSize + "] elementValidator:"
						+ this.elementValidator).toString();
	}
}
