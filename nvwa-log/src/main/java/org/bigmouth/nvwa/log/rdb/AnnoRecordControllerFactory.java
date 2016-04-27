package org.bigmouth.nvwa.log.rdb;

import java.lang.reflect.Field;
import java.util.List;

import org.bigmouth.nvwa.log.rdb.annotation.RdbColumn;
import org.bigmouth.nvwa.log.rdb.annotation.RdbRecord;
import org.bigmouth.nvwa.utils.ReflectUtils;
import org.bigmouth.nvwa.utils.ReflectUtils.FieldFilter;

import com.google.common.collect.Lists;

public class AnnoRecordControllerFactory implements RecordControllerFactory {

	private final LogAppender logAppender;

	public AnnoRecordControllerFactory(LogAppender logAppender) {
		super();
		if (null == logAppender)
			throw new NullPointerException("logAppender");
		this.logAppender = logAppender;
	}

	@Override
	public RecordController create(Object logInfo) {
		if (null == logInfo)
			throw new NullPointerException("logInfo");

		Class<?> logClass = logInfo.getClass();
		RdbRecord recordAnno = getRdbRecord(logClass);
		if (null == recordAnno)
			throw new RuntimeException("No such RdbRecord annotation on Class:" + logClass);

		String tableName = recordAnno.tableName();
		long timeThreshold = recordAnno.timeThreshold();
		int amountThreshold = recordAnno.amountThreshold();
		String threadName = recordAnno.threadName();

		final List<Column> columns = createColumns(logClass);
		String recordSql = createSql(tableName, columns);

		RecordController ret = new RecordController(recordSql, columns, logAppender, threadName,
				timeThreshold, amountThreshold);

		return ret;
	}

	private RdbRecord getRdbRecord(Class<?> logClass) {
		RdbRecord recordAnno = ReflectUtils.getAnnotation(logClass, RdbRecord.class);
		return recordAnno;
	}

	private List<Column> createColumns(Class<?> logClass) {
		final List<Column> columns = Lists.newArrayList();
		ReflectUtils.findFields(logClass, new FieldFilter() {

			@Override
			public boolean accept(Field field) {
				RdbColumn columnAnno = field.getAnnotation(RdbColumn.class);
				String fieldName = field.getName();
				String columnName = columnAnno.name();
				int maxLength = columnAnno.maxLength();

				Column column = new Column(fieldName, columnName, maxLength);
				columns.add(column);

				return false;
			}
		});
		return columns;
	}

	private String createSql(String tableName, final List<Column> columns) {
		StringBuilder insertSb = new StringBuilder();
		StringBuilder valuesSb = new StringBuilder();

		insertSb.append("insert into ");
		insertSb.append(tableName);
		insertSb.append(" (");

		valuesSb.append(" values (");

		for (Column column : columns) {
			insertSb.append(column.getColumnName()).append(",");
			valuesSb.append("?,");
		}

		insertSb = new StringBuilder(insertSb.substring(0, insertSb.length() - 1));
		valuesSb = new StringBuilder(valuesSb.substring(0, valuesSb.length() - 1));

		insertSb.append(") ");
		valuesSb.append(")");
		insertSb.append(valuesSb);

		String recordSql = insertSb.toString();
		return recordSql;
	}
}
