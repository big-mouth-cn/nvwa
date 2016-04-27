package org.bigmouth.nvwa.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SerializeUtils {

	private static final Logger logger = LoggerFactory.getLogger(SerializeUtils.class);

	public static final <T> byte[] encode(T obj) {
		if (null == obj)
			throw new NullPointerException("input object");
		if (!(obj instanceof Serializable))
			throw new IllegalArgumentException("input object expect implements Serializable,but "
					+ obj.getClass());

		byte[] bytes = null;
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bout);
			out.writeObject(obj);
			bytes = bout.toByteArray();
		} catch (IOException e) {
			logger.warn("Error serializing object[{}], message[{}]!", obj, e);
		}
		return bytes;
	}

	@SuppressWarnings("unchecked")
	public static final <T> T decode(byte[] bytes, Class<T> clazz) {
		if (null == bytes || 0 == bytes.length)
			throw new IllegalArgumentException("input bytes is blank.");
		if (null == clazz)
			throw new NullPointerException("clazz");
		T t = null;
		try {
			ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bytes));
			t = (T) oin.readObject();
		} catch (Exception e) {
			logger.warn("Error decoding byte[] data [{}], message[{}]!", bytes, e);
		}
		return t;
	}
}
