package org.bigmouth.nvwa.codec.jms.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TLVMessageBean {

	private TLVSqlBean sqlBean;

	private List<TLVArgumentsBean> argumentsBeanList;

	public TLVMessageBean(String sql, List<Object[]> args) {
		TLVSqlBean sqlBean = new TLVSqlBean(sql);
		this.sqlBean = sqlBean;

		argumentsBeanList = new ArrayList<TLVArgumentsBean>();
		for (Object[] objs : args)
			argumentsBeanList.add(new TLVArgumentsBean(objs));

	}

	public TLVSqlBean getSqlBean() {
		return sqlBean;
	}

	public List<TLVArgumentsBean> getArgumentsBeanList() {
		return argumentsBeanList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
