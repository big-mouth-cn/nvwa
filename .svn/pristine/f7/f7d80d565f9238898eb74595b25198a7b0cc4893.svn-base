package org.bigmouth.nvwa.utils;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

public final class HexUtils {

	private static final String DEFAULT_HEX_SEPARATOR = " ";

	private HexUtils() {
	}

	/**
	 * 
	 * @param bytes
	 * @param maxShowBytes
	 * @param hexSeparator
	 *            null means no hexSeparator
	 * @return
	 */
	public static String bytesAsHexCodes(byte[] bytes, int maxShowBytes, String hexSeparator) {
		if (null == bytes)
			throw new NullPointerException("hexSeparator");
		boolean isLimitLen = true;
		if (maxShowBytes <= 1)
			isLimitLen = false;
		boolean hasHexSeparator = true;
		if (null == hexSeparator)
			hasHexSeparator = false;
		int idx = 0;
		StringBuilder body = new StringBuilder();
		body.append("bytes size is:[");
		body.append(bytes.length);
		body.append("]\r\n");

		for (byte b : bytes) {
			int hex = ((int) b) & 0xff;
			String shex = Integer.toHexString(hex).toUpperCase();
			if (1 == shex.length()) {
				body.append("0");
			}
			body.append(shex);
			if (hasHexSeparator)
				body.append(hexSeparator);
			idx++;
			if (16 == idx) {
				body.append("\r\n");
				idx = 0;
			}
			if (isLimitLen) {
				maxShowBytes--;
				if (maxShowBytes <= 0) {
					break;
				}
			}
		}
		if (idx != 0) {
			body.append("\r\n");
		}
		return body.toString();
	}

	public static String bytesAsHexCodes(byte[] bytes, int maxShowBytes) {
		return bytesAsHexCodes(bytes, maxShowBytes, DEFAULT_HEX_SEPARATOR);
	}

	public static void printAsHexCodes(byte[] bytes, int maxShowBytes) {
		printAsHexCodes(bytes, maxShowBytes, System.out);
	}

	public static void printAsHexCodes(byte[] bytes, int maxShowBytes, PrintStream p) {
		printAsHexCodes(bytes, maxShowBytes, DEFAULT_HEX_SEPARATOR, p);
	}

	public static void printAsHexCodes(byte[] bytes, int maxShowBytes, String hexSeparator,
			PrintStream p) {
		if (null == p)
			throw new NullPointerException("PrintStream");
		synchronized (p) {
			String hex = bytesAsHexCodes(bytes, maxShowBytes, hexSeparator);
			p.print(hex);
			p.flush();
		}
	}

	public static byte[] hexCodesAsBytes(String hexCodes, String hexSeparator) {
		if (StringUtils.isBlank(hexCodes))
			throw new IllegalArgumentException("hexCodes");
		String _hexCodes = resolveHexCodes(hexCodes, hexSeparator);
		return doHexCodesAsBytes(_hexCodes);
	}

	public static byte[] hexCodesAsBytes(String hexCodes) {
		return hexCodesAsBytes(hexCodes, null);
	}

	private static String resolveHexCodes(String hexCodes, String hexSeparator) {
		if (null == hexSeparator)
			return hexCodes;
		return hexCodes.replaceAll(hexSeparator, "");
	}

	public static String hexCodesAsString(String hexCodes, String hexSepartor, String encoding)
			throws UnsupportedEncodingException {
		byte[] bytes = hexCodesAsBytes(hexCodes, hexSepartor);
		if (null == encoding)
			return new String(bytes);
		try {
			return new String(bytes, encoding);
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
	}

	public static String hexCodesAsString(String hexCodes, String hexSepartor)
			throws UnsupportedEncodingException {
		return hexCodesAsString(hexCodes, hexSepartor, null);
	}

	public static String hexCodesAsString(String hexCodes) throws UnsupportedEncodingException {
		return hexCodesAsString(hexCodes, null, null);
	}

	private static byte[] doHexCodesAsBytes(String hexCodes) {
		if (0 != (hexCodes.length() % 2))
			throw new IllegalArgumentException("hexCodes'length must be multiple of 2,but "
					+ hexCodes.length());
		byte[] ret = new byte[hexCodes.length() / 2];
		int j = 0;
		for (int i = 0; i < ret.length; i++) {
			char high = hexCodes.charAt(j++);
			char low = hexCodes.charAt(j++);
			ret[i] = (byte) ((parse(high) << 4) | parse(low));
		}
		return ret;
	}

	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	@Deprecated
	public static String hexCodes2String(String hexCodes) {
		try {
			return hexCodesAsString(hexCodes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("hexCodesAsString:", e);
		}
	}

	@Deprecated
	public static byte[] hexString2Bytes(String hexCodes) {
		return hexCodesAsBytes(hexCodes);
	}
}
