package org.bigmouth.nvwa.sap;

import java.io.Serializable;

import org.apache.mina.core.buffer.IoBuffer;

public interface SapMessage extends Serializable {

	byte getProtocolVer();

	MessageType getMessageType();

	ContentType getContentType();

	byte getContentVer();

	int getContentLength();

	/**
	 * PROBLEM:Coupled with MINA.
	 */
	IoBuffer getContent();
}
