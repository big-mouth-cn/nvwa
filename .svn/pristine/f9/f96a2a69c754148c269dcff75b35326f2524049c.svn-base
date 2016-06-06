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
package org.bigmouth.nvwa.pay;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

public interface PayDefaults {

    ContentType APPLICATION_XML = ContentType.create("application/xml", Consts.UTF_8);
    
    String SUCCESS = "SUCCESS";
    String FAIL = "FAIL";
    
    interface DigestType {
        
        String DSA = "DSA";
        String RSA = "RSA";
        String MD5 = "MD5";
    }
}
