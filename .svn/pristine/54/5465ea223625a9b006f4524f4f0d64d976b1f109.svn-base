package org.bigmouth.nvwa.access.request.standard;

import java.util.Map;

import org.apache.asyncweb.common.HttpMethod;
import org.bigmouth.nvwa.access.request.HttpRequestExt;
import org.bigmouth.nvwa.access.request.IllegalAccessProtocolException;
import org.bigmouth.nvwa.access.request.UnsupportHttpMethodException;
import org.bigmouth.nvwa.sap.SapRequest;
import org.bigmouth.nvwa.utils.Transformer;

import com.google.common.collect.Lists;

public class StandardHttp2SapRequestTransformer implements Transformer<HttpRequestExt, SapRequest> {

	private final RequestParamSetExtractor requestParamSetExtractor;
	private final Map<String, SapRequestFactory> sapRequestFactories;

	public StandardHttp2SapRequestTransformer(RequestParamSetExtractor requestParamSetExtractor,
			Map<String, SapRequestFactory> sapRequestFactories) {
		super();
		this.requestParamSetExtractor = requestParamSetExtractor;
		this.sapRequestFactories = sapRequestFactories;
	}

	@Override
	public SapRequest transform(HttpRequestExt httpRequest) {
		if (HttpMethod.GET != httpRequest.getMethod()) {
			throw new UnsupportHttpMethodException(httpRequest,
					"Web standard access only accept Get method.",
					Lists.newArrayList(HttpMethod.GET));
		}

		RequestParamSet paramSet = requestParamSetExtractor.extract(httpRequest);
		SapRequestFactory sapRequestFactory = sapRequestFactories.get(paramSet.getType());
		if (null == sapRequestFactory)
			throw new IllegalAccessProtocolException(httpRequest, "Standard access illegal type:"
					+ paramSet.getType());

		SapRequest sapRequest = sapRequestFactory.create(httpRequest, paramSet);
		return sapRequest;
	}
}
