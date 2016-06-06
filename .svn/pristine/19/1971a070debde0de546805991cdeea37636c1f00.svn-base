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
package org.bigmouth.nvwa.authority.realm;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.bigmouth.nvwa.authority.entity.Resource;
import org.bigmouth.nvwa.authority.entity.Role;
import org.bigmouth.nvwa.authority.entity.User;
import org.bigmouth.nvwa.authority.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NvwaRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(NvwaRealm.class);
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        Object primaryPrincipal = arg0.getPrimaryPrincipal();
        if (null != primaryPrincipal && primaryPrincipal instanceof User) {
            User user = ((User) primaryPrincipal);
            SimpleAuthorizationInfo inf = new SimpleAuthorizationInfo();
            for (Role role : user.getRoles()) {
                inf.addRole(role.getIdentifying());
                
                for (Resource resource : role.getResources()) {
                    inf.addStringPermission(resource.getIdentifying());
                }
            }
            return inf;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        if (!(arg0 instanceof UsernamePasswordToken)) {
            LOGGER.warn("token must be instanceof UsernamePasswordToken!");
            return null;
        }
        UsernamePasswordToken upt = (UsernamePasswordToken) arg0;
        String username = upt.getUsername();
        String password = new String(upt.getPassword());
        User user = userService.select(username);
        if (null == user) {
            throw new UnknownAccountException();
        }
        if (!StringUtils.equals(password, user.getPassword())) {
            throw new IncorrectCredentialsException();
        }
        if (!user.isUseable()) {
            throw new DisabledAccountException();
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
