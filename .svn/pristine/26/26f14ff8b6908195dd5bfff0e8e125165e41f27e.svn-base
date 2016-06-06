package org.bigmouth.nvwa.cache;


public class KeyUtils {
	
	private static final int DEFAULT_LENGTH = 64;
	
	private static final String SPLIT = "#";

	public static String generateKey(String... factors) {
		if (null == factors || factors.length == 0)
			throw new IllegalArgumentException("factors is null or its length is 0.");
		StringBuilder sb = new StringBuilder(DEFAULT_LENGTH);
		for (String s : factors)
			sb.append(s).append(SPLIT);
		return sb.substring(0, sb.length() - 1);
	}

}
