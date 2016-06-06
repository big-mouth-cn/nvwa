package org.bigmouth.nvwa.servicelogic.codec.kv;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;

import org.apache.commons.beanutils.BeanUtils;
import org.bigmouth.nvwa.servicelogic.codec.ContentDecoder;


public class KvContentDecoder implements ContentDecoder {

	private static final String CONNECTOR = "&";
	private static final String EQ = "=";
	private static final String CHARSET = "UTF-8";

	private Object createModel(Class<?> template) throws InstantiationException,
			IllegalAccessException {
		return template.newInstance();
	}

	@Override
	public Object decode(byte[] source, Class<?> template) {
		if (null == source)
			throw new NullPointerException("source");
		if (null == template)
			throw new NullPointerException("template");

		Object model = null;

		try {
			String data = new String(source, CHARSET);

			model = createModel(template);

			int idx = 0;
			while (idx < data.length()) {
				int connectorPos = data.indexOf(CONNECTOR, idx);

				String subData;
				if (connectorPos < 0) {
					subData = data.substring(idx);
					connectorPos = data.length();
				} else {
					subData = data.substring(idx, connectorPos);
				}

				int equalPos = subData.indexOf(EQ);

				String key = "";
				String value = "";
				if (equalPos < 0) {
					key = URLDecoder.decode(subData, CHARSET);
					value = "";
				} else {
					key = URLDecoder.decode(subData.substring(0, equalPos), CHARSET);
					value = URLDecoder.decode(subData.substring(equalPos + 1), CHARSET);
				}

				BeanUtils.setProperty(model, key, value);

				idx = connectorPos + 1;
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("decode:", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("decode:", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("decode:", e);
		} catch (InstantiationException e) {
			throw new RuntimeException("decode:", e);
		} catch (Exception e) {
			throw new RuntimeException("decode:", e);
		}
		return model;
	}
}
