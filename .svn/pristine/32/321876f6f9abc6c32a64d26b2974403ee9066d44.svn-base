package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

public class TLVbArrayDecoder extends AbstractTLVDecoder<byte[]> {

	private static final String[] DECODER_KEYES = new String[] { byte[].class.getName() };

	private TLVDecoderProvider decoderProvider;

	@Override
	public byte[] codec(byte[] source, Object additionInfo) {
		return source;
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
