package org.bigmouth.nvwa.sap;

public enum MessageType {

	MESSAGE((byte) 0), TRANSACTION_MESSAGE((byte) 1), REQUEST((byte) 2), RESPONSE((byte) 3);

	private static final byte MIN_ID = 0;
	private static final byte MAX_ID = 3;
	private static final MessageType[] MESSAGE_TYPE_TABLE = new MessageType[MAX_ID + 1];

	private final byte innerCode;

	MessageType(byte innerCode) {
		this.innerCode = innerCode;
	}

	public byte code() {
		return innerCode;
	}

	static {
		MESSAGE_TYPE_TABLE[0] = MESSAGE;
		MESSAGE_TYPE_TABLE[1] = TRANSACTION_MESSAGE;
		MESSAGE_TYPE_TABLE[2] = REQUEST;
		MESSAGE_TYPE_TABLE[3] = RESPONSE;
	}

	public static MessageType forCode(byte typeCode) {
		if (MIN_ID > typeCode || MAX_ID < typeCode)
			throw new IllegalArgumentException("typeCode:" + typeCode);
		return MESSAGE_TYPE_TABLE[typeCode];
	}
}
