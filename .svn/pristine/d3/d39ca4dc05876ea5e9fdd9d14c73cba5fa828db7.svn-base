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

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.bigmouth.nvwa.authority.dao.ResourceDao;
import org.bigmouth.nvwa.authority.entity.Resource;
import org.springframework.beans.factory.FactoryBean;

/**
 * 
 * @since 1.0
 * @author Allen Hu - (big-mouth.cn)
 */
public class ChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section> {

    private ResourceDao resourceDao;
    private String filterChainDefinitions;
    
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Override
    public Section getObject() throws Exception {
        // 获取所有Resource
        List<Resource> list = resourceDao.queryAll();
        Ini ini = new Ini();
        // 加载默认的url
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        // 循环Resource的url,逐个添加到section中。section就是filterChainDefinitionMap,
        // 里面的键就是链接URL,值就是存在什么条件才能访问该链接
        for (Iterator<Resource> it = list.iterator(); it.hasNext();) {
            Resource resource = it.next();
            // 如果不为空值添加到section中
            if (StringUtils.isNotEmpty(resource.getUrl()) && StringUtils.isNotEmpty(resource.getIdentifying())) {
                section.put(resource.getUrl(), MessageFormat.format(PREMISSION_STRING, resource.getIdentifying()));
            }
        }
        return section;
    }

    @Override
    public Class<?> getObjectType() {
        return this.getClass();
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }
}
