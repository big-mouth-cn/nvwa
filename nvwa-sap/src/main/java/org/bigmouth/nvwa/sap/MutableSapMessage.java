package org.bigmouth.nvwa.sap;

import org.apache.mina.core.buffer.IoBuffer;

public interface MutableSapMessage extends SapMessage {

	void setProtocolVer(byte pv);

	void setMessageType(MessageType mt);

	void setContentType(ContentType ct);

	void setContentVer(byte contentVer);

	void setContent(IoBuffer content);

	void setMessage(SapMessage message);

	void normalize();
}
