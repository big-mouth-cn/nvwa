package org.bigmouth.nvwa.sap;

import java.util.UUID;

//0               1               2               3                
//0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7  
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                   protocol identifier(4)                      |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|   ptl ver(1)  |   msg type(1) |content type(1)| content ver(1)|
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                                                               |
//|                       transactionId(16)                       |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                      content length(4)                        |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                                                               |
//|                    application data field(...)                |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

public class DefaultSapTransactionMessage extends DefaultSapMessage implements
		MutableSapTransactionMessage {

	private static final long serialVersionUID = -4732282025518071711L;

	private volatile UUID transactionId;

	public DefaultSapTransactionMessage() {
		super();
		this.setMessageType(MessageType.TRANSACTION_MESSAGE);
	}

	public DefaultSapTransactionMessage(SapMessage message) {
		super(message);
		this.setMessageType(MessageType.TRANSACTION_MESSAGE);
		if (message instanceof SapTransactionMessage)
			this.setIdentification(((SapTransactionMessage) message).getIdentification());
	}

	public DefaultSapTransactionMessage(SapMessage message, UUID transactionId) {
		this(message);
		this.setIdentification(transactionId);
	}

	@Override
	public UUID getIdentification() {
		return transactionId;
	}

	@Override
	public void setIdentification(UUID id) {
		if (null == id)
			throw new NullPointerException("id");
		this.transactionId = id;
	}

	@Override
	public boolean existIdentification() {
		return null != transactionId;
	}
}
