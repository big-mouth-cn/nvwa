package org.bigmouth.nvwa.codec.tlv.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;


public class IgnoreNumberValueBean {

	@TLVAttribute(tag = 1, ignoreNumberValue = 0)
	private int id;
	@TLVAttribute(tag = 2)
	private String name;
	@TLVAttribute(tag = 3, ignoreNumberValue = 0)
	private long id0;
	@TLVAttribute(tag = 4, ignoreNumberValue = 0)
	private short id1;
	@TLVAttribute(tag = 5, ignoreNumberValue = 0)
	private byte id2;

	public short getId1() {
		return id1;
	}

	public void setId1(short id1) {
		this.id1 = id1;
	}

	public byte getId2() {
		return id2;
	}

	public void setId2(byte id2) {
		this.id2 = id2;
	}

	public long getId0() {
		return id0;
	}

	public void setId0(long id0) {
		this.id0 = id0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
