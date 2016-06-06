package org.bigmouth.nvwa.servicelogic.codec;

import org.bigmouth.nvwa.sap.ContentType;

public interface CodecSelector {

    @Deprecated
	ContentEncoder selectEncoder(Class<?> template);

    @Deprecated
	ContentDecoder selectDecoder(Class<?> template);

	ContentType selectContentType(Class<?> template);
	
	ContentEncoder selectEncoder(ContentType contentType);
	
	ContentDecoder selectDecoder(ContentType contentType);
	
	ContentEncoder selectEncoder(ContentType contentType, ContentType defaultContentType);
	
	ContentDecoder selectDecoder(ContentType contentType, ContentType defaultContentType);
}
