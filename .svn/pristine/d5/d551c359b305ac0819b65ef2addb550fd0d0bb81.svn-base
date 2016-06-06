package org.bigmouth.nvwa.codec.tlv;

import org.bigmouth.nvwa.codec.tlv.decoders.TLVBooleanArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVBooleanDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVByteArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVIntArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVIntDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVIntegerArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVLongArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVObjectArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVObjectDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVShortArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVStringArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVStringDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVbArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVboolArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVbyteDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVlArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVlongDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVsArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVshortDecoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVBooleanArrayEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVBooleanEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVByteArrayEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVByteEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVIntArrayEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVIntEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVLongArrayEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVLongEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVObjectArrayEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVObjectEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVShortArrayEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVShortEncoder;
import org.bigmouth.nvwa.codec.tlv.encoders.TLVStringEncoder;

/**
 * Convenience factory methods.
 * 
 * @author nada
 * 
 */
public class TLVCodecProviders {

	public static TLVEncoderProvider newBigEndianTLVEncoderProvider() {
		TLVEncoderProvider encoderProvider = new TLVEncoderProviderImpl();

		encoderProvider.registerCodec(new TLVBooleanArrayEncoder());
		encoderProvider.registerCodec(new TLVBooleanEncoder());

		encoderProvider.registerCodec(new TLVByteArrayEncoder());
		encoderProvider.registerCodec(new TLVByteEncoder());

		encoderProvider.registerCodec(new TLVIntArrayEncoder());
		encoderProvider.registerCodec(new TLVIntEncoder());

		encoderProvider.registerCodec(new TLVLongArrayEncoder());
		encoderProvider.registerCodec(new TLVLongEncoder());

		encoderProvider.registerCodec(new TLVObjectArrayEncoder());
		encoderProvider.registerCodec(new TLVObjectEncoder());

		encoderProvider.registerCodec(new TLVShortArrayEncoder());
		encoderProvider.registerCodec(new TLVShortEncoder());

		encoderProvider.registerCodec(new TLVStringEncoder());

		return encoderProvider;
	}

	/**
	 * for list
	 * 
	 * @param tlvConfig
	 * @return
	 */
	public static TLVEncoderProvider newBigEndianTLVListEncoderProvider() {
		TLVEncoderProviderImpl encoderProvider = new TLVEncoderProviderImpl();

		encoderProvider.registerCodec(new TLVBooleanArrayEncoder());
		encoderProvider.registerCodec(new TLVBooleanEncoder());

		encoderProvider.registerCodec(new TLVByteArrayEncoder());
		encoderProvider.registerCodec(new TLVByteEncoder());

		encoderProvider.registerCodec(new TLVIntArrayEncoder());
		encoderProvider.registerCodec(new TLVIntEncoder());

		encoderProvider.registerCodec(new TLVLongArrayEncoder());
		encoderProvider.registerCodec(new TLVLongEncoder());

		encoderProvider.registerCodec(new TLVObjectArrayEncoder());

		encoderProvider.registerCodec(new TLVObjectEncoder());

		encoderProvider.registerCodec(new TLVShortArrayEncoder());
		encoderProvider.registerCodec(new TLVShortEncoder());

		encoderProvider.registerCodec(new TLVStringEncoder());

		TLVConfig tlvConfig = new TLVConfig();
		tlvConfig.setTagLen(1);
		tlvConfig.setLengthLen(2);

		encoderProvider.setTlvConfig(tlvConfig);

		return encoderProvider;
	}

	public static TLVDecoderProvider newBigEndianTLVDecoderProvider() {
		TLVDecoderProvider decoderProvider = new TLVDecoderProviderImpl();

		decoderProvider.registerCodec(new TLVBooleanDecoder());
		decoderProvider.registerCodec(new TLVboolArrayDecoder());
		decoderProvider.registerCodec(new TLVBooleanArrayDecoder());

		decoderProvider.registerCodec(new TLVbyteDecoder());
		decoderProvider.registerCodec(new TLVbArrayDecoder());
		decoderProvider.registerCodec(new TLVByteArrayDecoder());

		decoderProvider.registerCodec(new TLVIntDecoder());
		decoderProvider.registerCodec(new TLVIntArrayDecoder());
		decoderProvider.registerCodec(new TLVIntegerArrayDecoder());

		decoderProvider.registerCodec(new TLVlongDecoder());
		decoderProvider.registerCodec(new TLVlArrayDecoder());
		decoderProvider.registerCodec(new TLVLongArrayDecoder());

		decoderProvider.registerCodec(new TLVObjectArrayDecoder());
		decoderProvider.registerCodec(new TLVObjectDecoder());

		decoderProvider.registerCodec(new TLVsArrayDecoder());
		decoderProvider.registerCodec(new TLVShortArrayDecoder());
		decoderProvider.registerCodec(new TLVshortDecoder());

		decoderProvider.registerCodec(new TLVStringDecoder());
		decoderProvider.registerCodec(new TLVStringArrayDecoder());

		return decoderProvider;
	}

