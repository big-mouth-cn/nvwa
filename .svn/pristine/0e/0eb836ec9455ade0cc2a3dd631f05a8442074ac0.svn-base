package org.bigmouth.nvwa.codec.tlv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bigmouth.nvwa.codec.byteorder.ByteOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TLVDecoderProviderImpl implements TLVDecoderProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLVDecoderProviderImpl.class);

	private static final String DEFAULT_OBJECT = "java.lang.Object";

	private static final String DEFAULT_OBJECT_ARRAY = "java.lang.Object[]";

	private int intByteLen = 4;

	private ByteOrder byteOrder = ByteOrder.bigEndian;

	private Map<String, TLVDecoder<?>> decoders = new HashMap<String, TLVDecoder<?>>();

	private TLVDecoder<Object> objectDecoder;

	private TLVConfig tlvConfig = new TLVConfig();

	public TLVDecoderProviderImpl() {
	}

	public TLVDecoderProviderImpl(int intByteLen, ByteOrder byteOrder) {
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

	@SuppressWarnings("unchecked")
	@Override
	public TLVDecoder<Object> getObjectDecoder() {
		if (null == objectDecoder) {
			objectDecoder = (TLVDecoder<Object>) this.lookupCodec(Object.class);
			if (null == objectDecoder) {
				LOGGER.error("objectDecoder is null.");
				return null;
			}
		}
		return objectDecoder;
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
	public List<TLVDecoder<?>> getAllCodecs() {
		List<TLVDecoder<?>> result = new ArrayList<TLVDecoder<?>>();
		for (Map.Entry<String, TLVDecoder<?>> e : decoders.entrySet())
			result.add(e.getValue());
		return result;
	}

	@Override
	public TLVDecoder<?> lookupCodec(Class<?> sourceClazz) {
		String key = sourceClazz.getName();
		TLVDecoder<?> decoder = decoders.get(sourceClazz.getName());
		if (null == decoder) {
			if (key.startsWith("["))
				return decoders.get(DEFAULT_OBJECT_ARRAY);
			else
				return decoders.get(DEFAULT_OBJECT);
		}

		return decoder;
	}

	@Override
	public void registerCodec(TLVDecoder<?> decoder) {
		if (null == decoder)
			throw new NullPointerException("decoder");

		String[] keyes = decoder.getCodecKeyes();
		for (String key : keyes)
			decoders.put(key, decoder);
		decoder.setCodecProvider(this);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("TLVDecoder[" + decoder + "] has registered successful.");
	}

	@Override
	public void removeCodec(String codecKey) {
		decoders.remove(codecKey);
	}

	@Override
	public TLVConfig getTlvConfig() {
		return tlvConfig;
	}

	public void setTlvConfig(TLVConfig config) {
		this.tlvConfig = config;
	}
	
	private static final String DEFAULT_CHARSET = "UTF-8";
	
	private String charset = DEFAULT_CHARSET;

	@Override
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
