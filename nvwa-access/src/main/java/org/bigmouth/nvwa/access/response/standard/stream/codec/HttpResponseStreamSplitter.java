package org.bigmouth.nvwa.access.response.standard.stream.codec;

import org.apache.asyncweb.common.codec.HttpResponseEncoder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderException;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.bigmouth.nvwa.access.response.HttpResponseExt;
import org.bigmouth.nvwa.access.response.standard.stream.HttpResponseStream;


public class HttpResponseStreamSplitter extends ProtocolEncoderAdapter {

	private final HttpResponseEncoder httpResponseEncoder = new HttpResponseEncoder();

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception {
		if (message instanceof HttpResponseExt) {
			httpResponseEncoder.encode(session, message, out);
			return;
		}

		if (message instanceof HttpResponseStream) {
			HttpResponseStream stream = (HttpResponseStream) message;
			out.write(stream);
			return;
		}

		throw new ProtocolEncoderException("Unkown message:" + message);
	}
}
