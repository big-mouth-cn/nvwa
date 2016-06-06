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
package org.bigmouth.nvwa.session.spring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.bigmouth.nvwa.session.Session;
import org.bigmouth.nvwa.session.SessionHolder;
import org.bigmouth.nvwa.session.SessionNotExistsException;
import org.bigmouth.nvwa.session.Trackable;
import org.bigmouth.nvwa.session.annotation.SessionTrackable;
import org.bigmouth.nvwa.session.annotation.SessionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import com.google.common.base.Preconditions;


/**
 * Session advisor
 * <pre>
 * &lt;bean class="org.bigmouth.nvwa.session.spring.SessionAdvisor" id="sessionAdvisor"&gt;&lt;/bean&gt;
 * 
 * &lt;bean class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator"&gt;
 *  &lt;property name="beanNames"&gt;
 *      &lt;list&gt;
 *          &lt;value&gt;*Service&lt;/value&gt;
 *      &lt;/list&gt;
 *  &lt;/property&gt;
 *  &lt;property name="interceptorNames"&gt;
 *      &lt;list&gt;
 *          &lt;value&gt;sessionAdvisor&lt;/value&gt;
 *      &lt;/list&gt;
 *  &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * @see org.bigmouth.nvwa.session.annotation.SessionValidator
 * @author Allen Hu - (big-mouth.cn) 
 * 2015-7-9
 */
public class SessionAdvisor implements MethodBeforeAdvice, AfterReturningAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionAdvisor.class);
    
    private final SessionHolder sessionHolder;
    
    /**
     * @param sessionHolder
     */
    public SessionAdvisor(SessionHolder sessionHolder) {
        Preconditions.checkNotNull(sessionHolder);
        this.sessionHolder = sessionHolder;
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        if (method.isAnnotationPresent(SessionValidator.class)) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Trackable trackable = getTrackable(args, parameterAnnotations);
            if (null != trackable) {
                Session session = sessionHolder.getSession(trackable);
                if (null == session) {
                    throw new SessionNotExistsException(trackable);
                }
            }
        }
    }

    private Trackable getTrackable(Object[] args, Annotation[][] parameterAnnotations) {
        Trackable trackable = null;
        if (ArrayUtils.isNotEmpty(parameterAnnotations)) {
            for (int i = 0, len = parameterAnnotations.length; i < len; i++) {
                Annotation[] annotations = parameterAnnotations[i];
                if (ArrayUtils.isEmpty(annotations)) {
                    continue;
                }
                else {
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof SessionTrackable) {
                            if (args[i] instanceof Trackable) {
                                trackable = (Trackable) args[i];
                                break;
                            }
                            else {
                                if (LOGGER.isWarnEnabled()) {
                                    LOGGER.warn("args[{}]'s java type must be org.bigmouth.nvwa.session.Trackable!", i);
                                }
                            }
                        }
                    }
                }
            }
        }
        return trackable;
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
    }
}
