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
package org.bigmouth.nvwa.zookeeper.addrs.fixedly;

import org.bigmouth.nvwa.zookeeper.addrs.AddressReader;
import org.bigmouth.nvwa.zookeeper.addrs.ReaderException;


/**
 * 不变的读取器，传入的值是什么就返回什么
 * 
 * @author Allen Hu 
 * 2016-3-17
 */
public class FixedlyAddressReader implements AddressReader {

    private final String value;
    
    public FixedlyAddressReader(String value) {
        this.value = value;
    }

    @Override
    public String read() throws ReaderException {
        return value;
    }
}
