package org.bigmouth.nvwa.cache.support;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.cache.Node;


public class DefaultNode implements Node {

	private static final String SPLIT = ":";

	private static final int DEFAULT_WEIGHT = 1;

	private String ip;

	private int port;

	private int weight = DEFAULT_WEIGHT;

	public DefaultNode(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	public DefaultNode(String ip, int port, int weight) {
		super();
		this.ip = ip;
		this.port = port;
		this.weight = weight;
	}

	@Override
	public String getDesc() {
		return ip + SPLIT + port;
	}

	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
