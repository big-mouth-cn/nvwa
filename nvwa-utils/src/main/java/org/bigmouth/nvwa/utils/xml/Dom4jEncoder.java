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
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bigmouth.nvwa.utils.Argument;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * XML编码帮助工具
 * 
 * @since 1.0
 * @author Allen
 */
public final class Dom4jEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dom4jEncoder.class);

    private Dom4jEncoder() {
    }

    /**
     * 将一个对象编码成XML字符串。<br>
     * 如果想指定节点的属性，可以使用{@linkplain org.bigmouth.nvwa.utils.Argument}注解来完成。
     * 
     * <pre>
     * e.g.
     * 
     * Object obj1 = new Object();
     * obj1.setName("Allen");
     * obj1.setOld(18);
     * obj1.setHomeAddr("Hangzhou");
     * obj1.setCellphon_no("10086");
     * 
     * String xml = Dom4jEncoder.encode(obj1, "/class/student");
     * System.out.println(xml);
     * 
     * -------------------------
     * | XML Result:
     * -------------------------
     * 
     * &lt;class&gt;
     *  &lt;student&gt;
     *      &lt;name&gt;Allen&lt;/Name&gt;
     *      &lt;old&gt;18&lt;/old&gt;
     *      &lt;home_addr&gt;Hangzhou&lt;/home_addr&gt;
     *      &lt;cellphone__no&gt;10086&lt;/cellphone__no&gt;
     *  &lt;/student&gt;
     * &lt;/class&gt;
     * 
     * </pre>
     * 
     * @param <T> 对象泛型
     * @param obj 需要进行编码的对象
     * @param xpath 根节点
     * @param itemNodeName 对象节点名称
     * @return
     * @see org.bigmouth.nvwa.utils.Argument
     */
    public static <T> String encode(T obj, String xpath) {
        if (null == obj)
            return null;
        List<T> list = Lists.newArrayList();
        list.add(obj);
        return encode(list, xpath, null);
    }

    /**
     * 将一个对象集合编码成XML字符串。<br>
     * 如果想指定节点的属性，可以使用{@linkplain org.bigmouth.nvwa.utils.Argument}注解来完成。
     * 
     * <pre>
     * e.g.
     * 
     * List&lt;Object&gt; list = new ArrayList&lt;Object&gt;();
     * 
     * Object obj1 = new Object();
     * obj1.setName("Allen");
     * obj1.setOld(18);
     * obj1.setHomeAddr("Hangzhou");
     * obj1.setCellphon_no("10086");
     * 
     * Object obj2 = new Object();
     * obj2.setName("Lulu");
     * obj2.setOld(16);
     * obj2.setHomeAddr("Hangzhou");
     * obj2.setCellphon_no("10086-1");
     * 
     * list.add(obj1);
     * list.add(obj2);
     * 
     * String xml = Dom4jEncoder.encode(list, "/class", "student");
     * System.out.println(xml);
     * 
     * -------------------------
     * | XML Result:
     * -------------------------
     * 
     * &lt;class&gt;
     *  &lt;student&gt;
     *      &lt;name&gt;Allen&lt;/Name&gt;
     *      &lt;old&gt;18&lt;/old&gt;
     *      &lt;home_addr&gt;Hangzhou&lt;/home_addr&gt;
     *      &lt;cellphone__no&gt;10086&lt;/cellphone__no&gt;
     *  &lt;/student&gt;
     *  &lt;student&gt;
     *      &lt;name&gt;Lulu&lt;/Name&gt;
     *      &lt;old&gt;16&lt;/old&gt;
     *      &lt;home_addr&gt;Hangzhou&lt;/home_addr&gt;
     *      &lt;cellphone__no&gt;10086-1&lt;/cellphone__no&gt;
     *  &lt;/student&gt;
     * &lt;/class&gt;
     * 
     * </pre>
     * 
     * @param <T> 对象泛型
     * @param objs 需要进行编码的集合
     * @param xpath 根节点
     * @param itemNodeName 对象节点名称
     * @return
     * @see org.bigmouth.nvwa.utils.Argument
     */
    public static <T> String encode(List<T> objs, String xpath, String itemNodeName) {
        if (CollectionUtils.isEmpty(objs))
            return null;
        if (StringUtils.isBlank(xpath) || StringUtils.equals(xpath, "/")) {
            throw new IllegalArgumentException("xpath cannot be blank or '/'!");
        }
        Document doc = DocumentHelper.createDocument();
        Element root = null;
        if (StringUtils.split(xpath, "/").length > 2) {
            root = DocumentHelper.makeElement(doc, xpath);
        }
        else {
            xpath = StringUtils.removeStart(xpath, "/");
            root = doc.addElement(xpath);
        }
        
        for (Object obj : objs) {
            addElement(itemNodeName, root, obj);
        }

        return doc.asXML();
    }

    private static void addElement(String itemNodeName, Element parent, Object obj) {
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        Element item = (StringUtils.isNotBlank(itemNodeName)) ? parent.addElement(itemNodeName) : parent;
        for (Field field : fields) {
            String name = field.getName();
            String nodename = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(name), "_").toLowerCase();
            if (field.isAnnotationPresent(Argument.class)) {
                nodename = field.getAnnotation(Argument.class).name();
            }
            Element node = item.addElement(nodename);

            String invokeName = StringUtils.join(new String[] { "get", StringUtils.capitalize(name) });
            try {
                Object value = MethodUtils.invokeMethod(obj, invokeName, new Object[0]);

                if (null != value) {
                    if (value instanceof String || value instanceof Double || value instanceof Float
                            || value instanceof Long || value instanceof Integer || value instanceof Short
                            || value instanceof Byte || value instanceof Boolean) {
                        node.setText(value.toString());
                    }
                    else {
                        addElement(null, node, value);
                    }
                }
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

    static class O {

        private String statusCode;
        private String status_message;
        @Argument(name = "class")
        private C c;

        /**
         * @param statusCode
         * @param status_message
         */
        public O(String statusCode, String status_message, C c) {
            super();
            this.statusCode = statusCode;
            this.status_message = status_message;
            this.c = c;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getStatus_message() {
            return status_message;
        }

        public void setStatus_message(String status_message) {
            this.status_message = status_message;
        }

        public C getC() {
            return c;
        }

        public void setC(C c) {
            this.c = c;
        }
    }

    static class C {

        private String cls;

        public C(String cls) {
            super();
            this.cls = cls;
        }

        public String getCls() {
            return cls;
        }

        public void setCls(String cls) {
            this.cls = cls;
        }
    }

    public static void main(String[] args) {
        List<O> l = Lists.newArrayList();
        l.add(new O("200", "100", new C("A08032")));
        l.add(new O("2300", "1200", new C("A08051")));
        String xml = encode(l, "/items", "item");
        System.out.println(xml);
    }
}
