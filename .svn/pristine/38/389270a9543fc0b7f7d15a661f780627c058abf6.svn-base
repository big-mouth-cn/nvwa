package org.bigmouth.nvwa.codec.tlv.decoders;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bigmouth.nvwa.codec.tlv.TLVCodecProviders;
import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.decoders.bean.Bean1;
import org.junit.Test;


public class TLVObjectDecoderTest {

	@Test
	public void test() {
		TLVEncoder<Object> tlvEncoder = TLVCodecProviders.newBigEndianTLVEncoderProvider()
				.getObjectEncoder();
		TLVDecoder<Object> tlvDecoder = TLVCodecProviders.newBigEndianTLVDecoderProvider()
				.getObjectDecoder();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			bos.write(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 1 });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			bos.write(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 100 });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bos.write(new byte[] { (byte) 3 });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			tlvDecoder.codec(bos.toByteArray(), Bean1.class);
			assertTrue(false);
		} catch (RuntimeException e) {
			e.printStackTrace();
			assertTrue(true);
		}

		bos = new ByteArrayOutputStream();
		try {
			bos.write(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 1 });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			bos.write(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 4 });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bos.write(new byte[] { 'a', 'b', 'c', 'd' });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Bean1 b = (Bean1) tlvDecoder.codec(bos.toByteArray(), Bean1.class);
		assertEquals("abcd", b.getV());
	}
}
