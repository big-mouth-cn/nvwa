package org.bigmouth.nvwa.sap;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public class Ip {

	private final byte[] bytes;

	protected Ip(byte[] bytes) {
		if (null == bytes)
			throw new NullPointerException("value");
		if (4 != bytes.length)
			throw new IllegalArgumentException("value length expect 4,but " + bytes.length);
		this.bytes = bytes;
	}

	public String getDesc() {
		return new StringBuilder(15).append(bytes[0] & 0xFF).append(".").append(bytes[1] & 0xFF)
				.append(".").append(bytes[2] & 0xFF).append(".").append(bytes[3] & 0xFF).toString();
	}

	public byte[] toBytes() {
		return bytes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bytes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ip other = (Ip) obj;
		if (!Arrays.equals(bytes, other.bytes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getDesc();
	}

	public static Ip create(String desc) {
		if (StringUtils.isBlank(desc))
			throw new IllegalArgumentException("desc is blank.");
		String[] items = desc.split("\\.");
		if (null == items || 4 != items.length)
			throw new IllegalArgumentException("desc:" + desc);
		byte[] v = new byte[4];
		int idx = 0;
		for (String item : items) {
			byte bv = (byte) Integer.parseInt(item);
			v[idx++] = bv;
		}
		return new Ip(v);
	}

	public static Ip create(byte[] bytes) {
		return new Ip(bytes);
	}
}
