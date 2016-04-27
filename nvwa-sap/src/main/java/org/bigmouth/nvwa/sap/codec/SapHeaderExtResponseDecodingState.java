package org.bigmouth.nvwa.sap.codec;

import java.util.List;
import java.util.UUID;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.statemachine.DecodingState;
import org.apache.mina.filter.codec.statemachine.DecodingStateMachine;
import org.apache.mina.filter.codec.statemachine.FixedLengthDecodingState;
import org.bigmouth.nvwa.sap.ExtendedItem;
import org.bigmouth.nvwa.sap.MutableSapResponse;
import org.bigmouth.nvwa.sap.SapResponseStatus;


abstract class SapHeaderExtResponseDecodingState extends DecodingStateMachine {

	private final MutableSapResponse sapResponse;

	SapHeaderExtResponseDecodingState(MutableSapResponse sapResponse) {
		if (null == sapResponse)
			throw new NullPointerException("sapResponse");
		this.sapResponse = sapResponse;
	}

	@Override
	protected DecodingState init() throws Exception {
		return READ_TRANSACTION_ID;
	}

	@Override
	protected void destroy() throws Exception {
	}

	private final DecodingState READ_TRANSACTION_ID = new SapHeaderExtTransactionDecodingState() {

		@Override
		protected DecodingState finishDecode(UUID tid, ProtocolDecoderOutput out) {
			sapResponse.setIdentification(tid);
			return READ_SAP_RESPONSE_STATUS;
		}
	};

	/**
	 * TODO:when occur any error,then enter this state.
	 */
	// private final DecodingState READ_PROTOCOL_ID =
	// READ_SAP_MESSAGE_HEADER.READ_PROTOCOL_ID;
	private final DecodingState READ_SAP_RESPONSE_STATUS = new FixedLengthDecodingState(4) {

		@Override
		protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out)
				throws Exception {
			// status
			short status = SapCodecUtils.readShort(product);
			sapResponse.setStatus(SapResponseStatus.forCode(status));

			// download item count
			short extendedItemCount = SapCodecUtils.readShort(product);
			if (extendedItemCount > 0) {
				return new ExtendedItemDecodingState(extendedItemCount) {

					@SuppressWarnings("unchecked")
					@Override
					protected DecodingState finishDecode(List<Object> childProducts,
							ProtocolDecoderOutput out) throws Exception {

						List<ExtendedItem> items = (List<ExtendedItem>) childProducts.get(0);
						sapResponse.addExtendedItems(items);
						return null;
					}

				};
			} else {
				return null;
			}
		}
	};
}
