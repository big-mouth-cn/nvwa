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
package org.bigmouth.nvwa.spring.boot;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.utils.JVMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.base.Preconditions;


public final class SpringBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(JVMUtils.class);
    
    private SpringBootstrap() {
    }
    
    public static ClassPathXmlApplicationContext bootUsingSpring(String[] contextFilePathes, String[] systemParameters) {
        return bootUsingSpring(JVMUtils.getFirstInvokeClassSimpleName(), contextFilePathes, systemParameters);
    }
    
    public static ClassPathXmlApplicationContext bootUsingSpring(String systemFlag, String[] contextFilePathes,
            String[] systemParameters) {
        Preconditions.checkArgument(StringUtils.isNotBlank(systemFlag), "systemFlag is blank.");
        Preconditions.checkArgument((!ArrayUtils.isEmpty(contextFilePathes))
                && (contextFilePathes.length > 0), "contextFilePathes is empty.");

        long beginMTime = System.currentTimeMillis();
        JVMUtils.setProperties(systemParameters);
        try {
            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(contextFilePathes);
//            ctx.registerShutdownHook();
            LOGGER.info(systemFlag + " boot in " + (System.currentTimeMillis() - beginMTime)
                    + " ms");
            return ctx;

        } catch (Exception e) {
            LOGGER.error(systemFlag + " boot occur error:", e);
            System.exit(-1);
            return null;
        }
    }
}
