package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

public class TLVBooleanDecoder extends AbstractTLVDecoder<Boolean> {

	private static final String[] DECODER_KEYES = new String[] { boolean.class.getName(),
			Boolean.class.getName() };

	private TLVDecoderProvider decoderProvider;

	@Override
	public Boolean codec(byte[] source, Object additionInfo) {
		if (null == source)
			return false;
		if (source.length != 1)
			return false;
		return source[0] == 1 ? true : false;
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
