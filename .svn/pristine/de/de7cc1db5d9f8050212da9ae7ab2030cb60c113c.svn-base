package org.bigmouth.nvwa.codec.tlv.decoders;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.codec.tlv.TLVCodecProviders;
import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVArrayAttribute;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;
import org.bigmouth.nvwa.utils.ByteUtils;
import org.bigmouth.nvwa.utils.HexUtils;


public class Bean {

	@TLVAttribute(tag = 1, arrayAttributes = { @TLVArrayAttribute(tag = 2) })
	private String[] picVers;

	public String[] getPicVers() {
		return picVers;
	}

	public void setPicVers(String[] picVers) {
		this.picVers = picVers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(picVers);
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
		Bean other = (Bean) obj;
		if (!Arrays.equals(picVers, other.picVers))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

	public static void main(String[] args) {
		TLVEncoder<Object> tlvEncoder = TLVCodecProviders.newBigEndianTLVEncoderProvider()
				.getObjectEncoder();
		TLVDecoder<Object> tlvDecoder = TLVCodecProviders.newBigEndianTLVDecoderProvider()
				.getObjectDecoder();

		Bean a = new Bean();
		a.setPicVers(new String[] { "aa", "bb" });

		System.out.println(a);

		List<byte[]> list = tlvEncoder.codec(a, null);
		byte[] bytes = ByteUtils.union(list);
		HexUtils.printAsHexCodes(bytes, 1024);

		Bean _a = (Bean) tlvDecoder.codec(bytes, Bean.class);
		System.out.println(_a);
	}
}
