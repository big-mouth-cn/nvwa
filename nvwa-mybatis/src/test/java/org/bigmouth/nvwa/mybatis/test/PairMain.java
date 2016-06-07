/*
 * Copyright 2016 big-mouth.cn
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
package org.bigmouth.nvwa.mybatis.test;

import java.util.List;

import org.bigmouth.nvwa.mybatis.page.PageInfo;
import org.bigmouth.nvwa.spring.SpringContextHolder;
import org.bigmouth.nvwa.spring.boot.SpringBootstrap;


public class PairMain {

    public static void main(String[] args) {
        SpringBootstrap.bootUsingSpring(new String[] {"applicationContext.xml"}, args);
        
        PairService service = SpringContextHolder.getBean("pairService");
        PageInfo<Pair> page = new PageInfo<Pair>(1, 2);
        List<Pair> queryAll = service.queryAll(page);
        System.out.println(page);
        System.out.println(queryAll);
    }
}
