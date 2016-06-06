package org.bigmouth.nvwa.codec.tlv.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.bigmouth.nvwa.codec.byteorder.ByteOrder;



@Retention(RetentionPolicy.RUNTIME)
public @interface TLVArrayAttribute {

	public abstract int tag();
	
	public abstract boolean ignoreWrapTag() default false;

	public abstract int byteLen() default 0;

	public abstract int maxLen() default 0;

	public abstract String charset() default "";

	public abstract ByteOrder byteOrder() default ByteOrder.none;

}
