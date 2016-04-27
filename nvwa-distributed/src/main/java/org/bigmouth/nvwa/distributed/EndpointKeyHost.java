package org.bigmouth.nvwa.distributed;

import com.google.common.base.Preconditions;

public final class EndpointKeyHost {

	private final Endpoint endpoint;

	public static EndpointKeyHost of(Endpoint endpoint) {
		return new EndpointKeyHost(endpoint);
	}

	private EndpointKeyHost(Endpoint endpoint) {
		Preconditions.checkNotNull(endpoint, "endpoint");
		this.endpoint = endpoint;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endpoint.getHost() == null) ? 0 : endpoint.getHost().hashCode());
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
		EndpointKeyHost other = (EndpointKeyHost) obj;
		if (endpoint.getHost() == null) {
			if (other.endpoint.getHost() != null)
				return false;
		} else if (!endpoint.getHost().equals(other.endpoint.getHost()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getEndpoint().getHost().toString();
	}
}
