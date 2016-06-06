package org.bigmouth.nvwa.codec.tlv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bigmouth.nvwa.codec.byteorder.ByteOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TLVEncoderProviderImpl implements TLVEncoderProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLVEncoderProviderImpl.class);

	private static final String DEFAULT_OBJECT = "java.lang.Object";

	private static final String DEFAULT_OBJECT_ARRAY = "java.lang.Object[]";

	private int intByteLen = 4;

	private ByteOrder byteOrder = ByteOrder.bigEndian;

	private Map<String, TLVEncoder<?>> encoders = new HashMap<String, TLVEncoder<?>>();

	private TLVEncoder<Object> objectEncoder;

	private TLVConfig tlvConfig = new TLVConfig();

	public TLVEncoderProviderImpl() {
	}

	public TLVEncoderProviderImpl(int intByteLen, ByteOrder byteOrder) {
		this.intByteLen = intByteLen;
		this.byteOrder = byteOrder;
	}

	@Override
	public ByteOrder getByteOrder() {
		return byteOrder;
	}

	@Override
	public int getIntByteLen() {
		return intByteLen;
	}

	@Override
	public void setByteOrder(ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
	}

	@Override
	public void setIntByteLen(int byteLen) {
		this.intByteLen = byteLen;
	}

	@Override
	public void registerCodec(TLVEncoder<?> encoder) {
		if (null == encoder)
			throw new NullPointerException("encoder");

		String[] keyes = encoder.getCodecKeyes();
		for (String key : keyes)
			encoders.put(key, encoder);
		encoder.setCodecProvider(this);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("TLVEncoder[" + encoder + "] has registered successful.");
	}

	@Override
	public void removeCodec(String encoderKey) {
		encoders.remove(encoderKey);
	}

	@Override
	public List<TLVEncoder<?>> getAllCodecs() {
		List<TLVEncoder<?>> result = new ArrayList<TLVEncoder<?>>();
		for (Map.Entry<String, TLVEncoder<?>> e : encoders.entrySet())
			result.add(e.getValue());
		return result;
	}

	@Override
	public TLVEncoder<?> lookupCodec(Class<?> sourceClazz) {
		String key = sourceClazz.getName();
		TLVEncoder<?> encoder = encoders.get(sourceClazz.getName());
		if (null == encoder) {
			if (key.startsWith("[["))
				throw new IllegalStateException("can not encoder 2-d array.");
			else if (key.startsWith("["))
				return encoders.get(DEFAULT_OBJECT_ARRAY);
			else
				return encoders.get(DEFAULT_OBJECT);
		}

		return encoder;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TLVEncoder<Object> getObjectEncoder() {
		if (null == objectEncoder) {
			objectEncoder = (TLVEncoder<Object>) this.lookupCodec(Object.class);
			if (null == objectEncoder) {
				LOGGER.error("objectEncoder is null.");
				return null;
			}
		}
		return objectEncoder;
	}

	@Override
	public TLVConfig getTlvConfig() {
		return tlvConfig;
	}

	public void setTlvConfig(TLVConfig tlvConfig) {
		this.tlvConfig = tlvConfig;
	}

}
