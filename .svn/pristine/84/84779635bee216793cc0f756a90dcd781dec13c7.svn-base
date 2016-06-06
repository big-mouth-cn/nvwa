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
package org.bigmouth.nvwa.zookeeper.addrs;


/**
 * 地址读取器
 * 
 * @see org.bigmouth.nvwa.zookeeper.addrs.url.UrlAddressReader
 * @author Allen Hu 
 * 2016-3-17
 */
public interface AddressReader {

    /**
     * 读取地址
     * 
     * @return 正确有效的地址列表
     * @throws ReaderException 如果发生内部错误或获取到的地址无效则会抛出该异常
     */
    String read() throws ReaderException;
}
