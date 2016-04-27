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
package org.bigmouth.nvwa.pay.service.prepay.wx;

import org.bigmouth.nvwa.pay.config.PayConfig;
import org.bigmouth.nvwa.pay.config.PayConfigService;
import org.bigmouth.nvwa.pay.service.prepay.AbstractPrepay;
import org.bigmouth.nvwa.pay.service.prepay.PrepayInsideRequest;
import org.bigmouth.nvwa.pay.service.prepay.PrepayRequest;
import org.bigmouth.nvwa.pay.service.prepay.PrepayResponse;


public class WxPrepayServiceImpl extends AbstractPrepay {

    public WxPrepayServiceImpl(PayConfigService configService) {
        super(configService);
    }

    @Override
    protected PrepayInsideRequest getPayRequest(PrepayRequest argument, PayConfig config) {
        return WxPrepayInsideRequest.of(argument, config);
    }

    @Override
    protected PrepayResponse convert(byte[] array) {
        WxPrepayInsideResponse inside = WxPrepayInsideResponse.of(array);
        if (null == inside) {
            return null;
        }
        WxPrepayResponse response = new WxPrepayResponse();
        response.setSuccess(inside.isSuccess());
        response.setErrCode(inside.getStatusCode());
        response.setErrMsg(inside.getStatusMessage());
        response.setPrepayId(inside.getPrepayId());
        return response;
    }
}
