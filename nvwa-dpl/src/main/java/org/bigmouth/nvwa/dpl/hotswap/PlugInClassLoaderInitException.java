package org.bigmouth.nvwa.dpl.hotswap;

public class PlugInClassLoaderInitException extends RuntimeException {

	private static final long serialVersionUID = -7946236892627557868L;

	public PlugInClassLoaderInitException() {
		super();
	}

	public PlugInClassLoaderInitException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlugInClassLoaderInitException(String message) {
		super(message);
	}

	public PlugInClassLoaderInitException(Throwable cause) {
		super(cause);
	}
}
