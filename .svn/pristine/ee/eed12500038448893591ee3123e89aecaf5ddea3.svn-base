package org.bigmouth.nvwa.access.response;

import java.util.Map;

import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.mina.core.buffer.IoBuffer;

public interface HttpResponseSource {

	HttpResponseStatus getStatus();

	Map<String, String> getHeaders();

	String getContentType();

	IoBuffer getContent();
}
