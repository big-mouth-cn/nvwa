package org.bigmouth.nvwa.servicelogic.handler;

public class CommonBizCode {

	protected CommonBizCode() {
	}

	public static final int SUCCESS = 2000;

	public static final int FAIL = 3000;

	public static final int ILLEGAL_PARAMS = 3001;

	/**
	 * Illegal.
	 */
	public static final int RESOURCE_NOT_FOUND = 5000;

	/**
	 * Legal.
	 */
	public static final int RESOURCE_NOT_FOUND_LEGAL = 5001;

	public static final int USER_LOGIN_UNSUCCESSFULLY = 5002;

	public static final int LASTED_VERSION = 6000;

	public static final int SUGGEST_UPDATE = 6001;

	public static final int FORCE_UPDATE = 6002;
	
	/**
	 * payment
	 */
	public static final int PAY_FIALED = 5003;
	public static final int PAYING = 5004;
}
