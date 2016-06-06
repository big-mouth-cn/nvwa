package org.bigmouth.nvwa.cache.xmc;

import java.io.IOException;
import java.util.List;

import org.bigmouth.nvwa.cache.CacheServiceException;
import org.bigmouth.nvwa.cache.MemClientBuilder;
import org.bigmouth.nvwa.cache.Node;
import org.bigmouth.nvwa.cache.NodeBuilder;

import net.rubyeye.xmemcached.MemcachedClient;


public class XmcMemClientBuilder implements MemClientBuilder {

	private NodeBuilder nodeBuilder;

	private MemcachedClient memcachedClient;

	@Override
	public Object build() {
		if (null == nodeBuilder)
			throw new NullPointerException("nodeBuilder");

		List<Node> nodes = nodeBuilder.build();
		if (null == nodes)
			throw new NullPointerException("nodes");
		if (0 == nodes.size())
			throw new CacheServiceException("nodes size is 0.");

		try {
			for (Node n : nodes)
				memcachedClient.addServer(n.getIp(), n.getPort(), n.getWeight());
		} catch (IOException e) {
			throw new CacheServiceException("build:", e);
		}
		return memcachedClient;
	}

	public void setNodeBuilder(NodeBuilder nodeBuilder) {
		this.nodeBuilder = nodeBuilder;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

}
