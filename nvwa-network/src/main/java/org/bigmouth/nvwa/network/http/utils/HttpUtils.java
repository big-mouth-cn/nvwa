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
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.bigmouth.nvwa.network.http.HttpClientHelper;
import org.bigmouth.nvwa.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpUtils
 * 
 * @since 1.0
 * @author Allen
 */
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    public static HttpResponse post(HttpRequest request) throws UnexpectStatusCodeException, HttpException {
        if (null == request) {
            throw new NullPointerException("request");
        }
        request.setMethod(Method.POST);
        return access(request);
    }

    public static HttpResponse get(HttpRequest request) throws UnexpectStatusCodeException, HttpException {
        if (null == request) {
            throw new NullPointerException("request");
        }
        request.setMethod(Method.GET);
        return access(request);
    }

    private static HttpResponse access(HttpRequest request) throws UnexpectStatusCodeException, HttpException {
        String url = request.getUrl();
        if (StringUtils.isBlank(url))
            throw new IllegalArgumentException("url");

        HttpClient http = HttpClientHelper.http(request.getTimeout());
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("请求访问-{}", url);
            }
            Method method = request.getMethod();
            HttpRequestBase requestBase = null;
            List<NameValuePair> pairs = request.getPairs();
            List<Header> headers = request.getHeaders();
            if (method == Method.POST) {
                HttpPost post = new HttpPost(url);
                byte[] entity = request.getEntity();
                if (!CollectionUtils.isEmpty(pairs)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("请求设置参数-{}", pairs);
                    }
                    HttpClientHelper.addPair(post, pairs.toArray(new NameValuePair[0]));
                }
                if (!ArrayUtils.isEmpty(entity)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("请求体设置数据-{}", entity);
                    }
                    HttpClientHelper.addByteArrayEntity(post, entity);
                }
                requestBase = post;
            }
            else if (method == Method.GET) {
                HttpGet get = new HttpGet(url);
                if (!CollectionUtils.isEmpty(pairs)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("请求参数-{}", pairs);
                    }
                    HttpClientHelper.addPair(get, pairs.toArray(new NameValuePair[0]));
                }
                requestBase = get;
            }
            else {
                throw new IllegalArgumentException("Not supported method type: " + method);
            }

            if (!CollectionUtils.isEmpty(headers)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("设置请求头-{}", headers);
                }
                for (Header header : headers) {
                    requestBase.addHeader(header);
                }
            }

            long start = System.currentTimeMillis();
            org.apache.http.HttpResponse httpResponse = http.execute(requestBase);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("请求耗时 {} 毫秒", (System.currentTimeMillis() - start));
            }
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != request.getExpectStatusCode()) {
                if (request.isThrowUnexpectStatusCode())
                    throw new UnexpectStatusCodeException(statusCode);
            }

            byte[] entity = HttpClientHelper.getResponse(httpResponse);
            Header[] allHeaders = httpResponse.getAllHeaders();
            HttpResponse hrp = new HttpResponse();
            hrp.setEntity(entity);
            hrp.addAllHeaders(allHeaders);
            hrp.setEntityString(StringHelper.convert(entity));
            hrp.setStatusCode(statusCode);
            return hrp;
        }
        catch (Exception e) {
            throw new HttpException(e);
        }
        finally {
            http.getConnectionManager().shutdown();
        }
    }
}
