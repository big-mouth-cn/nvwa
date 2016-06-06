package org.bigmouth.nvwa.sap;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.codec.byteorder.ByteOrder;
import org.bigmouth.nvwa.codec.byteorder.NumberCodec;
import org.bigmouth.nvwa.codec.byteorder.NumberCodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

//0:ULP range flag  1:required tlv wrapper?
//| ext type(1)   |0|1| reserved  |   content flag(md5) count(2)  |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|	    data format mark(if TLV,then this means tag value)(4)     |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

//|	        data sharding mark (4)                                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

//|	        content merge offset (4)                              |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

//|	        data global begin offset(4)                           |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|	        data relative begin offset(4)                         |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|	        data length(4)                                        |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|	        data total length(4)                                  |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|	                                                              |
//|	    content flag set(md5)(32 * content flag count)            |
//|	                                                              |
//|	                                                              |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//TODO:约束还需要细化
public class ExtendedItem implements Serializable, Comparable<ExtendedItem> {

	private static final long serialVersionUID = 1L;
	private static final NumberCodec numberCodec = NumberCodecFactory
			.fetchNumberCodec(ByteOrder.bigEndian);
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtendedItem.class);
	private static final byte STATE_FLAG_ULP_RANGE = 1 << 0;
	private static final byte STATE_FLAG_REQUIRED_WRAP = 1 << 1;

	private final ExtendedItemType type;
	private byte stateFlagSet = (byte) 0x00;
	private int formatMark = -1;
	private int shardingMark = 0;

	// content merge offset
	private int mergeOffset = -1;

	private final List<String> contentFlags = Lists.newArrayList();

	private ContentRange contentRange = ContentRange.createFullRange();

	private ExtendedItem(ExtendedItemType type) {
		if (null == type)
			throw new NullPointerException("type");
		this.type = type;
	}

	public byte getStateFlagSet() {
		return stateFlagSet;
	}

	private void setStateFlagSet(byte stateFlagSet) {
		this.stateFlagSet = stateFlagSet;
	}

	public int getFormatMark() {
		return formatMark;
	}

	private void setFormatMark(int formatMark) {
		this.formatMark = formatMark;
	}

	public int getShardingMark() {
		return shardingMark;
	}

	public void setShardingMark(int shardingMark) {
		this.shardingMark = shardingMark;
	}

	/**
	 * -1 means this content may be append end of content
	 * 
	 * @return
	 */
	public int getMergeOffset() {
		return mergeOffset;
	}

	public void setMergeOffset(int mergeOffset) {
		this.mergeOffset = mergeOffset;
	}

	public ContentRange getContentRange() {
		return contentRange;
	}

	private void setContentRange(ContentRange contentRange) {
		this.contentRange = contentRange;
	}

	public ExtendedItemType getType() {
		return type;
	}

	public boolean isRequiredWrap() {
		return (stateFlagSet & STATE_FLAG_REQUIRED_WRAP) == STATE_FLAG_REQUIRED_WRAP;
	}

	public byte[] wrapContent(byte[] content) {
		if (null == content)
			throw new NullPointerException("content");
		if (!isRequiredWrap())
			return content;
		int tag = getFormatMark();
		int len = content.length;

		byte[] ret = new byte[8 + len];
		byte[] tagBytes = numberCodec.int2Bytes(tag, 4);
		byte[] lenBytes = numberCodec.int2Bytes(len, 4);

		System.arraycopy(tagBytes, 0, ret, 0, 4);
		System.arraycopy(lenBytes, 0, ret, 4, 4);
		System.arraycopy(content, 0, ret, 8, len);
		return ret;
	}

	public boolean isRequireCutContent() {
		return this.contentRange.isRequireCutContent();
	}

	public byte[] getExactContent(byte[] content) {
		return this.contentRange.getExactContent(content);
	}

	// TODO:reserved
	public boolean isULPRange() {
		return (stateFlagSet & STATE_FLAG_ULP_RANGE) == STATE_FLAG_ULP_RANGE;
	}

	public short getContentFlagsCount() {
		return (short) contentFlags.size();
	}

	private void addContentFlag(String contentFlag) {
		if (StringUtils.isBlank(contentFlag))
			throw new IllegalArgumentException("contentFlag is blank.");
		if (contentFlags.size() >= Short.MAX_VALUE) {
			if (LOGGER.isWarnEnabled())
				LOGGER.warn("contentFlags.size():" + contentFlags.size() + ",ignore.");
			return;
		}
		contentFlags.add(contentFlag);
	}

	public List<String> getContentFlags() {
		return Collections.unmodifiableList(contentFlags);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

	public static final class Builder {
		private final ExtendedItemType type;

		private byte stateFlagSet = (byte) 0x00;
		private int formatMark = -1;
		private int shardingMark = 0;
		private int mergeOffset = -1;
		private final List<String> contentFlags = Lists.newArrayList();
		private ContentRange contentRange = ContentRange.createFullRange();

		public Builder(ExtendedItemType type) {
			if (null == type)
				throw new NullPointerException("type");
			this.type = type;
		}

		public Builder setStateFlagSet(byte stateFlagSet) {
			this.stateFlagSet = stateFlagSet;
			return this;
		}

		public Builder setFormatMark(int formatMark) {
			this.formatMark = formatMark;
			return this;
		}

		public Builder setShardingMark(int shardingMark) {
			this.shardingMark = shardingMark;
			return this;
		}

		public Builder setMergeOffset(int mergeOffset) {
			this.mergeOffset = mergeOffset;
			return this;
		}

		public Builder setContentRange(ContentRange contentRange) {
			this.contentRange = contentRange;
			return this;
		}

		public Builder setRequiredWrap(boolean requiredWrap) {
			if (requiredWrap) {
				stateFlagSet |= STATE_FLAG_REQUIRED_WRAP;
			} else {
				stateFlagSet &= (~STATE_FLAG_REQUIRED_WRAP);
			}
			return this;
		}

		public Builder setULPRange(boolean ulpRange) {
			if (ulpRange) {
				stateFlagSet |= STATE_FLAG_ULP_RANGE;
			} else {
				stateFlagSet &= (~STATE_FLAG_ULP_RANGE);
			}
			return this;
		}

		public Builder addContentFlag(String contentFlag) {
			if (StringUtils.isBlank(contentFlag))
				throw new IllegalArgumentException("contentFlag is blank.");
			if (contentFlags.size() >= Short.MAX_VALUE) {
				throw new RuntimeException("contentFlags.size():" + contentFlags.size());
			}
			contentFlags.add(contentFlag);
			return this;
		}

		public ExtendedItem build() {
			ExtendedItem ret = new ExtendedItem(type);
			ret.setStateFlagSet(stateFlagSet);
			ret.setFormatMark(formatMark);
			ret.setShardingMark(shardingMark);
			ret.setMergeOffset(mergeOffset);
			ret.setContentRange(contentRange);

			for (String flag : contentFlags) {
				ret.addContentFlag(flag);
			}

			return ret;
		}
	}

	@Override
	public int compareTo(ExtendedItem o) {
		if (null == o)
			throw new NullPointerException("input ExtendedItem");

		if (o.getMergeOffset() == -1 && this.getMergeOffset() > -1)
			return -1;
		else if (o.getMergeOffset() > -1 && this.getMergeOffset() == -1)
			return 1;
		else
			return getMergeOffset() - o.getMergeOffset();
	}
}
