package org.bigmouth.nvwa.sap;

import java.util.UUID;

import org.apache.mina.core.buffer.IoBuffer;

public final class SapResponseUtils {

	private SapResponseUtils() {
	}

	public static SapResponse createEmptySapResponse(UUID tid, SapResponseStatus status) {
		return createSapResponse(tid, status, null);
	}

	public static SapResponse createSapResponse(UUID tid, SapResponseStatus status, IoBuffer content) {
		if (null == tid)
			throw new NullPointerException("tid");
		if (null == status)
			throw new NullPointerException("status");
		if (null == content)
			content = IoBuffer.wrap(new byte[0]);
		MutableSapResponse sapResponse = new DefaultSapResponse();
		sapResponse.setMessageType(MessageType.RESPONSE);
		sapResponse.setIdentification(tid);
		sapResponse.setStatus(status);
		sapResponse.setContent(content);
		return sapResponse;
	}
}
