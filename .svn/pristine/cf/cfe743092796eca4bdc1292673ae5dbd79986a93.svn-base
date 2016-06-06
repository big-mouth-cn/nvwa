package org.bigmouth.nvwa.codec.tlv.decoders;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.bigmouth.nvwa.codec.byteorder.NumberCodec;
import org.bigmouth.nvwa.codec.byteorder.NumberCodecFactory;
import org.bigmouth.nvwa.codec.tlv.TLVConfig;
import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

import com.google.common.collect.Lists;

public final class TLVStringArrayDecoder extends AbstractTLVDecoder<String[]> {

	private static final String[] DECODER_KEYES = new String[] { String[].class.getName() };

	private TLVDecoderProvider decoderProvider;

	@Override
	public String[] getCodecKeyes() {
		return DECODER_KEYES;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] codec(byte[] source, Object additionInfo) {
		if (null == decoderProvider)
			throw new IllegalStateException("TLVbArrayDecoder must be registered to decodeContext.");
		if (null == source)
			return null;

		List<String> ret = Lists.newArrayList();
		TLVConfig tlvConfig = decoderProvider.getTlvConfig();
		NumberCodec nc = NumberCodecFactory.fetchNumberCodec(decoderProvider.getByteOrder());

		ByteArrayInputStream buf = new ByteArrayInputStream(source);
		while (buf.available() > 0) {
			buf.skip(tlvConfig.getTagLen());
			byte[] lenBytes = new byte[tlvConfig.getLengthLen()];
			try {
				buf.read(lenBytes);
			} catch (IOException e) {
				throw new RuntimeException("TLVStringArrayDecoder.codec:", e);
			}
			int len = nc.bytes2Int(lenBytes, tlvConfig.getLengthLen());

			byte[] dataBytes = new byte[len];
			try {
				buf.read(dataBytes);
			} catch (IOException e) {
				throw new RuntimeException("TLVStringArrayDecoder.codec:", e);
			}
			TLVDecoder<String> tlvStringDecoder = (TLVDecoder<String>) decoderProvider
					.lookupCodec(String.class);
			String v = tlvStringDecoder.codec(dataBytes, null);
			ret.add(v);
		}

		return ret.toArray(new String[0]);
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
