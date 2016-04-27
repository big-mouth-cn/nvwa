package org.bigmouth.nvwa.sap;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ContentRange {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentRange.class);

	private int globalBeginOffset = -1;
	private int relativeBeginOffset = -1;
	private int length = -1;
	private int totalLength = -1;

	public static ContentRange create(int globalBeginOffset, int relativeBeginOffset, int length,
			int totalLength) {
		return new ContentRange(globalBeginOffset, relativeBeginOffset, length, totalLength);
	}

	public static ContentRange createFullRange() {
		return new ContentRange(-1, -1, -1, -1);
	}

	public static ContentRange createOnlyRelativeRange(int relativeBeginOffset, int length) {
		return new ContentRange(-1, relativeBeginOffset, length, -1);
	}

	private ContentRange(int globalBeginOffset, int relativeBeginOffset, int length, int totalLength) {
		this.globalBeginOffset = globalBeginOffset;
		this.relativeBeginOffset = relativeBeginOffset;
		this.length = length;
		this.totalLength = totalLength;
	}

	public byte[] getExactContent(byte[] content) {
		if (null == content)
			throw new NullPointerException("content");
		if (!isRequireCutContent()) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Content is !NOT! require cut.");
			return content;
		} else {
			if (-1 == getRelativeBeginOffset()) {
				setRelativeBeginOffset(0);
			}
			if (-1 == getRelativeBeginOffset()) {
				length = content.length - relativeBeginOffset + 1;
			}
			if (length <= 0) {
				// TODO:
				throw new RuntimeException("illegal length:" + length);
			}
			byte[] ret = new byte[length];
			System.arraycopy(content, getRelativeBeginOffset(), ret, 0, length);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Content is require cut,relativeRange:" + this);
			return ret;
		}
	}

	public boolean isRequireCutContent() {
		if (length <= 0)
			return false;
		else if (getRelativeBeginOffset() < 0)
			return false;
		return true;
	}

	public int getRelativeBeginOffset() {
		return relativeBeginOffset;
	}

	private void setRelativeBeginOffset(int relativeBeginOffset) {
		this.relativeBeginOffset = relativeBeginOffset;
	}

	public int getGlobalBeginOffset() {
		return globalBeginOffset;
	}

	public int getLength() {
		return length;
	}

	public int getTotalLength() {
		return totalLength;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
