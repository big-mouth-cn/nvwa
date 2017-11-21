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
package org.bigmouth.nvwa.pay.service.prepay.wx;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.pay.config.PayConfig;
import org.bigmouth.nvwa.pay.config.wx.WxFeeType;
import org.bigmouth.nvwa.pay.config.wx.WxPayConfig;
import org.bigmouth.nvwa.pay.config.wx.WxTradeType;
import org.bigmouth.nvwa.pay.service.prepay.PrepayInsideRequest;
import org.bigmouth.nvwa.pay.service.prepay.PrepayRequest;
import org.bigmouth.nvwa.utils.StringHelper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 微信支付请求参数
 * 
 * @author Allen Hu - (big-mouth.cn) 2015-8-6
 */
public class WxPrepayInsideRequest extends PrepayInsideRequest {

    private static final long serialVersionUID = -8818542885709909599L;
    private static final Logger LOGGER = LoggerFactory.getLogger(WxPrepayInsideRequest.class);

    /** 微信分配的公众账号ID（企业号corpid即为此appId） */
    private String appId;

    /** 微信支付分配的商户号 */
    private String mchId;

    /** 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB" */
    private String deviceInfo;

    /** 随机字符串，不长于32位。 */
    private String nonceStr;

    /** 签名 */
    private String sign;

    /** 商品或支付单简要描述 */
    private String body;

    /** 商品名称明细列表 */
    private String detail;

    /** 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据 */
    private String attach;

    /** 商户系统内部的订单号,32个字符内、可包含字母 */
    private String outTradeNo;

    /** 货币类型 */
    private String feeType = WxFeeType.CNY;

    /** 订单总金额，只能为整数。单位：分 */
    private int totalFee;

    /** APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。 */
    private String spbillIp;

    /** 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010 */
    private String timeStart;

    /**
     * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。 <b>注意：最短失效时间间隔必须大于5分钟</b>
     */
    private String timeExpire;

    /** 商品标记，代金券或立减优惠功能的参数 */
    private String goodsTag = "WXG";

    /** 接收支付异步通知回调地址 */
    private String notifyUrl;

    /** 微信交易类型 */
    private String tradeType = WxTradeType.JSAPI;

    /** tradeType=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。 */
    private String productId;

    /** 指定支付方式。no_credit--指定不能使用信用卡支付 */
    private String limitPay;

    /** trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。 */
    private String openId;
    
    public static WxPrepayInsideRequest of(PrepayRequest arg, PayConfig config) {
        if (null == arg) 
            return null;
        if (null == config)
            return null;
        arg.validate();
        config.validate();
        WxPrepayInsideRequest req = new WxPrepayInsideRequest();
        req.setAppId(arg.getAppId());
        req.setMchId(config.getMchId());
        req.setNonceStr(StringHelper.uuid());
        req.setBody(arg.getDescription());
        req.setOutTradeNo(arg.getOutTradeNo());
        req.setTotalFee(arg.getFee());
        req.setSpbillIp(arg.getIp());
        req.setNotifyUrl(arg.getNotifyUrl());
        req.setOpenId(arg.getOpenId());
        req.setDetail(arg.getDetail());
        req.setAttach(arg.getAttach());
        req.setSign(sign(req, config.getAppSecret()));
        return req;
    }
    
    public static String sign(WxPrepayInsideRequest request, String appSecret) {
        Preconditions.checkArgument(StringUtils.isNotBlank(appSecret), "appSecret");
        List<String> $arguments = $arguments(request);
        Collections.sort($arguments);
        $arguments.add(StringUtils.join(new String[] { "key", appSecret }, "="));
        String join = StringUtils.join($arguments.toArray(new String[0]), "&");
        return DigestUtils.md5Hex(join).toUpperCase();
    }
    
