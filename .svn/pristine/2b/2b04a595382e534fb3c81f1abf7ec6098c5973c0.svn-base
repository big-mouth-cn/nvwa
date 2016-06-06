package org.bigmouth.nvwa.validate.factory;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bigmouth.nvwa.validate.Validator;


public class SoftRefValidatorFactory implements ValidatorFactory {

	private final ValidatorFactory factoryImpl;

	private final ConcurrentMap<Class<?>, SoftReference<Validator>> validators = new ConcurrentHashMap<Class<?>, SoftReference<Validator>>();

	public SoftRefValidatorFactory(ValidatorFactory factoryImpl) {
		super();
		if (null == factoryImpl)
			throw new NullPointerException("factoryImpl");
		this.factoryImpl = factoryImpl;
	}

	@Override
	public Validator create(Class<?> beanClass) {
		if (null == beanClass)
			throw new NullPointerException("beanClass");
		SoftReference<Validator> sr = validators.get(beanClass);
		if (null == sr) {
			Validator validator = factoryImpl.create(beanClass);
			sr = new SoftReference<Validator>(validator);
			validators.put(beanClass, sr);
		}
		Validator validator = sr.get();
		if (null == validator) {
			validator = factoryImpl.create(beanClass);
			sr = new SoftReference<Validator>(validator);
			validators.put(beanClass, sr);
		}

		return validator;
	}
}
