package org.bigmouth.nvwa.dpl.service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractMethodServiceSupport {

	protected static final Object[] EMPTY_ARGUMENTS = new Object[0];

	private final Method method;
	private final Object target;

	public AbstractMethodServiceSupport(Object target, Method method) {
		if (null == target)
			throw new NullPointerException("target");
		if (null == method)
			throw new NullPointerException("method");
		checkLegalMethod(method);
		this.target = target;
		this.method = method;
	}

	public AbstractMethodServiceSupport(Object target, String methodName) {
		if (null == target)
			throw new NullPointerException("target");
		if (StringUtils.isBlank(methodName))
			throw new IllegalArgumentException("methodName is blank.");

		Method[] methods = null;
		Class<?> itr = target.getClass();
		while (!itr.equals(Object.class)) {
			methods = (Method[]) ArrayUtils.addAll(itr.getDeclaredMethods(), methods);
			itr = itr.getSuperclass();
		}

		Method targetMethod = null;
		for (Method methodItem : methods) {
			if (!isLegalMethod(methodItem))
				continue;
			if (methodItem.getName().equals(methodName)) {
				targetMethod = methodItem;
				break;
			}
		}
		if (null == targetMethod) {
			throw new NoSuchMethodException("method [" + target.getClass() + "." + methodName
					+ "] !NOT! exist.");
		}
		this.target = target;
		this.method = targetMethod;
	}

	protected boolean isLegalMethod(Method method) {
		try {
			checkLegalMethod(method);
			return true;
		} catch (IllegalMethodException e) {
			return false;
		}
	}

	protected abstract void checkLegalMethod(Method method) throws IllegalMethodException;

	protected Method getMethod() {
		return method;
	}

	protected Object getTarget() {
		return target;
	}

	protected boolean isPublic(Method method) {
		return Modifier.isPublic(method.getModifiers());
	}

	protected boolean isVoid(Method method) {
		return method.getReturnType() == Void.class;
	}
}
