package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

public class TLVbyteDecoder extends AbstractTLVDecoder<Byte> {

	private static final String[] DECODER_KEYES = new String[] { byte.class.getName(),
			Byte.class.getName() };

	private TLVDecoderProvider decoderProvider;

	@Override
	public boolean isObjectDecoder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Byte codec(byte[] source, Object additionInfo) {
		if (null == source)
			throw new NullPointerException("source");

		if (source.length == 1)
			return source[0];
		return 0;
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

}
