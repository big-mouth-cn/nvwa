package org.bigmouth.nvwa.codec.tlv.encoders;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TLVLongArrayEncoder implements TLVEncoder<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLVLongArrayEncoder.class);

	private static final String[] ENCODER_KEYES = new String[] { long[].class.getName(),
			Long[].class.getName() };

	private TLVEncoderProvider encoderProvider;

	public TLVLongArrayEncoder() {
	}

	@Override
	public String[] getCodecKeyes() {
		return ENCODER_KEYES;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<byte[]> codec(Object source, Object additionInfo) {
		if (null == encoderProvider)
			throw new IllegalStateException(
					"TLVLongArrayEncoder must be registered to encodeContext.");
		if (null == source)
			return new ArrayList<byte[]>();

		// primitive type first
		long[] _source = null;
		if (source instanceof long[]) {
			_source = (long[]) source;
		} else if (source instanceof Long[]) {
			Long[] _s = (Long[]) source;
			_source = new long[_s.length];
			for (int i = 0, count = _s.length; i < count; i++)
				_source[i] = _s[i];
		} else {
			LOGGER.warn("source is not match type of long[] or Long[].");
			return new ArrayList<byte[]>();
		}

		List<byte[]> ret = new ArrayList<byte[]>();
		TLVEncoder longEncoder = encoderProvider.lookupCodec(long.class);

		for (long arrValue : _source) {
			ret.addAll((List<byte[]>) longEncoder.codec(arrValue, null));
		}
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
		return "TLVLongArrayEncoder";
	}

}
