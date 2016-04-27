package org.bigmouth.nvwa.transport;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IoSession;
import org.bigmouth.nvwa.sap.Identifiable;
import org.bigmouth.nvwa.sap.MutableIdentifiable;
import org.bigmouth.nvwa.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaReplyReceiver extends MinaReceiver implements ReplyReceiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(MinaReplyReceiver.class);

	private volatile MinaReplier replier;

	public MinaReplyReceiver(Map<String, IoFilter> filters, int port) {
		super(filters, port);
	}

	public MinaReplyReceiver(TcpConfig tcpConfig, Map<String, IoFilter> filters,
			InetSocketAddress boundAddress) {
		super(tcpConfig, filters, boundAddress);
	}

	public MinaReplyReceiver(TcpConfig tcpConfig, Map<String, IoFilter> filters, int port) {
		super(tcpConfig, filters, port);
	}

	public MinaReplyReceiver(TcpConfig tcpConfig, Map<String, IoFilter> filters,
			Set<InetSocketAddress> boundAddresses) {
		super(tcpConfig, filters, boundAddresses);
	}

	public MinaReplyReceiver(TcpConfig tcpConfig, List<Pair<String, IoFilter>> filters, String ip,
			int port) {
		super(tcpConfig, filters, ip, port);
	}

	public MinaReplyReceiver(TcpConfig tcpConfig, Map<String, IoFilter> filters, String ip, int port) {
		super(tcpConfig, filters, ip, port);
	}

	@Override
	protected void onMessageReceived(IoSession session, Object message) throws Exception {
		super.onMessageReceived(session, message);

		if (!(message instanceof Identifiable)) {
			LOGGER.error("request message can not Identifiable,close session.");
			session.close(true);
			return;
			// if (LOGGER.isWarnEnabled())
			// LOGGER.warn("request message can not Identifiable,maybe it is notice.");
		} else {
			// transaction id
			Identifiable transactionPacket = (Identifiable) message;
			UUID transactionId = null;
			if (transactionPacket.existIdentification()) {
				transactionId = transactionPacket.getIdentification();
			} else {
				if (!(message instanceof MutableIdentifiable)) {
					LOGGER.error("request message can not MutableIdentifiable,close session.");
					session.close(true);
					return;
				} else {
					transactionId = UUID.randomUUID();
					((MutableIdentifiable) transactionPacket).setIdentification(transactionId);
				}
			}
			storeState(session, transactionPacket);
		}
	}

	private void storeState(IoSession session, Identifiable transactionPacket) {
		replier.storeTransactionState(transactionPacket, session);
	}

	@Override
	protected void onSessionClosed(IoSession session) throws Exception {
		replier.removeTransactionState(session);
	}

	@Override
	public void setReplier(Replier replier) {
		if (null == replier)
			throw new NullPointerException("replier");
		if (!(replier instanceof MinaReplier))
			throw new IllegalArgumentException("replier expect MinaReplier,but " + replier);
		this.replier = (MinaReplier) replier;
	}
}
