package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

public class TLVIntegerArrayDecoder extends AbstractTLVDecoder<Integer[]> {

	private static final String[] DECODER_KEYES = new String[] { Integer[].class.getName() };

	private TLVDecoderProvider decoderProvider;

	@SuppressWarnings("unchecked")
	@Override
	public Integer[] codec(byte[] source, Object additionInfo) {
		if (null == source)
			return null;

		TLVDecoder intDecoder = decoderProvider.lookupCodec(int.class);
		int len = decoderProvider.getIntByteLen();

		Integer[] result = new Integer[source.length / len];
		int result_index = 0;
		for (int i = 0, count = source.length; i < count; i = i + len) {
			byte[] tmp = new byte[len];
			System.arraycopy(source, i, tmp, 0, len);
			Integer v = (Integer) intDecoder.codec(tmp, null);
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
