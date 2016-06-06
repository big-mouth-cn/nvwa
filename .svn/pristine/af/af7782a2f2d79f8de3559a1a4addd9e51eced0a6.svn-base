/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.asyncweb.common;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DefaultHttpRequestTest extends TestCase {
    
    public void testSetParametersFromQueryString() throws Exception {
        DefaultHttpRequest req = new DefaultHttpRequest();
        req.setParameters("a=b&c&d=%26&");

        Assert.assertEquals(3, req.getParameters().size());
        Assert.assertEquals("b", req.getParameter("a"));
        Assert.assertEquals("", req.getParameter("c"));
        Assert.assertEquals("&", req.getParameter("d"));

        req.setParameters("%00%e4=%f6%fc", "UTF-16BE");

        Assert.assertEquals(1, req.getParameters().size());
        Assert.assertEquals("\uf6fc", req.getParameter("\u00e4"));
    }
    
}
