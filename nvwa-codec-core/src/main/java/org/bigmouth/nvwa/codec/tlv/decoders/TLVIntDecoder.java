package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.byteorder.ByteOrder;
import org.bigmouth.nvwa.codec.byteorder.NumberCodec;
import org.bigmouth.nvwa.codec.byteorder.NumberCodecFactory;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;

public class TLVIntDecoder extends AbstractTLVDecoder<Integer> {

	private static final String[] DECODER_KEYES = new String[] { int.class.getName(),
			Integer.class.getName() };

	private TLVDecoderProvider decoderProvider;

	private NumberCodec numberCodec;

	@Override
	public Integer codec(byte[] source, Object additionInfo) {
		if (null == decoderProvider || null == numberCodec)
			throw new IllegalStateException("TLVIntDecoder must be registered to decodeContext.");

		if (null == source)
			return 0;

		// not ignore meta data.

		return numberCodec.bytes2Int(source, decoderProvider.getIntByteLen());
	}

	@Override
	public TLVDecoderProvider getCodecProvider() {
		return decoderProvider;
	}

	@Override
	public String[] getCodecKeyes() {
		return DECODER_KEYES;
	}

	@Override
	public void setCodecProvider(TLVDecoderProvider decoderProvider) {
		this.decoderProvider = decoderProvider;
		ByteOrder byteOrder = decoderProvider.getByteOrder();
		this.numberCodec = NumberCodecFactory.fetchNumberCodec(byteOrder);
	}

	@Override
	public boolean isObjectDecoder() {
		return false;
	}

}
