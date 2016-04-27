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
package org.bigmouth.nvwa.zookeeper.addrs.url;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.network.http.utils.HttpException;
import org.bigmouth.nvwa.network.http.utils.HttpRequest;
import org.bigmouth.nvwa.network.http.utils.HttpResponse;
import org.bigmouth.nvwa.network.http.utils.HttpUtils;
import org.bigmouth.nvwa.zookeeper.addrs.AddressReader;
import org.bigmouth.nvwa.zookeeper.addrs.ReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


/**
 * 从可访问的URL中读取地址
 * <pre>
 * e.g.
 * 
 * ----- Code -----
 * AddressReader reader = new UrlAddressReader("http://www.big-mouth.cn/zkaddrs");
 * System.out.println(reader.read());
 * 
 * ----- Result -----
 * "192.168.1.100:2181,192.168.1.101:2181,192.168.1.102:2181"
 * </pre>
 * @author Allen Hu 
 * 2016-3-17
 */
public class UrlAddressReader implements AddressReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlAddressReader.class);
    public static final String REGX = "(http|https):\\/\\/.*";
    private static final char SPLIT_CHAR = ',';
    private final String url;
    
    public UrlAddressReader(String url) {
        Preconditions.checkArgument(StringUtils.isNotBlank(url));
        if (!url.matches(REGX)) {
            throw new RuntimeException("Invalid url! " + url);
        }
        this.url = url;
    }

    @Override
    public String read() {
        String[] urls = StringUtils.split(url, SPLIT_CHAR);
        String addrs = null;
        for (String url : urls) {
            addrs = read(url);
        }
        if (StringUtils.isBlank(addrs))
            throw new ReaderException("empty!");
        return StringUtils.trim(addrs);
    }

    private String read(String url) {
        try {
            HttpResponse resp = HttpUtils.get(new HttpRequest(url));
            String addrs = resp.getEntityString();
            return StringUtils.isBlank(addrs) ? null : StringUtils.trim(addrs);
        }
        catch (HttpException e) {
            LOGGER.error("read:", e);
            return null;
        }
    }
}
