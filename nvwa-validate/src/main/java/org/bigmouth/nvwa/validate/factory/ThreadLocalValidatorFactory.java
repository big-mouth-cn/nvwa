package org.bigmouth.nvwa.validate.factory;

import java.util.Map;

import org.bigmouth.nvwa.validate.Validator;

import com.google.common.collect.Maps;

public class ThreadLocalValidatorFactory implements ValidatorFactory {

	private final ThreadLocal<Map<Class<?>, Validator>> localData = new ThreadLocal<Map<Class<?>, Validator>>() {

		@Override
		protected Map<Class<?>, Validator> initialValue() {
			return Maps.newHashMap();
		}
	};

	private final ValidatorFactory factoryImpl;

	public ThreadLocalValidatorFactory(ValidatorFactory factoryImpl) {
		super();
		if (null == factoryImpl)
			throw new NullPointerException("factoryImpl");
		this.factoryImpl = factoryImpl;
	}

	@Override
	public Validator create(Class<?> beanClass) {
		if (null == beanClass)
			throw new NullPointerException("beanClass");
		Map<Class<?>, Validator> m = localData.get();
		if (null == m)
			m = Maps.newHashMap();
		Validator validator = m.get(beanClass);
		if (null == validator) {
			validator = factoryImpl.create(beanClass);
			m.put(beanClass, validator);
		}
		return validator;
	}

	public void shutdown() {
		localData.remove();
	}
}
