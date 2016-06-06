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
package org.bigmouth.nvwa.network.http.utils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;

import com.google.common.collect.Lists;

/**
 * 
 * @since 1.0
 * @author Allen
 */
public class HttpRequest {

    public static final int DEFAULT_TIME_OUT = 5 * 60 * 1000;
    public static final String DEFAULT_CHARSET = "UTF-8";

    private Method method = Method.POST;
    private String url;
    private int timeout = DEFAULT_TIME_OUT;
    private String charset = DEFAULT_CHARSET;
    private List<Header> headers;
    private List<NameValuePair> pairs;
    private byte[] entity;
    private int expectStatusCode = HttpStatus.SC_OK;
    private boolean throwUnexpectStatusCode = false;

    public HttpRequest() {
        super();
    }

    public HttpRequest(String url) {
        super();
        this.url = url;
    }

    public HttpRequest(String url, List<NameValuePair> pairs) {
        super();
        this.url = url;
        this.pairs = pairs;
    }

    public HttpRequest(String url, byte[] entity) {
        super();
        this.url = url;
        this.entity = entity;
    }

    public HttpRequest(String url, List<Header> headers, List<NameValuePair> pairs) {
        super();
        this.url = url;
        this.headers = headers;
        this.pairs = pairs;
    }

    public HttpRequest(String url, List<Header> headers, byte[] entity) {
        super();
        this.url = url;
        this.headers = headers;
        this.entity = entity;
    }

    public HttpRequest(String url, List<Header> headers, List<NameValuePair> pairs, byte[] entity) {
        super();
        this.url = url;
        this.headers = headers;
        this.pairs = pairs;
        this.entity = entity;
    }

    public HttpRequest(Method method, String url, int timeout, String charset, List<Header> headers,
            List<NameValuePair> pairs, byte[] entity, int expectStatusCode) {
        super();
        this.method = method;
        this.url = url;
        this.timeout = timeout;
        this.charset = charset;
        this.headers = headers;
        this.pairs = pairs;
        this.entity = entity;
        this.expectStatusCode = expectStatusCode;
    }

    public void addHeader(Header header) {
        if (CollectionUtils.isEmpty(this.headers))
            this.headers = Lists.newArrayList();
        
        this.headers.add(header);
    }
    
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public List<NameValuePair> getPairs() {
        return pairs;
    }

    public void setPairs(List<NameValuePair> pairs) {
        this.pairs = pairs;
    }

    public byte[] getEntity() {
        return entity;
    }

    public void setEntity(byte[] entity) {
        this.entity = entity;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public int getExpectStatusCode() {
        return expectStatusCode;
    }

    public void setExpectStatusCode(int expectStatusCode) {
        this.expectStatusCode = expectStatusCode;
    }

    public boolean isThrowUnexpectStatusCode() {
        return throwUnexpectStatusCode;
    }

    public void setThrowUnexpectStatusCode(boolean throwUnexpectStatusCode) {
        this.throwUnexpectStatusCode = throwUnexpectStatusCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
