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

import javax.servlet.http.HttpServletResponse;

import org.bigmouth.nvwa.network.http.HttpServletResponseHelper;
import org.bigmouth.nvwa.utils.JsonHelper;


/**
 * Do respond of JSON.
 * 
 * @since 1.0
 * @author Allen 
 */
public class JsonResponseSupport implements ResponseWrapper {

    protected void doSuccess(HttpServletResponse response) {
        doSuccess(response, null);
    }
    
    protected void doSuccess(HttpServletResponse response, Object object) {
        doRespond(response, JsonResponse.SUCCESS, "", object);
    }
    
    protected void doFail(HttpServletResponse response) {
        doFail(response, "");
    }
    
    protected void doFail(HttpServletResponse response, String fail) {
        doFail(response, JsonResponse.FAIL, fail);
    }
    
    protected void doFail(HttpServletResponse response, int statusCode) {
        doFail(response, statusCode, "");
    }
    
    protected void doFail(HttpServletResponse response, int statusCode, String fail) {
        doFail(response, statusCode, fail, null);
    }
    
    protected void doFail(HttpServletResponse response, int statusCode, String fail, Object object) {
        doRespond(response, statusCode, fail, object);
    }
    
    protected void doRespond(HttpServletResponse response, int statusCode, String fail, Object object) {
        HttpServletResponseHelper.doRespond(response, JsonHelper.convert(new JsonResponse(statusCode, fail, object)));
    }
}
