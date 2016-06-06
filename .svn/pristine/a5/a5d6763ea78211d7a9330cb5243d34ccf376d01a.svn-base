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
package org.bigmouth.nvwa.network.http.response;


/**
 * Response Entity of JSON.
 * 
 * @since 1.0
 * @author Allen 
 */
public class JsonResponse {

    public static final int SUCCESS = 0;
    public static final int FAIL = -1;
    
    private int statusCode = SUCCESS;
    private String fail;
    private Object data;
    
    public JsonResponse(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public JsonResponse(Object data) {
        this.data = data;
    }

    public JsonResponse(int statusCode, String fail) {
        this.statusCode = statusCode;
        this.fail = fail;
    }

    public JsonResponse(int statusCode, String fail, Object data) {
        this.statusCode = statusCode;
        this.fail = fail;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getFail() {
        return fail;
    }
    
    public void setFail(String fail) {
        this.fail = fail;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
}
