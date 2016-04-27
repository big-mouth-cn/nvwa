package org.bigmouth.nvwa.dpl.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadModel {

	/**
	 * cached<br>
	 * fixed<br>
	 * single<br>
	 * share(Reserved)
	 * 
	 * @return
	 */
	String type() default "cached";

	String threadNameFlag() default "";

	int threadCount() default 1;

	boolean isDaemon() default false;

	int priority() default Thread.NORM_PRIORITY;
}
