package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

public class TLVlArrayDecoder extends AbstractTLVDecoder<long[]> {

	private static final String[] DECODER_KEYES = new String[] { long[].class.getName() };

	private TLVDecoderProvider decoderProvider;

	private static final int DEFAULT_LONG_LENGTH = 8;

	@SuppressWarnings("unchecked")
	@Override
	public long[] codec(byte[] source, Object additionInfo) {
		if (null == source)
			return null;

		TLVDecoder longDecoder = decoderProvider.lookupCodec(long.class);

		long[] result = new long[source.length / DEFAULT_LONG_LENGTH];
		int result_index = 0;
		for (int i = 0, count = source.length; i < count; i = i + DEFAULT_LONG_LENGTH) {
			byte[] tmp = new byte[DEFAULT_LONG_LENGTH];
			System.arraycopy(source, i, tmp, 0, DEFAULT_LONG_LENGTH);
			Long v = (Long) longDecoder.codec(tmp, null);
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
