package org.bigmouth.nvwa.cache.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.cache.Node;
import org.bigmouth.nvwa.cache.NodeBuilder;


public class ConfigNodeBuilder implements NodeBuilder {

	private static final String ADDR_SEPARATOR = ",";

	private static final String HOST_PORT_SEPARATOR = ":";

	private List<Node> nodes = new ArrayList<Node>();

	public ConfigNodeBuilder(String nodesDesc) {
		super();
		if (StringUtils.isBlank(nodesDesc))
			throw new IllegalArgumentException("nodesDesc is blank.");

		this.nodes = transformNodeList(nodesDesc.trim());
	}

	private List<Node> transformNodeList(String nodesDesc) {
		List<Node> result = new ArrayList<Node>();

		String[] addresses = nodesDesc.split(ADDR_SEPARATOR);
		for (String addr : addresses) {
			String[] host_port = addr.trim().split(HOST_PORT_SEPARATOR);
			result.add(new DefaultNode(host_port[0].trim(), Integer.parseInt(host_port[1].trim())));
		}

		return result;
	}

	@Override
	public List<Node> build() {
		return nodes;
	}
}
