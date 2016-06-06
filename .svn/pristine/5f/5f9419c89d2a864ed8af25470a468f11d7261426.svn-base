package org.bigmouth.nvwa.transport;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TcpConfig {

	private static boolean DEFAULT_REUSE_ADDRESS = true;
	private static int DEFAULT_RECEIVE_BUFFER_SIZE = 1024;
	private static int DEFAULT_SEND_BUFFER_SIZE = 1024;
	private static boolean DEFAULT_TCPNODELAY = true;
	private static int DEFAULT_SOLINGER = -1;
	private static int DEFAULT_BACKLOG = 10240;

	private boolean reuseAddress = DEFAULT_REUSE_ADDRESS;
	private int receiveBufferSize = DEFAULT_RECEIVE_BUFFER_SIZE;
	private int sendBufferSize = DEFAULT_SEND_BUFFER_SIZE;
	private boolean tcpNoDelay = DEFAULT_TCPNODELAY;
	private int soLinger = DEFAULT_SOLINGER;
	private int backlog = DEFAULT_BACKLOG;

	public boolean isReuseAddress() {
		return reuseAddress;
	}

	public void setReuseAddress(boolean reuseAddress) {
		this.reuseAddress = reuseAddress;
	}

	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}

	public int getSendBufferSize() {
		return sendBufferSize;
	}

	public void setSendBufferSize(int sendBufferSize) {
		this.sendBufferSize = sendBufferSize;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public int getSoLinger() {
		return soLinger;
	}

	public void setSoLinger(int soLinger) {
		this.soLinger = soLinger;
	}

	public int getBacklog() {
		return backlog;
	}

	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
