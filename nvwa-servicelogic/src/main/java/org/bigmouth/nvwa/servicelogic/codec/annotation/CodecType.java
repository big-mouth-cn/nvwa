package org.bigmouth.nvwa.servicelogic.codec.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.bigmouth.nvwa.sap.ContentType;


@Retention(RetentionPolicy.RUNTIME)
public @interface CodecType {

	public abstract ContentType value() default ContentType.TLV;
}
