package org.bigmouth.nvwa.servicelogic.codec.json;

import java.io.UnsupportedEncodingException;

import org.bigmouth.nvwa.servicelogic.codec.ContentEncoder;

import com.google.gson.Gson;

public class JsonContentEncoder implements ContentEncoder {

	private static final String CHARSET = "UTF-8";
	private final Gson gson = new Gson();

	@Override
	public byte[] encode(Object source) {
		if (null == source)
			throw new NullPointerException("source");

		String json = gson.toJson(source);
		try {
			return json.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("encode:", e);
		}
	}
}
