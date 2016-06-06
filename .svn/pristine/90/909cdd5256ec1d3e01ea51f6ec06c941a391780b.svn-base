package org.bigmouth.nvwa.sap.codec;

import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.bigmouth.nvwa.sap.MessageType;
import org.bigmouth.nvwa.sap.SapMessage;
import org.bigmouth.nvwa.sap.namecode.NameCodeMapper;

import com.google.common.collect.Maps;

final class SapEncoder extends ProtocolEncoderAdapter {

	private final Map<MessageType, ProtocolEncoder> sapMessageEncoders = Maps.newHashMap();

	SapEncoder(NameCodeMapper ncMapper) {
		if (null == ncMapper)
			throw new NullPointerException("ncMapper");

		register(MessageType.MESSAGE, new SapMessageEncoder());
		register(MessageType.TRANSACTION_MESSAGE, new SapTransactionMessageEncoder());
		register(MessageType.REQUEST, new SapRequestEncoder(ncMapper));
		register(MessageType.RESPONSE, new SapResponseEncoder());

	}

	protected void register(MessageType messageType, SapMessageEncoder encoder) {
		sapMessageEncoders.put(messageType, encoder);
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception {
		if (null == message)
			throw new NullPointerException("message");
		if (!(message instanceof SapMessage))
			throw new IllegalArgumentException("message expect SapMessage,but "
					+ message.getClass());

		SapMessage sapMessage = (SapMessage) message;
		ProtocolEncoder encoder = sapMessageEncoders.get(sapMessage.getMessageType());
		if (null == encoder)
			throw new SapEncodingException("Can not found any SapMessageEncoder for messageType:"
					+ sapMessage.getMessageType());
		encoder.encode(session, message, out);
	}
}
