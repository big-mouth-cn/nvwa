package org.apache.asyncweb.common.integration;

import java.nio.charset.Charset;
import java.util.Set;

import org.apache.asyncweb.common.Cookie;
import org.apache.asyncweb.common.DefaultHttpRequest;
import org.apache.asyncweb.common.HttpHeaderConstants;
import org.apache.asyncweb.common.HttpResponse;
import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.asyncweb.common.MutableHttpRequest;

public class SimpleHttpTest extends TomcatTest {

    public void testHelloWorld() throws Exception {
        // Send request
        MutableHttpRequest request = new DefaultHttpRequest();
        request.setRequestUri(getBaseURI().resolve("/helloworld.jsp"));
        request.normalize();
        session.write(request);
        
        // Wait for response
        HttpResponse response = (HttpResponse) session.read().await().getMessage();
        
        // Test response
        assertEquals(HttpResponseStatus.OK, response.getStatus());
        assertEquals(ResponseOutput.HELLO_WORLD, response.getContent().getString(Charset.defaultCharset().newDecoder()));
    }
    
    public void testRedirectResponse() throws Exception {
        // Send request
        MutableHttpRequest request = new DefaultHttpRequest();
        request.setRequestUri(getBaseURI().resolve("/redirect.jsp"));
        request.normalize();
        session.write(request);
        
        // Wait for response
        HttpResponse response = (HttpResponse) session.read().await().getMessage();
        
        // Test response
        assertEquals(HttpResponseStatus.FOUND, response.getStatus());
        assertEquals(getBaseURI().resolve("/helloworld.jsp").toString(), response.getHeader(HttpHeaderConstants.KEY_LOCATION));
    }

    public void testCookieResponse() throws Exception {
        // Send request
        MutableHttpRequest request = new DefaultHttpRequest();
        request.setRequestUri(getBaseURI().resolve("/cookie.jsp"));
        request.normalize();
        session.write(request);
        
        // Wait for response
        HttpResponse response = (HttpResponse) session.read().await().getMessage();
        // Test response
        System.out.println(response.getHeaders());
        assertEquals(HttpResponseStatus.OK, response.getStatus());
        Set<Cookie> cookies = response.getCookies();
        assertEquals(2, cookies.size());

	    Cookie cookie = findCookie(ResponseOutput.COOKIE_NAME, cookies);
		assertNotNull(cookie);
        assertEquals(ResponseOutput.COOKIE_NAME, cookie.getName());
        assertEquals(ResponseOutput.COOKIE_VALUE, cookie.getValue());
	    assertTrue(Math.abs(ResponseOutput.COOKIE_MAX_AGE - cookie.getMaxAge()) < 100); // Make sure the cookie max age is close (because of difference from date decoding
    }

	private Cookie findCookie(String name, Iterable<Cookie> cookies) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name)) {
				return cookie;
			}
		}
		return null;
	}
    
}
