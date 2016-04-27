/*
 * Copyright 2015
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
package org.bigmouth.nvwa.authority;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @since 1.0
 * @author Allen Hu - (big-mouth.cn)
 */
@Deprecated
public class NvwaAuthorityFilter extends PathMatchingFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NvwaAuthorityFilter.class);
    
    @Override
    public boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        if (request instanceof HttpServletRequest) {
            String uri = WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Access url: {}", uri);
            }
        }
        Subject subject = SecurityUtils.getSubject();
        subject.checkRole("admin");
        return true;
    }
}
