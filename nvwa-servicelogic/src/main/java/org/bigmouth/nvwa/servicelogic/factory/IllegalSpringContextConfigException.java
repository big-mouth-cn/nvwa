package org.bigmouth.nvwa.servicelogic.factory;

public class IllegalSpringContextConfigException extends PlugInDiscoverException {

	private static final long serialVersionUID = -8708349488954148490L;

	public IllegalSpringContextConfigException() {
		super();
	}

	public IllegalSpringContextConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalSpringContextConfigException(String message) {
		super(message);
	}

	public IllegalSpringContextConfigException(Throwable cause) {
		super(cause);
	}
}
