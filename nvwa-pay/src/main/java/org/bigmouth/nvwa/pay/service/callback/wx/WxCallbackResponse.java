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
package org.bigmouth.nvwa.pay.service.callback.wx;

import java.io.Serializable;

import org.bigmouth.nvwa.pay.PayDefaults;
import org.bigmouth.nvwa.pay.ability.ToXMLWrapper;
import org.bigmouth.nvwa.pay.service.callback.CallbackResponse;
import org.bigmouth.nvwa.utils.StringHelper;
import org.bigmouth.nvwa.utils.xml.Dom4jEncoder;


/**
 * 微信支付回调处理响应结果
 * 
 * @author Allen Hu - (big-mouth.cn) 
 * 2015-8-6
 */
public class WxCallbackResponse extends CallbackResponse {

    private static final long serialVersionUID = 6624958656268634906L;

    private WxCallbackArgument argument;
    private byte[] responseEntity;
    
    public WxCallbackArgument getArgument() {
        return argument;
    }
    
    public void setArgument(WxCallbackArgument argument) {
        this.argument = argument;
        this.responseEntity = getRespond();
    }
    
    public byte[] getResponseEntity() {
        return responseEntity;
    }
    
    private byte[] getRespond() {
        
        return (argument.isSuccess()) ? StringHelper.convert(new Respond().success().toXML()) 
                : StringHelper.convert(new Respond().fail().toXML());
    }
    
    public static class Respond implements Serializable, ToXMLWrapper {
        
        private static final long serialVersionUID = 1326520072891927633L;
        
        private String returnCode;
        private String returnMsg;
        
        public Respond() {
        }

        public Respond(String returnCode) {
            this.returnCode = returnCode;
        }

        public Respond(String returnCode, String returnMsg) {
            this.returnCode = returnCode;
            this.returnMsg = returnMsg;
        }

        public Respond success() {
            return new Respond(PayDefaults.SUCCESS);
        }
        
        public Respond fail() {
            return new Respond(PayDefaults.FAIL);
        }
        
        public String getReturnCode() {
            return returnCode;
        }
        
        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }
        
        public String getReturnMsg() {
            return returnMsg;
        }
        
        public void setReturnMsg(String returnMsg) {
            this.returnMsg = returnMsg;
        }

        @Override
        public String toXML() {
            return Dom4jEncoder.encode(this, "/xml");
        }
    }
}
