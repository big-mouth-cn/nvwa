package org.apache.asyncweb.common;

import junit.framework.TestCase;

public class DefaultCookieTest extends TestCase {

    public void testCopyConstructor() throws Exception {
        // Cookie values
        final String name = "cookieName";
        final String value = "The cookie value goes here";
        final int version = -489243132;
        final String domain = "mina.apache.org";
        final String path = "/cookie/path";
        final boolean secure = true;
        final boolean httpOnly = true;
        final int maxAge = 324987;
        final String comment = "This is the cookie comment";
        final long createdDate = 437874235475L;

        // Create an instance of Cookie
        Cookie cookie = new Cookie() {
            public int getVersion() {
                return version;
            }
            public String getName() {
                return name;
            }
            public String getValue() {
                return value;
            }
            public String getDomain() {
                return domain;
            }
            public String getPath() {
                return path;
            }
            public boolean isSecure() {
                return secure;
            }
            public boolean isHttpOnly() {
                return httpOnly;
            }
            public int getMaxAge() {
                return maxAge;
            }
            public String getComment() {
                return comment;
            }
            public long getCreatedDate() {
                return createdDate;
            }

            public long getExpirationDate() {
                // This should be a calculated field so we'll just return 0
                return 0;
            }
            public int compareTo(Cookie o) {
                return -1;
            }
        };

        // Invoke copy constructor
        Cookie copy = new DefaultCookie(cookie);
        
        // Ensure all fields were copied successfully
        assertEquals(name, copy.getName());
        assertEquals(value, copy.getValue());
        assertEquals(version, copy.getVersion());
        assertEquals(domain, copy.getDomain());
        assertEquals(path, copy.getPath());
        assertEquals(secure, copy.isSecure());
        assertEquals(httpOnly, copy.isHttpOnly());
        assertEquals(maxAge, copy.getMaxAge());
        assertEquals(comment, copy.getComment());
        assertEquals(createdDate, copy.getCreatedDate());
    }

}
