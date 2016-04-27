package org.bigmouth.nvwa.sap;

public enum ExtendedItemType {

	APPLIST((byte) 0), APK((byte) 1), FRAME((byte) 2), PIC((byte) 3),
	/**
	 * Generic segment resource,<br>
	 * e.g. pc driver data.
	 */
	GENERIC_SEG_RES((byte) 4), OP_STORE_APK((byte) 5), SECTION_PIC((byte) 6), VOICE_COMMENT(
			(byte) 7), OTHER_PIC((byte) 8), APK_CEN((byte) 9),BOX((byte)10),
			WEBP((byte) 11);

	private static final byte MIN_ID = 0;
	private static final byte MAX_ID = 11;
	private static final ExtendedItemType[] TYPE_TABLE = new ExtendedItemType[MAX_ID + 1];

	private final byte code;

	ExtendedItemType(byte code) {
		this.code = code;
	}

	public byte code() {
		return code;
	}

	static {
		TYPE_TABLE[0] = APPLIST;
		TYPE_TABLE[1] = APK;
		TYPE_TABLE[2] = FRAME;
		TYPE_TABLE[3] = PIC;
		TYPE_TABLE[4] = GENERIC_SEG_RES;
		TYPE_TABLE[5] = OP_STORE_APK;
		TYPE_TABLE[6] = SECTION_PIC;
		TYPE_TABLE[7] = VOICE_COMMENT;
		TYPE_TABLE[8] = OTHER_PIC;
		TYPE_TABLE[9] = APK_CEN;
		TYPE_TABLE[10] = BOX;
		TYPE_TABLE[11] = WEBP;
	}

	public static ExtendedItemType forCode(byte code) {
		if (MIN_ID > code || MAX_ID < code)
			throw new IllegalArgumentException("code:" + code);
		return TYPE_TABLE[code];
	}
}
