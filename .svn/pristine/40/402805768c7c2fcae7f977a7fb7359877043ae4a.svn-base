package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

public class TLVByteArrayDecoder extends AbstractTLVDecoder<Byte[]> {

	private static final String[] DECODER_KEYES = new String[] { Byte[].class.getName() };

	private TLVDecoderProvider decoderProvider;

	@Override
	public Byte[] codec(byte[] source, Object additionInfo) {
		if (null == source)
			return null;
		Byte[] result = new Byte[source.length];
		for (int i = 0, count = source.length; i < count; i++)
			result[i] = source[i];
		return result;
	}

	@Override
	public String[] getCodecKeyes() {
		return DECODER_KEYES;
	}

	@Override
	public TLVDecoderProvider getCodecProvider() {
		return decoderProvider;
	}

	@Override
	public void setCodecProvider(TLVDecoderProvider codecPrivoder) {
		this.decoderProvider = codecPrivoder;
	}

	@Override
	public boolean isObjectDecoder() {
		return false;
	}

}