    public static List<String> $arguments(PrepayInsideRequest obj) {
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        List<String> parameters = Lists.newArrayList();
        for (Field field : fields) {
            String name = field.getName();
            try {
                StringBuilder str = new StringBuilder();
                String invokeName = StringUtils.join(new String[] {
                        "get",
                        StringUtils.capitalize(name)
                });
                Method method = cls.getMethod(invokeName);
                Object result = method.invoke(obj);
                if (null == result || 
                        (result instanceof String && StringUtils.isBlank(result.toString()))) {
                    continue;
                }
                str.append(name).append("=").append(result.toString());
                parameters.add(str.toString());
            }
            catch (NoSuchMethodException e) {
                ;
            }
            catch (Exception e) {
                LOGGER.warn("$arguments-(" + name + "):" + e.getMessage());
            }
        }
        return parameters;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getFeeType() {
        return feeType;
    }
    
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
    
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpbillIp() {
        return spbillIp;
    }

    public void setSpbillIp(String spbillIp) {
        this.spbillIp = spbillIp;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
    
    public String getTradeType() {
        return tradeType;
    }

    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(getAppId()), "appId");
        Preconditions.checkArgument(StringUtils.isNotBlank(getMchId()), "mchId");
        Preconditions.checkArgument(StringUtils.isNotBlank(getNonceStr()), "nonceStr");
        Preconditions.checkArgument(StringUtils.isNotBlank(getSign()), "sign");
        Preconditions.checkArgument(StringUtils.isNotBlank(getBody()), "body");
        Preconditions.checkArgument(StringUtils.isNotBlank(getOutTradeNo()), "outTradeNo");
        Preconditions.checkArgument(getTotalFee() > 0, "totalFee must be > 0");
        Preconditions.checkArgument(StringUtils.isNotBlank(getSpbillIp()), "spbillIp");
        Preconditions.checkArgument(StringUtils.isNotBlank(getNotifyUrl()), "notifyUrl");
        Preconditions.checkArgument(StringUtils.isNotBlank(getTradeType()), "tradeType");
        if (StringUtils.equals(WxTradeType.NATIVE, getTradeType()))
            Preconditions.checkArgument(StringUtils.isNotBlank(getProductId()), "productId");
        if (StringUtils.equals(WxTradeType.JSAPI, getTradeType()))
            Preconditions.checkArgument(StringUtils.isNotBlank(getOpenId()), "openId");
        
    }
    
    public String toXML() {
        Document doc = DocumentHelper.createDocument();
        Element xml = doc.addElement("xml");
        if (StringUtils.isNotBlank(getAppId()))
            xml.addElement("appid").setText(getAppId());
        if (StringUtils.isNotBlank(getMchId()))
            xml.addElement("mch_id").setText(getMchId());
        if (StringUtils.isNotBlank(getDeviceInfo()))
            xml.addElement("device_info").setText(getDeviceInfo());
        if (StringUtils.isNotBlank(getNonceStr()))
            xml.addElement("nonce_str").setText(getNonceStr());
        if (StringUtils.isNotBlank(getSign()))
            xml.addElement("sign").setText(getSign());
        if (StringUtils.isNotBlank(getBody()))
            xml.addElement("body").setText(getBody());
        if (StringUtils.isNotBlank(getDetail()))
            xml.addElement("detail").setText(getDetail());
        if (StringUtils.isNotBlank(getAttach()))
            xml.addElement("attach").setText(getAttach());
        if (StringUtils.isNotBlank(getOutTradeNo()))
            xml.addElement("out_trade_no").setText(getOutTradeNo());
        if (null != getFeeType())
            xml.addElement("fee_type").setText(getFeeType());
        xml.addElement("total_fee").setText(String.valueOf(getTotalFee()));
        if (StringUtils.isNotBlank(getSpbillIp()))
            xml.addElement("spbill_create_ip").setText(getSpbillIp());
        if (StringUtils.isNotBlank(getTimeStart()))
            xml.addElement("time_start").setText(getTimeStart());
        if (StringUtils.isNotBlank(getTimeExpire()))
            xml.addElement("time_expire").setText(getTimeExpire());
        if (StringUtils.isNotBlank(getGoodsTag()))
            xml.addElement("goods_tag").setText(getGoodsTag());
        if (StringUtils.isNotBlank(getNotifyUrl()))
            xml.addElement("notify_url").setText(getNotifyUrl());
        if (null != getTradeType())
            xml.addElement("trade_type").setText(getTradeType());
        if (StringUtils.isNotBlank(getProductId()))
            xml.addElement("product_id").setText(getProductId());
        if (StringUtils.isNotBlank(getLimitPay()))
            xml.addElement("limit_pay").setText(getLimitPay());
        if (StringUtils.isNotBlank(getOpenId()))
            xml.addElement("openid").setText(getOpenId());
        return doc.asXML();
    }
    
    public static void main(String[] args) {
        PrepayRequest arg = new PrepayRequest();
        arg.setAppId("WxPayConfig");
        arg.setAttach("1");
        arg.setDescription("desc");
        arg.setDetail("detail");
        arg.setFee(100);
        arg.setIp("127.0.0.1");
        arg.setNotifyUrl("http://");
        arg.setOpenId("wx910298309123");
        WxPayConfig config = new WxPayConfig();
        config.setApiKey("bhP7hP2AgCLanOCXmytX8HDKG92vmT6l");
        config.setAppId("wx71bdaee829cc2b8d");
        config.setAppSecret("46e5bd2eeafe116ab54683525c6c4625");
        config.setMchId("1235638902");
        config.setUrlPrepay("https://api.mch.weixin.qq.com/pay/unifiedorder");
        config.setPkcs12(new File("D:\\f.xml"));
        System.out.println(WxPrepayInsideRequest.of(arg, config).getSign());
    }
}
