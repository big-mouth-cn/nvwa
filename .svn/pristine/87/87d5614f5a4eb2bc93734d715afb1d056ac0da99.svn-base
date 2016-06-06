package org.bigmouth.nvwa.access.response.standard.stream.codec.wq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;

public class BoundedWriteRequestQueue implements WriteRequestQueue {

	private static final int DEFAULT_MESSAGE_COUNT = 1024;
	private final BlockingQueue<WriteRequest> q;

	public BoundedWriteRequestQueue() {
		this(DEFAULT_MESSAGE_COUNT);
	}

	public BoundedWriteRequestQueue(int messageCount) {
		if (messageCount <= 0)
			throw new IllegalArgumentException("messageCount:" + messageCount);
		q = new LinkedBlockingDeque<WriteRequest>(messageCount);
	}

	public void dispose(IoSession session) {
		// Do nothing
	}

	public void clear(IoSession session) {
		q.clear();
	}

	public boolean isEmpty(IoSession session) {
		return q.isEmpty();
	}

	public void offer(IoSession session, WriteRequest writeRequest) {
		q.offer(writeRequest);
	}

	public WriteRequest poll(IoSession session) {
		return q.poll();
	}

	@Override
	public String toString() {
		return q.toString();
	}
}
