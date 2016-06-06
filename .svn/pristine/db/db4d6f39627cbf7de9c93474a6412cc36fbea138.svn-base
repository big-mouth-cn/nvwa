package org.bigmouth.nvwa.codec.jms.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;


public class TLVSqlBean {

	@TLVAttribute(tag = 1)
	private String sql;

	public TLVSqlBean(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
