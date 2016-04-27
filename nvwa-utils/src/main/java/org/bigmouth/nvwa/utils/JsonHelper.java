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

import com.alibaba.fastjson.JSONObject;


public final class JsonHelper {

    private JsonHelper() {
    }
    
    public static String convert(Object object) {
        return JSONObject.toJSONString(object);
    }
    
    public static byte[] convert2bytes(Object object) {
        return StringHelper.convert(convert(object));
    }
    
    public static <T> T convert(String string, Class<T> clazz) {
        return JSONObject.parseObject(string, clazz);
    }
    
    public static <T> T convert(byte[] array, Class<T> clazz) {
        return JSONObject.parseObject(StringHelper.convert(array), clazz);
    }
}
