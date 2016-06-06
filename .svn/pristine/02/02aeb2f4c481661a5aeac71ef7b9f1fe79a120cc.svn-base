package org.bigmouth.nvwa.codec.tlv.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.bigmouth.nvwa.codec.byteorder.ByteOrder;


@Retention(RetentionPolicy.RUNTIME)
public @interface TLVAttribute {

	public abstract int tag() default -1;

	public abstract String propertyName() default "";

	public abstract int byteLen() default 0;

	public abstract int maxLen() default 0;

	public abstract String charset() default "";

	public abstract ByteOrder byteOrder() default ByteOrder.none;

	/**
	 * Being ignored.
	 * 
	 * @return
	 */
	public abstract int codeOrder() default -1;

	public abstract boolean ignoreTagLen() default false;

	public abstract TLVArrayAttribute[] arrayAttributes() default {};

	/**
	 * e.g. if field(number) value is 0,then ignore this field when encoding.<br>
	 * this attribute support for byte,short,int and long.<br>
	 * if this attribute'value is -1L,then ignore this attribute.
	 * 
	 * @return
	 */
	public abstract long ignoreNumberValue() default -1L;
}
