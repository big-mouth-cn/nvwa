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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public final class HttpServletRequestHelper {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServletRequestHelper.class);
    public static final String UTF_8 = "UTF-8";
    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static byte[] getContentByteArray(HttpServletRequest request) throws IOException {
        if (null == request) {
            return new byte[0];
        }
        ByteArrayOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            inputStream = request.getInputStream();
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
            outputStream.flush();
            return outputStream.toByteArray();
        }
        finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }
    
    public static String getContentString(HttpServletRequest request) throws IOException {
        byte[] bytes = getContentByteArray(request);
        if (ArrayUtils.isNotEmpty(bytes)) {
            try {
                return new String(bytes, UTF_8);
            }
            catch (UnsupportedEncodingException e) {
                return new String(bytes);
            }
        }
        return null;
    }
    
    public static String read(BufferedReader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        return content.toString();
    }
    
    public static PrintWriter getPrintWriter(HttpServletResponse response) {
        try {
            response.setCharacterEncoding(UTF_8);
            response.setContentType("application/json; charset=" + UTF_8);
            return response.getWriter();
        }
        catch (IOException e) {
            LOGGER.error("getPrintWriter:", e);
        }
        return null;
    }

    public static void write(HttpServletResponse response, Object object) {
        PrintWriter printWriter = getPrintWriter(response);
        try {
            Preconditions.checkNotNull(printWriter);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(LONG_DATE_FORMAT)
                    .create();
            printWriter.write(gson.toJson(object));
            printWriter.flush();
        }
        finally {
            IOUtils.closeQuietly(printWriter);
        }
    }
}
