package org.bigmouth.nvwa.servicelogic.codec.json;

import org.bigmouth.nvwa.servicelogic.codec.ContentDecoder;
import org.bigmouth.nvwa.servicelogic.codec.error.IllegalContentHandler;
import org.bigmouth.nvwa.utils.JsonHelper;

public class JsonContentDecoder implements ContentDecoder {

	private IllegalContentHandler illegalContentHandler;

	@Override
	public Object decode(byte[] source, Class<?> template) {
		if (null == source)
			throw new NullPointerException("source");
		if (null == template)
			throw new NullPointerException("template");

		try {
		    if (source.length == 0) {
		        return template.newInstance();
		    }
			return JsonHelper.convert(source, template);
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
