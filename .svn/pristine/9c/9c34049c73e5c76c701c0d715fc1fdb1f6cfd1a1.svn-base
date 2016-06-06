package org.bigmouth.nvwa.codec.jms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.bigmouth.nvwa.codec.jms.bean.TLVMessageBean;
import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;


public class MessageDecoderImpl implements MessageDecoder {

	private static final int DEFAULT_TAG_LENGTH = 4;

	private static final int DEFAULT_LEN_LENGTH = 4;
	
	private TLVDecoderProvider tlvDecoderProvider;

	public MessageDecoderImpl(TLVDecoderProvider tlvDecoderProvider) {
		this.tlvDecoderProvider = tlvDecoderProvider;
	}

	@Override
	public TLVMessageBean decode(byte[] bytes) {
		if (null == bytes)
			throw new NullPointerException("bytes is null");

		if (0 == bytes.length)
			throw new IllegalArgumentException("bytes length is 0");
		
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		
		String sql = extractSql(bis);
		
		
//		tmp = new byte[DEFAULT_TAG_LENGTH];
//		readLen = 0;
//		try {
//			readLen = bis.read(tmp);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		if (readLen != DEFAULT_TAG_LENGTH)
//			return null;
//		int len = (Integer) intDecoder.codec(tmp, null);
		
		
		
		return null;
	}
	
	private List<Object[]> extractArgumentsList(ByteArrayInputStream bis){
		return null;
	}

	private String extractSql(ByteArrayInputStream bis) {
		TLVDecoder intDecoder = tlvDecoderProvider.lookupCodec(int.class);
		TLVDecoder stringDecoder = tlvDecoderProvider.lookupCodec(String.class);
		
		// tag
		byte[] tmp = new byte[DEFAULT_TAG_LENGTH];
		int readLen = 0;
		try {
			readLen = bis.read(tmp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (readLen != DEFAULT_TAG_LENGTH)
			return null;
		int tag = (Integer) intDecoder.codec(tmp, null);
		
		// length
		tmp = new byte[DEFAULT_LEN_LENGTH];
		readLen = 0;
		try {
			readLen = bis.read(tmp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (readLen != DEFAULT_LEN_LENGTH)
			return null;
		int len = (Integer) intDecoder.codec(tmp, null);
		
		// value
		tmp = new byte[len];
		readLen = 0;
		try {
			readLen = bis.read(tmp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (readLen != len)
			return null;
		return (String)stringDecoder.codec(tmp, null);
	}

}
