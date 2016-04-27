package org.bigmouth.nvwa.sap;

import org.bigmouth.nvwa.sap.namecode.PlugInServiceNamePair;

public interface MutableSapRequest extends SapRequest, MutableSapTransactionMessage {

	void setSourcePlugInName(String name);

	void setSourceServiceName(String name);

	void setTargetPlugInName(String name);

	void setTargetServiceName(String name);

	void setSourcePlugInServiceNamePair(PlugInServiceNamePair pair);

	void setTargetPlugInServiceNameSource(PlugInServiceNamePair pair);

	void setULP_beginOffset(int offset);

	void setULP_endOffset(int offset);

	void setClientIp(Ip clientIp);

	void setAccessAddress(IpPort accessAddress);

	void setPeerIp(Ip peerIp);

	void setLocalAddress(IpPort localAddress);
}
