package org.bigmouth.nvwa.servicelogic.codec.tlv;

import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.bigmouth.nvwa.servicelogic.codec.ContentEncoder;
import org.bigmouth.nvwa.utils.ByteUtils;

public class TlvContentEncoder implements ContentEncoder {

	private final TLVEncoderProvider tlvEncoderProvider;

	public TlvContentEncoder(TLVEncoderProvider tlvEncoderProvider) {
		this.tlvEncoderProvider = tlvEncoderProvider;
	}

	@Override
	public byte[] encode(Object source) {
		if (null == source)
			throw new NullPointerException("source");
		TLVEncoder<Object> tlvObjectEncoder = tlvEncoderProvider.getObjectEncoder();
		List<byte[]> bytesList = tlvObjectEncoder.codec(source, null);
		return ByteUtils.union(bytesList);
	}
}
