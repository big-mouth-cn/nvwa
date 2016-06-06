package org.bigmouth.nvwa.codec.tlv.decoders;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVCodecProviders;
import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.utils.ByteUtils;
import org.bigmouth.nvwa.utils.HexUtils;
import org.junit.Test;


public class TLVStringArrayDecoderTest {

	@Test
	public void testTLVStringArrayDecoder() {
		TLVEncoder<Object> tlvEncoder = TLVCodecProviders.newBigEndianTLVEncoderProvider()
				.getObjectEncoder();
		TLVDecoder<Object> tlvDecoder = TLVCodecProviders.newBigEndianTLVDecoderProvider()
				.getObjectDecoder();

		Bean a = new Bean();
		a.setPicVers(new String[] { "aa", "bb" });

		List<byte[]> list = tlvEncoder.codec(a, null);
		byte[] bytes = ByteUtils.union(list);
		HexUtils.printAsHexCodes(bytes, 1024);

		Bean _a = (Bean) tlvDecoder.codec(bytes, Bean.class);

		assertEquals(a, _a);
	}
}
