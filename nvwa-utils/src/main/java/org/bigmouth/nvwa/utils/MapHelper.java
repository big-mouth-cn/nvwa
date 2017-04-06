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
package org.bigmouth.nvwa.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @since 1.0
 * @author Allen 
 */
public class MapHelper {

    private static final Logger LOG = LoggerFactory.getLogger(MapHelper.class);
    
    public static String toURI(Map<String, String> params) {
        if (MapUtils.isEmpty(params))
            return StringUtils.EMPTY;
        
        StringBuilder s = new StringBuilder();
        for (Entry<String, String> e : params.entrySet()) {
            String value = e.getValue();
            s.append(e.getKey()).append("=").append(value).append("&");
        }
        return StringUtils.removeEnd(s.toString(), "&");
    }
    
    public static <T> T packaging(Map<String, ?> map, Class<T> cls) {
        try {
            T t = cls.newInstance();
            for (Entry<String, ?> entry : map.entrySet()) {
                String key = entry.getKey();
                String setName = StringUtils.join(new String[] { "set", StringUtils.capitalize(key) });
                try {
                    MethodUtils.invokeMethod(t, setName, entry.getValue());
                }
                catch (NoSuchMethodException e) {
                    LOG.warn("NoSuchMethod: {} of {}", setName, cls);
                }
                catch (InvocationTargetException e) {
                    LOG.warn("InvocationTarget: {} of {}", setName, cls);
                }
            }
            return t;
        }
        catch (InstantiationException e) {
            LOG.warn("newInstantce - InstantiationException: {}", e.getMessage());
        }
        catch (IllegalAccessException e) {
            LOG.warn("newInstantce - IllegalAccessException: {}", e.getMessage());
        }
        return null;
    }
}
