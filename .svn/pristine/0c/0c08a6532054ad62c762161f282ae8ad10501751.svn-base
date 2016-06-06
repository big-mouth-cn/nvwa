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

import java.io.Serializable;

import org.bigmouth.nvwa.pay.ability.Fail;
import org.bigmouth.nvwa.pay.ability.Success;
import org.bigmouth.nvwa.utils.JsonHelper;
import org.bigmouth.nvwa.utils.url.URLDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 支付宝支付回调处理请求结果
 * 
 * @author Allen Hu - (big-mouth.cn) 2015-8-8
 */
public class AlipayCallbackArgument implements Serializable, Success, Fail {

    private static final long serialVersionUID = 7555916166170814406L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayCallbackArgument.class);

    /** 通知的发送时间。格式为 yyyy-MM-dd HH:mm:ss。 */
    private String notifyTime;

    /** 通知的类型。 */
    private String notifyType;

    /** 通知校验 ID。 */
    private String notifyId;

    /** DSA、RSA、MD5 三个值可选，必须大写。 */
    private String signType;

    private String sign;

    // //////////////// Business argument /////////////////

    /** 商户网站唯一订单号 */
    private String outTradeNo;

    /** 商品名称 */
    private String subject;

    /** 支付类型 */
    private String paymentType;

    /** 支付宝交易号 */
    private String tradeNo;

    /** 交易状态 */
    private String tradeStatus;

    /** 交易创建时间 */
    private String gmtCreate;

    /** 交易付款时间 */
    private String gmtPayment;

    /** 交易关闭时间 */
    private String gmtClose;

    /** 退款状态 */
    private String refundStatus;

    /** 卖家支付宝账号，可以是Email 或手机号码。 */
    private String sellerEmail;

    /** 买家支付宝账号，可以是Email 或手机号码。 */
    private String buyerEmail;

    /** 卖家支付宝账号对应的支付宝唯一用户号。以 2088开头的纯16位数字。 */
    private String sellerId;

    /** 买家支付宝账号对应的支付宝唯一用户号。以 2088开头的纯16位数字。 */
    private String buyerId;

    /**
     * <p>
     * 商品单价
     * </p>
     * 如果请求时使用的是 total_fee，那么 price 等于 total_fee；如果请求时使用的 是 price，那么对应请求时的 price 参数，原样通知回来。
     */
    private float price;

    /**
     * <p>
     * 交易金额
     * </p>
     * 该笔订单的总金额。 请求时对应的参数，原样通知 回来。
     */
    private float totalFee;

    /**
     * <p>
     * 购买数量
     * </p>
     * 如果请求时使用的是 total_fee，那么 quantity 等于 1；如果请求时使用的是 quantity，那么对应请求时的 quantity 参数，原样通知回 来。
     */
    private int quantity;

    /**
     * <p>
     * 商品描述
     * </p>
     * 该笔订单的备注、描述、明细 等。 对应请求时的 body 参数，原 样通知回来。
     */
    private String body;

    /**
     * <p>
     * 折扣
     * </p>
     * 支付宝系统会把 discount 的 值加到交易金额上，如果需要 折扣，本参数为负数。
     */
    private int discount;

    /** 该交易是否调整过价格。 */
    private String isTotalFeeAdjust;

    /**
     * 是否在交易过程中使用了红 包。
     */
    private String useCoupon;

    /**
     * 公用回 传参数
     */
    private String extraCommonParam;
    /** 支付渠道组合信息 */
    private String outChannelType;
    /** 支付金额组合信息 */
    private String outChannelAmount;
    /** 实际支付渠道 */
    private String outChannelInst;
    /**
     * <p>
     * 是否扫码支付
     * </p>
     * 回传给商户此标识为 qrpay 时，表示对应交易为扫码支 付。 目前只有 qrpay 一种回传值。 非扫码支付方式下，目前不会 返回该参数。
     */
    private String businessScene;

    public static AlipayCallbackArgument of(String uri) {
        try {
            return URLDecoder.decode(uri, AlipayCallbackArgument.class);
        }
        catch (Exception e) {
            LOGGER.error("of:", e);
        }
        return null;
    }
    
    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public String getStatusCode() {
        return null;
    }

    @Override
    public String getStatusMessage() {
        return null;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtPayment() {
        return gmtPayment;
    }

    public void setGmtPayment(String gmtPayment) {
        this.gmtPayment = gmtPayment;
    }

    public String getGmtClose() {
        return gmtClose;
    }

    public void setGmtClose(String gmtClose) {
        this.gmtClose = gmtClose;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(float totalFee) {
        this.totalFee = totalFee;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getIsTotalFeeAdjust() {
        return isTotalFeeAdjust;
    }

    public void setIsTotalFeeAdjust(String isTotalFeeAdjust) {
        this.isTotalFeeAdjust = isTotalFeeAdjust;
    }

    public String getUseCoupon() {
        return useCoupon;
    }

    public void setUseCoupon(String useCoupon) {
        this.useCoupon = useCoupon;
    }

    public String getExtraCommonParam() {
        return extraCommonParam;
    }

    public void setExtraCommonParam(String extraCommonParam) {
        this.extraCommonParam = extraCommonParam;
    }

    public String getOutChannelType() {
        return outChannelType;
    }

    public void setOutChannelType(String outChannelType) {
        this.outChannelType = outChannelType;
    }

    public String getOutChannelAmount() {
        return outChannelAmount;
    }

    public void setOutChannelAmount(String outChannelAmount) {
        this.outChannelAmount = outChannelAmount;
    }

    public String getOutChannelInst() {
        return outChannelInst;
    }

    public void setOutChannelInst(String outChannelInst) {
        this.outChannelInst = outChannelInst;
    }

    public String getBusinessScene() {
        return businessScene;
    }

    public void setBusinessScene(String businessScene) {
        this.businessScene = businessScene;
    }

    @Override
    public String toString() {
        return JsonHelper.convert(this);
    }
}
