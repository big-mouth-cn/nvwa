/**
 * 
 */
package org.bigmouth.nvwa.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public final class ByteUtils {

	private ByteUtils() {
	}

	/**
	 * Please call HexUtils.bytesAsHexCodes.
	 * 
	 * @param bytes
	 * @param maxShowBytes
	 * @return
	 */
	@Deprecated
	public static String bytesAsHexString(byte[] bytes, int maxShowBytes) {
		return HexUtils.bytesAsHexCodes(bytes, maxShowBytes, " ");
	}

	/**
	 * Please call HexUtils.printAsHexCodes.
	 * 
	 * @param bytes
	 * @param maxShowBytes
	 */
	@Deprecated
	public static void printAsHexString(byte[] bytes, int maxShowBytes) {
		HexUtils.printAsHexCodes(bytes, maxShowBytes);
	}

	/**
	 * Please call HexUtils.printAsHexCodes.
	 * 
	 * @param bytes
	 * @param maxShowBytes
	 */
	@Deprecated
	public static void printAsHexString(byte[] bytes, int maxShowBytes, PrintStream p) {
		HexUtils.printAsHexCodes(bytes, maxShowBytes, p);
	}

	public static byte[] union(byte[] s1, byte[] s2) {
		if (null == s1 || null == s2)
			throw new NullPointerException("union input null");
		byte[] result = new byte[s1.length + s2.length];
		System.arraycopy(s1, 0, result, 0, s1.length);
		System.arraycopy(s2, 0, result, s1.length, s2.length);
		return result;
	}

	public static byte[] union(List<byte[]> byteList) {
		int size = 0;
		for (byte[] bs : byteList) {
			size += bs.length;
		}
		byte[] ret = new byte[size];
		int pos = 0;
		for (byte[] bs : byteList) {
			System.arraycopy(bs, 0, ret, pos, bs.length);
			pos += bs.length;
		}
		return ret;
	}

	public static int totalByteSizeOf(List<byte[]> byteList) {
		int len = 0;
		for (byte[] bs : byteList) {
			len += bs.length;
		}

		return len;
	}

	@Deprecated
	public static byte[] string2BCD(String src) {
		byte[] ret = new byte[(src.length() + 1) / 2];

		int charIdx = 0;
		for (int idx = 0; idx < ret.length; idx++) {
			byte Htemp = (byte) (src.charAt(charIdx) - '0');
			Htemp = (byte) (Htemp << 4);
			Htemp = (byte) (Htemp & 0xf0);
			charIdx++;
			byte Ltemp = (byte) (src.charAt(charIdx) - '0');
			Ltemp = (byte) (Ltemp & 0x0f);
			ret[idx] = (byte) (Htemp | Ltemp);
			charIdx++;
		}

		return ret;
	}

	public static byte[] getBytesFromStream(InputStream in) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int bLen = 0;
		while ((bLen = in.read(buffer)) > 0) {
			baos.write(buffer, 0, bLen);
		}
		in.close();
		return baos.toByteArray();
	}
	
}
