package org.bigmouth.nvwa.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.*;

/**
 * MD5工具类.
 * 
 * @version 1.0
 * @see org.apache.commons.codec.digest.DigestUtils
 */
@SuppressWarnings("restriction")
@Deprecated
public class MD5Utils {

	// 定义 加密算法,可用DES,DESede,Blowfish
	private static String Algorithm = "DES";

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"a", "b", "c", "d", "e", "f" };

	static boolean debug = false;

	static {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
	}

	/**
	 * 生成密钥, 注意此步骤时间比较长
	 */
	public static byte[] getKey() throws Exception {
		KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
		SecretKey deskey = keygen.generateKey();
		if (debug)
			System.out.println("生成密钥:" + byte2hex(deskey.getEncoded()));
		return deskey.getEncoded();
	}

	/**
	 * 加密
	 * 
	 * @param input
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encode(byte[] input, byte[] key) throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		if (debug) {
			System.out.println("加密前的二进串:" + byte2hex(input));
			System.out.println("加密前的字符串:" + new String(input));
		}
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] cipherByte = c1.doFinal(input);
		if (debug)
			System.out.println("加密后的二进串:" + byte2hex(cipherByte));
		return cipherByte;
	}

	/**
	 * 解密
	 */
	public static byte[] decode(byte[] input, byte[] key) throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		if (debug)
			System.out.println("解密前的信息:" + byte2hex(input));
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey);
		byte[] clearByte = c1.doFinal(input);
		if (debug) {
			System.out.println("解密后的二进串:" + byte2hex(clearByte));
			System.out.println("解密后的字符串:" + (new String(clearByte)));
		}
		return clearByte;
	}

	/**
	 * 二进制转成16进制字符
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		return hexDigits[(b & 0xf0) >> 4] + hexDigits[b & 0x0f];
	}

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			buf.append(byteToHexString(b[i]));
		}
		return buf.toString();
	}

	public static String encode(String origin) {
		String resultString = null;

		resultString = new String(origin);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return resultString;
	}

	/**
	 * md5()信息摘要, 不可逆
	 */
	public static byte[] md5(byte[] input) throws Exception {
		java.security.MessageDigest alg = java.security.MessageDigest.getInstance("MD5"); // or
																							// "SHA-1"
		if (debug) {
			System.out.println("摘要前的二进串:" + byte2hex(input));
			System.out.println("摘要前的字符串:" + new String(input));
		}
		alg.update(input);
		byte[] digest = alg.digest();
		if (debug)
			System.out.println("摘要后的二进串:" + byte2hex(digest));
		return digest;
	}

	/**
	 * 字节码转换成16进制字符串
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}
	public static void  main(String args[]){
		String result=encode("skyaRkIlx123pdl.elevensky.net/www/TMP/mailiuliang/fm-market-sdk-example(JAVA).zip");
		System.out.println("result uuid="+result);
	}
}
