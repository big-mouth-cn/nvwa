package org.bigmouth.nvwa.codec.compress;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.bigmouth.nvwa.utils.ByteUtils;
import org.bigmouth.nvwa.utils.Transformer;


/**
 * Separate data.
 * 
 * @author nada
 * 
 */
public class SeparateFileAdapter implements Transformer<byte[], byte[]> {

	private TLVEncoderProvider tlvListEncoderProvider;

	private CompressSegmentFactory listSegmentFactory;

	@Override
	public byte[] transform(byte[] from) {
		if (null == from || from.length == 0)
			throw new IllegalArgumentException("from");
		return encode(from);
	}

	private byte[] encode(byte[] data) {
		List<byte[]> compressDataList = new ArrayList<byte[]>();
		TLVEncoder<Object> objectTLVEncoder = tlvListEncoderProvider.getObjectEncoder();
		List<CompressSegment> listSegments = listSegmentFactory.createListSegments(data);
		for (CompressSegment c : listSegments) {
			List<byte[]> bytes = objectTLVEncoder.codec(c, null);
			compressDataList.addAll(bytes);
		}
		byte[] compressData = ByteUtils.union(compressDataList);
		return compressData;
	}

	public void setTlvListEncoderProvider(TLVEncoderProvider tlvListEncoderProvider) {
		this.tlvListEncoderProvider = tlvListEncoderProvider;
	}

	public void setListSegmentFactory(CompressSegmentFactory listSegmentFactory) {
		this.listSegmentFactory = listSegmentFactory;
	}
}
