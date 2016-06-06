package org.bigmouth.nvwa.sap;

public final class SapResponseStatus {

	private static final short MIN_ID = 200;
	private static final short MAX_ID = 599;
	private static final SapResponseStatus[] STATUS_TABLE = new SapResponseStatus[MAX_ID + 1];

	public static SapResponseStatus OK = new SapResponseStatus((short) 200, "OK",
			Category.SUCCESSFUL);

	public static SapResponseStatus ILLEGAL_PARAMETER = new SapResponseStatus((short) 400,
			"Illegal Parameter", Category.CLIENT_ERROR);

	public static SapResponseStatus INTERNAL_SERVER_ERROR = new SapResponseStatus((short) 500,
			"Internal Server Error", Category.SERVER_ERROR);

	public static SapResponseStatus PLUGIN_OR_SERVICE_UNAVAILABLE = new SapResponseStatus(
			(short) 404, "PlugIn Or Service Unavailable", Category.SERVER_ERROR);

	private final short code;
	private final String description;
	private final Category category;

	private SapResponseStatus(short code, String description, Category category) {
		STATUS_TABLE[code] = this;
		this.code = code;
		this.description = description;
		this.category = category;
	}

	public short getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code + ": " + category + " - " + description;
	}

	public static SapResponseStatus forCode(short code) {
		if (MIN_ID > code || MAX_ID < code)
			throw new IllegalArgumentException("code:" + code);
		SapResponseStatus status = STATUS_TABLE[code];
		if (null == status)
			throw new RuntimeException("can not found SapResponseStatus for code[" + code + "].");
		return status;
	}

	private static enum Category {

		SUCCESSFUL,

		CLIENT_ERROR,

		SERVER_ERROR;
	}
}
