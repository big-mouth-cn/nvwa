package org.bigmouth.nvwa.access.request;

import org.apache.asyncweb.common.HttpRequest;
import org.apache.mina.core.session.IoSession;

public interface HttpRequestExtFactory {

	HttpRequestExt create(HttpRequest httpRequest, IoSession session);
}
