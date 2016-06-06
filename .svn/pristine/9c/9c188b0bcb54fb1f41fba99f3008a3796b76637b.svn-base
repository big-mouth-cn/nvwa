/*
 * Copyright 2014 Mopote.com
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

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

/**
 * 
 * @since 1.0
 * @author Allen
 */
public final class AesUtils {

    private static final byte[] INIT_VECTOR = { 0x31, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31, 0x38, 0x27, 0x36, 0x35,
            0x33, 0x23, 0x32, 0x33 };

    /**
     * Encrypt the content with a given key using aes algorithm.
     * 
     * @param content
     * @param apiKey must contain exactly 32 characters
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String apiKey) throws Exception {
        return encrypt(content, apiKey, INIT_VECTOR);
    }
    
    /**
     * Encrypt the content with a given key using aes algorithm.
     * 
     * @param content
     * @param apiKey must contain exactly 32 characters
     * @param iv iv
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String apiKey, byte[] iv) throws Exception {
        return new String(Hex.encode(encrypt2(content, apiKey, iv)));
    }
    
    public static byte[] encrypt2(String content, String apiKey, byte[] iv) throws Exception {
        if (apiKey == null) {
            throw new IllegalArgumentException("Key cannot be null!");
        }
        byte[] keyBytes = apiKey.getBytes();
        if (keyBytes.length != 32 && keyBytes.length != 24 && keyBytes.length != 16) {
            throw new IllegalArgumentException("Key length must be 128/192/256 bits!");
        }
        return encrypt(content.getBytes(), keyBytes, iv);
    }

    public static String decrypt(String content, String apiKey, byte[] iv) throws Exception {
        if (apiKey == null) {
            throw new IllegalArgumentException("Key cannot be null!");
        }
        String decrypted = null;
        byte[] encryptedContent = Hex.decode(content);
        byte[] keyBytes = apiKey.getBytes("ASCII");
        byte[] decryptedBytes = null;
        if (keyBytes.length != 32 && keyBytes.length != 24 && keyBytes.length != 16) {
            throw new IllegalArgumentException("Key length must be 128/192/256 bits!");
        }
        decryptedBytes = decrypt(encryptedContent, keyBytes, iv);
        decrypted = new String(decryptedBytes);
        return decrypted;
    }
    
    /**
     * Decrypt the content with a given key using aes algorithm.
     * 
     * @param content
     * @param apiKey must contain exactly 32 characters
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String apiKey) throws Exception {
        return decrypt(content, apiKey, INIT_VECTOR);
    }

    /**
     * Encrypt data.
     * 
     * @param plain
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] plain, byte[] key, byte[] iv) throws Exception {
        PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
        CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
        aes.init(true, ivAndKey);
        return cipherData(aes, plain);
    }

    /**
     * Decrypt data.
     * 
     * @param cipher
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] cipher, byte[] key, byte[] iv) throws Exception {
        PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
        CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
        aes.init(false, ivAndKey);
        return cipherData(aes, cipher);
    }

    /**
     * Encrypt or decrypt data.
     * 
     * @param cipher
     * @param data
     * @return
     * @throws Exception
     */
    private static byte[] cipherData(PaddedBufferedBlockCipher cipher, byte[] data) throws Exception {
        int minSize = cipher.getOutputSize(data.length);
        byte[] outBuf = new byte[minSize];
        int length1 = cipher.processBytes(data, 0, data.length, outBuf, 0);
        int length2 = cipher.doFinal(outBuf, length1);
        int actualLength = length1 + length2;
        byte[] result = new byte[actualLength];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }
    
    public static void main(String[] args) {
        try {
//            System.out.println(encrypt(DigestUtils.md5Hex("123456"), "3LBx;8G-bq-l#@nk", "".getBytes()));
//            System.out.println(encrypt("{\"Channel_id\":\"1\",\"Activity_id\":\"14\",\"Ordertel\":\"18212846551\",\"Service_code\":\"FS0001\",\"Userorid\":\"O150814170905007\",\"Order_type\":\"1\",\"Remarks\":\"\",\"Contract_id\":\"11\"}", "Zr5psZnJHxXWVThX", "2685181350450401".getBytes()));
//            System.out.println(NativeAesUtils.decrypt("oggpdhaekeioncgbogngkdgmjecakbbamjdjlidlkcgjebechlekliagbggcjkhpmemdkjhcbaamibdiakndbnbbfhpffgapcliilbilpbbkhopcnbefoiedfkmeoacgdkeeokpolmfdegceekemeeeofmmclmhjdimnjfhmfokidaipphfghckadihhilfmbhafceemgampljfnbmjlikpomogopnmndcphjhbohfdgohlggfdaochebmcgkekggifdjbakogimhfbijefjfnkempjnedobcclfmagbebfmnehbcamoolifhfgbhnkfpjgmnmodjhelchhapcjcfjijjadllenghgomeofbdgfhikdeaabdgciadifdhfaknelejaebbhfcopgcnoanipngpoeenphj", "Zr5psZnJHxXWVThX", "2685181350450401"));
            System.out.println(CryptoAesHelper.decrypt("oggpdhaekeioncgbogngkdgmjecakbbamjdjlidlkcgjebechlekliagbggcjkhpmemdkjhcbaamibdiakndbnbbfhpffgapcliilbilpbbkhopcnbefoiedfkmeoacgdkeeokpolmfdegceekemeeeofmmclmhjdimnjfhmfokidaipphfghckadihhilfmbhafceemgampljfnbmjlikpomogopnmndcphjhbohfdgohlggfdaochebmcgkekggifdjbakogimhfbijefjfnkempjnedobcclfmagbebfmnehbcamoolifhfgbhnkfpjgmnmodjhelchhapcjcfjijjadllenghgomeofbdgfhikdeaabdgciadifdhfaknelejaebbhfcopgcnoanipngpoeenphj", "Zr5psZnJHxXWVThX", "2685181350450401"));
//            System.out.println(decrypt("oggpdhaekeioncgbogngkdgmjecakbbamjdjlidlkcgjebechlekliagbggcjkhpmemdkjhcbaamibdiakndbnbbfhpffgapcliilbilpbbkhopcnbefoiedfkmeoacgdkeeokpolmfdegceekemeeeofmmclmhjdimnjfhmfokidaipphfghckadihhilfmbhafceemgampljfnbmjlikpomogopnmndcphjhbohfdgohlggfdaochebmcgkekggifdjbakogimhfbijefjfnkempjnedobcclfmagbebfmnehbcamoolifhfgbhnkfpjgmnmodjhelchhapcjcfjijjadllenghgomeofbdgfhikdeaabdgciadifdhfaknelejaebbhfcopgcnoanipngpoeenphj", "Zr5psZnJHxXWVThX", "2685181350450401".getBytes()));
            System.out.println(new String(INIT_VECTOR));
            
            System.out.println(AesUtils.encrypt("123456", "gkxEwVfJUJA2BpohktvyrfJLEnvlQYt7", INIT_VECTOR));
            System.out.println(AesUtils.encrypt("123456", "gkxEwVfJUJA2BpohktvyrfJLEnvlQYt7", "176543218'653#23".getBytes()));
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}