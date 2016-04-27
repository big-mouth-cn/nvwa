package org.bigmouth.nvwa.contentstore.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class ContentStoreUtils {

	private ContentStoreUtils() {
	}

	public static String content2key(byte[] content) {
		MessageDigest md5inst;
		try {
			md5inst = MessageDigest.getInstance("MD5");
			md5inst.update(content);
			byte[] digest = md5inst.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}
}
