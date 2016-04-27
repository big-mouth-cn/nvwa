package org.bigmouth.nvwa.dpl.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodServiceFunctor extends AbstractMethodServiceSupport implements ServiceFunctor {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodServiceFunctor.class);

	public MethodServiceFunctor(Object target, Method method) {
		super(target, method);
	}

	public MethodServiceFunctor(Object target, String methodName) {
		super(target, methodName);
	}

	@Override
	public Object execute(Object... arguments) {
		if (null == arguments || arguments.length == 0) {
			return invokeMethod(EMPTY_ARGUMENTS);
		}
		return invokeMethod(arguments);
	}

	private Object invokeMethod(Object[] arguments) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Executing method " + getMethod() + " with arguments "
						+ Arrays.asList(arguments));
			}
			return getMethod().invoke(getTarget(), arguments);
		} catch (InvocationTargetException ite) {
			if (ite.getCause() instanceof RuntimeException) {
				throw (RuntimeException) ite.getCause();
			}
			throw new MethodInvocationException(getMethod(), ite);
		} catch (IllegalAccessException iae) {
			throw new MethodInvocationException(getMethod(), iae);
		}
	}

	@Override
	protected void checkLegalMethod(Method method) throws IllegalMethodException {
		if (!isPublic(method))
			throw new IllegalMethodException("method is not public", method);
		if (isVoid(method))
			throw new IllegalMethodException("method return type expect not void,but", method);
	}
}
