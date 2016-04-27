package org.bigmouth.nvwa.sap.codec;

import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.codec.byteorder.ByteOrder;
import org.bigmouth.nvwa.codec.byteorder.NumberCodec;
import org.bigmouth.nvwa.codec.byteorder.NumberCodecFactory;
import org.bigmouth.nvwa.sap.SapMessage;


public final class SapCodecUtils {

	private SapCodecUtils() {
	}

	public static final String US_ASCII_CHARSET_NAME = "US-ASCII";
	public static final Charset US_ASCII_CHARSET = Charset.forName(US_ASCII_CHARSET_NAME);
	public static final int PROTOCAL_ID = (0xBE << 24) | (0xEF << 16) | (0xBA << 8) | (0xBE);

	public final static NumberCodec NUMBER_CODEC = NumberCodecFactory
			.fetchNumberCodec(ByteOrder.bigEndian);

	public static boolean isValidProtocol(byte[] prefix) {
		int _p = NUMBER_CODEC.bytes2Int(prefix, 4);
		return _p == PROTOCAL_ID;
	}

	public static NumberCodec getNumberCodec() {
		return NUMBER_CODEC;
	}

	public static void encodeHeaderCommon(SapMessage sapMessage, IoBuffer buffer) {
		buffer.setAutoExpand(true);
		// protocol id
		buffer.put(getNumberCodec().int2Bytes(PROTOCAL_ID, 4));
		// protocol version
		buffer.put(sapMessage.getProtocolVer());
		// message type
		buffer.put(sapMessage.getMessageType().code());
		// content type
		buffer.put(sapMessage.getContentType().code());
		// content version
		buffer.put(sapMessage.getContentVer());
		// // identification(transaction id)
		// buffer.put(encodeUUID(sapMessage.getIdentification()));
	}

	public static void encodeContent(SapMessage sapMessage, IoBuffer buffer) {
		IoBuffer content = sapMessage.getContent();
		int contentLength = content.limit();
		// TODO:maybe flip()?
		byte[] lenBytes = getNumberCodec().int2Bytes(contentLength, 4);
		buffer.put(lenBytes);

		if (contentLength > 0)
			buffer.put(content);
	}

	public static byte[] short2Bytes(short bizTypeCode) {
		return getNumberCodec().short2Bytes(bizTypeCode, 2);
	}

	public static byte[] long2Bytes(long v) {
		return getNumberCodec().long2Bytes(v, 8);
	}

	public static byte[] int2Bytes(int beginOffset) {
		return getNumberCodec().int2Bytes(beginOffset, 4);
	}

	public static short bytes2Short(byte[] bytes) {
		return getNumberCodec().bytes2Short(bytes, 2);
	}

	public static int bytes2Int(byte[] bytes) {
		return getNumberCodec().bytes2Int(bytes, 4);
	}

	public static long bytes2Long(byte[] bytes) {
		return getNumberCodec().bytes2Long(bytes, 8);
	}

	public static byte[] string2Bytes(String source) {
		return source.getBytes(US_ASCII_CHARSET);
	}

	public static String bytes2String(byte[] bytes) {
		return new String(bytes, US_ASCII_CHARSET);
	}

	// read type methods
	public static String readString(IoBuffer buffer, int len) {
		byte[] bytes = new byte[len];
		buffer.get(bytes);
		return bytes2String(bytes);
	}

	public static byte readByte(IoBuffer buffer) {
		return buffer.get();
	}

	public static short readShort(IoBuffer buffer) {
		byte[] bytes = new byte[2];
		buffer.get(bytes);
		return bytes2Short(bytes);
	}

	public static int readInt(IoBuffer buffer) {
		byte[] bytes = new byte[4];
		buffer.get(bytes);
		return bytes2Int(bytes);
	}

	// write type methods
	public static void writeString(IoBuffer buffer, String v) {
		buffer.put(string2Bytes(v));
	}

	public static void writeInt(IoBuffer buffer, int v) {
		buffer.put(int2Bytes(v));
	}

	public static void writeShort(IoBuffer buffer, short v) {
		buffer.put(short2Bytes(v));
	}

	public static void writeByte(IoBuffer buffer, byte v) {
		buffer.put(v);
	}

	// uuid
	public static byte[] encodeUUID(UUID id) {
		if (null == id)
			throw new NullPointerException("id");
		byte[] mostBytes = long2Bytes(id.getMostSignificantBits());
		byte[] leastBytes = long2Bytes(id.getLeastSignificantBits());
		byte[] ret = new byte[16];
		System.arraycopy(mostBytes, 0, ret, 0, 8);
		System.arraycopy(leastBytes, 0, ret, 8, 8);
		return ret;
	}

	public static UUID decodeUUID(byte[] bytes) {
		if (null == bytes)
			throw new NullPointerException("bytes");
		if (16 != bytes.length)
			throw new IllegalArgumentException("uuid bytes'length:" + bytes.length);
		byte[] mostBytes = new byte[8];
		byte[] leastBytes = new byte[8];
		System.arraycopy(bytes, 0, mostBytes, 0, 8);
		System.arraycopy(bytes, 8, leastBytes, 0, 8);

		long most = bytes2Long(mostBytes);
		long least = bytes2Long(leastBytes);

		return new UUID(most, least);
	}
}
