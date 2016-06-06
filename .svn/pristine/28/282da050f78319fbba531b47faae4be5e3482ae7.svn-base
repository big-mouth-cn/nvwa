package org.bigmouth.nvwa.access.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public final class HttpUtils {

	private static final String REG_HTTP_URI_PREFIX = "http://[0-9a-zA-Z\\-\\.]+(:[0-9]{4})?";

	private HttpUtils() {
	}

	public static String genContentRange(int begin, int end, int total) {
		StringBuilder sb = new StringBuilder("bytes ");
		sb.append(begin);
		sb.append("-");
		sb.append(end);
		sb.append("/");
		sb.append(total);
		return sb.toString();
	}

	public static String genGMTString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
				Locale.ENGLISH);
		dateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
		return dateFormat.format(date);
	}

	public static String genGMTString() {
		return genGMTString(new Date());
	}

	/**
	 * http://test.sky-mobi.com:8081/a/b => /a/b
	 * 
	 * @param mappingUrl
	 * @return
	 */
	public static String removeReqUriPrefix(String mappingUrl) {
		return mappingUrl.replaceFirst(REG_HTTP_URI_PREFIX, "");
	}
}
