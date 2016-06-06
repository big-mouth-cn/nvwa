package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

/**
 * Boolean Object Array.
 * 
 * @author nada
 * 
 */
public class TLVBooleanArrayDecoder extends AbstractTLVDecoder<Boolean[]> {

	private static final String[] DECODER_KEYES = new String[] { Boolean[].class.getName() };

	private TLVDecoderProvider decoderProvider;

	@Override
	public Boolean[] codec(byte[] source, Object additionInfo) {
		if (null == decoderProvider)
			throw new IllegalStateException(
					"TLVBooleanArrayDecoder must be registered to decodeContext.");
		if (null == source)
			return null;

		Boolean[] result = new Boolean[source.length];
		for (int i = 0, count = source.length; i < count; i++)
			result[i] = source[i] == 1 ? true : false;

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
