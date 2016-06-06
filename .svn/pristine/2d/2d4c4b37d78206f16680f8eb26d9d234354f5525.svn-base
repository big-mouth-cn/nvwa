package org.bigmouth.nvwa.access.response.standard.stream.codec;

import org.apache.asyncweb.common.codec.HttpRequestDecoder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.bigmouth.nvwa.access.utils.HttpCodecFactory;


public class HttpStreamCodecFactory implements ProtocolCodecFactory {

	private static final AttributeKey REQUEST_DECODER = new AttributeKey(HttpCodecFactory.class,
			"request.decoder");
	private static final AttributeKey RESPONSE_ENCODER = new AttributeKey(HttpCodecFactory.class,
			"response.encoder");

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		ProtocolEncoder encoder = null;
		if (session.getService() instanceof IoAcceptor) {
			encoder = (ProtocolEncoder) session.getAttribute(RESPONSE_ENCODER);
			if (null == encoder) {
				encoder = new HttpResponseStreamSplitter();
				session.setAttribute(RESPONSE_ENCODER, encoder);
			}
		} else {
			throw new RuntimeException("Not support client model.");
		}
		return encoder;
	}

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
			throw new RuntimeException("Not support client model.");
		}
		return decoder;
	}
}
