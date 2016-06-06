package org.bigmouth.nvwa.codec.tlv.encoders;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.byteorder.ByteOrder;
import org.bigmouth.nvwa.codec.byteorder.NumberCodec;
import org.bigmouth.nvwa.codec.byteorder.NumberCodecFactory;
import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVArrayAttribute;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;



public class TLVLongEncoder implements TLVEncoder<Long> {

	private static final String[] ENCODER_KEYES = new String[] { long.class.getName(),
			Long.class.getName() };

	private TLVEncoderProvider encoderProvider;

	private NumberCodec numberCodec;
	
	private static final int DEFAULT_LONG_LENGTH = 8;

	public TLVLongEncoder() {
	}

	@Override
	public List<byte[]> codec(Long source, Object additionInfo) {
		if (null == encoderProvider || null == numberCodec)
			throw new IllegalStateException("TLVLongEncoder must be registered to encodeContext.");

		if (null == source)
			return new ArrayList<byte[]>();

		// process meta data
		int byteLen = DEFAULT_LONG_LENGTH;
		if (null != additionInfo) {
			if (additionInfo instanceof TLVAttribute) {
				TLVAttribute _a = (TLVAttribute) additionInfo;
				if (_a.byteLen() != 0)
					byteLen = _a.byteLen();
			} else if (additionInfo instanceof TLVArrayAttribute) {
				TLVArrayAttribute _a = (TLVArrayAttribute) additionInfo;
				if (_a.byteLen() != 0)
					byteLen = _a.byteLen();
			}
		}

		byte[] bytes = numberCodec.long2Bytes(source, byteLen);
		List<byte[]> list = new ArrayList<byte[]>();
		list.add(bytes);

		return list;
	}

	@Override
	public TLVEncoderProvider getCodecProvider() {
		return encoderProvider;
	}

	@Override
	public void setCodecProvider(TLVEncoderProvider encoderProvider) {
		this.encoderProvider = encoderProvider;
		ByteOrder byteOrder = encoderProvider.getByteOrder();
		this.numberCodec = NumberCodecFactory.fetchNumberCodec(byteOrder);
	}

	@Override
	public String[] getCodecKeyes() {
		return ENCODER_KEYES;
	}

	@Override
	public String toString() {
		return "TLVLongEncoder";
	}

}
