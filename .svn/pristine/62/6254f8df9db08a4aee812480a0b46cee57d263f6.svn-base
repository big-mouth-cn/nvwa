package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

/**
 * process source as 1-dimensional array.<br>
 * 4 byte = 1 int.<br>
 * 
 * @author nada
 * 
 */
public class TLVIntArrayDecoder extends AbstractTLVDecoder<int[]> {

	private static final String[] DECODER_KEYES = new String[] { int[].class.getName() };

	private TLVDecoderProvider decoderProvider;

	@SuppressWarnings("unchecked")
	@Override
	public int[] codec(byte[] source, Object additionInfo) {
		if (null == source)
			return null;

		TLVDecoder intDecoder = decoderProvider.lookupCodec(int.class);
		int len = decoderProvider.getIntByteLen();

		int[] result = new int[source.length / len];
		int result_index = 0;
		for (int i = 0, count = source.length; i < count; i++) {
			byte[] tmp = new byte[len];
			int begin = i;
			int end = i + len;
			if (end <= count) {
				System.arraycopy(source, begin, tmp, 0, len);
				Integer v = (Integer) intDecoder.codec(tmp, null);
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
