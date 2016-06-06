package org.bigmouth.nvwa.log.rdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RdbRecord {

	String tableName();

	/**
	 * Millisecond
	 * 
	 * @return
	 */
	long timeThreshold() default 5000L;

	int amountThreshold() default 1000;

	String threadName() default "RecordController-thread";
}
