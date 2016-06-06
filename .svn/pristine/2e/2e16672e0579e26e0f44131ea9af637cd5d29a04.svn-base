package org.bigmouth.nvwa.codec.tlv.encoders;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TLVShortArrayEncoder implements TLVEncoder<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLVShortArrayEncoder.class);

	private static final String[] ENCODER_KEYES = new String[] { short[].class.getName(),
			Short[].class.getName() };

	private TLVEncoderProvider encoderProvider;

	public TLVShortArrayEncoder() {
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
					"TLVShortArrayEncoder must be registered to encodeContext.");
		if (null == source)
			return new ArrayList<byte[]>();

		short[] _source = null;
		if (source instanceof short[]) {
			_source = (short[]) source;
		} else if (source instanceof Short[]) {
			Short[] _s = (Short[]) source;
			_source = new short[_s.length];
			for (int i = 0, count = _s.length; i < count; i++)
				_source[i] = _s[i];
		} else {
			LOGGER.warn("source is not match type of short[] or Short[].");
			return new ArrayList<byte[]>();
		}

		List<byte[]> ret = new ArrayList<byte[]>();
		TLVEncoder shortEncoder = encoderProvider.lookupCodec(short.class);

		for (short arrValue : _source)
			ret.addAll((List<byte[]>) shortEncoder.codec(arrValue, null));
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
		return "TLVShortArrayEncoder";
	}

}
