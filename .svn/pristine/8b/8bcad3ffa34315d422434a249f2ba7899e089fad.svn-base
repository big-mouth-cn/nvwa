package org.bigmouth.nvwa.servicelogic.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bigmouth.nvwa.dpl.event.listener.LoggingServiceListener;
import org.bigmouth.nvwa.dpl.event.listener.ServiceListener;
import org.bigmouth.nvwa.dpl.factory.annotation.ThreadModel;
import org.bigmouth.nvwa.dpl.status.ServiceStatusListener;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionService {

	String code();

	String name();

	String description() default "";

	boolean isRunning() default true;

	Class<? extends ServiceListener>[] listeners() default { LoggingServiceListener.class, ServiceStatusListener.class };

	ThreadModel threadModel() default @ThreadModel();
}
