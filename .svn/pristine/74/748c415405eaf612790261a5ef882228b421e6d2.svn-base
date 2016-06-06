package org.bigmouth.nvwa.access.request.standard;

import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.access.request.HttpRequestExt;
import org.bigmouth.nvwa.sap.DefaultSapRequest;
import org.bigmouth.nvwa.sap.MutableSapRequest;
import org.bigmouth.nvwa.sap.SapRequest;
import org.bigmouth.nvwa.sap.namecode.PlugInServiceNamePair;


public abstract class SapRequestFactory {

	public SapRequest create(HttpRequestExt httpRequest, RequestParamSet paramSet) {
		MutableSapRequest sapRequest = new DefaultSapRequest();

		// transaction id
		sapRequest.setIdentification(httpRequest.getIdentification());

		// client ip & access ip & access port
		sapRequest.setClientIp(httpRequest.getClientIp());
		sapRequest.setAccessAddress(httpRequest.getAccessAddress());

		// src plugin service
		sapRequest.setSourcePlugInName("access");
		sapRequest.setSourceServiceName("default");

		PlugInServiceNamePair tgtPlugInServiceNamePair = getPlugInServiceNamePair();
		IoBuffer sapReqContent = getContent(paramSet);

		sapRequest.setTargetPlugInServiceNameSource(tgtPlugInServiceNamePair);
		sapRequest.setContent(sapReqContent);

		return sapRequest;
	}

	public abstract PlugInServiceNamePair getPlugInServiceNamePair();

	public abstract IoBuffer getContent(RequestParamSet paramSet);
}
