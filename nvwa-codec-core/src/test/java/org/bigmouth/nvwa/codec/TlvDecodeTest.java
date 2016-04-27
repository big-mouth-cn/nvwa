package org.bigmouth.nvwa.codec;

import org.bigmouth.nvwa.codec.tlv.TLVConfig;
import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProviderImpl;
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
import org.bigmouth.nvwa.codec.tlv.decoders.TLVStringDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVbArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVboolArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVbyteDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVlArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVlongDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVsArrayDecoder;
import org.bigmouth.nvwa.codec.tlv.decoders.TLVshortDecoder;
import org.bigmouth.nvwa.utils.HexUtils;


public class TlvDecodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		
		TLVConfig config = new TLVConfig();
		config.setTagLen(1);
		config.setLengthLen(1);
		decoderProvider.setTlvConfig(config);

		TLVDecoder<Object> decoder = decoderProvider.getObjectDecoder();
		byte[] bytes = HexUtils.hexString2Bytes("06 0F 33 33 30 35 32 31 31 39 38 34 30 32 31 31 30 07 0F 34 36 30 30 32 30 35 34 31 30 34 30 38 38 30 0A 05 65 79 63 6F 6D 0B 05 65 79 32 34 30 09 04 06 06 60 52 22 01 03 1A 04 00 00 00 00 23 04 00 F0 01 40".replaceAll(" ", ""));
		Object obj = decoder.codec(bytes, BizObject.class);
		System.out.println(obj);
	}

}
