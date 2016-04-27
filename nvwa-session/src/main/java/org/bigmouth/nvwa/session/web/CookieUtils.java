package org.bigmouth.nvwa.session.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bigmouth.nvwa.utils.StringHelper;


public final class CookieUtils {

    private CookieUtils() {
    }
    
    public static final String MSESSIONID = "MSESSIONID";
    
    public static String getSessionId(HttpServletRequest request, HttpServletResponse response) {
        Cookie result = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (MSESSIONID.equals(cookie.getName())) {
                    result = cookie;
                }
            }
            if (result != null) {
                return result.getValue();
            }
        }
        String id = StringHelper.uuid();
        addCookie(id, response);
        return id;
    }
    
    private static void addCookie(String id, HttpServletResponse response) {
        Cookie cookie = new Cookie(MSESSIONID, id);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
