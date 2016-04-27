package org.bigmouth.nvwa.transport;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.bigmouth.nvwa.sap.Identifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaReplier implements Replier {

	private static final Logger LOGGER = LoggerFactory.getLogger(MinaReplier.class);
	private static final AttributeKey TRANSACTION_ID = new AttributeKey(MinaReceiver.class,
			"MinaReplier.transaction.identifiable");
	private static final AttributeKey REQUEST_OBJECT = new AttributeKey(MinaReceiver.class,
			"MinaReplier.request.object");
	private static final boolean NOT_MULTITRANSACTION = false;

	private final boolean multiTransaction;
	private final ConcurrentMap<UUID, IoSession> sessionHolder = new ConcurrentHashMap<UUID, IoSession>();

	public MinaReplier() {
		this(NOT_MULTITRANSACTION);
	}

	public MinaReplier(boolean multiTransaction) {
		this.multiTransaction = multiTransaction;
	}

	public static UUID getTransactionId(IoSession session) {
		return (UUID) session.getAttribute(TRANSACTION_ID);
	}

	public void storeTransactionState(Identifiable tia, IoSession session) {
		UUID tid = tia.getIdentification();
		sessionHolder.put(tid, session);
		session.setAttribute(TRANSACTION_ID, tid);
		storeRequest(tia, session);
	}

	private void storeRequest(Identifiable tia, IoSession session) {
		session.setAttribute(REQUEST_OBJECT, tia);
	}

	public void removeTransactionState(IoSession session) {
		UUID tid = (UUID) session.getAttribute(TRANSACTION_ID);
		if (null == tid)
			return;
		sessionHolder.remove(tid);
	}

	protected IoSession find(Identifiable tia) {
		return sessionHolder.get(tia.getIdentification());
	}

	@Override
	public void reply(Identifiable packet, boolean completeClose) {
		if (null == packet)
			throw new NullPointerException("packet");
		IoSession session = find(packet);
		if (null == session) {
			if (LOGGER.isWarnEnabled())
				LOGGER.warn("Can not found response session,ignore.");
			return;
		}
		Identifiable request = getRequest(session);
		if (null == request) {
			// TODO:error
			throw new RuntimeException("Request object is null.");
		}
		doReply(request, packet, completeClose, session);
	}

	private void doReply(Identifiable request, Identifiable response, boolean completeClose,
			IoSession session) {
		WriteFuture wf = session.write(response);
		if (multiTransaction) {
			sessionHolder.remove(response.getIdentification());
		}
		replyCompleted(request, response, completeClose, session, wf);
	}

	protected void replyCompleted(Identifiable request, Identifiable response,
			boolean completeClose, IoSession session, WriteFuture wf) {
		if (completeClose) {
			wf.addListener(IoFutureListener.CLOSE);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Server closing session when transport completed.");
		} else {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Server holding session when transport completed.");
		}
	}

	private Identifiable getRequest(IoSession session) {
		return (Identifiable) session.getAttribute(REQUEST_OBJECT);
	}

	@Override
	public void reply(Identifiable packet) {
		reply(packet, false);
	}

	@Override
	public void closeSession(Identifiable packet, boolean immediately) {
		if (null == packet)
			throw new NullPointerException("packet");
		IoSession session = find(packet);
		if (null == session) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Can not found IoSession for " + packet
						+ ",ignore.maybe this session has closed.");
		} else {
			session.close(immediately);
		}
		closeComplete(packet, immediately, session);
	}

	public int getSessionCount() {
		return sessionHolder.size();
	}

	protected void closeComplete(Identifiable packet, boolean immediately, IoSession session) {
	}
}
