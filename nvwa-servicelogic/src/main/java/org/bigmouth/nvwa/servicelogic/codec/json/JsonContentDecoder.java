package org.bigmouth.nvwa.servicelogic.codec.json;

import java.io.UnsupportedEncodingException;

import org.bigmouth.nvwa.servicelogic.codec.ContentDecoder;
import org.bigmouth.nvwa.servicelogic.codec.error.IllegalContentHandler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonContentDecoder implements ContentDecoder {

	private static final String CHARSET = "UTF-8";
	private final Gson gson = new Gson();

	private IllegalContentHandler illegalContentHandler;

	@Override
	public Object decode(byte[] source, Class<?> template) {
		if (null == source)
			throw new NullPointerException("source");
		if (null == template)
			throw new NullPointerException("template");

		try {
			String json = new String(source, CHARSET);
			return gson.fromJson(json, template);
		} catch (UnsupportedEncodingException e) {
			recordIllegalContent(template.toString(), source);
			throw new RuntimeException("decode:", e);
		} catch (JsonSyntaxException e) {
			recordIllegalContent(template.toString(), source);
			throw new RuntimeException("decode:", e);
		} catch (Exception e) {
			recordIllegalContent(template.toString(), source);
			throw new RuntimeException("decode:", e);
		}
	}

	private void recordIllegalContent(String flag, byte[] content) {
		if (null == illegalContentHandler)
			return;
		illegalContentHandler.handle(flag, content);
	}

	public void setIllegalContentHandler(IllegalContentHandler illegalContentHandler) {
		this.illegalContentHandler = illegalContentHandler;
	}
}
