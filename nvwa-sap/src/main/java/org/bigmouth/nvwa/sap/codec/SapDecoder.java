package org.bigmouth.nvwa.sap.codec;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.statemachine.DecodingState;
import org.apache.mina.filter.codec.statemachine.DecodingStateProtocolDecoder;
import org.bigmouth.nvwa.sap.namecode.NameCodeMapper;


public final class SapDecoder extends DecodingStateProtocolDecoder {

	public SapDecoder(NameCodeMapper ncMapper, IoSession session) {
		super(new SapDecodingStateMachine(ncMapper, session) {
			@Override
			protected DecodingState finishDecode(List<Object> childProducts,
					ProtocolDecoderOutput out) throws Exception {
				for (Object m : childProducts) {
					out.write(m);
				}
				return null;
			}
		});
	}
}
