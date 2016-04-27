package org.bigmouth.nvwa.sap;

import org.bigmouth.nvwa.sap.namecode.PlugInServiceNamePair;

public interface SapRequest extends SapTransactionMessage {

	String getSourcePlugInName();

	String getSourceServiceName();

	String getTargetPlugInName();

	String getTargetServiceName();

	PlugInServiceNamePair getSourcePlugInServiceNamePair();

	PlugInServiceNamePair getTargetPlugInServiceNamePair();

	/**
	 * Upper-layer protocols begin offset.(e.g. http header:Range)
	 * 
	 * @return
	 */
	int getULP_beginOffset();

	/**
	 * Upper-layer protocols end offset.(e.g. http header:Range)
	 * 
	 * @return
	 */
	int getULP_endOffset();

	Ip getClientIp();

	IpPort getAccessAddress();

	Ip getPeerIp();

	IpPort getLocalAddress();
}
