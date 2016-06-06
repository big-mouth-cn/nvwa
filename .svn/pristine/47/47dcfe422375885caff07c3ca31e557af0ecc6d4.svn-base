package org.bigmouth.nvwa.codec.tlv.encoders;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.byteorder.NumberCodec;
import org.bigmouth.nvwa.codec.byteorder.NumberCodecFactory;
import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVArrayAttribute;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * only support to encode 1-dimensional array
 * 
 * @author nada
 * 
 */
public class TLVObjectArrayEncoder implements TLVEncoder<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLVObjectArrayEncoder.class);

	private static final String[] ENCODER_KEYES = new String[] { "java.lang.Object[]" };

	private TLVEncoderProvider encoderProvider;

	public TLVObjectArrayEncoder() {
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
					"TLVObjectArrayEncoder must be registered to encodeContext.");
		if (null == source)
			return new ArrayList<byte[]>();

		Class<?> sourceClazz = source.getClass();

		if (!sourceClazz.isArray())
			throw new IllegalArgumentException("source must be type of Array.");

		Class<?> subClazz = sourceClazz.getComponentType();
		if (subClazz.isArray())
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("source must be 1-dimensional array,not support >= 2 dimensional array.");

		TLVArrayAttribute[] arrayAttrs = ((TLVAttribute)additionInfo).arrayAttributes();
		if (null == arrayAttrs || arrayAttrs.length != 1)
			throw new IllegalArgumentException(
					"invalid TLVArrayAttribute,the tag value of array will be ignorn.");

		List<byte[]> ret = new ArrayList<byte[]>();
//		TLVEncoder intEncoder = encoderProvider.lookupCodec(Integer.class);
		TLVArrayAttribute arrayAttr = arrayAttrs[0];
		NumberCodec numberCodec = NumberCodecFactory.fetchNumberCodec(this.encoderProvider
				.getByteOrder());

		Object[] arrValues = (Object[]) source;
		for (Object arrValue : arrValues) {
			if(null == arrValue)
				continue;
			// tag
			byte[] tag_bytes = numberCodec.int2Bytes(arrayAttr.tag(), encoderProvider.getTlvConfig().getTagLen());
//			ret.addAll((List<byte[]>) intEncoder.codec(arrayAttr.tag(), null));

			TLVEncoder encoder = encoderProvider.lookupCodec(arrValue.getClass());
			List<byte[]> byteList = (List<byte[]>) encoder.codec(arrValue, null);
			int len = 0;
			for (byte[] bs : byteList)
				len += bs.length;

			// len
			byte[] len_bytes = numberCodec.int2Bytes(len, encoderProvider.getTlvConfig().getLengthLen());
//			ret.addAll((List<byte[]>) intEncoder.codec(len, null));

			//add by gallen.chu 2011-08-16
			boolean ignoreWrapTag = arrayAttr.ignoreWrapTag();
			if(!ignoreWrapTag){
				ret.add(tag_bytes);
				ret.add(len_bytes);
			}
			// value
			ret.addAll(byteList);
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
		return "TLVObjectArrayEncoder";
	}

}
