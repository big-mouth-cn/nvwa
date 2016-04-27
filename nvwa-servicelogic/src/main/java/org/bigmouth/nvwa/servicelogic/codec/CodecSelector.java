package org.bigmouth.nvwa.servicelogic.codec;

import org.bigmouth.nvwa.sap.ContentType;

public interface CodecSelector {

	ContentEncoder selectEncoder(Class<?> template);

	ContentDecoder selectDecoder(Class<?> template);

	ContentType selectContentType(Class<?> template);
}
