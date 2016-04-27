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
package org.bigmouth.nvwa.pay.service.pay.alipay;

import org.bigmouth.nvwa.pay.AlipayDefaults;
import org.bigmouth.nvwa.pay.PayDefaults;
import org.bigmouth.nvwa.pay.annotation.NotNull;
import org.bigmouth.nvwa.pay.service.pay.PayRequest;
import org.bigmouth.nvwa.utils.Argument;

/**
 * 支付宝支付请求参数
 * 
 * @author Allen Hu - (big-mouth.cn) 2015-8-10
 */
public class AlipayPayRequest extends PayRequest {

    private static final long serialVersionUID = 7141181032984217209L;

    /** 接口名称。 */
    @NotNull
    private String service = AlipayDefaults.Service.CREATE_DIRECT_PAY_BY_USER;

    /**
     * 签约的支付宝账号对应的支付宝 唯一用户号。 以 2088 开头的 16 位纯数字组成。
     */
    @NotNull
    private String partner;

    /**
     * 商户网站使用的编码格式，如 utf-8、gbk、gb2312 等。
     */
    @NotNull
    @Argument(name = "_input_charset")
    private String inputCharset = AlipayDefaults.Charset.UTF8;

    /**
     * DSA、RSA、MD5 三个值可选， 必须大写。
     */
    @NotNull
    private String signType = PayDefaults.DigestType.MD5;

    /** 签名 */
    @NotNull
    private String sign;

    /**
     * 服务器异 步通知页 面路径
     */
    private String notifyUrl;

    /**
     * 页面跳转 同步通知 页面路径
     */
    private String returnUrl;

    /**
     * 请求出错 时的通知 页面路径
     */
    private String errorNotifyUrl;

    /** 支付宝合作商户网站唯一订单号。 */
    @NotNull
    private String outTradeNo;

    /**
     * 商品的标题/交易标题/订单标题/订 单关键字等。 该参数最长为 128 个汉字。
     */
    @NotNull
    private String subject;

    /** 支付类型 */
    @NotNull
    private String paymentType;

    /**
     * 该笔订单的资金总额，单位为 RMB-Yuan。取值范围为[0.01， 100000000.00]，精确到小数点后 两位。
     */
    @NotNull
    private float totalFee;

    /** 卖家支付宝账号，可以是Email 或手机号码。 */
    @NotNull
    private String sellerEmail;

    /** 买家支付宝账号，可以是Email 或手机号码。 */
    private String buyerEmail;

    /** 卖家支付宝账号对应的支付宝唯一用户号。以 2088开头的纯16位数字。 */
    private String sellerId;

    /** 买家支付宝账号对应的支付宝唯一用户号。以 2088开头的纯16位数字。 */
    private String buyerId;

    /** 卖家别名支付宝账号。 */
    private String sellerAccountName;
    /** 买家别名支付宝账号。 */
    private String buyerAccountName;

    /**
     * 商品单价
     * <p>
     * 单位为：RMB Yuan。取值范围为 [0.01，100000000.00]，精确到小 数点后两位。此参数为单价 规则：price、quantity 能代替 total_fee。即存在 total_fee，就不 能存在
     * price 和 quantity；存在 price、quantity，就不能存在 total_fee。
     * </p>
     */
    private float price;
    /**
     * 购买数量
     * <p>
     * price、quantity 能代替 total_fee。 即存在 total_fee，就不能存在 price 和 quantity；存在 price、quantity， 就不能存在 total_fee。
     * </p>
     */
    private int quantity;

    /** 商品描述 */
    private String body;

    /** 商品展示网址。收银台页面上，商品展示的超链接。 */
    private String showUrl;

    /**
     * <p>
     * 默认支付方式
     * </p>
     * 取值范围： - creditPay（信用支付） - directPay（余额支付） 如果不设置，默认识别为余额支 付。 说明： 必须注意区分大小写。
     */
    private String paymethod;

    /**
     * <p>
     * 支付渠道
     * <p>
     * 用于控制收银台支付渠道显示，该 值的取值范围请参见
     */
    private String enablePaymethod;

    /**
     * 网银支付 时是否做 CTU 校验
     */
    private String needCtuCheck = AlipayDefaults.Y;

    /**
     * 提成类型。目前只支持一种类型：10（卖家给 第三方提成）。 当传递了参数 royalty_parameters 时，提成类型参数不能为空。
     */
    private String royaltType;

    /**
     * 分润账号集
     */
    private String royaltyParameters;

    /**
     * 防钓鱼时间戳
     */
    private String antiPhishingKey;

    /** 客户端 IP */
    private String exterInvokeIp;

    /**
     * 如果用户请求时传递了该参数，则返回给商户时会回传该参数。
     */
    private String extraCommonParam;

    /**
     * <p>
     * 公用业务 扩展参数
     * </p>
     * 用于商户的特定业务信息的传递， 只有商户与支付宝约定了传递此 参数且约定了参数含义，此参数才 有效。<br>
     * 参数格式：参数名 1^参数值 1|参数 名 2^参数值 2|…… 多条数据用“|”间隔。 支付类型（payment_type）为 47 （电子卡券）时，需要包含凭证号 （evoucheprod_evouche_id）参
     * 数名和参数值。
     */
    private String extendParam;

    /**
     * 超时时间<br>
     * 设置未付款交易的超时时间，一旦 超时，该笔交易就会自动被关闭。<br>
     * 取值范围：1m～15d。<br>
     * m-分钟，h-小时，d-天，1c-当天（无 论交易何时创建，都在 0 点关闭）。 该参数数值不接受小数点，如 1.5h，可转换为 90m。
     */
    @Argument(name = "it_b_pay")
    private String itBPay;

