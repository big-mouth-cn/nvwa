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
package org.bigmouth.nvwa.spring;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;


public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext = null;

    private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

    /**
     * 实现ApplicationContextAware接口, 注入Context到静态变量中.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        if (SpringContextHolder.applicationContext != null) {
            logger.warn("applicationContext is override.");
        }
        SpringContextHolder.applicationContext = applicationContext; //NOSONAR
    }

    /**
     * 实现DisposableBean接口,在Context关闭时清理静态变量.
     * @Override
     */
    
    public void destroy() throws Exception {
        SpringContextHolder.cleanApplicationContext();

    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }
    
    public static void loadContext(String... configLocations) throws BeanDefinitionStoreException, IOException {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(
                (BeanDefinitionRegistry) getConfigurableApplicationContext().getBeanFactory());
        beanDefinitionReader.setResourceLoader(getApplicationContext());
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(getApplicationContext()));
        for (int i = 0; i < configLocations.length; i++) {
            beanDefinitionReader.loadBeanDefinitions(getApplicationContext().getResources(configLocations[i]));
        }
    }
    
    public static ConfigurableApplicationContext getConfigurableApplicationContext() {
        checkApplicationContext();
        return (ConfigurableApplicationContext) applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext has not found! Please defined SpringContexHolder in applicationContext.xml.");
        }
    }
    

    /**
     * 从 Spring 上下文获取Bean实例， BeanID 默认为类简单名称或首字母小写的类简单名称。
     * 
     * @param <T> 泛型类型
     * @param beanClass
     * @return Spring Bean 实例
     * @author Allen.Hu / 2012-6-29 
     * @since SkyMarket 1.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> beanClass) {
        Object obj = null;
        String simpleName = beanClass.getSimpleName();
        obj = getBean(simpleName);
        if (null == obj) {
            String beanId = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
            obj = getBean(beanId);
        }
        
        return (T) obj;
    }
}
