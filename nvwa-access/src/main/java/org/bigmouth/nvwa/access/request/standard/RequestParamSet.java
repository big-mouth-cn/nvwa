package org.bigmouth.nvwa.access.request.standard;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.collect.Lists;

public class RequestParamSet {

	private final String type;
	private final List<String> parameters = Lists.newArrayList();
	private final int beginOffset;
	private final int endOffset;

	public RequestParamSet(String type, List<String> parameters, int beginOffset, int endOffset) {
		super();
		if (StringUtils.isBlank(type))
			throw new IllegalArgumentException("type is blank.");
		this.type = type;
		this.parameters.addAll(parameters);
		this.beginOffset = beginOffset;
		this.endOffset = endOffset;
	}

	public String getType() {
		return type;
	}

	public List<String> getParameters() {
		return Collections.unmodifiableList(parameters);
	}

	public int getBeginOffset() {
		return beginOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
