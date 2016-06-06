package org.bigmouth.nvwa.sap.codec;

import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.statemachine.DecodingState;
import org.apache.mina.filter.codec.statemachine.DecodingStateMachine;
import org.apache.mina.filter.codec.statemachine.FixedLengthDecodingState;
import org.bigmouth.nvwa.sap.ContentRange;
import org.bigmouth.nvwa.sap.ExtendedItem;
import org.bigmouth.nvwa.sap.ExtendedItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

abstract class ExtendedItemDecodingState extends DecodingStateMachine {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtendedItemDecodingState.class);
	private static final int CONTENT_FLAG_LENGTH = 32;

	private short itemCount;
	private List<ExtendedItem> items;

	public ExtendedItemDecodingState(short itemCount) {
		if (0 >= itemCount)
			throw new IllegalArgumentException("itemCount:" + itemCount);
		this.itemCount = itemCount;
	}

	@Override
	protected DecodingState init() throws Exception {
		items = Lists.newArrayList();
		return READ_DOWNLOAD_ITEMS_LOOP;
	}

	@Override
	protected void destroy() throws Exception {
		items = null;
		itemCount = 0;
	}

	private final DecodingState READ_DOWNLOAD_ITEMS_LOOP = new DecodingState() {

		@Override
		public DecodingState decode(IoBuffer product, ProtocolDecoderOutput out) throws Exception {
			if (!product.hasRemaining()) {
				throw new SapDecoderException("expect download item count.");
			}

			while (items.size() < itemCount) {
				return READ_EXTENDED_ITEM;
			}
			out.write(items);
			return null;
		}

		@Override
		public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
			return null;
		}
	};

	private final DecodingState READ_EXTENDED_ITEM = new FixedLengthDecodingState(32) {

		@Override
		protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out)
				throws Exception {

			// item type
			ExtendedItemType itemType = ExtendedItemType.forCode(product.get());
			// state flag set
			byte stateFlags = SapCodecUtils.readByte(product);
			// content flag count
			final short contentFlagCount = SapCodecUtils.readShort(product);
			if (contentFlagCount <= 0) {
				// TODO:
				throw new SapDecoderException("contentFlagCount:" + contentFlagCount);
			}
			// format mark
			int formatMark = SapCodecUtils.readInt(product);
			// sharding mark
			int shardingMark = SapCodecUtils.readInt(product);
			// content merge offset
			int mergeOffset = SapCodecUtils.readInt(product);
			// content range
			ContentRange contentRange = decodeContentRange(product);

			final ExtendedItem.Builder extendedItemBuilder = new ExtendedItem.Builder(itemType)
					.setStateFlagSet(stateFlags).setFormatMark(formatMark)
					.setShardingMark(shardingMark).setMergeOffset(mergeOffset)
					.setContentRange(contentRange);

			// content flag
			return new FixedLengthDecodingState(CONTENT_FLAG_LENGTH * contentFlagCount) {

				@Override
				protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out)
						throws Exception {
					for (int i = 0; i < contentFlagCount; i++) {
						String contentFlag = SapCodecUtils.readString(product, CONTENT_FLAG_LENGTH);
						extendedItemBuilder.addContentFlag(contentFlag);
					}
					ExtendedItem extendedItem = extendedItemBuilder.build();
					items.add(extendedItem);
					if (LOGGER.isDebugEnabled())
						LOGGER.debug("Decoding ExtendedItem:" + extendedItem);

					return READ_DOWNLOAD_ITEMS_LOOP;
				}

			};
		}

		private ContentRange decodeContentRange(IoBuffer product) {
			// global begin offset
			int globalBeginOffset = SapCodecUtils.readInt(product);
			// relative begin offset
			int relativeBeginOffset = SapCodecUtils.readInt(product);
			// data length
			int dataLength = SapCodecUtils.readInt(product);
			// data total length
			int dataTotalLength = SapCodecUtils.readInt(product);

			return ContentRange.create(globalBeginOffset, relativeBeginOffset, dataLength,
					dataTotalLength);
		}
	};
}
