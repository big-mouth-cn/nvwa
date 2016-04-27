package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

public class TLVShortArrayDecoder extends AbstractTLVDecoder<Short[]> {

	private static final String[] DECODER_KEYES = new String[] { Short[].class.getName() };

	private TLVDecoderProvider decoderProvider;

	private static final int DEFAULT_SHORT_LENGTH = 2;

	@SuppressWarnings("unchecked")
	@Override
	public Short[] codec(byte[] source, Object additionInfo) {
		if (null == source)
			return null;

		TLVDecoder shortDecoder = decoderProvider.lookupCodec(short.class);

		Short[] result = new Short[source.length / DEFAULT_SHORT_LENGTH];
		int result_index = 0;
		for (int i = 0, count = source.length; i < count; i++) {
			byte[] tmp = new byte[DEFAULT_SHORT_LENGTH];
			int begin = i;
			int end = i + DEFAULT_SHORT_LENGTH;
			if (end <= count) {
				System.arraycopy(source, begin, tmp, 0, DEFAULT_SHORT_LENGTH);
				Short v = (Short) shortDecoder.codec(tmp, null);
				result[result_index++] = v;
			} else {
				break;
			}
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
