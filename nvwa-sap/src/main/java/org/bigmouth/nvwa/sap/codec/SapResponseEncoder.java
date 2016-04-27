package org.bigmouth.nvwa.sap.codec;

import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.sap.ExtendedItem;
import org.bigmouth.nvwa.sap.SapMessage;
import org.bigmouth.nvwa.sap.SapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


final class SapResponseEncoder extends SapTransactionMessageEncoder {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SapResponseEncoder.class);

	@Override
	protected void encodeHeaderExt(SapMessage sapMessage, IoBuffer buffer)
			throws SapEncodingException {
		super.encodeHeaderExt(sapMessage, buffer);
		if (!(sapMessage instanceof SapResponse))
			throw new SapEncodingException("sapMessage expect SapResponse,but " + sapMessage);
		SapResponse sapResponse = (SapResponse) sapMessage;

		// status code
		SapCodecUtils.writeShort(buffer, sapResponse.getStatus().getCode());

		// content flag count
		List<ExtendedItem> items = sapResponse.getExtendedItems();
		if (items.size() > Short.MAX_VALUE) {
			throw new SapEncodingException("Illegal DownloadItem size:" + items.size());
		}
		short itemSize = (short) items.size();
		SapCodecUtils.writeShort(buffer, itemSize);

		if (0 < itemSize) {
			for (ExtendedItem item : items) {
				encodeExtendedItem(item, buffer);
			}
		}
	}

	private void encodeExtendedItem(ExtendedItem item, IoBuffer buffer) throws SapEncodingException {
		// type
		SapCodecUtils.writeByte(buffer, item.getType().code());
		// state flags
		SapCodecUtils.writeByte(buffer, item.getStateFlagSet());
		// content flag count
		short countFlagsCount = item.getContentFlagsCount();
		if (countFlagsCount <= 0) {
			// TODO:
			throw new SapEncodingException("countFlagsCount:" + countFlagsCount + ",ignore.");
		}
		SapCodecUtils.writeShort(buffer, countFlagsCount);
		// format mark
		SapCodecUtils.writeInt(buffer, item.getFormatMark());
		// sharding mark
		SapCodecUtils.writeInt(buffer, item.getShardingMark());
		// content merge offset
		SapCodecUtils.writeInt(buffer, item.getMergeOffset());
		// global begin offset
		SapCodecUtils.writeInt(buffer, item.getContentRange().getGlobalBeginOffset());
		// relative begin offset
		SapCodecUtils.writeInt(buffer, item.getContentRange().getRelativeBeginOffset());
		// data length
		SapCodecUtils.writeInt(buffer, item.getContentRange().getLength());
		// data total length
		SapCodecUtils.writeInt(buffer, item.getContentRange().getTotalLength());
		// content flag set
		for (String contentFlag : item.getContentFlags()) {
			SapCodecUtils.writeString(buffer, contentFlag);
		}
	}
}
