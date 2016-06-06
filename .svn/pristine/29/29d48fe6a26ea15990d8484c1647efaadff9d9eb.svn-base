/*
 * Copyright 2016 big-mouth.cn
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
package org.bigmouth.nvwa.zookeeper.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


public final class ZkPathUtils {

    private ZkPathUtils() {
    }
    
    public static enum DateUnit {
        
        MONTH, DATE, HOUR, MINUTE
    }
    
    public static String leftAppend(String primary, DateUnit unit) {
        SimpleDateFormat format = null;
        switch (unit) {
            case MONTH:
                format = new SimpleDateFormat("yyyyMM");
                break;
            case HOUR:
                format = new SimpleDateFormat("yyyyMMddHH");
                break;
            case MINUTE:
                format = new SimpleDateFormat("yyyyMMddHHmm");
                break;
            case DATE:
            default:
                format = new SimpleDateFormat("yyyyMMdd");
                break;
        }
        String date = format.format(new Date());
        return "/" + StringUtils.join(new String[] { date, primary}, '/');
    }
    
    /**
     * <pre>
     * e.g. 
     * group("O81982719823781212213"); "/O81/982/719/823/781/212/213"
     * 
     * </pre>
     * @param primary
     * @return
     */
    public static String group(String primary) {
        return group(primary, 3);
    }
    
    /**
     * <pre>
     * e.g. 
     * group("O81982719823781212213", 1); "/O/8/1/9/8/2/7/1/9/8/2/3/7/8/1/2/1/2/2/1/3"
     * group("O81982719823781212213", 2); "/O8/19/82/71/98/23/78/12/12/21/3"
     * group("O81982719823781212213", 3); "/O81/982/719/823/781/212/213"
     * group("O81982719823781212213", 4); "/O819/8271/9823/7812/1221/3"
     * group("O81982719823781212213", 5); "/O8198/27198/23781/21221/3"
     * 
     * </pre>
     * @param primary
     * @return
     */
    public static String group(String primary, int groupLen) {
        if (StringUtils.isBlank(primary))
            return null;
        
        char[] chars = primary.toCharArray();
        
        StringBuilder sb = new StringBuilder();
        int len = chars.length;
        
        for (int i = 0; i < len; i++) {
            if (i % groupLen == 0) {
                sb.append("/");
            }
            sb.append(chars[i]);
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        System.out.println(group("O819827198237812122131", 1));
        System.out.println(group("O819827198237812122131", 2));
        System.out.println(group("O819827198237812122135", 3));
        System.out.println(group("O8198271982378121221355", 4));
        System.out.println(group("O819827198237812122134100", 5));
        System.out.println(group("O81982719823781212213410", 5));
        System.out.println(group("O819827198237812122134109910", 5));
        System.out.println(leftAppend("O81982719823781", DateUnit.DATE));
    }
}
