/*
 * Copyright 2015 mopote.com
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
package org.bigmouth.nvwa.mq;


/**
 * 消息延迟消费定义
 * 
 * @since 1.0
 * @author Allen Hu - (big-mouth.cn)
 */
public final class DelayTimeLevel {

    /** 不延迟 */
    public static final int NO_DELAY = 0;
    public static final int Second_1 = 1;
    public static final int Second_5 = 2;
    public static final int Second_10 = 3;
    public static final int Second_30 = 4;
    public static final int Minute_1 = 5;
    public static final int Minute_2 = 6;
    public static final int Minute_3 = 7;
    public static final int Minute_4 = 8;
    public static final int Minute_5 = 9;
    public static final int Minute_6 = 10;
    public static final int Minute_7 = 11;
    public static final int Minute_8 = 12;
    public static final int Minute_9 = 13;
    public static final int Minute_10 = 14;
    public static final int Minute_20 = 15;
    public static final int Minute_30 = 16;
    public static final int Hour_1 = 17;
    public static final int Hour_2 = 18;
    
    private DelayTimeLevel() {
    }
    
    /**
     * 转换成系统时间
     * 
     * @param level
     * @return
     */
    public static long ofSystemTimeInMillis(int level) {
        if (level == Second_1) {
            return System.currentTimeMillis() + 1 * 1000;
        }
        if (level == Second_5) {
            return System.currentTimeMillis() + 5 * 1000;
        }
        if (level == Second_10) {
            return System.currentTimeMillis() + 10 * 1000;
        }
        if (level == Second_30) {
            return System.currentTimeMillis() + 30 * 1000;
        }
        if (level == Minute_1) {
            return System.currentTimeMillis() + 1 * 60 * 1000;
        }
        if (level == Minute_2) {
            return System.currentTimeMillis() + 2 * 60 * 1000;
        }
        if (level == Minute_3) {
            return System.currentTimeMillis() + 3 * 60 * 1000;
        }
        if (level == Minute_4) {
            return System.currentTimeMillis() + 4 * 60 * 1000;
        }
        if (level == Minute_5) {
            return System.currentTimeMillis() + 5 * 60 * 1000;
        }
        if (level == Minute_6) {
            return System.currentTimeMillis() + 6 * 60 * 1000;
        }
        if (level == Minute_7) {
            return System.currentTimeMillis() + 7 * 60 * 1000;
        }
        if (level == Minute_8) {
            return System.currentTimeMillis() + 8 * 60 * 1000;
        }
        if (level == Minute_9) {
            return System.currentTimeMillis() + 9 * 60 * 1000;
        }
        if (level == Minute_10) {
            return System.currentTimeMillis() + 10 * 60 * 1000;
        }
        if (level == Minute_20) {
            return System.currentTimeMillis() + 20 * 60 * 1000;
        }
        if (level == Minute_30) {
            return System.currentTimeMillis() + 30 * 60 * 1000;
        }
        if (level == Hour_1) {
            return System.currentTimeMillis() + 1 * 60 * 60 * 1000;
        }
        if (level == Hour_2) {
            return System.currentTimeMillis() + 2 * 60 * 60 * 1000;
        }
        return System.currentTimeMillis();
    }
    
    /**
     * 转换成毫秒
     * 
     * @param level
     * @return
     */
    public static long ofTimeInMillis(int level) {
        if (level == Second_1) {
            return 1 * 1000;
        }
        if (level == Second_5) {
            return 5 * 1000;
        }
        if (level == Second_10) {
            return 10 * 1000;
        }
        if (level == Second_30) {
            return 30 * 1000;
        }
        if (level == Minute_1) {
            return 1 * 60 * 1000;
        }
        if (level == Minute_2) {
            return 2 * 60 * 1000;
        }
        if (level == Minute_3) {
            return 3 * 60 * 1000;
        }
        if (level == Minute_4) {
            return 4 * 60 * 1000;
        }
        if (level == Minute_5) {
            return 5 * 60 * 1000;
        }
        if (level == Minute_6) {
            return 6 * 60 * 1000;
        }
        if (level == Minute_7) {
            return 7 * 60 * 1000;
        }
        if (level == Minute_8) {
            return 8 * 60 * 1000;
        }
        if (level == Minute_9) {
            return 9 * 60 * 1000;
        }
        if (level == Minute_10) {
            return 10 * 60 * 1000;
        }
        if (level == Minute_20) {
            return 20 * 60 * 1000;
        }
        if (level == Minute_30) {
            return 30 * 60 * 1000;
        }
        if (level == Hour_1) {
            return 1 * 60 * 60 * 1000;
        }
        if (level == Hour_2) {
            return 2 * 60 * 60 * 1000;
        }
        return 0;
    }
}
