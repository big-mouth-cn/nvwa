package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

/**
 * primitive type boolean array.
 * 
 * @author nada
 * 
 */
public class TLVboolArrayDecoder extends AbstractTLVDecoder<boolean[]> {

	private static final String[] DECODER_KEYES = new String[] { boolean[].class.getName() };

	private TLVDecoderProvider decoderProvider;

	@Override
	public boolean[] codec(byte[] source, Object additionInfo) {
		if (null == decoderProvider)
			throw new IllegalStateException("TLVbArrayDecoder must be registered to decodeContext.");
		if (null == source)
			return null;

		boolean[] result = new boolean[source.length];
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
