package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

public class TLVLongArrayDecoder extends AbstractTLVDecoder<Long[]> {

	private static final String[] DECODER_KEYES = new String[] { Long[].class.getName() };

	private TLVDecoderProvider decoderProvider;

	private static final int DEFAULT_LONG_LENGTH = 8;

	@SuppressWarnings("unchecked")
	@Override
	public Long[] codec(byte[] source, Object additionInfo) {
		if (null == source)
			return null;

		TLVDecoder longDecoder = decoderProvider.lookupCodec(long.class);

		Long[] result = new Long[source.length / DEFAULT_LONG_LENGTH];
		int result_index = 0;
		for (int i = 0, count = source.length; i < count; i++) {
			byte[] tmp = new byte[DEFAULT_LONG_LENGTH];
			int begin = i;
			int end = i + DEFAULT_LONG_LENGTH;
			if (end <= count) {
				System.arraycopy(source, begin, tmp, 0, DEFAULT_LONG_LENGTH);
				Long v = (Long) longDecoder.codec(tmp, null);
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
