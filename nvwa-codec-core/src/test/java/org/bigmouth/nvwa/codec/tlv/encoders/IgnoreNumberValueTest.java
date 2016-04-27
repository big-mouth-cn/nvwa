package org.bigmouth.nvwa.codec.tlv.encoders;

import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVCodecProviders;
import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.bean.IgnoreNumberValueBean;
import org.bigmouth.nvwa.codec.tlv.bean.NumberValueBean;
import org.bigmouth.nvwa.utils.ByteUtils;
import org.bigmouth.nvwa.utils.HexUtils;


public class IgnoreNumberValueTest {

	public static void main(String[] args) {
		TLVEncoder<Object> tlvEncoder = TLVCodecProviders.newBigEndianTLVEncoderProvider()
				.getObjectEncoder();

		NumberValueBean bean0 = new NumberValueBean();
		bean0.setName("aa");

		List<byte[]> bytesList = tlvEncoder.codec(bean0, null);
		byte[] ret0 = ByteUtils.union(bytesList);
		HexUtils.printAsHexCodes(ret0, 1024);

		// //
		IgnoreNumberValueBean bean1 = new IgnoreNumberValueBean();
		bean1.setName("aa");

		bytesList = tlvEncoder.codec(bean1, null);
		byte[] ret1 = ByteUtils.union(bytesList);
		HexUtils.printAsHexCodes(ret1, 1024);
	}
}
