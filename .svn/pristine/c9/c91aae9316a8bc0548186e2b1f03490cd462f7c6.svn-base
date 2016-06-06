package org.bigmouth.nvwa.access.request.standard;

import java.util.Arrays;

import org.apache.asyncweb.common.HttpRequest;
import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.access.request.IllegalAccessProtocolException;
import org.bigmouth.nvwa.access.utils.HttpUtils;

import com.google.common.collect.Lists;

public class DefaultRequestParamSetExtractor implements RequestParamSetExtractor {

	private static final String HTTP_HEADER_RANGE = "Range";
	private static final String SPLIT = "/";

	@Override
	public RequestParamSet extract(HttpRequest httpRequest) throws IllegalAccessProtocolException {
		String mappingUrl = httpRequest.getRequestUri().toASCIIString();
		mappingUrl = mappingUrl.trim();
		mappingUrl = HttpUtils.removeReqUriPrefix(mappingUrl);

		String[] factors = mappingUrl.split("\\?");
		mappingUrl = factors[0];

		if (mappingUrl.startsWith(SPLIT))
			mappingUrl = mappingUrl.substring(1);

		if (mappingUrl.endsWith(SPLIT))
			mappingUrl = mappingUrl.substring(0, mappingUrl.length() - 1);

		String[] elements = mappingUrl.split(SPLIT);
		if (0 == elements.length)
			throw new IllegalAccessProtocolException(httpRequest, "Illegal request uri.");

		String type = elements[0];
		String[] params = Arrays.copyOfRange(elements, 1, elements.length);

		// content-range
		int begin = 0;
		int end = -1;
		String range = httpRequest.getHeader(HTTP_HEADER_RANGE);
		if (!StringUtils.isBlank(range)) {
			// bytes=0-801
			String[] rElements = range.trim().split("=");
			if (2 != rElements.length) {
				throw new IllegalAccessProtocolException(httpRequest, "Illegal Content-Range.");
			}
			String[] rvs = rElements[1].split("-");
			begin = Integer.parseInt(rvs[0]);
			if (2 == rvs.length) {
				if (!StringUtils.isBlank(rvs[1])) {
					end = Integer.parseInt(rvs[1]);
				}
			}
		}
		return new RequestParamSet(type, Lists.newArrayList(params), begin, end);
	}
}
