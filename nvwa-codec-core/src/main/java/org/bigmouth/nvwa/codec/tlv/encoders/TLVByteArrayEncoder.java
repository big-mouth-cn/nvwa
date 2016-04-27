package org.bigmouth.nvwa.codec.tlv.encoders;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TLVByteArrayEncoder implements TLVEncoder<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLVByteArrayEncoder.class);

	private static final String[] ENCODER_KEYES = new String[] { Byte[].class.getName(),
			byte[].class.getName() };

	private TLVEncoderProvider encoderProvider;

	@Override
	public String[] getCodecKeyes() {
		return ENCODER_KEYES;
	}

	@Override
	public List<byte[]> codec(Object source, Object additionInfo) {
		if (null == encoderProvider)
			throw new IllegalStateException(
					"TLVByteArrayEncoder must be registered to encodeContext.");

		if (null == source)
			return new ArrayList<byte[]>();

		byte[] _source = null;

		// primitive type priority
		if (source instanceof byte[]) {
			_source = (byte[]) source;
		} else if (source instanceof Byte[]) {
			Byte[] _s = (Byte[]) source;
			_source = new byte[_s.length];
			for (int i = 0; i < _s.length; i++)
				_source[i] = _s[i];
		} else {
			LOGGER.warn("source is not match type of byte[] or Byte[].");
			return new ArrayList<byte[]>();
		}

		// process meta data
		if (null != additionInfo) {
			TLVAttribute _additionInfo = (TLVAttribute) additionInfo;
			if (_additionInfo.maxLen() > 0 && _additionInfo.maxLen() < _source.length) {
				byte[] tmp = new byte[_additionInfo.maxLen()];
				System.arraycopy(_source, 0, tmp, 0, _additionInfo.maxLen());
				_source = tmp;
			}
		}

		List<byte[]> list = new ArrayList<byte[]>();
		list.add(_source);

		return list;
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
		return "TLVByteArrayEncoder";
	}
}
