package org.bigmouth.nvwa.sap.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.statemachine.DecodingState;
import org.apache.mina.filter.codec.statemachine.DecodingStateMachine;
import org.apache.mina.filter.codec.statemachine.FixedLengthDecodingState;
import org.bigmouth.nvwa.sap.ContentType;
import org.bigmouth.nvwa.sap.DefaultSapMessage;
import org.bigmouth.nvwa.sap.MessageType;
import org.bigmouth.nvwa.sap.MutableSapMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


abstract class SapHeaderCommonDecodingState extends DecodingStateMachine {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SapHeaderCommonDecodingState.class);

	private MutableSapMessage sapMessage;

	@Override
	protected DecodingState init() throws Exception {
		sapMessage = new DefaultSapMessage();
		return READ_PROTOCOL_ID;
	}

	@Override
	protected void destroy() throws Exception {
		sapMessage = null;
	}

	final DecodingState READ_PROTOCOL_ID = new FixedLengthDecodingState(4) {

		@Override
		protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out)
				throws Exception {
			if (!product.hasRemaining()) {
				throw new SapDecoderException("expect protocol id data.");
			}

			byte[] v = new byte[4];
			product.get(v);
			if (!SapCodecUtils.isValidProtocol(v)) {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("unkown request,ignore.");
				return READ_PROTOCOL_ID;
			} else {
				// TODO:
				return READ_PROTOCOL_METADATA;
			}
		}
	};

	private final DecodingState READ_PROTOCOL_METADATA = new FixedLengthDecodingState(4) {

		@Override
		protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out)
				throws Exception {
			if (!product.hasRemaining()) {
				throw new SapDecoderException("expect protocol meta data.");
			}

			try {
				byte pv = product.get();
				sapMessage.setProtocolVer(pv);

				MessageType mt = MessageType.forCode(product.get());
				sapMessage.setMessageType(mt);

				ContentType ct = ContentType.forCode(product.get());
				sapMessage.setContentType(ct);

				byte contentVer = product.get();
				sapMessage.setContentVer(contentVer);

			} catch (Exception e) {
				throw new SapDecoderException("READ_PROTOCOL_METADATA:", e);
			}

			out.write(sapMessage);
			return null;
		}
	};
}
