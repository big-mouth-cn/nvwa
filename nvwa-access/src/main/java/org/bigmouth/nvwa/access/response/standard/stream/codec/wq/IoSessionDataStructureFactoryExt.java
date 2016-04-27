package org.bigmouth.nvwa.access.response.standard.stream.codec.wq;

import org.apache.mina.core.session.DefaultIoSessionDataStructureFactory;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequestQueue;

public class IoSessionDataStructureFactoryExt extends DefaultIoSessionDataStructureFactory {

	private static final boolean NOT_MEMORY_PROTECT = false;
	private static final int DEFAULT_MESSAGE_COUNT = 1024;

	private final boolean memoryProtect;
	private final int messageCount;

	public IoSessionDataStructureFactoryExt() {
		this(NOT_MEMORY_PROTECT, DEFAULT_MESSAGE_COUNT);
	}

	public IoSessionDataStructureFactoryExt(int messageCount) {
		this(NOT_MEMORY_PROTECT, messageCount);
	}

	public IoSessionDataStructureFactoryExt(boolean memoryProtect) {
		this(memoryProtect, DEFAULT_MESSAGE_COUNT);
	}

	public IoSessionDataStructureFactoryExt(boolean memoryProtect, int messageCount) {
		if (messageCount <= 0)
			throw new IllegalArgumentException("messageCount:" + messageCount);
		this.memoryProtect = memoryProtect;
		this.messageCount = messageCount;
	}

	@Override
	public WriteRequestQueue getWriteRequestQueue(IoSession session) throws Exception {
		if (memoryProtect) {
			return new BoundedWriteRequestQueue(messageCount);
		} else {
			return new BoundedWriteRequestQueue(Integer.MAX_VALUE);
		}
	}
}
