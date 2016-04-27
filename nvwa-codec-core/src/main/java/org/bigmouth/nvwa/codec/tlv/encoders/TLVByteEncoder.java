package org.bigmouth.nvwa.codec.tlv.encoders;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;


public class TLVByteEncoder implements TLVEncoder<Byte> {

	private static final String[] ENCODER_KEYES = new String[] { byte.class.getName(),
			Byte.class.getName() };

	private TLVEncoderProvider encoderProvider;

	@Override
	public String[] getCodecKeyes() {
		return ENCODER_KEYES;
	}

	@Override
	public List<byte[]> codec(Byte source, Object additionInfo) {
		if (null == encoderProvider)
			throw new IllegalStateException("TLVByteEncoder must be registered to encodeContext.");

		if (null == source)
			return new ArrayList<byte[]>();
			
		byte[] bytes = new byte[] { source.byteValue() };
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
	}

	@Override
	public String toString() {
		return "TLVByteEncoder";
	}
}
