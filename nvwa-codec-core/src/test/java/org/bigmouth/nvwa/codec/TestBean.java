package org.bigmouth.nvwa.codec;

import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;

public class TestBean {

	@TLVAttribute(tag = 1,ignoreTagLen=true,byteLen=2)
	private int id;
	
	@TLVAttribute(tag = 2)
	private String name;

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
	
	
}
