package org.bigmouth.nvwa.dpl.service;

import java.lang.reflect.Method;

public class MethodInvocationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MethodInvocationException(Method method, Throwable cause) {
		super("Invoking method: " + method, cause);
	}
}
