package org.bigmouth.nvwa.sap.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.bigmouth.nvwa.sap.MutableSapMessage;
import org.bigmouth.nvwa.sap.SapMessage;


class SapMessageEncoder extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception {
		if (null == message)
			throw new NullPointerException("message");
		if (!(message instanceof SapMessage))
			throw new IllegalArgumentException("message expect SapMessage,but "
					+ message.getClass());

		SapMessage sapMessage = (SapMessage) message;
		if (sapMessage instanceof MutableSapMessage) {
			((MutableSapMessage) sapMessage).normalize();
		}
		IoBuffer buffer = IoBuffer.allocate(512);
		buffer.setAutoExpand(true);

		SapCodecUtils.encodeHeaderCommon(sapMessage, buffer);
		encodeHeaderExt(sapMessage, buffer);
		SapCodecUtils.encodeContent(sapMessage, buffer);
		buffer.flip();

		out.write(buffer);
	}

	protected void encodeHeaderExt(SapMessage sapMessage, IoBuffer buffer)
			throws SapEncodingException {
	}
}
