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
package org.bigmouth.nvwa.pay.config;

import org.bigmouth.nvwa.pay.exceptions.PayException;


/**
 * 支付配置异常
 * 
 * @author Allen Hu - (big-mouth.cn) 
 * 2015-8-6
 */
public class PayConfigException extends PayException {

    private static final long serialVersionUID = -637431325281176808L;

    /**
     * 
     */
    public PayConfigException() {
    }

    /**
     * @param message
     */
    public PayConfigException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public PayConfigException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public PayConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
