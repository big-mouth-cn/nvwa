package org.bigmouth.nvwa.session.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;

public class SessionSupportFilter implements Filter {

	private static final String SESSION_FIELD_NAME = "sessionFieldName";
	private FilterConfig config;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String fn = config.getInitParameter(SESSION_FIELD_NAME);
		if (StringUtils.isBlank(fn))
			throw new RuntimeException("Can not found session field name.");
		// request.get
	}

	@Override
	public void init(FilterConfig fc) throws ServletException {
		this.config = fc;
	}

	@Override
	public void destroy() {
	}
}
