package org.bigmouth.nvwa.dpl.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO:how to handler multiple method?
public class MethodServiceClosure extends AbstractMethodServiceSupport implements ServiceClosure {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodServiceClosure.class);

	public MethodServiceClosure(Object target, Method method) {
		super(target, method);
	}

	public MethodServiceClosure(Object target, String methodName) {
		super(target, methodName);
	}

	@Override
	public void execute(Object... arguments) {
		if (null == arguments || arguments.length == 0) {
			invokeMethod(EMPTY_ARGUMENTS);
			return;
		}
		invokeMethod(arguments);
	}

	private void invokeMethod(Object[] arguments) {
		Method method = getMethod();
		Object target = getTarget();
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Executing method " + method + " with arguments "
						+ Arrays.asList(arguments));
			}
			method.invoke(target, arguments);
		} catch (InvocationTargetException ite) {
			if (ite.getCause() instanceof RuntimeException) {
				throw (RuntimeException) ite.getCause();
			}
			throw new MethodInvocationException(method, ite);
		} catch (IllegalAccessException iae) {
			throw new MethodInvocationException(method, iae);
		}
	}

	@Override
	protected void checkLegalMethod(Method method) throws IllegalMethodException {
		if (!isPublic(method))
			throw new IllegalMethodException("method is not public", method);
		if (isVoid(method))
			throw new IllegalMethodException("method return type expect void,but", method);
	}
}
