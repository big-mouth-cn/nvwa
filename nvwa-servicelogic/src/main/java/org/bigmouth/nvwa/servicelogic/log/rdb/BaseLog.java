package org.bigmouth.nvwa.servicelogic.log.rdb;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.log.rdb.annotation.RdbColumn;
import org.bigmouth.nvwa.servicelogic.handler.CommonBizCode;


public class BaseLog {

	@RdbColumn(name = "create_time")
	private Date createTime = new Date();
	@RdbColumn(name = "biz_code")
	private int bizCode = CommonBizCode.SUCCESS;
	@RdbColumn(name = "exception_desc")
	private String exceptionDesc;

	public void mergeInvokeStatus(Object requestModel) {
		if (null == requestModel)
			throw new NullPointerException("requestModel");
		try {
			PropertyUtils.copyProperties(this, requestModel);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("mergeInvokeStatus:", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("mergeInvokeStatus:", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("mergeInvokeStatus:", e);
		}
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getBizCode() {
		return bizCode;
	}

	public void setBizCode(int bizCode) {
		this.bizCode = bizCode;
	}

	public String getExceptionDesc() {
		return exceptionDesc;
	}

	public void setExceptionDesc(String exceptionDesc) {
		this.exceptionDesc = exceptionDesc;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
