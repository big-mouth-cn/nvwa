package org.bigmouth.nvwa.codec;

import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVCodecProviders;
import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.bigmouth.nvwa.utils.ByteUtils;


public class TlvTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TLVEncoderProvider tlvEncoderProvider = TLVCodecProviders.newBigEndianTLVEncoderProvider();
		TestBean bean = new TestBean();
		bean.setId(1);
		bean.setName("ok");
		
		TLVEncoder e = tlvEncoderProvider.getObjectEncoder();
		List<byte[]> result = (List<byte[]>)e.codec(bean, null);
		
		byte[] bytes = ByteUtils.union(result);
		
		String s = ByteUtils.bytesAsHexString(bytes, 1000000);
		
		System.out.println(s);
	}

}
