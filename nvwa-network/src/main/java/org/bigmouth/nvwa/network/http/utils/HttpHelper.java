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
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.Lists;


/**
 * 
 * @since 1.0
 * @author Allen 
 */
public class HttpHelper {

    public static NameValuePair[] toArray(Map<String, String> params) {
        if (MapUtils.isEmpty(params))
            return new NameValuePair[0];
        
        List<NameValuePair> pairs = Lists.newArrayList();
        for (Entry<String, String> e : params.entrySet()) {
            String value = e.getValue();
            
            pairs.add(new BasicNameValuePair(e.getKey(), value));
        }
        
        return pairs.toArray(new NameValuePair[0]);
    }
}
