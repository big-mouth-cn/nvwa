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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class HttpServletResponseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServletResponseHelper.class);
    private static final String CONTENT_TYPE = "application/octet-stream";
    
    private HttpServletResponseHelper() {
    }
    
    public static boolean doRespond(HttpServletResponse response, String data) {
        if (null == response)
            return false;
        if (StringUtils.isNotBlank(data))
        {
            PrintWriter out = null;
            try {
                response.setContentType(CONTENT_TYPE);
                out = response.getWriter();
                out.println(data);
                out.flush();
                return true;
            }
            catch (IOException e) {
                LOGGER.error("println:", e);
            }
            finally {
                IOUtils.closeQuietly(out);
            }
        }
        return false;
    }
    
    public static boolean doRespond(HttpServletResponse response, byte[] data) {
        if (null == response)
            return false;
        if (ArrayUtils.isNotEmpty(data))
        {
            PrintWriter out = null;
            try {
                response.setContentType(CONTENT_TYPE);
                out = response.getWriter();
                out.println(data);
                out.flush();
                return true;
            }
            catch (IOException e) {
                LOGGER.error("println:", e);
            }
            finally {
                IOUtils.closeQuietly(out);
            }
        }
        return false;
    }
}
