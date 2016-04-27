package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

/**
 * 2 bytes = 1 short.
 * 
 * @author nada
 * 
 */
public class TLVsArrayDecoder extends AbstractTLVDecoder<short[]> {

	private static final String[] DECODER_KEYES = new String[] { short[].class.getName() };

	private TLVDecoderProvider decoderProvider;

	private static final int DEFAULT_SHORT_LENGTH = 2;

	@SuppressWarnings("unchecked")
	@Override
	public short[] codec(byte[] source, Object additionInfo) {
		if (null == source)
			return null;

		TLVDecoder shortDecoder = decoderProvider.lookupCodec(short.class);

		short[] result = new short[source.length / DEFAULT_SHORT_LENGTH];
		int result_index = 0;
		for (int i = 0, count = source.length; i < count; i = i + DEFAULT_SHORT_LENGTH) {
			byte[] tmp = new byte[DEFAULT_SHORT_LENGTH];
			System.arraycopy(source, i, tmp, 0, DEFAULT_SHORT_LENGTH);
			Short v = (Short) shortDecoder.codec(tmp, null);
			result[result_index++] = v;
		}
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
