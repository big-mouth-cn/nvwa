package org.bigmouth.nvwa.sap.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.statemachine.DecodingState;
import org.apache.mina.filter.codec.statemachine.DecodingStateMachine;
import org.apache.mina.filter.codec.statemachine.FixedLengthDecodingState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


abstract class SapContentDecodingState extends DecodingStateMachine {

	private static final Logger LOGGER = LoggerFactory.getLogger(SapContentDecodingState.class);

	private int contentLength;

	@Override
	protected DecodingState init() throws Exception {
		contentLength = 0;
		return READ_CONTENT_LENGTH;
	}

	@Override
	protected void destroy() throws Exception {
		contentLength = 0;
	}

	private final DecodingState READ_CONTENT_LENGTH = new FixedLengthDecodingState(4) {

		@Override
		protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out)
				throws Exception {
			if (!product.hasRemaining()) {
				throw new SapDecoderException("Expect content length.");
			}

			contentLength = SapCodecUtils.readInt(product);
			if (0 > contentLength) {
				throw new SapDecoderException("Illegal content length:" + contentLength);
			} else if (0 == contentLength) {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("No content for this message");
				out.write(IoBuffer.allocate(0));
				return null;
			} else {
				return READ_CONTENT;
			}
		}
	};

	private final DecodingState READ_CONTENT = new DecodingState() {

		@Override
		public DecodingState decode(IoBuffer product, ProtocolDecoderOutput out) throws Exception {
			if (!product.hasRemaining()) {
				throw new SapDecoderException("expect content data.");
			}

			return new FixedLengthDecodingState(contentLength) {

				@Override
				protected DecodingState finishDecode(IoBuffer readData, ProtocolDecoderOutput out)
						throws Exception {
					out.write(readData);
					return null;
				}
			};
		}

		@Override
		public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
			return null;
		}
	};
}
