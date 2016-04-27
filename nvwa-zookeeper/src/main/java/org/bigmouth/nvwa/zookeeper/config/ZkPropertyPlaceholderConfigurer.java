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
package org.bigmouth.nvwa.zookeeper.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.common.PathUtils;
import org.bigmouth.nvwa.spring.SpringContextProperty;
import org.bigmouth.nvwa.zookeeper.ZkClientHolder;
import org.bigmouth.nvwa.zookeeper.addrs.ReaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从ZooKeeper中读取内容并转换成Properties填充到Spring Properties上下文中。 基础文件中必须包含ZooKeeper的连接基础信息。
 * 
 * <pre>
 * "application.properties"
 * zookeeper.servers=172.16.3.24:2181
 * zookeeper.node.paths=/mopote/allen/mdum-urs,/mopote/allen/mdum-urs/server_1
 * 
 * &lt;bean class="org.bigmouth.nvwa.zookeeper.config.ZkPropertyPlaceholderConfigurer"&gt;
 *     &lt;property name="systemPropertiesModeName" value="SYSTEM_PROPERTIES_MODE_OVERRIDE" /&gt;
 *     &lt;property name="ignoreResourceNotFound" value="true" /&gt;
 *     &lt;property name="locations"&gt;
 *         &lt;list&gt;
 *             &lt;value&gt;classpath:application.properties&lt;/value&gt;
 *         &lt;/list&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * @author Allen Hu 2015-6-3
 */
public class ZkPropertyPlaceholderConfigurer extends SpringContextProperty {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkPropertyPlaceholderConfigurer.class);
    private static final String KEY_ZOOKEEPER_SERVERS = "zookeeper.servers";
    private static final String KEY_ZOOKEEPER_NODE_PATHS = "zookeeper.node.paths";
    private static final String PATH_SPLIT = ",";

    private static final String ITEM_SPLIT = "\r\n";
    private static final String PROPERTY_SPLIT = "=";

    private ZkClientHolder zkClientHolder;

    @Override
    protected void loadProperties(Properties props) throws IOException {
        super.loadProperties(props);
        fillRemoteProperties(props);
    }

    private void fillRemoteProperties(Properties props) {
        String cfg = props.getProperty(KEY_ZOOKEEPER_SERVERS);
        String servers = ReaderFactory.matching(cfg).read();
        if (StringUtils.isNotBlank(servers)) {
            zkClientHolder = new ZkClientHolder(servers);
            zkClientHolder.init();
        }
        if (null == zkClientHolder) {
            return;
        }
        CuratorFramework zk = zkClientHolder.get();
        if (null == zk) {
            return;
        }
        String paths = props.getProperty(KEY_ZOOKEEPER_NODE_PATHS);
        if (StringUtils.isNotBlank(paths)) {
            String[] nodePaths = paths.split(PATH_SPLIT);
            for (String path : nodePaths) {
                path = StringUtils.removeStart(path, "!");
                try {
                    PathUtils.validatePath(path);
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Loading properties file from ZooKeeper [{}]", path);
                    }
                    byte[] data = getData(zk, path);
                    if (!ArrayUtils.isEmpty(data)) {
                        Properties properties = convert(data);
                        props.putAll(properties);
                    }
                }
                catch (IllegalArgumentException e) {
                    LOGGER.warn("Illegal path: {}, Has been ignored!", path);
                }
            }
        }
    }

    private byte[] getData(CuratorFramework zk, String path) {
        try {
            return zk.getData().forPath(path);
        }
        catch (Exception e) {
            return null;
        }
    }

    private Properties convert(byte[] data) {
        Properties properties = new Properties();
        String string = null;
        try {
            string = new String(data, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            string = new String(data);
        }

        if (StringUtils.isNotBlank(string)) {
            String[] items = StringUtils.split(string, ITEM_SPLIT);
            for (String item : items) {
                if (StringUtils.isBlank(item)) {
                    continue;
                }
                if (StringUtils.startsWith(item, "#")) {
                    continue;
                }
                int index = StringUtils.indexOf(item, PROPERTY_SPLIT);
                String key = StringUtils.substring(item, 0, index);
                String value = StringUtils.substring(item, index + 1);
                properties.put(StringUtils.trim(key), StringUtils.trim(value));
            }
        }
        return properties;
    }

    public void destroy() {
        if (zkClientHolder != null) {
            zkClientHolder.destroy();
        }
    }
}
