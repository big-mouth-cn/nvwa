package org.bigmouth.nvwa.sap;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.mina.core.buffer.IoBuffer;

//0               1               2               3                
//0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7  
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                   protocol identifier(4)                      |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|   ptl ver(1)  |   msg type(1) |content type(1)| content ver(1)|
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                      content length(4)                        |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                                                               |
//|                    application data field(...)                |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

public class DefaultSapMessage implements MutableSapMessage {

	private static final long serialVersionUID = -6130102785417803559L;

	private static final byte DEFAULT_PROTOCOL_VER = (byte) 1;
	private static final MessageType DEFAULT_MESSAGE_TYPE = MessageType.MESSAGE;
	private static final ContentType DEFAULT_CONTENT_TYPE = ContentType.TLV;
	private static final byte DEFAULT_CONTENT_VER = 1;
	private static final int DEFAULT_CONTENT_LENGTH = 0;
	private static final IoBuffer DEFAULT_CONTENT = IoBuffer.allocate(0);

	private byte protocolVer = DEFAULT_PROTOCOL_VER;
	private MessageType messageType = DEFAULT_MESSAGE_TYPE;
	private ContentType contentType = DEFAULT_CONTENT_TYPE;
	private byte contentVer = DEFAULT_CONTENT_VER;
	private int contentLength = DEFAULT_CONTENT_LENGTH;
	private IoBuffer content = DEFAULT_CONTENT;

	public DefaultSapMessage() {
	}

	public DefaultSapMessage(SapMessage message) {
		if (null != message)
			doSetMessage(message);
	}

	@Override
	public byte getProtocolVer() {
		return protocolVer;
	}

	@Override
	public IoBuffer getContent() {
		return content;
	}

	@Override
	public int getContentLength() {
		return contentLength;
	}

	@Override
	public MessageType getMessageType() {
		return messageType;
	}

	@Override
	public ContentType getContentType() {
		return contentType;
	}

	@Override
	public void setContent(IoBuffer content) {
		if (null == content)
			throw new NullPointerException("content");
		if (!content.hasRemaining()) {
			content.flip();
		}
		this.content = content;
		this.contentLength = content.limit();
	}

	@Override
	public void setContentType(ContentType ct) {
		if (null == ct)
			throw new NullPointerException("contentType");
		this.contentType = ct;
	}

	@Override
	public void setMessageType(MessageType mt) {
		if (null == mt)
			throw new NullPointerException("messageType");
		this.messageType = mt;
	}

	@Override
	public void setProtocolVer(byte pv) {
		if (0 >= pv)
			throw new IllegalArgumentException("protocolVer:" + protocolVer);
		this.protocolVer = pv;
	}

	@Override
	public void setContentVer(byte contentVer) {
		if (contentVer < 1)
			throw new IllegalArgumentException("contentVer:" + contentVer);
		this.contentVer = contentVer;
	}

	@Override
	public byte getContentVer() {
		return contentVer;
	}

	@Override
	public void setMessage(SapMessage message) {
		if (null == message)
			throw new NullPointerException("message");

		doSetMessage(message);
	}

	protected void doSetMessage(SapMessage message) {
		this.setProtocolVer(message.getProtocolVer());
		this.setMessageType(message.getMessageType());
		this.setContentType(message.getContentType());
	}

	@Override
	public void normalize() {
		// TODO:
		// throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
