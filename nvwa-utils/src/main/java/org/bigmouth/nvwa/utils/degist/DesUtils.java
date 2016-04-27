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
package org.bigmouth.nvwa.utils.degist;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @since 1.0
 * @author Allen 
 */
public final class DesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesUtils.class);
    
    private DesUtils() {
    }
    
    public static String encrypt(String content, String key, byte[] iv) {
        try {
            if (StringUtils.length(key) != 8) {
                throw new IllegalArgumentException("Key must be 8 byte");
            }
            SecretKeySpec secretkey = new SecretKeySpec(key.getBytes(), "DES");
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretkey, zeroIv);
            byte[] encryptedData = cipher.doFinal(content.getBytes());
            return Base64.encodeBase64String(encryptedData);
        }
        catch (Exception e) {
            LOGGER.error("encrypt:", e);
            return null;
        }
    }
    
    public static String decrypt(String content, String key, byte[] iv) {
        try {
            if (StringUtils.length(key) != 8) {
                throw new IllegalArgumentException("Key must be 8 byte");
            }
            byte[] contentBytes = Base64.decodeBase64(content);
            SecretKeySpec secretkey = new SecretKeySpec(key.getBytes(), "DES");
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretkey, zeroIv);
            byte[] decryptedData = cipher.doFinal(contentBytes);
            return new String(decryptedData);
        }
        catch (Exception e) {
            LOGGER.error("decrypt:", e);
            return null;
        }
    }
}
