package org.bigmouth.nvwa.dpl.service;

import java.lang.reflect.Method;

public class IllegalMethodException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IllegalMethodException(String message, Method method, Throwable cause) {
		super(message + ",Illegal method: " + method, cause);
	}

	public IllegalMethodException(String message, Method method) {
		super(message + ",Illegal method: " + method);
	}
}
