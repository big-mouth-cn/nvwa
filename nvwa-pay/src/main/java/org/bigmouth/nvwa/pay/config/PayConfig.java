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
package org.bigmouth.nvwa.pay.config;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;


/**
 * 支付配置信息
 * 
 * @author Allen Hu - (big-mouth.cn) 
 * 2015-8-6
 */
public class PayConfig {

    /** APPID */
    private String appId;
    /** 商户号 */
    private String mchId;
    /** APP_SECRET */
    private String appSecret;
    /** APIKEY */
    private String apiKey;
    /** PKCS12证书文件 */
    private File pkcs12;
    /** 预支付地址 */
    private String urlPrepay;
    
    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(appId), "appId");
        Preconditions.checkArgument(StringUtils.isNotBlank(mchId), "mchId");
        Preconditions.checkArgument(StringUtils.isNotBlank(appSecret), "appSecret");
        Preconditions.checkArgument(null != pkcs12, "pkcs12");
        Preconditions.checkArgument(StringUtils.isNotBlank(urlPrepay), "urlPrepay");
    }
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getMchId() {
        return mchId;
    }
    
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
    
    public String getAppSecret() {
        return appSecret;
    }
    
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public File getPkcs12() {
        return pkcs12;
    }
    
    public void setPkcs12(File pkcs12) {
        this.pkcs12 = pkcs12;
    }

    public String getUrlPrepay() {
        return urlPrepay;
    }

    public void setUrlPrepay(String urlPrepay) {
        this.urlPrepay = urlPrepay;
    }
}
