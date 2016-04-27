package org.bigmouth.nvwa.sap.codec;

import java.util.Random;

import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.bigmouth.nvwa.sap.namecode.NameCodeMapper;


public final class SapCodecFactory implements ProtocolCodecFactory {

	private static final int RN = new Random().nextInt(100);
	private static final AttributeKey SAP_ENCODER = new AttributeKey(SapCodecFactory.class,
			"sap.encoder." + RN);
	private static final AttributeKey SAP_DECODER = new AttributeKey(SapCodecFactory.class,
			"sap.decoder." + RN);

	private final NameCodeMapper ncMapper;

	public SapCodecFactory(NameCodeMapper ncMapper) {
		if (null == ncMapper)
			throw new NullPointerException("ncMapper");
		this.ncMapper = ncMapper;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		ProtocolEncoder encoder = (ProtocolEncoder) session.getAttribute(SAP_ENCODER);
		if (null == encoder) {
			encoder = new SapEncoder(ncMapper);
			session.setAttribute(SAP_ENCODER, encoder);
		}
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		ProtocolDecoder decoder = (ProtocolDecoder) session.getAttribute(SAP_DECODER);
		if (null == decoder) {
			decoder = new SapDecoder(ncMapper, session);
			session.setAttribute(SAP_DECODER, decoder);
		}
		return decoder;
	}
}
