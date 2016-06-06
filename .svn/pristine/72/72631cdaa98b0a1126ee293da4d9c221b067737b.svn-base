package org.bigmouth.nvwa.access.request;

import java.io.UnsupportedEncodingException;

import org.apache.asyncweb.common.HttpMethod;
import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.sap.ContentType;
import org.bigmouth.nvwa.sap.DefaultSapRequest;
import org.bigmouth.nvwa.sap.MutableSapRequest;
import org.bigmouth.nvwa.sap.SapRequest;
import org.bigmouth.nvwa.sap.namecode.PlugInServiceNamePair;
import org.bigmouth.nvwa.utils.Transformer;

public class Http2SapRequestTransformer implements Transformer<HttpRequestExt, SapRequest> {

	private static final String HTTP_HEADER_RANGE = "Range";
	private static final String SPLIT = "/";

	@Override
	public SapRequest transform(HttpRequestExt httpRequest) {
		try {
			MutableSapRequest sapRequest = new DefaultSapRequest();

			// transaction id
			sapRequest.setIdentification(httpRequest.getIdentification());

			// client ip & access ip & access port
			sapRequest.setClientIp(httpRequest.getClientIp());
			sapRequest.setAccessAddress(httpRequest.getAccessAddress());

			// src plugin service
			sapRequest.setSourcePlugInName("access");
			sapRequest.setSourceServiceName("default");

			// tgt plugin service
			sapRequest
					.setTargetPlugInServiceNameSource(getTargetPlugInServiceNamePair(httpRequest));

			// ULP range
			String range = httpRequest.getHeader(HTTP_HEADER_RANGE);
			if (null != range) {
				// TODO: ULP range
			}
			
			if (httpRequest.getMethod() == HttpMethod.GET) {
			    sapRequest.setContentType(ContentType.KV);
			}
			else {
			    String contentType = httpRequest.getContentType();
			    sapRequest.setContentType(ContentTypeFactory.build(contentType));
			}

			// content
			sapRequest.setContent(extractContent(httpRequest));

			return sapRequest;
		} catch (Exception e) {
			throw new IllegalAccessProtocolException(httpRequest, e);
		}
	}

	private IoBuffer extractContent(HttpRequestExt httpRequest) {
		String mappingUrl = httpRequest.getRequestUri().toASCIIString().trim();

		String[] factors = mappingUrl.split("\\?");
		if (1 == factors.length) {
			return httpRequest.getContent();
		}

		byte[] bytes = null;
		try {
			bytes = factors[1].getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("extractContent:", e);
		}
		return IoBuffer.wrap(bytes);
	}

	private PlugInServiceNamePair getTargetPlugInServiceNamePair(HttpRequestExt httpRequest) {
		String plugInName = "";
		String serviceName = "";

		String mappingUrl = httpRequest.getRequestUri().toASCIIString();
		mappingUrl = mappingUrl.trim();

		String[] factors = mappingUrl.split("\\?");
		mappingUrl = factors[0];

		if (mappingUrl.startsWith(SPLIT))
			mappingUrl = mappingUrl.substring(1);

		if (mappingUrl.endsWith(SPLIT))
			mappingUrl = mappingUrl.substring(0, mappingUrl.length() - 1);

		int lastSplit = mappingUrl.lastIndexOf(SPLIT);
		if (-1 == lastSplit) {
			plugInName = mappingUrl;
			serviceName = "";
		} else {
			serviceName = mappingUrl.substring(lastSplit + 1);
			mappingUrl = mappingUrl.substring(0, lastSplit);
			lastSplit = mappingUrl.lastIndexOf(SPLIT);
			if (-1 == lastSplit) {
				plugInName = mappingUrl;
			} else {
				plugInName = mappingUrl.substring(lastSplit + 1);
			}
		}
		final String _plugInName = plugInName;
		final String _serviceName = serviceName;

		return new PlugInServiceNamePair() {

			@Override
			public String getPlugInName() {
				return _plugInName;
			}

			@Override
			public String getServiceName() {
				return _serviceName;
			}
		};
	}
}
