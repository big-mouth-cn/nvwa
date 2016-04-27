package org.bigmouth.nvwa.dpl.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bigmouth.nvwa.dpl.event.listener.LoggingPlugInListener;
import org.bigmouth.nvwa.dpl.event.listener.PlugInListener;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlugIn {

	String code();

	String name();

	String description() default "";

	boolean isRunning() default true;

	Class<? extends PlugInListener>[] listeners() default { LoggingPlugInListener.class };
}
