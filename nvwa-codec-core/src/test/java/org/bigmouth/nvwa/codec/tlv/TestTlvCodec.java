package org.bigmouth.nvwa.codec.tlv;

import java.util.List;

import org.bigmouth.nvwa.utils.ByteUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestTlvCodec {

	@Test
	public void testUTF16BE() {
		TLVEncoderProvider tlvEncoderProvider = TLVCodecProviders.newBigEndianTLVEncoderProvider();
		TLVEncoder<Object> tlvEncoder = tlvEncoderProvider.getObjectEncoder();

		TLVDecoderProvider tlvDecoderProvider = TLVCodecProviders
				.newBigEndianUTF16BETLVDecoderProvider();
		TLVDecoder<Object> tlvDecoder = tlvDecoderProvider.getObjectDecoder();

		Utf16beObject obj = new Utf16beObject();
		obj.setName("中文哦123abc");
		obj.setValue("这里是中文值哦");

		List<byte[]> byteList = tlvEncoder.codec(obj, null);
		byte[] bytes = ByteUtils.union(byteList);

		Utf16beObject other = (Utf16beObject) tlvDecoder.codec(bytes, Utf16beObject.class);

		System.out.println("other:" + other);

		Assert.assertEquals(obj, other);
	}

}
