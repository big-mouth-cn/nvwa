package org.bigmouth.nvwa.codec.tlv.decoders;

import static junit.framework.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bigmouth.nvwa.codec.byteorder.ByteOrder;
import org.bigmouth.nvwa.codec.byteorder.NumberCodec;
import org.bigmouth.nvwa.codec.byteorder.NumberCodecFactory;
import org.bigmouth.nvwa.codec.tlv.TLVCodecProviders;
import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.bean.NumberValueBean;
import org.bigmouth.nvwa.codec.tlv.decoders.IllegalTLVLengthException;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVObjectArrayDecoder;
import org.junit.Test;


public class TLVObjectArrayDecoderTest {

	@Test
	public void testTLVObjectArrayDecoder() {

		TLVDecoder<Object> tlvDecoder = TLVCodecProviders.newBigEndianTLVDecoderProvider()
				.getObjectDecoder();
		
		TLVObjectArrayDecoder decoder = new TLVObjectArrayDecoder();
		decoder.setCodecProvider(tlvDecoder.getCodecProvider());
		
		NumberCodec nc = NumberCodecFactory.fetchNumberCodec(ByteOrder.bigEndian);

		// create bytes
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			bos.write(nc.int2Bytes(10, 4));
			bos.write(nc.int2Bytes(21, 4));
			
			bos.write(nc.int2Bytes(1, 4));
			bos.write(nc.int2Bytes(4, 4));
			bos.write(nc.int2Bytes(1, 4));
			bos.write(nc.int2Bytes(2, 4));
			bos.write(nc.int2Bytes(1, 4));
			bos.write('a');
			
			bos.write(nc.int2Bytes(10, 4));
			bos.write(nc.int2Bytes(24, 4));
//			bos.write(nc.int2Bytes(240000000, 4));
			
			bos.write(nc.int2Bytes(1, 4));
			bos.write(nc.int2Bytes(4, 4));
			bos.write(nc.int2Bytes(2, 4));
			bos.write(nc.int2Bytes(2, 4));
			bos.write(nc.int2Bytes(4, 4));
			bos.write(new byte[]{'a','b','c','d'});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Object[] ret = decoder.codec(bos.toByteArray(), NumberValueBean.class);
		
		assertEquals(new NumberValueBean(1,"a"),ret[0]);
		assertEquals(new NumberValueBean(2,"abcd"),ret[1]);
	}
	
	@Test
	public void testTLVObjectArrayDecoder_exception() {

		TLVDecoder<Object> tlvDecoder = TLVCodecProviders.newBigEndianTLVDecoderProvider()
				.getObjectDecoder();
		
		TLVObjectArrayDecoder decoder = new TLVObjectArrayDecoder();
		decoder.setCodecProvider(tlvDecoder.getCodecProvider());
		
		NumberCodec nc = NumberCodecFactory.fetchNumberCodec(ByteOrder.bigEndian);

		// create bytes
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			bos.write(nc.int2Bytes(10, 4));
			bos.write(nc.int2Bytes(21, 4));
			
			bos.write(nc.int2Bytes(1, 4));
			bos.write(nc.int2Bytes(4, 4));
			bos.write(nc.int2Bytes(1, 4));
			bos.write(nc.int2Bytes(2, 4));
			bos.write(nc.int2Bytes(1, 4));
			bos.write('a');
			
			bos.write(nc.int2Bytes(10, 4));
			bos.write(nc.int2Bytes(240000000, 4));
			
			bos.write(nc.int2Bytes(1, 4));
			bos.write(nc.int2Bytes(4, 4));
			bos.write(nc.int2Bytes(2, 4));
			bos.write(nc.int2Bytes(2, 4));
			bos.write(nc.int2Bytes(4, 4));
			bos.write(new byte[]{'a','b','c','d'});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean ifException = false;
		try {
			decoder.codec(bos.toByteArray(), NumberValueBean.class);
		} catch (IllegalTLVLengthException e) {
			e.printStackTrace();
			ifException = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(ifException);
	}
}
