package org.bigmouth.nvwa.utils.jms;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DefaultMessageBean implements MessageBean, Serializable {

	private static final long serialVersionUID = 3255347119835601788L;

	private String sql;

	private Object[][] argumentsList;

	public DefaultMessageBean(String sql, Object[][] argumentsList) {
		this.sql = sql;
		this.argumentsList = argumentsList;
	}

	@Override
	public String getSql() {
		return sql;
	}

	@Override
	public Object[][] getArgumentsList() {
		return argumentsList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
