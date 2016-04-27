/**
 * 
 */
package org.bigmouth.nvwa.contentstore;

import java.util.List;

/**
 * @author hp
 * 
 *         from skymobi base architecture
 */
public interface ContentStore {
	
	public List<String> getKeys();

	public String store(byte[] content);

	public byte[] fetch(String key);

	public boolean remove(String key);

	public boolean contains(String key);
}
