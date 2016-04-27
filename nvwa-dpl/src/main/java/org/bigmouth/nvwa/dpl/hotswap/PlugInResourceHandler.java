package org.bigmouth.nvwa.dpl.hotswap;

import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class PlugInResourceHandler extends URLStreamHandler {

	private static final String DEFAULT_PROTOCOL = "pluginresource:";
	private static final String DEFAULT_CONTENT_TYPE = "text/plain";

	private PlugInClassLoader plugInClassLoader;

	public PlugInResourceHandler(PlugInClassLoader plugInClassLoader) {
		super();
		this.plugInClassLoader = plugInClassLoader;
	}

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		String p = url.getProtocol();
		if (!isValid(p))
			throw new RuntimeException("invalid protocol,expect " + getProtocol() + ",but " + p);

		final String resource = url.getPath();
		return new URLConnection(url) {
			public void connect() {
			}

			public String getContentType() {
				FileNameMap fileNameMap = java.net.URLConnection.getFileNameMap();
				String contentType = fileNameMap.getContentTypeFor(resource);
				if (contentType == null)
					contentType = DEFAULT_CONTENT_TYPE;
				return contentType;
			}

			public InputStream getInputStream() throws IOException {
				InputStream is = plugInClassLoader.getByteStream(resource);

				if (is == null)
					throw new IOException("plugInClassLoader.getByteStream() returned null for "
							+ resource);

				return is;
			}
		};
	}

	protected static String getProtocol() {
		return DEFAULT_PROTOCOL;
	}

	private boolean isValid(String p) {
		return p.equals(getProtocol().substring(0, getProtocol().length() - 1));
	}

}
