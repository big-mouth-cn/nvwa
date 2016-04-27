package org.bigmouth.nvwa.codec.tlv.encoders;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;


/**
 * 1 byte
 * 
 * @author nada
 * 
 */
public class TLVBooleanEncoder implements TLVEncoder<Boolean> {

	private static final String[] ENCODER_KEYES = new String[] { boolean.class.getName(),
			Boolean.class.getName() };

	private TLVEncoderProvider encoderProvider;

	@Override
	public List<byte[]> codec(Boolean source, Object additionInfo) {
		if (null == encoderProvider)
			throw new IllegalStateException(
					"TLVBooleanEncoder must be registered to encodeContext.");

		if (null == source)
			return new ArrayList<byte[]>();

		byte v = source ? (byte) 1 : (byte) 0;

		byte[] bytes = new byte[] { v };
		List<byte[]> list = new ArrayList<byte[]>();
		list.add(bytes);

		return list;
	}

	@Override
	public String[] getCodecKeyes() {
		return ENCODER_KEYES;
	}

	@Override
	public TLVEncoderProvider getCodecProvider() {
		return encoderProvider;
	}

	@Override
	public void setCodecProvider(TLVEncoderProvider codecPrivoder) {
		this.encoderProvider = codecPrivoder;

	}

	@Override
	public String toString() {
		return "TLVBooleanEncoder";
	}

}
