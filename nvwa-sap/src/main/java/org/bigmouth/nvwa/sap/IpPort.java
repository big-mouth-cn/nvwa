package org.bigmouth.nvwa.sap;

import java.util.Arrays;

import org.bigmouth.nvwa.codec.byteorder.ByteOrder;
import org.bigmouth.nvwa.codec.byteorder.NumberCodec;
import org.bigmouth.nvwa.codec.byteorder.NumberCodecFactory;


public class IpPort extends Ip {

	private static final NumberCodec numberCodec = NumberCodecFactory
			.fetchNumberCodec(ByteOrder.bigEndian);
	private final byte[] portBytes;

	private IpPort(byte[] ipBytes, byte[] portBytes) {
		super(ipBytes);
		if (null == portBytes)
			throw new NullPointerException("portBytes");
		if (2 != portBytes.length)
			throw new IllegalArgumentException("portBytes length expect 2,but " + portBytes.length);
		this.portBytes = portBytes;
	}

	public byte[] toBytes() {
		byte[] ret = new byte[6];
		byte[] ipBytes = super.toBytes();
		System.arraycopy(ipBytes, 0, ret, 0, 4);
		System.arraycopy(portBytes, 0, ret, 4, 2);
		return ret;
	}

	public String getDesc() {
		return getIpDesc() + ":" + getPort();
	}

	public String getIpDesc() {
		return super.getDesc();
	}

	public int getPort() {
		return numberCodec.bytes2Int(portBytes, 2);
	}

	public static IpPort create(String ipDesc, int port) {
		byte[] ipBytes = Ip.create(ipDesc).toBytes();
		byte[] portBytes = numberCodec.int2Bytes(port, 2);
		return new IpPort(ipBytes, portBytes);
	}

	public static IpPort create(byte[] bytes) {
		if (null == bytes || 6 != bytes.length)
			throw new IllegalArgumentException("bytes");
		byte[] ipBytes = new byte[4];
		byte[] portBytes = new byte[2];
		System.arraycopy(bytes, 0, ipBytes, 0, 4);
		System.arraycopy(bytes, 4, portBytes, 0, 2);
		return new IpPort(ipBytes, portBytes);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(toBytes());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		IpPort other = (IpPort) obj;
		if (!Arrays.equals(toBytes(), other.toBytes()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getDesc();
	}
}
