package org.bigmouth.nvwa.codec.tlv;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TLVConfig {

	private int tagLen = 4;

	private int lengthLen = 4;

	public int getTagLen() {
		return tagLen;
	}

	public void setTagLen(int tagLen) {
		this.tagLen = tagLen;
	}

	public int getLengthLen() {
		return lengthLen;
	}

	public void setLengthLen(int lengthLen) {
		this.lengthLen = lengthLen;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
