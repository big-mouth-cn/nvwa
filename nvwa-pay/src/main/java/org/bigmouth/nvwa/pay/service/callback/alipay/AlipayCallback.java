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

import org.bigmouth.nvwa.pay.PayProvider;
import org.bigmouth.nvwa.pay.service.callback.Callback;
import org.bigmouth.nvwa.pay.service.callback.CallbackAnalysisException;
import org.bigmouth.nvwa.pay.service.callback.CallbackException;
import org.bigmouth.nvwa.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 支付宝回调处理
 * 
 * @author Allen Hu - (big-mouth.cn) 
 * 2015-8-8
 */
public class AlipayCallback implements Callback<AlipayCallbackRequest, AlipayCallbackResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayCallback.class);
    
    @Override
    public AlipayCallbackResponse callback(AlipayCallbackRequest request) {
        return callback(request.getUri());
    }

    public AlipayCallbackResponse callback(String uri) {
        if (StringHelper.isBlank(uri)) {
            throw new CallbackException("uri has be blank!");
        }
        AlipayCallbackArgument args = AlipayCallbackArgument.of(uri);
        if (null == args) {
            
            throw new CallbackAnalysisException();
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Receive callback from Alipay, Request parameters are [{}]", args);
        }
        AlipayCallbackResponse response = new AlipayCallbackResponse();
        response.setArgument(args);
        return response;
    }
    
    @Override
    public PayProvider getProvider() {
        return PayProvider.ALIPAY;
    }
}
