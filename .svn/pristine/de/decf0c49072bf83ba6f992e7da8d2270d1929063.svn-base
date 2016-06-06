/*
 * Copyright 2015 big-mouth.cn
 *
 * The Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.bigmouth.nvwa.network.http;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.http.Header;

import com.google.common.collect.Lists;

public class Response implements Serializable {

    private static final long serialVersionUID = -7472362267202250497L;

    private List<Header> headers = Lists.newArrayList();

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }
    
    public void addHeader(Header e) {
        this.headers.add(e);
    }
    
    public void addSetCookie(List<Header> headers) {
        for (Header header : headers) {
            if (StringUtils.equals("Set-Cookie", header.getName())) {
                this.headers.add(header);
            }
        }
    }
    
    public void removeAllCookies() {
        List<Header> removes = Lists.newArrayList();
        for (Header header : headers) {
            if (StringUtils.equals("Set-Cookie", header.getName())) {
                removes.add(header);
            }
        }
        headers.removeAll(removes);
    }
    
    public void removeCookie(String key) {
        List<Header> removes = Lists.newArrayList();
        for (Header header : headers) {
            if (StringUtils.equals("Set-Cookie", header.getName())) {
                String value = header.getValue();
                if (StringUtils.isNotBlank(value) && 
                        StringUtils.startsWithIgnoreCase(value, key)) {
                    removes.add(header);
                }
            }
        }
        headers.removeAll(removes);
    }
    
    public void removeJSESSIONID() {
        removeCookie("JSESSIONID");
    }

    public String getCookie(String key) {
        for (Header header : headers) {
            if (StringUtils.equals("Set-Cookie", header.getName())) {
                String value = header.getValue();
                if (StringUtils.isNotBlank(value) && 
                        StringUtils.startsWithIgnoreCase(value, key)) {
                    // Because of the need to remove the effective range: path=/
                    String[] values = StringUtils.split(value, ";");
                    String[] jsessionid = values[0].split("=");
                    return (jsessionid.length >= 2) ? jsessionid[1] : null;
                }
            }
        }
        return null;
    }
    
    public String getJSESSIONID() {
        return getCookie("JSESSIONID");
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