	public static TLVDecoderProvider newBigEndianUTF16BETLVDecoderProvider() {
		TLVDecoderProviderImpl decoderProvider = new TLVDecoderProviderImpl();

		decoderProvider.registerCodec(new TLVBooleanDecoder());
		decoderProvider.registerCodec(new TLVboolArrayDecoder());
		decoderProvider.registerCodec(new TLVBooleanArrayDecoder());

		decoderProvider.registerCodec(new TLVbyteDecoder());
		decoderProvider.registerCodec(new TLVbArrayDecoder());
		decoderProvider.registerCodec(new TLVByteArrayDecoder());

		decoderProvider.registerCodec(new TLVIntDecoder());
		decoderProvider.registerCodec(new TLVIntArrayDecoder());
		decoderProvider.registerCodec(new TLVIntegerArrayDecoder());

		decoderProvider.registerCodec(new TLVlongDecoder());
		decoderProvider.registerCodec(new TLVlArrayDecoder());
		decoderProvider.registerCodec(new TLVLongArrayDecoder());

		decoderProvider.registerCodec(new TLVObjectArrayDecoder());
		decoderProvider.registerCodec(new TLVObjectDecoder());

		decoderProvider.registerCodec(new TLVsArrayDecoder());
		decoderProvider.registerCodec(new TLVShortArrayDecoder());
		decoderProvider.registerCodec(new TLVshortDecoder());

		decoderProvider.registerCodec(new TLVStringDecoder());
		decoderProvider.registerCodec(new TLVStringArrayDecoder());

		decoderProvider.setCharset("UTF-16BE");

		return decoderProvider;
	}

	public static TLVDecoderProvider newBigEndianTLVDecoderProvider(TLVConfig config) {
		if (null == config)
			throw new NullPointerException("config");

		TLVDecoderProviderImpl decoderProvider = new TLVDecoderProviderImpl();

		decoderProvider.registerCodec(new TLVBooleanDecoder());
		decoderProvider.registerCodec(new TLVboolArrayDecoder());
		decoderProvider.registerCodec(new TLVBooleanArrayDecoder());

		decoderProvider.registerCodec(new TLVbyteDecoder());
		decoderProvider.registerCodec(new TLVbArrayDecoder());
		decoderProvider.registerCodec(new TLVByteArrayDecoder());

		decoderProvider.registerCodec(new TLVIntDecoder());
		decoderProvider.registerCodec(new TLVIntArrayDecoder());
		decoderProvider.registerCodec(new TLVIntegerArrayDecoder());

		decoderProvider.registerCodec(new TLVlongDecoder());
		decoderProvider.registerCodec(new TLVlArrayDecoder());
		decoderProvider.registerCodec(new TLVLongArrayDecoder());

		decoderProvider.registerCodec(new TLVObjectArrayDecoder());
		decoderProvider.registerCodec(new TLVObjectDecoder());

		decoderProvider.registerCodec(new TLVsArrayDecoder());
		decoderProvider.registerCodec(new TLVShortArrayDecoder());
		decoderProvider.registerCodec(new TLVshortDecoder());

		decoderProvider.registerCodec(new TLVStringDecoder());
		decoderProvider.registerCodec(new TLVStringArrayDecoder());

		decoderProvider.setTlvConfig(config);

		return decoderProvider;
	}

	public static TLVDecoderProvider newTightenBigEndianTLVDecoderProvider() {
		TLVDecoderProviderImpl decoderProvider = new TLVDecoderProviderImpl();

		decoderProvider.registerCodec(new TLVBooleanDecoder());
		decoderProvider.registerCodec(new TLVboolArrayDecoder());
		decoderProvider.registerCodec(new TLVBooleanArrayDecoder());

		decoderProvider.registerCodec(new TLVbyteDecoder());
		decoderProvider.registerCodec(new TLVbArrayDecoder());
		decoderProvider.registerCodec(new TLVByteArrayDecoder());

		decoderProvider.registerCodec(new TLVIntDecoder());
		decoderProvider.registerCodec(new TLVIntArrayDecoder());
		decoderProvider.registerCodec(new TLVIntegerArrayDecoder());

		decoderProvider.registerCodec(new TLVlongDecoder());
		decoderProvider.registerCodec(new TLVlArrayDecoder());
		decoderProvider.registerCodec(new TLVLongArrayDecoder());

		decoderProvider.registerCodec(new TLVObjectArrayDecoder());
		decoderProvider.registerCodec(new TLVObjectDecoder());

		decoderProvider.registerCodec(new TLVsArrayDecoder());
		decoderProvider.registerCodec(new TLVShortArrayDecoder());
		decoderProvider.registerCodec(new TLVshortDecoder());

		decoderProvider.registerCodec(new TLVStringDecoder());
		decoderProvider.registerCodec(new TLVStringArrayDecoder());

		TLVConfig config = new TLVConfig();
		config.setTagLen(1);
		config.setLengthLen(1);

		decoderProvider.setTlvConfig(config);

		return decoderProvider;
	}

}
