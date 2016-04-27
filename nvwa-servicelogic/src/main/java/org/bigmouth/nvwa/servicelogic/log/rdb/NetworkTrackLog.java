package org.bigmouth.nvwa.servicelogic.log.rdb;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.log.rdb.annotation.RdbColumn;
import org.bigmouth.nvwa.sap.SapRequest;


public class NetworkTrackLog extends BaseLog {

	@RdbColumn(name = "client_host")
	private String clientHost;
	@RdbColumn(name = "access_host")
	private String accessHost;
	@RdbColumn(name = "access_port")
	private short accessPort;
	@RdbColumn(name = "local_host")
	private String localHost;
	@RdbColumn(name = "local_port")
	private short localPort;

	public void mergeNetworkInfo(SapRequest sapRequest) {
		if (null == sapRequest)
			throw new NullPointerException("sapRequest");
		this.clientHost = sapRequest.getClientIp().toString();
		this.accessHost = sapRequest.getAccessAddress().getIpDesc();
		this.accessPort = (short) sapRequest.getAccessAddress().getPort();
		this.localHost = sapRequest.getLocalAddress().getIpDesc();
		this.localPort = (short) sapRequest.getLocalAddress().getPort();
	}

	public String getClientHost() {
		return clientHost;
	}

	public void setClientHost(String clientHost) {
		this.clientHost = clientHost;
	}

	public String getAccessHost() {
		return accessHost;
	}

	public void setAccessHost(String accessHost) {
		this.accessHost = accessHost;
	}

	public short getAccessPort() {
		return accessPort;
	}

	public void setAccessPort(short accessPort) {
		this.accessPort = accessPort;
	}

	public String getLocalHost() {
		return localHost;
	}

	public void setLocalHost(String localHost) {
		this.localHost = localHost;
	}

	public short getLocalPort() {
		return localPort;
	}

	public void setLocalPort(short localPort) {
		this.localPort = localPort;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
