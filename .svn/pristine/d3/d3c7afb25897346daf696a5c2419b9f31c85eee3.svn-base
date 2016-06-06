package org.bigmouth.nvwa.log.rdb;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public final class Column {

	public final String fieldName;
	public final String columnName;
	public final int maxLength;

	public Column(String fieldName, String columnName, int maxLength) {
		if (StringUtils.isBlank(fieldName))
			throw new IllegalArgumentException("fieldName is blank.");
		if (StringUtils.isBlank(columnName))
			throw new IllegalArgumentException("columnName is blank.");
		this.fieldName = fieldName;
		this.columnName = columnName;
		this.maxLength = maxLength;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getColumnName() {
		return columnName;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public Object getValue(Object logInfo) {
		if (null == logInfo)
			throw new NullPointerException("logInfo");

		Object value = null;
		try {
			value = PropertyUtils.getProperty(logInfo, fieldName);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("getValue:", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("getValue:", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("getValue:", e);
		}
		if ((value instanceof String) && (maxLength > 0) && ((String) value).length() > maxLength) {
			value = ((String) value).substring(0, maxLength);
		}
		return value;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
