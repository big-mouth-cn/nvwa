package org.bigmouth.nvwa.codec.tlv.decoders;

import java.io.UnsupportedEncodingException;

import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TLVStringDecoder extends AbstractTLVDecoder<String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLVStringDecoder.class);

	private static final String[] DECODER_KEYES = new String[] { String.class.getName() };

	private TLVDecoderProvider decoderProvider;

	@Override
	public boolean isObjectDecoder() {
		return false;
	}

	@Override
	public String codec(byte[] source, Object additionInfo) {
		if (null == source)
			throw new NullPointerException("source");

		String result = "";
		String charset = decoderProvider.getCharset();
		try {
			result = new String(source, charset);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("codec", e);
			return null;
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

}
