package org.bigmouth.nvwa.codec.tlv.encoders;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * encode type of int[] or Integer[].
 * 
 * @author nada
 * 
 */
public class TLVIntArrayEncoder implements TLVEncoder<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLVIntArrayEncoder.class);

	private static final String[] ENCODER_KEYES = new String[] { int[].class.getName(),
			Integer[].class.getName() };

	private TLVEncoderProvider encoderProvider;

	@Override
	public String[] getCodecKeyes() {
		return ENCODER_KEYES;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<byte[]> codec(Object source, Object additionInfo) {
		if (null == encoderProvider)
			throw new IllegalStateException(
					"TLVIntArrayEncoder must be registered to encodeContext.");
		if (null == source)
			return new ArrayList<byte[]>();

		int[] _source = null;
		// primitive type first
		if (source instanceof int[]) {
			_source = (int[]) source;
		} else if (source instanceof Integer[]) {
			Integer[] _s = (Integer[]) source;
			_source = new int[_s.length];
			for (int i = 0, count = _s.length; i < count; i++)
				_source[i] = _s[i];
		} else {
			LOGGER.warn("source is not match type of int[] or Integer[].");
			return new ArrayList<byte[]>();
		}

		TLVEncoder intEncoder = encoderProvider.lookupCodec(int.class);
		List<byte[]> ret = new ArrayList<byte[]>();
		for (int arrValue : _source)
			ret.addAll((List<byte[]>) intEncoder.codec(arrValue, null));

		return ret;
	}

	@Override
	public TLVEncoderProvider getCodecProvider() {
		return encoderProvider;
	}

	@Override
	public void setCodecProvider(TLVEncoderProvider encoderProvider) {
		this.encoderProvider = encoderProvider;
	}

	@Override
	public String toString() {
		return "TLVIntArrayEncoder";
	}
}
