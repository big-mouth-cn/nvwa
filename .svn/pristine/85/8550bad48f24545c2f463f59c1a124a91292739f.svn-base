package org.bigmouth.nvwa.sap.codec;

import java.net.InetSocketAddress;
import java.util.UUID;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.statemachine.DecodingState;
import org.apache.mina.filter.codec.statemachine.DecodingStateMachine;
import org.apache.mina.filter.codec.statemachine.FixedLengthDecodingState;
import org.bigmouth.nvwa.sap.Ip;
import org.bigmouth.nvwa.sap.IpPort;
import org.bigmouth.nvwa.sap.MutableSapRequest;
import org.bigmouth.nvwa.sap.namecode.NameCodeMapper;
import org.bigmouth.nvwa.sap.namecode.NoSuchNameCodeMappingException;
import org.bigmouth.nvwa.sap.namecode.PlugInServiceCodePair;
import org.bigmouth.nvwa.sap.namecode.PlugInServiceNamePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


abstract class SapHeaderExtRequestDecodingState extends DecodingStateMachine {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SapHeaderExtRequestDecodingState.class);

	private final NameCodeMapper ncMapper;
	private final IoSession session;

	private final MutableSapRequest sapRequest;

	SapHeaderExtRequestDecodingState(NameCodeMapper ncMapper, IoSession session,
			MutableSapRequest sapRequest) {
		if (null == ncMapper)
			throw new NullPointerException("ncMapper");
		if (null == session)
			throw new NullPointerException("session");
		if (null == sapRequest)
			throw new NullPointerException("sapRequest");
		this.ncMapper = ncMapper;
		this.session = session;
		this.sapRequest = sapRequest;
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
			sapRequest.setIdentification(tid);
			return READ_PLUGIN_SERVICE_METADATA;
		}
	};

	private final DecodingState READ_PLUGIN_SERVICE_METADATA = new FixedLengthDecodingState(18) {

		@Override
		protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out)
				throws Exception {
			// client ip
			byte[] clientIpBytes = new byte[4];
			product.get(clientIpBytes);
			Ip clientIp = Ip.create(clientIpBytes);
			sapRequest.setClientIp(clientIp);

			// access ip port
			byte[] accessAddrBytes = new byte[6];
			product.get(accessAddrBytes);
			IpPort accessAddress = IpPort.create(accessAddrBytes);
			sapRequest.setAccessAddress(accessAddress);

			// set peerIp,localAddress
			InetSocketAddress peerAddress = (InetSocketAddress) session.getRemoteAddress();
			Ip peerIp = Ip.create(peerAddress.getAddress().getHostAddress());
			sapRequest.setPeerIp(peerIp);

			InetSocketAddress localAddress = (InetSocketAddress) session.getLocalAddress();
			IpPort _localAddress = IpPort.create(localAddress.getAddress().getHostAddress(),
					localAddress.getPort());
			sapRequest.setLocalAddress(_localAddress);

			// source plugin service
			final short srcPlugInCode = SapCodecUtils.readShort(product);
			final short srcServiceCode = SapCodecUtils.readShort(product);

			// target plugin service
			final short tgtPlugInCode = SapCodecUtils.readShort(product);
			final short tgtServiceCode = SapCodecUtils.readShort(product);

			// transform code to name
			PlugInServiceNamePair srcNamePair = decodeSourceNamePair(srcPlugInCode, srcServiceCode);
			sapRequest.setSourcePlugInServiceNamePair(srcNamePair);

			PlugInServiceNamePair tgtNamePair = decodeTargetNamePair(tgtPlugInCode, tgtServiceCode);
			sapRequest.setTargetPlugInServiceNameSource(tgtNamePair);

			return READ_ULP_RANGE;
		}

		private PlugInServiceNamePair decodeTargetNamePair(final short tgtPlugInCode,
				final short tgtServiceCode) {
			PlugInServiceNamePair tgtNamePair = null;
			try {
				tgtNamePair = ncMapper.getNameOf(new PlugInServiceCodePair() {

					@Override
					public short getPlugInCode() {
						return tgtPlugInCode;
					}

					@Override
					public short getServiceCode() {
						return tgtServiceCode;
					}

				});
			} catch (NoSuchNameCodeMappingException e) {
				// TODO:
				LOGGER.error("ncMapper.getNameOf:", e);
				tgtNamePair = new PlugInServiceNamePair() {

					@Override
					public String getPlugInName() {
						return "unkown";
					}

					@Override
					public String getServiceName() {
						return "unkown";
					}
				};
			}
			return tgtNamePair;
		}

		private PlugInServiceNamePair decodeSourceNamePair(final short srcPlugInCode,
				final short srcServiceCode) {
			PlugInServiceNamePair srcNamePair = null;
			try {
				srcNamePair = ncMapper.getNameOf(new PlugInServiceCodePair() {

					@Override
					public short getPlugInCode() {
						return srcPlugInCode;
					}

					@Override
					public short getServiceCode() {
						return srcServiceCode;
					}

				});
			} catch (NoSuchNameCodeMappingException e) {
				// TODO:
				LOGGER.error("ncMapper.getNameOf:", e);
				srcNamePair = new PlugInServiceNamePair() {

					@Override
					public String getPlugInName() {
						return "unkown";
					}

					@Override
					public String getServiceName() {
						return "unkown";
					}
				};
			}
			return srcNamePair;
		}
	};

	private final DecodingState READ_ULP_RANGE = new FixedLengthDecodingState(8) {

		@Override
		protected DecodingState finishDecode(IoBuffer product, ProtocolDecoderOutput out)
				throws Exception {

			int beginOffset = SapCodecUtils.readInt(product);
			sapRequest.setULP_beginOffset(beginOffset);

			int endOffset = SapCodecUtils.readInt(product);
			sapRequest.setULP_endOffset(endOffset);

			return null;
		}
	};
}
