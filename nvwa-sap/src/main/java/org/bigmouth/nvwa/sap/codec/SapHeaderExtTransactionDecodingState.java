package org.bigmouth.nvwa.sap.codec;

import java.util.UUID;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.statemachine.DecodingState;
import org.apache.mina.filter.codec.statemachine.FixedLengthDecodingState;

abstract class SapHeaderExtTransactionDecodingState extends FixedLengthDecodingState {

	private final static int TRANSACTION_ID_LENGTH = 16;

	public SapHeaderExtTransactionDecodingState() {
		super(TRANSACTION_ID_LENGTH);
	}

	@Override
	protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out)
			throws Exception {
		if (!product.hasRemaining()) {
			throw new SapDecoderException("expect transaction id.");
		}

		byte[] bytes = new byte[TRANSACTION_ID_LENGTH];
		product.get(bytes);
		UUID tid = SapCodecUtils.decodeUUID(bytes);

		return finishDecode(tid, out);
	}

	protected abstract DecodingState finishDecode(UUID tid, ProtocolDecoderOutput out);
}
