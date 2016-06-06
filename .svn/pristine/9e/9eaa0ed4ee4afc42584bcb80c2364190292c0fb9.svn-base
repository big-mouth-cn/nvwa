package org.bigmouth.nvwa.jmx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MBeanAttribute {

	String domain();

	String type();

	String name();

	// include attribute?
	// include operation?
}