    /**
     * 自动登录 标识<br>
     * 用于标识商户是否使用自动登录 的流程。如果和参数 buyer_email 一起使用时，就不会再让用户登录 支付宝，即在收银台中不会出现登 录页面。<br>
     * 取值有以下情况：<br>
     * - Y 代表使用<br>
     * - N 代表不使用<br>
     * 该功能需要联系支付宝配置。
     */
    private String defaultLogin;

    /**
     * 商户申请 的产品类 型<br>
     * 用于针对不同的产品，采取不同的 计费策略。<br>
     * 如果开通了航旅垂直搜索平台产 品，请填写 CHANNEL_FAST_PAY；如果没 有，则为空。
     */
    private String productType;

    /**
     * 快捷登录 授权令牌<br>
     * 如果开通了快捷登录产品，则需要 填写；如果没有开通，则为空。
     */
    private String token;

    /**
     * 商户回传 业务参数<br>
     * 买家通过 etao 购买的商品的详细 清单。如果是 etao 商户则填写； 如果不是，则为空。
     */
    private String itemOrderInfo;

    /**
     * 商户买家 签约号<br>
     * 用于唯一标识商户买家。 如果本参数不为空，则 sign_name_ext 不能为空。
     */
    private String signIdExt;

    /**
     * 商户买家 签约名<br>
     * 商户买家唯一标识对应的名字。
     */
    private String signNameExt;

    /**
     * 扫码支付 方式<br>
     * 扫码支付的方式，支持前置模式和 跳转模式。<br>
     * 前置模式是将二维码前置到商户 的订单确认页的模式。需要商户在 自己的页面中以 iframe 方式请求 支付宝页面。具体分为以下 3 种：<br>
     * - 0：订单码-简约前置模式，对 应 iframe 宽度不能小于 600px，高度不能小于 300px；<br>
     * - 1：订单码-前置模式，对应 iframe 宽度不能小于 300px， 高度不能小于 600px；<br>
     * - 3：订单码-迷你前置模式，对 应 iframe 宽度不能小于 75px， 高度不能小于 75px。<br>
     * 跳转模式下，用户的扫码界面是由 支付宝生成的，不在商户的域名 下。<br>
     * - 2：订单码-跳转模式
     */
    private String qrPayMode;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
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

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getErrorNotifyUrl() {
        return errorNotifyUrl;
    }

    public void setErrorNotifyUrl(String errorNotifyUrl) {
        this.errorNotifyUrl = errorNotifyUrl;
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

    public float getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(float totalFee) {
        this.totalFee = totalFee;
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

    public String getSellerAccountName() {
        return sellerAccountName;
    }

    public void setSellerAccountName(String sellerAccountName) {
        this.sellerAccountName = sellerAccountName;
    }

    public String getBuyerAccountName() {
        return buyerAccountName;
    }

    public void setBuyerAccountName(String buyerAccountName) {
        this.buyerAccountName = buyerAccountName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    public String getEnablePaymethod() {
        return enablePaymethod;
    }

    public void setEnablePaymethod(String enablePaymethod) {
        this.enablePaymethod = enablePaymethod;
    }

    public String getNeedCtuCheck() {
        return needCtuCheck;
    }

    public void setNeedCtuCheck(String needCtuCheck) {
        this.needCtuCheck = needCtuCheck;
    }

    public String getRoyaltType() {
        return royaltType;
    }

    public void setRoyaltType(String royaltType) {
        this.royaltType = royaltType;
    }

    public String getRoyaltyParameters() {
        return royaltyParameters;
    }

    public void setRoyaltyParameters(String royaltyParameters) {
        this.royaltyParameters = royaltyParameters;
    }

    public String getAntiPhishingKey() {
        return antiPhishingKey;
    }

    public void setAntiPhishingKey(String antiPhishingKey) {
        this.antiPhishingKey = antiPhishingKey;
    }

    public String getExterInvokeIp() {
        return exterInvokeIp;
    }

    public void setExterInvokeIp(String exterInvokeIp) {
        this.exterInvokeIp = exterInvokeIp;
    }

    public String getExtraCommonParam() {
        return extraCommonParam;
    }

    public void setExtraCommonParam(String extraCommonParam) {
        this.extraCommonParam = extraCommonParam;
    }

    public String getExtendParam() {
        return extendParam;
    }

    public void setExtendParam(String extendParam) {
        this.extendParam = extendParam;
    }

    public String getItBPay() {
        return itBPay;
    }

    public void setItBPay(String itBPay) {
        this.itBPay = itBPay;
    }

    public String getDefaultLogin() {
        return defaultLogin;
    }

    public void setDefaultLogin(String defaultLogin) {
        this.defaultLogin = defaultLogin;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getItemOrderInfo() {
        return itemOrderInfo;
    }

    public void setItemOrderInfo(String itemOrderInfo) {
        this.itemOrderInfo = itemOrderInfo;
    }

    public String getSignIdExt() {
        return signIdExt;
    }

    public void setSignIdExt(String signIdExt) {
        this.signIdExt = signIdExt;
    }

    public String getSignNameExt() {
        return signNameExt;
    }

    public void setSignNameExt(String signNameExt) {
        this.signNameExt = signNameExt;
    }

    public String getQrPayMode() {
        return qrPayMode;
    }

    public void setQrPayMode(String qrPayMode) {
        this.qrPayMode = qrPayMode;
    }
}
