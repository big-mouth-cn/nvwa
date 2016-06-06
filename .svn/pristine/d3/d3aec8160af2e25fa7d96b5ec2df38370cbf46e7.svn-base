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
package org.bigmouth.nvwa.zookeeper.observers;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.zookeeper.CreateMode;
import org.bigmouth.nvwa.utils.JsonHelper;
import org.bigmouth.nvwa.utils.StringHelper;
import org.bigmouth.nvwa.zookeeper.ZkClientHolder;
import org.bigmouth.nvwa.zookeeper.observers.exceptions.AlreadyExistException;
import org.bigmouth.nvwa.zookeeper.observers.exceptions.CharacterException;
import org.bigmouth.nvwa.zookeeper.observers.exceptions.ObserverException;

import com.google.common.collect.Maps;


/**
 * 
 * @author Allen Hu 
 * 2015-8-2
 */
public class SubjectPack {
    
    private final ZkClientHolder zkClientHolder;
    private final Map<String, Subject> SUBJECTS = Maps.newConcurrentMap();
    private SubjectConfig config = SubjectConfig.DEFAULT;
    
    public SubjectPack(ZkClientHolder zkClientHolder) {
        this.zkClientHolder = zkClientHolder;
    }
    
    public SubjectPack(ZkClientHolder zkClientHolder, Map<String, Subject> subjects) {
        this.zkClientHolder = zkClientHolder;
        if (MapUtils.isNotEmpty(subjects)) {
            for (Entry<String, Subject> subject : subjects.entrySet()) {
                addSubject(subject.getKey(), subject.getValue());
            }
        }
    }

    public void addSubject(String subjectType, Subject subject) {
        if (SUBJECTS.containsKey(subjectType)) {
            throw new AlreadyExistException("Subject type '" + subjectType + "' is already exist!");
        }
        if (StringHelper.contains(subjectType, "/")) {
            throw new CharacterException("subjectType cannot contain character '" + SubjectConfig.PREFIX + "'");
        }
        
        String path = config.getSubjectPath(subjectType);
        
        try {
            if (!exists(path)) {
                zkClientHolder.get().create().creatingParentsIfNeeded().forPath(path);
            }
            
            SUBJECTS.put(subjectType, subject);
        }
        catch (Exception e) {
            throw new ObserverException("addSubject:", e);
        }
    }

    private boolean exists(String path) throws Exception {
        return zkClientHolder.get().checkExists().forPath(path) != null;
    }
    
    public void notify(String subjectType, Object message) {
        String path = config.getPath(subjectType);
        try {
            String json = JsonHelper.convert(message);
            byte[] content = StringHelper.convert(json);
            zkClientHolder.get().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, content);
        }
        catch (Exception e) {
            throw new ObserverException("notify:", e);
        }
    }

    public void setConfig(SubjectConfig config) {
        this.config = config;
    }
}
