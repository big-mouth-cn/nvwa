package org.bigmouth.nvwa.utils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class UUIDUtils {

	private UUIDUtils() {
	}

	private static AtomicInteger uuid = new AtomicInteger(0);

	public static String generateUUID(String systemId) {
		int no = uuid.incrementAndGet();
		while (no >= 100) {
			uuid.compareAndSet(no, 0);
			no = uuid.incrementAndGet();
		}
		return systemId + (System.currentTimeMillis() - 1230719496953L) + no;
	}

	public static String generateUUID() {
		String source = UUID.randomUUID().toString();
		return source.replaceAll("-", "");
	}
}
