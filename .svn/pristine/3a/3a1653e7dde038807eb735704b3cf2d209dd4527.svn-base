package org.bigmouth.nvwa.codec.tlv.encoders;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.bigmouth.nvwa.codec.byteorder.NumberCodec;
import org.bigmouth.nvwa.codec.byteorder.NumberCodecFactory;
import org.bigmouth.nvwa.codec.tlv.TLVConfig;
import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;
import org.bigmouth.nvwa.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TLVObjectEncoder implements TLVEncoder<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TLVObjectEncoder.class);

	private static final String[] ENCODER_KEYES = new String[] { "java.lang.Object" };

	private TLVEncoderProvider encoderProvider;

	public TLVObjectEncoder() {
	}

	@Override
	public String[] getCodecKeyes() {
		return ENCODER_KEYES;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<byte[]> codec(Object source, Object additionInfo) {
		if (null == encoderProvider)
			throw new IllegalStateException("TLVObjectEncoder must be registered to encodeContext.");

		TLVFieldNameFilter filter = null;
		if (additionInfo != null) {
			if (!(additionInfo instanceof Annotation)) {
				try {
					filter = TLVFieldNameFilter.class.cast(additionInfo);
				} catch (ClassCastException e) {
					throw new IllegalArgumentException("additionInfo is not TLVFieldNameFilter.", e);
				}
			}
		}

		if (null == source)
			return new ArrayList<byte[]>();

		List<byte[]> ret = new ArrayList<byte[]>();
		Field[] fields = null;

		Class<?> itr = source.getClass();
		while (!itr.equals(Object.class)) {
			fields = (Field[]) ArrayUtils.addAll(itr.getDeclaredFields(), fields);
			itr = itr.getSuperclass();
		}

		for (Field field : fields) {
			TLVAttribute param = field.getAnnotation(TLVAttribute.class);

			if (null == param)
				continue;

			if (!field.isAccessible())
				field.setAccessible(true);

			if (null != filter) {
				if (!filter.accept(field.getName()))
					continue;
			}

			//TODO:cached class descriptor
			Object src = null;
			try {
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), source.getClass());
				Method method = pd.getReadMethod();
				method.setAccessible(true);
				src = method.invoke(source);
			} catch (IntrospectionException e) {
				LOGGER.error("object encode:", e);
			} catch (IllegalArgumentException e) {
				LOGGER.error("object encode:", e);
			} catch (IllegalAccessException e) {
				LOGGER.error("object encode:", e);
			} catch (InvocationTargetException e) {
				LOGGER.error("object encode:", e);
			}
//			Object src = null;
//			try {
//				src = field.get(source);
//			} catch (IllegalArgumentException e) {
//				LOGGER.error("object encode:", e);
//			} catch (IllegalAccessException e) {
//				LOGGER.error("object encode:", e);
//			}
			if (null == src)
				continue;

			//2012-01-20
			// ignore number value
			if (-1 != param.ignoreNumberValue()) {
				if ((src instanceof Integer) || (src instanceof Long) || (src instanceof Short)
						|| (src instanceof Byte)) {
					if ((((Number) src).longValue()) == param.ignoreNumberValue())
						continue;
				}
			}
			
			List<byte[]> dest = null;

			// only extract single property from object.
			String propertyName = param.propertyName();
			if (!propertyName.equals("")) {
				Field f = null;
				try {
					f = ReflectUtils.findField(src.getClass(), propertyName);
					if (null == f) {
						String errorInfo = " field [" + propertyName + "] can not found in "
						+ field.getType();
						LOGGER.error(errorInfo);
						throw new RuntimeException(errorInfo);
					}

					if (!f.isAccessible())
						f.setAccessible(true);
					Object v = f.get(src);
					Class propertyClass = f.getType();

					TLVEncoder propertyEncoder = encoderProvider.lookupCodec(propertyClass);
					if (null == propertyEncoder) {
						LOGGER.error("field[" + field + "] could not found encoder, ignore");
						continue;
					}

					dest = (List<byte[]>) propertyEncoder.codec(v, null);
				} catch (SecurityException e1) {
					LOGGER.error("extract property name occur error.", e1);
				} catch (IllegalArgumentException e) {
					LOGGER.error("extract property name occur error.", e);
				} catch (IllegalAccessException e) {
					LOGGER.error("extract property name occur error.", e);
				}
			}

			// as object
			if (null == dest) {
				Class clazz = src.getClass();

				TLVEncoder encoder = encoderProvider.lookupCodec(clazz);
				if (null == encoder) {
					LOGGER.error("field[" + field + "] could not found encoder, ignore");
					continue;
				}
				dest = (List<byte[]>) encoder.codec(src, param);
			}

			NumberCodec numberCodec = NumberCodecFactory.fetchNumberCodec(this.encoderProvider
					.getByteOrder());

			if (!param.ignoreTagLen()) {
				TLVConfig tlvConfig = encoderProvider.getTlvConfig();
				// tag
				byte[] tag_bytes = numberCodec.int2Bytes(param.tag(), tlvConfig.getTagLen());
				ret.add(tag_bytes);

				// len
				int len = 0;
				for (byte[] bs : dest)
					len += bs.length;
				byte[] len_bytes = numberCodec.int2Bytes(len, tlvConfig.getLengthLen());
				ret.add(len_bytes);
			}
			// value
			ret.addAll(dest);
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
		return "TLVObjectEncoder";
	}
}
