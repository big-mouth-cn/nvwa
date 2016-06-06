package org.bigmouth.nvwa.codec.jms;

import java.util.ArrayList;
import java.util.List;

import org.bigmouth.nvwa.codec.jms.MessageEncoder;
import org.bigmouth.nvwa.codec.jms.MessageEncoderImpl;
import org.bigmouth.nvwa.codec.jms.bean.TLVMessageBean;
import org.bigmouth.nvwa.codec.tlv.TLVCodecProviders;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.bigmouth.nvwa.utils.ByteUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class MessageEncoderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEncoder() {
		String sql = "insert test(id,name) values(?,?)";
		List<Object[]> arguments = new ArrayList<Object[]>();

		Object[] args1 = new Object[2];
		args1[0] = 1;
		args1[1] = "name1";
		arguments.add(args1);

		Object[] args2 = new Object[2];
		args2[0] = 2;
		args2[1] = "name2";
		arguments.add(args2);

		TLVMessageBean msgBean = new TLVMessageBean(sql, arguments);

		TLVEncoderProvider tlvEncoderProvider = TLVCodecProviders.newBigEndianTLVEncoderProvider();
		MessageEncoder messageEncoder = new MessageEncoderImpl(tlvEncoderProvider);

		byte[] bytes = messageEncoder.encode(msgBean);
		String s = ByteUtils.bytesAsHexString(bytes, 1000000);
		System.out.println(s);
	}

}
