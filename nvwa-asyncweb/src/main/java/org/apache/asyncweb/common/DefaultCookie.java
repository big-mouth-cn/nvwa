/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.asyncweb.common;

import java.util.concurrent.TimeUnit;

/**
 * Default cookie implementation.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class DefaultCookie implements MutableCookie {

    private static final long serialVersionUID = 5713305385740914300L;

    private final String name;

    private String value;

    private String domain;

    private String path;

    private String comment;

    private boolean secure;

    private boolean httpOnly;

    private int version = 0;

    private int maxAge = -1;
    
    private long createdDate = System.currentTimeMillis();

    /**
     * Creates a new cookie with a specified name.
     *
     * @param name the name of the new cookie
     */
    public DefaultCookie(String name) {
        if (name == null) {
    		throw new IllegalArgumentException("name can NOT be null");
        }

        this.name = name;
    }
    
    /**
     * Creates a new cookie with a specified name and value.
     * 
     * @param name  the name of the new cookie
     * @param value  the default value of the new cookie
     */
    public DefaultCookie(String name, String value) {
        this(name);
    	this.value = value;
    }
    
    /**
     * Creates a new cookie based on the values of the specified <tt>Cookie</tt>.
     * 
     * @param cookie  the <tt>Cookie</tt> object from which the new
     *                  instance of <tt>DefaultCookie</tt> will copy its values
     */
    public DefaultCookie(Cookie cookie) {
        this(cookie.getName());
        this.comment = cookie.getComment();
        this.createdDate = cookie.getCreatedDate();
        this.domain = cookie.getDomain();
        this.maxAge = cookie.getMaxAge();
        this.path = cookie.getPath();
        this.secure = cookie.isSecure();
        this.value = cookie.getValue();
        this.version = cookie.getVersion();
        this.httpOnly = cookie.isHttpOnly();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }
    
    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly= httpOnly;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }
    
    public long getCreatedDate() {
    	return createdDate;
    }
    
    public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

    public long getExpirationDate() {
    	return createdDate + TimeUnit.SECONDS.toMillis(maxAge);
    }
    
    /**
     * Builds the hash code of this object based on the <em>name</em>, <em>path</em>, and <em>domain</em> fields.
     */
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

    /**
     * Determines the equality of this cookie to another Cookie object based on the <em>name</em>, <em>path</em>, and
     * <em>domain</em> fields of the cookies. 
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Cookie)) {
            return false;
        }

        Cookie that = (Cookie) o;
        if (!name.equals(that.getName())) {
            return false;
        }

        if (path == null) {
            if (that.getPath() != null) {
                return false;
            }
        } else if (!path.equals(that.getPath())) {
            return false;
        }

        if (domain == null) {
            if (that.getDomain() != null) {
                return false;
            }
        } else if (!domain.equals(that.getDomain())) {
            return false;
        }

        return true;
    }

    public int compareTo(Cookie o) {
        int answer;

        // Compare the name first.
        answer = name.compareTo(o.getName());
        if (answer != 0) {
            return answer;
        }

        // and then path
        if (path == null) {
            if (o.getPath() != null) {
                answer = -1;
            } else {
                answer = 0;
            }
        } else {
            answer = path.compareTo(o.getPath());
        }

        if (answer != 0) {
            return answer;
        }

        // and then domain
        if (domain == null) {
            if (o.getDomain() != null) {
                answer = -1;
            } else {
                answer = 0;
            }
        } else {
            answer = domain.compareTo(o.getDomain());
        }

        return answer;
    }

    @Override
    public String toString() {
        return "name=" + getName() + " value=" + getValue() + " domain="
                + getDomain() + " path=" + getPath() + " maxAge=" + getMaxAge()
                + " secure=" + isSecure()+ " httpOnly="+isHttpOnly();
    }
}
