package org.bigmouth.nvwa.access.response.standard.stream;

import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.access.service.ContentNotFoundException;


public interface HttpResponseSegment {

	IoBuffer getContent() throws ContentNotFoundException;
}
