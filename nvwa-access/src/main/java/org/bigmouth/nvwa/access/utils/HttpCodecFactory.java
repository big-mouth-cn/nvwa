package org.bigmouth.nvwa.access.utils;

import org.apache.asyncweb.common.codec.HttpRequestDecoder;
import org.apache.asyncweb.common.codec.HttpRequestEncoder;
import org.apache.asyncweb.common.codec.HttpResponseDecoder;
import org.apache.asyncweb.common.codec.HttpResponseEncoder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public final class HttpCodecFactory implements ProtocolCodecFactory {

	private static final AttributeKey REQUEST_ENCODER = new AttributeKey(HttpCodecFactory.class,
			"request.encoder");
	private static final AttributeKey REQUEST_DECODER = new AttributeKey(HttpCodecFactory.class,
			"request.decoder");
	private static final AttributeKey RESPONSE_ENCODER = new AttributeKey(HttpCodecFactory.class,
			"response.encoder");
	private static final AttributeKey RESPONSE_DECODER = new AttributeKey(HttpCodecFactory.class,
			"response.decoder");

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		ProtocolDecoder decoder = null;
		if (session.getService() instanceof IoAcceptor) {
			decoder = (ProtocolDecoder) session.getAttribute(REQUEST_DECODER);
			if (null == decoder) {
				decoder = new HttpRequestDecoder();
				session.setAttribute(REQUEST_DECODER, decoder);
			}
		} else {
			decoder = (ProtocolDecoder) session.getAttribute(RESPONSE_DECODER);
			if (null == decoder) {
				decoder = new HttpResponseDecoder();
				session.setAttribute(RESPONSE_DECODER, decoder);
			}
		}
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		ProtocolEncoder encoder = null;
		if (session.getService() instanceof IoAcceptor) {
			encoder = (ProtocolEncoder) session.getAttribute(RESPONSE_ENCODER);
			if (null == encoder) {
				encoder = new HttpResponseEncoder();
				session.setAttribute(RESPONSE_ENCODER, encoder);
			}
		} else {
			encoder = (ProtocolEncoder) session.getAttribute(REQUEST_ENCODER);
			if (null == encoder) {
				encoder = new HttpRequestEncoder();
				session.setAttribute(REQUEST_ENCODER, encoder);
			}
		}
		return encoder;
	}
}
