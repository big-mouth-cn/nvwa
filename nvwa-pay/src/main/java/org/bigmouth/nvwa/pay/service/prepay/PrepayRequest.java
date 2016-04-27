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
package org.bigmouth.nvwa.pay.service.prepay;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;


/**
 * 预支付请求参数
 * 
 * @author Allen Hu - (big-mouth.cn) 
 * 2015-8-6
 */
public class PrepayRequest implements Serializable {

    private static final long serialVersionUID = -6576854146839194016L;

    /** APPID */
    private String appId;
    /** 商品描述 */
    private String description;
    /** 商品详情 */
    private String detail;
    /** 附加参数 */
    private String attach;
    /** 商品金额。单位：分 */
    private int fee;
    /** 终端IP */
    private String ip;
    /** 通知地址 */
    private String notifyUrl;
    /** 用户标识 */
    private String openId;
    
    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(appId), "appId");
        Preconditions.checkArgument(StringUtils.isNotBlank(description), "description");
        Preconditions.checkArgument(fee > 0, "fee must > 0");
        Preconditions.checkArgument(StringUtils.isNotBlank(ip), "ip");
        Preconditions.checkArgument(StringUtils.isNotBlank(notifyUrl), "notifyUrl");
        Preconditions.checkArgument(StringUtils.isNotBlank(openId), "openId");
    }
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDetail() {
        return detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public String getAttach() {
        return attach;
    }
    
    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getFee() {
        return fee;
    }
    
    public void setFee(int fee) {
        this.fee = fee;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getNotifyUrl() {
        return notifyUrl;
    }
    
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
    
    public String getOpenId() {
        return openId;
    }
    
    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
