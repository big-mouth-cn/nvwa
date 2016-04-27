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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.pay.PayDefaults;
import org.bigmouth.nvwa.pay.service.prepay.PrepayInsideResponse;
import org.bigmouth.nvwa.utils.StringHelper;
import org.bigmouth.nvwa.utils.xml.Dom4jDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信支付响应信息
 * 
 * <pre>
 * e.g.
 * 
 * &lt;xml&gt;
 *    &lt;return_code&gt;&lt;![CDATA[SUCCESS]]&gt;&lt;/return_code&gt;
 *    &lt;return_msg&gt;&lt;![CDATA[OK]]&gt;&lt;/return_msg&gt;
 *    &lt;appid&gt;&lt;![CDATA[wx2421b1c4370ec43b]]&gt;&lt;/appid&gt;
 *    &lt;mch_id&gt;&lt;![CDATA[10000100]]&gt;&lt;/mch_id&gt;
 *    &lt;nonce_str&gt;&lt;![CDATA[IITRi8Iabbblz1Jc]]&gt;&lt;/nonce_str&gt;
 *    &lt;sign&gt;&lt;![CDATA[7921E432F65EB8ED0CE9755F0E86D72F]]&gt;&lt;/sign&gt;
 *    &lt;result_code&gt;&lt;![CDATA[SUCCESS]]&gt;&lt;/result_code&gt;
 *    &lt;prepay_id&gt;&lt;![CDATA[wx201411101639507cbf6ffd8b0779950874]]&gt;&lt;/prepay_id&gt;
 *    &lt;trade_type&gt;&lt;![CDATA[JSAPI]]&gt;&lt;/trade_type&gt;
 * &lt;/xml&gt;
 * </pre>
 * 
 * @author Allen Hu - (big-mouth.cn) 2015-8-6
 */
public class WxPrepayInsideResponse extends PrepayInsideResponse {

    private static final long serialVersionUID = -4761384316800264505L;
    private static final Logger LOGGER = LoggerFactory.getLogger(WxPrepayInsideResponse.class);

    public static final String SUCCESS = PayDefaults.SUCCESS;
    public static final String FAIL = PayDefaults.FAIL;
    
    /** 返回状态码：SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断 */
    private String returnCode;
    /** 返回信息，如非空，为错误原因、签名失败、参数格式校验错误 */
    private String returnMsg;

    /** 调用接口提交的公众账号ID */
    private String appid;
    /** 调用接口提交的商户号 */
    private String mchId;
    /** 调用接口提交的终端设备号 */
    private String deviceInfo;
    /** 微信返回的随机字符串 */
    private String nonceStr;
    /** 签名 */
    private String sign;
    /** 业务结果：SUCCESS/FAIL */
    private String resultCode;
    /** 错误代码 */
    private String errCode;
    /** 错误返回的信息描述 */
    private String errCodeDes;

    /** 调用接口提交的交易类型 */
    private String tradeType;
    /** 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时 */
    private String prepayId;
    /** trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付 */
    private String codeUrl;
    
    @Override
    public boolean isSuccess() {
        return StringUtils.equalsIgnoreCase(SUCCESS, returnCode) && StringUtils.equalsIgnoreCase(SUCCESS, resultCode);
    }

    @Override
    public String getStatusCode() {
        if (!StringUtils.equalsIgnoreCase(SUCCESS, returnCode)) {
            return getReturnCode();
        }
        return getResultCode();
    }

    @Override
    public String getStatusMessage() {
        if (!StringUtils.equalsIgnoreCase(SUCCESS, returnCode)) {
            return getReturnMsg();
        }
        return getErrCodeDes();
    }

    public static WxPrepayInsideResponse of(byte[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return null;
        }
        String xml = StringHelper.convert(array);
        try {
            WxPrepayInsideResponse resp = Dom4jDecoder.decode(xml, "/xml", WxPrepayInsideResponse.class);
            return resp;
        }
        catch (Exception e) {
            LOGGER.error("of:", e);
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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
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

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }
}
