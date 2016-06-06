package org.bigmouth.nvwa.codec.byteorder;

import java.util.HashMap;
import java.util.Map;

public class NumberCodecFactory {

	private static final Map<ByteOrder, NumberCodec> codecs;

	static {
		codecs = new HashMap<ByteOrder, NumberCodec>();
		codecs.put(ByteOrder.littleEndian, new LittleEndianCodec());
		codecs.put(ByteOrder.bigEndian, new BigEndianCodec());
		codecs.put(ByteOrder.none, null);
	}

	private static int b2ui(byte b) {
		// return (int) (b + 256) % 256;
		return b & 0xFF;
	}

	private static long b2ul(byte b) {
		// return (long) (b + 256) % 256;
		return b & 0xFFL;
	}

	private static class LittleEndianCodec implements NumberCodec {
		public int bytes2Int(byte[] bytes, int byteLength) {
			int value = 0;
			for (int i = 0; i < byteLength; i++) {
				value |= b2ui(bytes[i]) << (i * 8);
			}
			return value;
		}

		public long bytes2Long(byte[] bytes, int byteLength) {
			long value = 0;
			for (int i = 0; i < byteLength; i++) {
				value |= b2ul(bytes[i]) << (i * 8);
			}

			return value;
		}

		public short bytes2Short(byte[] bytes, int byteLength) {
			short value = 0;
			for (int i = 0; i < byteLength; i++) {
				value |= b2ui(bytes[i]) << (i * 8);
			}

			return value;
		}

		public byte[] int2Bytes(int value, int byteLength) {
			byte[] bytes = new byte[byteLength];

			for (int i = 0; i < byteLength; i++) {
				int shiftCount = i * 8;
				bytes[i] = (byte) ((value & (0x000000ff << shiftCount)) >> shiftCount);
			}
			return bytes;
		}

		public byte[] long2Bytes(long value, int byteLength) {
			byte[] bytes = new byte[byteLength];

			for (int i = 0; i < byteLength; i++) {
				int shiftCount = i * 8;
				bytes[i] = (byte) ((value & (0x00000000000000ffL << shiftCount)) >> shiftCount);
			}
			return bytes;
		}

		public byte[] short2Bytes(short value, int byteLength) {
			byte[] bytes = new byte[byteLength];

			for (int i = 0; i < byteLength; i++) {
				int shiftCount = i * 8;
				bytes[i] = (byte) ((value & (0x00ff << shiftCount)) >> shiftCount);
			}
			return bytes;
		}
	}

	private static class BigEndianCodec implements NumberCodec {
		public int bytes2Int(byte[] bytes, int byteLength) {
			int value = 0;
			for (int i = 0; i < byteLength; i++) {
				value |= b2ui(bytes[i]) << ((byteLength - 1 - i) * 8);
			}
			return value;
		}

		public long bytes2Long(byte[] bytes, int byteLength) {
			long value = 0;
			for (int i = 0; i < byteLength; i++) {
				value |= b2ul(bytes[i]) << ((byteLength - 1 - i) * 8);
			}

			return value;
		}

		public short bytes2Short(byte[] bytes, int byteLength) {
			short value = 0;
			for (int i = 0; i < byteLength; i++) {
				value |= b2ui(bytes[i]) << ((byteLength - 1 - i) * 8);
			}

			return value;
		}

		public byte[] int2Bytes(int value, int byteLength) {
			byte[] bytes = new byte[byteLength];

			for (int i = 0; i < byteLength; i++) {
				int shiftCount = ((byteLength - 1 - i) * 8);
				bytes[i] = (byte) ((value & (0x000000ff << shiftCount)) >> shiftCount);
			}
			return bytes;
		}

		public byte[] long2Bytes(long value, int byteLength) {
			byte[] bytes = new byte[byteLength];

			for (int i = 0; i < byteLength; i++) {
				int shiftCount = ((byteLength - 1 - i) * 8);
				bytes[i] = (byte) ((value & (0x00000000000000ffL << shiftCount)) >> shiftCount);
			}
			return bytes;
		}

		public byte[] short2Bytes(short value, int byteLength) {
			byte[] bytes = new byte[byteLength];

			for (int i = 0; i < byteLength; i++) {
				int shiftCount = ((byteLength - 1 - i) * 8);
				bytes[i] = (byte) ((value & (0x00ff << shiftCount)) >> shiftCount);
			}
			return bytes;
		}
	}

	public static NumberCodec fetchNumberCodec(ByteOrder byteOrder) {
		if (null == byteOrder || ByteOrder.none == byteOrder)
			throw new IllegalArgumentException(
					"value of byteOrder expect bigEndian or littleEndian,but [" + byteOrder + "]");
		return codecs.get(byteOrder);
	}

}
