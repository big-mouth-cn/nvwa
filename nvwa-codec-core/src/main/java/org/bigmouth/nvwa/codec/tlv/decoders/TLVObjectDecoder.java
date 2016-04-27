package org.bigmouth.nvwa.codec.tlv.decoders;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.bigmouth.nvwa.codec.byteorder.NumberCodec;
import org.bigmouth.nvwa.codec.byteorder.NumberCodecFactory;
import org.bigmouth.nvwa.codec.tlv.TLVConfig;
import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;
import org.bigmouth.nvwa.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Depth-first decode.
 * 
 * @author nada
 * 
 */
public class TLVObjectDecoder extends AbstractTLVDecoder<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLVObjectDecoder.class);

	private static final String[] DECODER_KEYES = new String[] { Object.class.getName() };

	private TLVDecoderProvider decoderProvider;

	private static final int DEFAULT_TAG_LENGTH = 4;

	private static final int DEFAULT_LEN_LENGTH = 4;

    @SuppressWarnings("unchecked")
	@Override
	public Object codec(byte[] source, Object targetClass) {
		// targetObject is type of tlv annotation class
		if (null == source || null == targetClass)
			throw new NullPointerException("source is null or targetObject is null.");

		if (!(targetClass instanceof Class<?>))
			throw new IllegalArgumentException("targetClass must be type of class.");

		// Map(tag -> field)
		final Map<Integer, Field> fieldMap = new HashMap<Integer, Field>();
		Class<?> targetClazz = (Class<?>) targetClass;
		ReflectUtils.findFields(targetClazz, new ReflectUtils.FieldFilter() {
			@Override
			public boolean accept(final Field filed) {
				TLVAttribute tlvAttr = filed.getAnnotation(TLVAttribute.class);
				if (null != tlvAttr && tlvAttr.tag() != 0) {
					if (!filed.isAccessible())
						filed.setAccessible(true);
					fieldMap.put(tlvAttr.tag(), filed);
				}
				return false;
			}
		});

		// init instance
		Object targetObject = null;
		try {
			targetObject = targetClazz.newInstance();
		} catch (InstantiationException e1) {
			LOGGER.error("codec:", e1);
			return targetObject;
		} catch (IllegalAccessException e1) {
			LOGGER.error("codec:", e1);
			return targetObject;
		}

		// parse list data
//		TLVDecoder intDecoder = decoderProvider.lookupCodec(int.class);
		NumberCodec nc = NumberCodecFactory.fetchNumberCodec(decoderProvider.getByteOrder());
		ByteArrayInputStream bis = new ByteArrayInputStream(source);
		do {

			TLVConfig tlvConfig = decoderProvider.getTlvConfig();
			// tag
			byte[] tmp = new byte[tlvConfig.getTagLen()];
			int readLen = 0;
			try {
				readLen = bis.read(tmp);
			} catch (IOException e) {
				LOGGER.error("codec:", e);
			}
			if (readLen != tlvConfig.getTagLen())
				break;
			
			int tag = nc.bytes2Int(tmp, tlvConfig.getTagLen());
//			int tag = (Integer) intDecoder.codec(tmp, null);

			// length
			tmp = new byte[tlvConfig.getLengthLen()];
			readLen = 0;
			try {
				readLen = bis.read(tmp);
			} catch (IOException e) {
				LOGGER.error("codec:", e);
			}
			if (readLen != tlvConfig.getLengthLen())
				break;
			
			int len = nc.bytes2Int(tmp, tlvConfig.getTagLen());
//			int len = (Integer) intDecoder.codec(tmp, null);

			// check length,avoid Out of memory.
			if (len > bis.available()) {
			    if (null != illegalTLVContentHandler) {
			        illegalTLVContentHandler.handle(source);
			    }
				throw new RuntimeException("Tag:" + tag + " illegal length:" + len);
			}
//			Field ff = fieldMap.get(tag);
//			tmp = new byte[len];
//			readLen = 0;
//			try {
//			    readLen = bis.read(tmp);
//			    System.out.print("tag: "+ tag + ", name: "+ff.getName() + ", value: ");
//			    if (readLen == 4) {
//			        // string
//			        System.out.println(nc.bytes2Int(tmp, readLen));
//			    }
//			    else if (readLen == 1) {
//			        // byte
//			        System.out.println(nc.bytes2Short(tmp, readLen));
//			    }
//			    else {
//			        System.out.println(new String(tmp));
//			    }
//			} catch (IOException e) {
//			    LOGGER.error("codec:", e);
//			}

			// value
			tmp = new byte[len];
			readLen = 0;
			try {
				readLen = bis.read(tmp);
			} catch (IOException e) {
				LOGGER.error("codec:", e);
			}
			if (readLen != len)
				break;
			Field f = fieldMap.get(tag);

			//fixed on 2011-03-10 begin
			if(null == f){
				if(LOGGER.isDebugEnabled())
					LOGGER.debug("tag value " + tag + " can not match any field in " + targetClazz + ",ignore.");

				continue;
			}
			//fixed on 2011-03-10 end

			Class<?> fieldClass = f.getType();
			TLVDecoder genericDecoder = decoderProvider.lookupCodec(fieldClass);

			Object fieldValue = null;
			if (fieldClass.isArray())// is array
				fieldValue = genericDecoder.codec(tmp, fieldClass.getComponentType());
			else {
				TLVAttribute tlvAttr = f.getAnnotation(TLVAttribute.class);
				String propertyName = tlvAttr.propertyName();
				if (propertyName.length() > 0) {// only one property of object.
					fieldValue = createObjectWithProperty(tmp, propertyName, fieldClass);
				} else {
					fieldValue = genericDecoder.codec(tmp, fieldClass);
				}
			}

			try {
				f.set(targetObject, fieldValue);
			} catch (IllegalArgumentException e) {
				LOGGER.error("codec", e);
				return targetObject;
			} catch (IllegalAccessException e) {
				LOGGER.error("codec", e);
				return targetObject;
			}

		} while (true);

		return targetObject;
	}

	@SuppressWarnings("unchecked")
	private Object createObjectWithProperty(byte[] source, String propertyName, Class<?> clazz) {
		Object object = null;
		try {
			object = clazz.newInstance();
			Field f = clazz.getDeclaredField(propertyName);
			TLVDecoder decoder = decoderProvider.lookupCodec(f.getType());
			Object v = decoder.codec(source, f.getType());
			BeanUtils.setProperty(object, propertyName, v);
			return object;
		} catch (InstantiationException e) {
			LOGGER.error("createObjectWithProperty;", e);
			return object;
		} catch (IllegalAccessException e) {
			LOGGER.error("createObjectWithProperty;", e);
			return object;
		} catch (SecurityException e) {
			LOGGER.error("createObjectWithProperty;", e);
			return object;
		} catch (NoSuchFieldException e) {
			LOGGER.error("createObjectWithProperty;", e);
			return object;
		} catch (InvocationTargetException e) {
			LOGGER.error("createObjectWithProperty;", e);
			return object;
		}
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

	public void setDecoderProvider(TLVDecoderProvider decoderProvider) {
		this.decoderProvider = decoderProvider;
	}

	@Override
	public boolean isObjectDecoder() {
		return true;
	}

}
