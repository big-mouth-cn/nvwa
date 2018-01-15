package org.bigmouth.nvwa.zkadmin.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.utils.StringHelper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AuthorizationInterceptor implements HandlerInterceptor {

	private final String username;
	private final String password;
	
	public AuthorizationInterceptor(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {

		String sessionAuth = (String) request.getSession().getAttribute("auth");
		if (sessionAuth == null) {
			if (!checkHeaderAuth(request, response)) {
				response.setStatus(401);
				response.setHeader("Cache-Control", "no-store");
				response.setDateHeader("Expires", 0);
				response.setHeader("WWW-authenticate", "Basic Realm=\"请输入授权账号密码\"");
				return false;
			}
		}
		return true;
	}
	
	private boolean checkHeaderAuth(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String auth = request.getHeader("Authorization");
		if ((auth != null) && (auth.length() > 6)) {
			auth = auth.substring(6, auth.length());
			
			byte[] decodeBase64 = Base64.decodeBase64(auth);
			String plaintext = StringHelper.convert(decodeBase64);
			if (StringUtils.equals(plaintext, username + ":" + password)) {
				request.getSession().setAttribute("auth", auth);
				
				return true;
			}
		}
		return false;
	}
}
