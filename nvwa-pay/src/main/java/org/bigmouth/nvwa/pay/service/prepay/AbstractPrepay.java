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

import java.io.File;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.bigmouth.nvwa.network.http.HttpClientHelper;
import org.bigmouth.nvwa.pay.PayDefaults;
import org.bigmouth.nvwa.pay.config.PayConfig;
import org.bigmouth.nvwa.pay.config.PayConfigException;
import org.bigmouth.nvwa.pay.config.PayConfigService;
import org.bigmouth.nvwa.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * 抽象的预支付服务
 * 
 * @author Allen Hu - (big-mouth.cn) 2015-8-6
 */
public abstract class AbstractPrepay implements Prepay {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPrepay.class);

    private final PayConfigService configService;

    public AbstractPrepay(PayConfigService configService) {
        Preconditions.checkNotNull(configService, "configService");
        this.configService = configService;
    }
    
    protected abstract PrepayInsideRequest getPayRequest(PrepayRequest argument, PayConfig config);
    protected abstract PrepayResponse convert(byte[] array);

    @Override
    public PrepayResponse prepay(PrepayRequest argument) {
        Preconditions.checkNotNull(argument, "argument");
        argument.validate();
        Map<String, PayConfig> configs = configService.getConfigs();
        if (MapUtils.isEmpty(configs)) {
            throw new PayConfigException("pay configs is empty!");
        }
        String appId = argument.getAppId();
        PayConfig config = configs.get(appId);
        config.validate();
        File pkcs12 = config.getPkcs12();
        String appSecret = config.getAppSecret();
        String url = config.getUrlPrepay();

        String xml = getPayRequest(argument, config).toXML();

        HttpClient https = HttpClientHelper.https(pkcs12, appSecret.toCharArray());

        try {
            HttpPost post = HttpClientHelper.post(url);
            HttpClientHelper.addByteArrayEntity(post, StringHelper.convert(xml), PayDefaults.APPLICATION_XML);
            HttpResponse httpResponse = https.execute(post);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Received response statusCode {} from {}", statusCode, url);
            }
            byte[] array = HttpClientHelper.getResponse(httpResponse);
            if (ArrayUtils.isNotEmpty(array)) {
                return convert(array);
            }
            else {
                LOGGER.warn("Empty response content! from {}", url);
            }
        }
        catch (Exception e) {
            LOGGER.error("Access " + url + " occur exception!", e);
        }
        finally {
            https.getConnectionManager().shutdown();
        }
        return null;
    }
}
