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

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

public class StringHelper extends StringUtils {

    public static final char[] CODE_ARRAY = new char[] {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    }; 
    
    public static String random(char[] scope, int len) {
        if (ArrayUtils.isEmpty(scope))
            scope = CODE_ARRAY;
        StringBuilder str = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            str.append(scope[RandomUtils.nextInt(scope.length)]);
        }
        return str.toString();
    }
    
    public static String random(int len) {
        return random(CODE_ARRAY, len);
    }
    
    public static String uuid() {
        return UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
    }
    
    public static String randomInt(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RandomUtils.nextInt(10));
        }
        return sb.toString();
    }
    
    public static String convert(byte[] b) {
        if (ArrayUtils.isEmpty(b))
            return null;
        try {
            return new String(b, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            return new String(b);
        }
    }

    public static byte[] convert(String string) {
        if (StringUtils.isBlank(string))
            return null;
        try {
            return string.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            return string.getBytes();
        }
    }

    /**
     * Unicode字符编码转普通字符串。
     * 
     * <pre>
     * StringHelper.unicode2native("\\u8ba2\\u8d2d\\u5931\\u8d25"); = "订购失败"
     * </pre>
     * @param str
     * @return
     */
    public static String unicode2native(String str) {
        char aChar;
        int len = str.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = str.charAt(x++);
            if (aChar == '\\') {
                aChar = str.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = str.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                }
                else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            }
            else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
