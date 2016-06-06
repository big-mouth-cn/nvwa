package org.bigmouth.nvwa.sap.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.sap.SapMessage;
import org.bigmouth.nvwa.sap.SapRequest;
import org.bigmouth.nvwa.sap.namecode.NameCodeMapper;
import org.bigmouth.nvwa.sap.namecode.NoSuchNameCodeMappingException;
import org.bigmouth.nvwa.sap.namecode.PlugInServiceCodePair;
import org.bigmouth.nvwa.sap.namecode.PlugInServiceNamePair;


final class SapRequestEncoder extends SapTransactionMessageEncoder {

	private final NameCodeMapper ncMapper;

	SapRequestEncoder(NameCodeMapper ncMapper) {
		if (null == ncMapper)
			throw new NullPointerException("ncMapper");
		this.ncMapper = ncMapper;
	}

	@Override
	protected void encodeHeaderExt(SapMessage sapMessage, IoBuffer buffer)
			throws SapEncodingException {
		super.encodeHeaderExt(sapMessage, buffer);
		if (!(sapMessage instanceof SapRequest))
			throw new SapEncodingException("sapMessage expect SapRequest,but " + sapMessage);
		SapRequest sapRequest = (SapRequest) sapMessage;
		encodeClientIp(sapRequest, buffer);
		encodeAccessAddress(sapRequest, buffer);

		encodeSourcePlugInServiceName(sapRequest, buffer);
		encodeTargetPlugInServiceName(sapRequest, buffer);

		encodeULPOffset(sapRequest, buffer);
	}

	private void encodeAccessAddress(SapRequest sapRequest, IoBuffer buffer) {
		buffer.put(sapRequest.getAccessAddress().toBytes());
	}

	private void encodeClientIp(SapRequest sapRequest, IoBuffer buffer) {
		buffer.put(sapRequest.getClientIp().toBytes());
	}

	private void encodeULPOffset(SapRequest sapRequest, IoBuffer buffer) {
		SapCodecUtils.writeInt(buffer, sapRequest.getULP_beginOffset());
		SapCodecUtils.writeInt(buffer, sapRequest.getULP_endOffset());
	}

	private void encodeTargetPlugInServiceName(SapRequest sapRequest, IoBuffer buffer)
			throws SapEncodingException {
		try {
			encodePlugInServiceName(sapRequest.getTargetPlugInServiceNamePair(), buffer);
		} catch (NoSuchNameCodeMappingException e) {
			throw new SapEncodingException("encodeTargetPlugInServiceName:", e);
		}
	}

	private void encodeSourcePlugInServiceName(SapRequest sapRequest, IoBuffer buffer)
			throws SapEncodingException {
		try {
			encodePlugInServiceName(sapRequest.getSourcePlugInServiceNamePair(), buffer);
		} catch (NoSuchNameCodeMappingException e) {
			throw new SapEncodingException("encodeSourcePlugInServiceName:", e);
		}
	}

	private void encodePlugInServiceName(PlugInServiceNamePair namePair, IoBuffer buffer)
			throws NoSuchNameCodeMappingException {
		PlugInServiceCodePair sourceCodePair = ncMapper.getCodeOf(namePair);

		byte[] plugInCodeBytes = SapCodecUtils.getNumberCodec().short2Bytes(
				sourceCodePair.getPlugInCode(), 2);
		byte[] serviceCodeBytes = SapCodecUtils.getNumberCodec().short2Bytes(
				sourceCodePair.getServiceCode(), 2);
		buffer.put(plugInCodeBytes);
		buffer.put(serviceCodeBytes);
	}
}
