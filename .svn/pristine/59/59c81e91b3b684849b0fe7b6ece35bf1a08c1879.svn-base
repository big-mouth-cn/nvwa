package org.bigmouth.nvwa.distributed;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public class Endpoint implements Comparable<Endpoint> {

	private static final int DEFAULT_WEIGHTS = 5;
	private static final String LOCAL_ADDRESS = "0.0.0.0";

	private final String host;
	private final int port;

	/**
	 * 0 => 9(low => high)
	 */
	private AtomicInteger weights = new AtomicInteger(DEFAULT_WEIGHTS);

	public static Endpoint of(String host, int port, byte[] w) {
		int _w = -1;
		try {
			_w = Integer.parseInt(new String(w));
		} catch (NumberFormatException e) {
			throw new RuntimeException("Illegal weights value:" + w);
		}
		return new Endpoint(host, port, _w);
	}

	public static Endpoint of(String host, int port, int w) {
		return new Endpoint(host, port, w);
	}

	public static Endpoint of(String host, int port) {
		return new Endpoint(host, port);
	}

	private Endpoint(String host, int port) {
		this(host, port, DEFAULT_WEIGHTS);
	}

	private Endpoint(String host, int port, int w) {
		Preconditions.checkNotNull(host, "host");
		Preconditions.checkArgument(port > 0 || port <= 65535, "port:" + port);
		Preconditions.checkArgument(w >= 1, "weight:" + w);

		if (LOCAL_ADDRESS.equals(host)) {
			try {
				host = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				throw new RuntimeException("Endpoint:", e);
			}
		}

		this.host = host;
		this.port = port;
		weights.set(w);
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return port;
	}

	public InetSocketAddress getAddress() {
		return new InetSocketAddress(getHost(), getPort());
	}

	public int getWeights() {
		return weights.get();
	}

	public String getEndpointPath(String namespace, String connector) {
		Preconditions.checkArgument(StringUtils.isNotBlank(namespace), "namespace");
		Preconditions.checkArgument(StringUtils.isNotBlank(connector), "connector");

		Joiner joiner = Joiner.on("");
		return joiner.join(namespace, "/", getHost(), connector, getPort()).toString();
	}

	public void setWeights(int w) {
		Preconditions.checkArgument(w >= 1, "weight:" + w);
		this.weights.set(w);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endpoint other = (Endpoint) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	@Override
	public int compareTo(Endpoint o) {
		Preconditions.checkNotNull(o, "endpoint");
		return ComparisonChain.start().compare(o.getHost(), this.getHost())
				.compare(o.getWeights(), this.getWeights()).compare(o.getPort(), this.getPort())
				.result();
	}

	@Override
	public String toString() {
		return new StringBuilder(getHost()).append(":").append(getPort()).append("_")
				.append(getWeights()).toString();
	}
}
