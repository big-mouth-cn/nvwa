package org.bigmouth.nvwa.cache;

public interface FetchService extends OriginalClientAware {

	public <T> T fetch(KeyGenerator keyGenerator, DataExtractor dataExtractor, Class<T> clazz);

	public <T> T fetch(KeyGenerator keyGenerator, DataExtractor dataExtractor, Class<T> clazz,
			int exp);

	public Object getOriginalClient();
}
