package org.bigmouth.nvwa.servicelogic.codec.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.bigmouth.nvwa.sap.ContentType;


/**
 * 请参考标准HTTP协议中的Content-Type
 * 
 * @since 1.0
 * @author Allen Hu - (big-mouth.cn)
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
public @interface CodecType {

	public abstract ContentType value() default ContentType.TLV;
}
