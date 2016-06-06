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
package org.bigmouth.nvwa.spring.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;
import org.bigmouth.nvwa.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.google.common.collect.Maps;


/**
 * 文件配置管理
 * 
 * @author Allen Hu - (big-mouth.cn) 
 * 2015-9-21
 */
public final class FileConfigurator extends BaseLifeCycleSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileConfigurator.class);
    private static final Map<String, byte[]> CONFS = Maps.newLinkedHashMap();
    private final Resource confDirectory;

    public FileConfigurator(Resource confDirectory) {
        this.confDirectory = confDirectory;
    }

    @Override
    protected void doInit() {
        if (null != confDirectory) {
            try {
                File dir = confDirectory.getFile();
                if (!dir.isDirectory()) {
                    LOGGER.warn("File [{}] does not directory path.", dir.getPath());
                    return;
                }
                
                Collection<File> files = FileUtils.listFiles(dir, null, true);
                for (File file : files) {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Load config file: {}", file.getPath());
                    }
                    String key = file.getAbsolutePath();
                    FileInputStream is = new FileInputStream(file);
                    byte[] buffer = new byte[is.available()];
                    IOUtils.readFully(is, buffer);
                    CONFS.put(convertKey(key), buffer);
                }
            }
            catch (IOException e) {
                LOGGER.error("init:", e);
            }
        }
    }

    @Override
    protected void doDestroy() {
    }
    
    private static String convertKey(String filePath) {
        filePath = PathUtils.replacePathSeparator(filePath);
        return StringUtils.replace(filePath, "/", "_");
    }
    
    public static byte[] getFile(String path) {
        path = convertKey(path);
        for (Entry<String, byte[]> entry : CONFS.entrySet()) {
            String key = entry.getKey();
            if (StringUtils.endsWith(key, path)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
