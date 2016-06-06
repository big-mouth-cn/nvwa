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
package org.bigmouth.nvwa.pay.service.callback.alipay;

import org.bigmouth.nvwa.pay.service.callback.CallbackResponse;
import org.bigmouth.nvwa.utils.StringHelper;

/**
 * 支付宝回调处理结果
 * 
 * @author Allen Hu - (big-mouth.cn) 2015-8-8
 */
public class AlipayCallbackResponse extends CallbackResponse {

    private static final long serialVersionUID = 3807446253085764491L;

    private AlipayCallbackArgument argument;

    private byte[] responseEntity;

    public AlipayCallbackArgument getArgument() {
        return argument;
    }

    public void setArgument(AlipayCallbackArgument argument) {
        this.argument = argument;
        this.responseEntity = getRespond(argument);
    }

    public byte[] getResponseEntity() {
        return responseEntity;
    }

    private byte[] getRespond(AlipayCallbackArgument args) {
        return (args.isSuccess()) ? StringHelper.convert("success") : StringHelper.convert("fail");
    }
}
