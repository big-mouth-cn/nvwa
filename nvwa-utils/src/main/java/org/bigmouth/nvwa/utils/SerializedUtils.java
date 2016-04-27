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

import org.apache.commons.lang.StringUtils;


/**
 * 
 * @since 1.0
 * @author Allen
 */
public class SerializedUtils {

    private static int seed = 0;
    private static final Object object = new Object();
    private static final String PATTERN = "yyyyMMddHHmmss";

    public static String genAbstract() {
        return genAbstract("O", PATTERN);
    }
    
    public static String genAbstract(String prefix) {
        return genAbstract(prefix, PATTERN);
    }
    
    public static String genAbstract(String prefix, String pattern) {
        int tmpCount = 1;
        synchronized (object) {
            seed++;
            if (seed > 999) {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    ;
                }
                seed = 1;
            }
            tmpCount = seed;
        }
        String strCount = "00" + tmpCount;
        strCount = strCount.substring(strCount.length() - 3);
        String strtime = DateUtils.getCurrentStringDate(pattern);
        return prefix + strtime + strCount;
    }
    
    private static final Object object2 = new Object();
    private static long seed2 = 0;
    
    public static String generate() {
        long tmp = 1;
        synchronized (object2) {
            seed2++;
            if (seed2 > 999999999999999l) {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    ;
                }
                seed2 = 1;
            }
            tmp = seed2;
        }
        return DateUtils.getCurrentStringDate("yyyyMMddHHmmssSSS") + StringUtils.leftPad(String.valueOf(tmp), 15, "0");
    }
}
