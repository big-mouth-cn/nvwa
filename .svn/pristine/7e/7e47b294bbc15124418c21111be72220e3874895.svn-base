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

import org.apache.curator.utils.PathUtils;
import org.apache.curator.utils.ZKPaths;


/**
 * 
 * @author Allen Hu 
 * 2015-8-2
 */
public final class SubjectConfig {

    public static final String SUBJECT_PATH = "/observers/subject";
    public static final String PREFIX = "P-";
    
    public static final SubjectConfig DEFAULT = new SubjectConfig();
    
    private String subjectRootPath = SUBJECT_PATH;
    
    public String getSubjectPath(String subjectType) {
        String path = ZKPaths.makePath(subjectRootPath, subjectType);
        PathUtils.validatePath(path);
        return path;
    }
    
    public String getPath(String subjectType) {
        String path = getSubjectPath(subjectType);
        path = ZKPaths.makePath(path, PREFIX);
        PathUtils.validatePath(path);
        return path;
    }

    public void setSubjectRootPath(String subjectRootPath) {
        this.subjectRootPath = subjectRootPath;
    }
}
