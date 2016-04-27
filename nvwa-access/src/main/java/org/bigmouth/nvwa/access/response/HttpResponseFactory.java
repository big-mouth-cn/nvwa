package org.bigmouth.nvwa.access.response;

import org.apache.asyncweb.common.HttpResponse;

public interface HttpResponseFactory {

	HttpResponse create(HttpResponseSource httpResponseSource);
}
