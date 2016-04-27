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

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.pay.PayDefaults;
import org.bigmouth.nvwa.pay.ability.Fail;
import org.bigmouth.nvwa.pay.ability.Success;
import org.bigmouth.nvwa.pay.config.wx.WxFeeType;
import org.bigmouth.nvwa.pay.config.wx.WxTradeType;
import org.bigmouth.nvwa.utils.JsonHelper;
import org.bigmouth.nvwa.utils.xml.Dom4jDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信支付回调处理请求结果
 * 
 * @author Allen Hu - (big-mouth.cn) 2015-8-6
 */
public class WxCallbackArgument implements Serializable, Success, Fail {

    private static final long serialVersionUID = 900047088595192404L;
    private static final Logger LOGGER = LoggerFactory.getLogger(WxCallbackArgument.class);

    private String returnCode;
    
    private String returnMsg;
    
    /** 微信分配的公众账号ID（企业号corpid即为此appId） */
    private String appId;

    /** 微信支付分配的商户号 */
    private String mchId;

    /** 微信支付分配的终端设备号 */
    private String deviceInfo;

    /** 随机字符串，不长于32位。 */
    private String nonceStr;

    /** 签名 */
    private String sign;

    /** 业务结果 */
    private String resultCode;

    /** 错误代码 */
    private String errCode;

    /** 错误代码描述 */
    private String errCodeDes;

    /** 用户在商户appid下的唯一标识 */
    private String openid;

    /** 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效 */
    private String isSubscribe;

    /** 微信交易类型 */
    private String tradeType = WxTradeType.JSAPI;

    /** 银行类型，采用字符串类型的银行标识 */
    private String bankType;

    /** 订单总金额，单位为分 */
    private int totalFee;

    /** 货币类型 */
    private String feeType = WxFeeType.CNY;

    /** 现金支付金额订单现金支付金额 */
    private int cashFee;

    /** 现金支付货币类型 */
    private String cashFeeType = WxFeeType.CNY;

    /** 代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额 */
    private int couponFee;

    /** 代金券或立减优惠使用数量 */
    private int couponCount;

    /** 微信支付订单号 */
    private String transactionId;

    /** 商户系统的订单号，与请求一致。 */
    private String outTradeNo;

    /** 商家数据包，原样返回 */
    private String attach;

    /** 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。 */
    private String timeEnd;

    @Override
    public String getStatusCode() {
        if (!StringUtils.equalsIgnoreCase(PayDefaults.SUCCESS, returnCode)) {
            return getReturnCode();
        }
        return getResultCode();
    }

    @Override
    public String getStatusMessage() {
        if (!StringUtils.equalsIgnoreCase(PayDefaults.SUCCESS, returnCode)) {
            return getReturnMsg();
        }
        return getErrCodeDes();
    }

    @Override
    public boolean isSuccess() {
        return StringUtils.equalsIgnoreCase(getReturnCode(), PayDefaults.SUCCESS) && StringUtils.equalsIgnoreCase(getResultCode(), PayDefaults.SUCCESS);
    }

    public static WxCallbackArgument of(String xml) {
        try {
            return Dom4jDecoder.decode(xml, "/xml", WxCallbackArgument.class);
        }
        catch (Exception e) {
            LOGGER.error("fromXML:", e);
        }
        return null;
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

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public int getCashFee() {
        return cashFee;
    }

    public void setCashFee(int cashFee) {
        this.cashFee = cashFee;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
    }

    public int getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(int couponFee) {
        this.couponFee = couponFee;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public String toString() {
        return JsonHelper.convert(this);
    }
}
