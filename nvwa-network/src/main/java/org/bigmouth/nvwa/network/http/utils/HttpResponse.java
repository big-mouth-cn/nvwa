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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.http.Header;

import com.google.common.collect.Lists;


/**
 * 
 * @since 1.0
 * @author Allen 
 */
public class HttpResponse {

    private List<Header> headers = Lists.newArrayList();
    private byte[] entity;
    private int statusCode;
    private String entityString;
    
    public void addAllHeaders(Header[] headers) {
        for (Header e : headers) {
            this.headers.add(e);
        }
    }
    
    public List<Header> getHeaders() {
        return headers;
    }
    
    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }
    
    public byte[] getEntity() {
        return entity;
    }
    
    public void setEntity(byte[] entity) {
        this.entity = entity;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getEntityString() {
        return entityString;
    }

    public void setEntityString(String entityString) {
        this.entityString = entityString;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
