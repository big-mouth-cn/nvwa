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
package org.bigmouth.nvwa.authority.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.bigmouth.nvwa.authority.dao.UserDao;
import org.bigmouth.nvwa.authority.entity.User;
import org.bigmouth.nvwa.authority.entity.UserSeletive;
import org.bigmouth.nvwa.mybatis.support.MyBatisServiceSupport;


public class UserServiceImpl extends MyBatisServiceSupport<User, Long, UserDao> implements UserService {

    private UserDao userDao;
    
    @Override
    public User select(String loginName) {
        UserSeletive seletive = new UserSeletive();
        seletive.setLoginName(loginName);
        List<User> rs = userDao.select(seletive);
        return CollectionUtils.isNotEmpty(rs) ? rs.get(0) : null;
    }

    @Override
    public User select(String loginName, String password) {
        UserSeletive seletive = new UserSeletive();
        seletive.setLoginName(loginName);
        seletive.setPassword(password);
        List<User> rs = userDao.select(seletive);
        return CollectionUtils.isNotEmpty(rs) ? rs.get(0) : null;
    }

    @Override
    protected UserDao getDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
