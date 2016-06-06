package org.bigmouth.nvwa.cache.support;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.cache.Node;
import org.bigmouth.nvwa.cache.NodeBuilder;


public class DummyNodeBuilder implements NodeBuilder {

	@Override
	public List<Node> build() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new DefaultNode("localhost", 11211));
		nodes.add(new DefaultNode("localhost", 11212));
		return nodes;
	}

}
