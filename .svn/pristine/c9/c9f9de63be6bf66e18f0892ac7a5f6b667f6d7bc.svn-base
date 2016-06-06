package org.bigmouth.nvwa.codec.tlv.encoders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVArrayAttribute;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TLVStringEncoder implements TLVEncoder<String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLVStringEncoder.class);

	private static final String[] ENCODER_KEYES = new String[] { String.class.getName() };

	private static final String CHARSET = "UTF-8";

	private TLVEncoderProvider encoderProvider;

	public TLVStringEncoder() {
	}

	@Override
	public List<byte[]> codec(String source, Object additionInfo) {
		if (null == encoderProvider)
			throw new IllegalStateException("TLVStringEncoder must be registered to encodeContext.");

		if (null == source)
			return new ArrayList<byte[]>();

		// process meta data
		String charset = CHARSET;
		if (null != additionInfo) {
			if (additionInfo instanceof TLVAttribute) {
				TLVAttribute _a = (TLVAttribute) additionInfo;
				if (!_a.charset().equals(""))
					charset = _a.charset();
			} else if (additionInfo instanceof TLVArrayAttribute) {
				TLVArrayAttribute _a = (TLVArrayAttribute) additionInfo;
				if (!_a.charset().equals(""))
					charset = _a.charset();
			}
		}

		try {
			return Arrays.asList(source.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("TLVStringEncoder:", e);
		}
		return null;
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
	public String[] getCodecKeyes() {
		return ENCODER_KEYES;
	}

	@Override
	public String toString() {
		return "TLVStringEncoder";
	}
}
