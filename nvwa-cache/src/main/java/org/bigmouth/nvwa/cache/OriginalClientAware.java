package org.bigmouth.nvwa.cache;

/**
 * The way to obtain original cache client which implement the function of
 * interacting with cache server.
 * 
 * @author nada
 * 
 */
public interface OriginalClientAware {

	public Object getOriginalClient();

}
