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
package org.bigmouth.nvwa.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class PathUtils {

    private static Set<Character> invalidChar = new HashSet<Character>();

    static {
        invalidChar.add('*');
        invalidChar.add('"');
        invalidChar.add('<');
        invalidChar.add('>');
        invalidChar.add('|');
        invalidChar.add(' ');
        invalidChar.add('\t');
    }

    public static boolean existsOfFolder(File folder) {
        if (!folder.exists())
            return folder.mkdirs();
        return true;
    }

    public static boolean existsOfFile(File file) throws IOException {
        if (!file.exists())
            return file.createNewFile();
        return true;
    }

    public static boolean isEqualsPath(String path1, String path2) {
        path1 = trimEndFileSeparator(path1).toLowerCase();
        path2 = trimEndFileSeparator(path2).toLowerCase();
        return path1.equals(path2);
    }

    public static boolean isNotEqualsPath(String path1, String path2) {
        return !isEqualsPath(path1, path2);
    }

    /**
     * 路径预处理, 替换路径分隔符为统一的"/", trim头尾空格, 如果路径不以"/"开头, 则添加"/"
     * 
     * @param path 目录路径
     * @return 预处理后的目录路径
     */
    public static String appendBeginFileSeparator(String path) {
        String afterPath = replacePathSeparator(path);
        if (!afterPath.startsWith("/")) {
            afterPath = "/" + afterPath;
        }
        return afterPath;
    }

    /**
     * 路径预处理, 替换路径分隔符为统一的"/", trim头尾空格, 截去开头的"/"
     * 
     * @param path 目录路径
     * @return 预处理后的目录路径
     */
    public static String trimBeginFileSeparator(String path) {
        String afterPath = replacePathSeparator(path);
        while (afterPath.startsWith("/")) {
            afterPath = afterPath.substring(1);
        }
        return afterPath;
    }

    /**
     * 路径预处理, 替换路径分隔符为统一的"/", trim头尾空格, 如果路径不以"/"结尾, 则追加"/"
     * 
     * @param path 目录路径
     * @return 预处理后的目录路径
     */
    public static String appendEndFileSeparator(String path) {
        String afterPath = replacePathSeparator(path);
        if (!afterPath.endsWith("/")) {
            afterPath = afterPath + "/";
        }
        return afterPath;
    }

    /**
     * 路径预处理, 替换路径分隔符为统一的"/", trim头尾空格, 截去末尾的"/"
     * 
     * @param path 目录路径
     * @return 预处理后的目录路径
     */
    public static String trimEndFileSeparator(String path) {
        String afterPath = replacePathSeparator(path);
        while (afterPath.endsWith("/")) {
            afterPath = afterPath.substring(0, afterPath.length() - 1);
        }
        return afterPath;
    }

    public static String replacePathSeparator(String path) {
        return path.trim().replaceAll("\\\\", "/");
    }

    /**
     * 判断指定路径是否包含非法字符
     * 
     * @param path 路径
     * @return
     */
    public static boolean containsInvalidCharacter(String path) {
        char[] charArray = path.trim().toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (invalidChar.contains(charArray[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取ClassPath路径
     * 
     * @return ClassPath路径
     */
    public static String getClassPath() {
        return PathUtils.class.getResource("/").getPath();
    }

    /**
     * 获取WebInf路径
     * 
     * @return WebInf路径
     */
    public static String getWebInfPath() {
        return StringUtils.substringBeforeLast(StringUtils.substringBeforeLast(getClassPath(), "/"), "/");
    }

    /**
     * 获取WebRoot路径
     * 
     * @return WebRoot路径
     */
    public static String getWebRootPath() {
        return StringUtils.substringBeforeLast(getWebInfPath(), "/");
    }
}
