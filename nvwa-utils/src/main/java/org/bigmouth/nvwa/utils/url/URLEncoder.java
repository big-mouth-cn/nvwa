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
package org.bigmouth.nvwa.utils.url;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bigmouth.nvwa.utils.Argument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


/**
 * 
 * @since 1.0
 * @author Allen 
 */
public final class URLEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLEncoder.class);
    
    public static void main(String[] args) {
        List<String> list = Lists.newArrayList();
        list.add("v=1.1");
        list.add("account=福建代理001");
        list.add("action=getBalance");
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        ;
        String sign = DigestUtils.md5Hex(StringUtils.join(list, "&") + "&key=88868b9d511241c1ba48cfbf705e430f");
        System.out.println(sign);
    }
    
    public static String encode(Object obj) {
        return encode(obj, true);
    }
    
    public static String encode(Object obj, boolean sort) {
        return encode(obj, "&", sort, null);
    }
    
    public static String encode(Object obj, String jointChar) {
        return encode(obj, jointChar, true);
    }
    
    public static String encode(Object obj, String jointChar, boolean sort) {
        return encode(obj, jointChar, sort, null);
    }
    
    public static String encode(Object obj, String jointChar, boolean sort, Comparator<String> comparator) {
        StringBuilder rst = new StringBuilder();
        List<String> list = Lists.newArrayList();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String paramName = fieldName;
            if (field.isAnnotationPresent(Argument.class)) {
                paramName = field.getAnnotation(Argument.class).name();
            }
            String invokeName = StringUtils.join(new String[] { "get", StringUtils.capitalize(fieldName) });
            try {
                Object value = MethodUtils.invokeMethod(obj, invokeName, new Object[0]);
                if (null != value)
                    list.add(StringUtils.join(new String[] {paramName, String.valueOf(value)}, "="));
            }
            catch (NoSuchMethodException e) {
                LOGGER.warn("NoSuchMethod-" + invokeName);
            }
            catch (IllegalAccessException e) {
                LOGGER.warn("IllegalAccess-" + invokeName);
            }
            catch (InvocationTargetException e) {
                LOGGER.warn("InvocationTarget-" + invokeName);
            }
        }
        if (sort)
            Collections.sort(list, (null != comparator) ? comparator : String.CASE_INSENSITIVE_ORDER);
        
        for (String item : list) {
            rst.append(item).append(jointChar);
        }
        return StringUtils.removeEnd(rst.toString(), jointChar);
    }
}
