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
package org.bigmouth.nvwa.utils.degist;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.bigmouth.nvwa.utils.StringHelper;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

/**
 * 
 * @since 1.0
 * @author Allen
 */
public class NativeAesUtils {

    private static final byte[] INIT_VECTOR = { 0x31, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31, 0x38, 0x27, 0x36, 0x35,
            0x33, 0x23, 0x32, 0x33 };

    public static String encryptAES(String input, String key) throws Exception {
        return encryptAES(input.getBytes(), key);
    }
    
    public static String encryptAES(byte[] input, String key) throws Exception {
        byte[] crypted = null;
        javax.crypto.KeyGenerator kgen = javax.crypto.KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec skey = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skey);
        crypted = cipher.doFinal(input);
        return new String(Hex.encode(crypted));
    }
    
    public static String encrypt(String input, String key) throws Exception {
        byte[] crypted = null;
        javax.crypto.KeyGenerator kgen = javax.crypto.KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(key.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec skey = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey, new IvParameterSpec(INIT_VECTOR, 0, INIT_VECTOR.length));
        crypted = cipher.doFinal(input.getBytes());
        return new String(Hex.encode(crypted));
    }
    
    public static byte[] encrypt(String input, String key, byte[] iv) throws Exception {
        javax.crypto.KeyGenerator kgen = javax.crypto.KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(key.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec skey = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey, new IvParameterSpec(INIT_VECTOR, 0, INIT_VECTOR.length));
        return cipher.doFinal(input.getBytes());
    }
    
    public static byte[] encrypt2(byte[] encData, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        return cipher.doFinal(encData);
    }
    
    public static byte[] encrypt2(String encData, String key, byte[] iv) throws Exception {
        return encrypt2(encData.getBytes(), key.getBytes(), iv);
    }

    public static String decrypt(String input, String key, String iv) throws Exception {
        return decrypt(input, key, StringHelper.convert(iv));
    }
    
    public static String decrypt(String input, String key, byte[] iv) throws Exception {
        byte[] output = null;
        javax.crypto.KeyGenerator kgen = javax.crypto.KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(key.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec skey = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(iv, 0, iv.length));
        output = cipher.doFinal(Hex.decode(input));
        return new String(output);
    }
    
    public static String decrypt(String input, String key) throws Exception {
        return decrypt(input, key, INIT_VECTOR);
    }

    public static String encryptWith16BitsKey(String input, String key) throws Exception {
        byte[] crypted = null;
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey);
        crypted = cipher.doFinal(input.getBytes());
        return new String(Base64.encode(crypted));
    }

    public static String decryptWith16BitsKey(String input, String key) throws Exception {
        byte[] output = null;
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey);
        output = cipher.doFinal(Base64.decode(input));
        return new String(output);
    }
    
    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }
        return strBuf.toString();
    }
    
    public static void main(String[] args) {
        try {
            System.out.println(encryptAES(DigestUtils.md5("CardCode=714&CardValue=100&ChannelId=3000054&ChannelOrderId=O20150623174253414&OpenType=1&OrderTime=2015-06-23 09:42:53&PhoneNum=078A429726BCA0B49781ADF31B52EFCA&RealFee=0&SaleDiscount=0.75&TransactionId=3000054002015062317425353415"), "3LBx;8G-bq-l#@nk"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}