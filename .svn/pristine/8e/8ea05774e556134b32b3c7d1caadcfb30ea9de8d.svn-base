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
package org.bigmouth.nvwa.utils.xml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bigmouth.nvwa.utils.Argument;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * XML解码帮助工具
 * 
 * @since 1.0
 * @author Allen 
 */
public final class Dom4jDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dom4jDecoder.class);
    
    private Dom4jDecoder() {
    }
    
    /**
     * <p>从XML字符串中解析转换成对象</p>
     * 支持的规则有：
     * 如果对象中有属性“appId”
     * 那么会解析XML中节点为appId、appid、APPID、app_id、APP_ID的值。
     * @param <T>
     * @param xml
     * @param xpath
     * @param cls
     * @return
     */
    public static <T> T decode(String xml, String xpath, Class<T> cls) throws Exception {
        if (StringUtils.isBlank(xml))
            return null;
        T t = cls.newInstance();
        Document doc = DocumentHelper.parseText(xml);
        Node itemNode = doc.selectSingleNode(xpath);
        
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            // if the field name is 'appId'
            String name = field.getName();
            String nodename = name;
            if (field.isAnnotationPresent(Argument.class)) {
                nodename = field.getAnnotation(Argument.class).name();
            }
            // select appId node
            Node current = itemNode.selectSingleNode(nodename);
            if (null == current) {
                // select appid node
                current = itemNode.selectSingleNode(nodename.toLowerCase());
            }
            if (null == current) {
                // select APPID node
                current = itemNode.selectSingleNode(nodename.toUpperCase());
            }
            if (null == current) {
                // select app_id node
                nodename = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(nodename), "_").toLowerCase();
                current = itemNode.selectSingleNode(nodename);
            }
            if (null == current) {
                // select APP_ID node
                nodename = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(nodename), "_").toUpperCase();
                current = itemNode.selectSingleNode(nodename);
            }
            if (null != current) {
                String invokeName = StringUtils.join(new String[] { "set", StringUtils.capitalize(name) });
                try {
                    MethodUtils.invokeMethod(t, invokeName, current.getText());
                }
                catch (NoSuchMethodException e) {
                    LOGGER.warn("NoSuchMethod-" + invokeName);
                }
                catch (IllegalAccessException e) {
                    LOGGER.warn("IllegalAccess-" + invokeName);
                }
                catch (InvocationTargetException e) {
                    LOGGER.warn("InvocationTarget-" + invokeName);
                }
            }
        }
        return t;
    }
}
