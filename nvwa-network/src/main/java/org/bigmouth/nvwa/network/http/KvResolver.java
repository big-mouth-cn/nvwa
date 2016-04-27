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

import java.net.URLDecoder;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Key=Value&Key=Value 解析器，可将字符串转换成对象
 * 
 * @since 1.0
 * @author Allen
 */
public final class KvResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(KvResolver.class);
    
    public static <T> T decode(String data, Class<T> template) {
        if (StringUtils.isBlank(data))
            throw new NullPointerException("data");
        if (template == null) {
            throw new NullPointerException("template");
        }
        T model = null;
        try {
            model = template.newInstance();

            int idx = 0;
            while (idx < data.length()) {
                int connectorPos = data.indexOf("&", idx);
                String subData = null;
                if (connectorPos < 0) {
                    subData = data.substring(idx);
                    connectorPos = data.length();
                }
                else {
                    subData = data.substring(idx, connectorPos);
                }
                int equalPos = subData.indexOf("=");
                String key = "";
                String value = "";
                if (equalPos < 0) {
                    key = URLDecoder.decode(subData, "UTF-8");
                    value = "";
                }
                else {
                    key = URLDecoder.decode(subData.substring(0, equalPos), "UTF-8");
                    value = URLDecoder.decode(subData.substring(equalPos + 1), "UTF-8");
                }
                BeanUtils.setProperty(model, key, value);
                idx = connectorPos + 1;
            }
            return model;
        }
        catch (Exception e) {
            LOGGER.error("decode: ", e);
            return null;
        }
    }
}
