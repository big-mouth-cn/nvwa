package org.bigmouth.nvwa.codec.byteorder;

import junit.framework.Assert;

import org.bigmouth.nvwa.codec.byteorder.ByteOrder;
import org.bigmouth.nvwa.codec.byteorder.NumberCodec;
import org.bigmouth.nvwa.codec.byteorder.NumberCodecFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NumberCodecTest {

	private NumberCodec littleNumberCodec;
	private NumberCodec bigNumberCodec;

	private short s0 = 100;
	private int i0 = 2000000;
	private long l0 = 4000000000000l;

	private short s1 = -10000;
	private int i1 = -2000000;
	private long l1 = -4000000000000l;

	@Before
	public void setUp() throws Exception {
		littleNumberCodec = NumberCodecFactory.fetchNumberCodec(ByteOrder.littleEndian);
		bigNumberCodec = NumberCodecFactory.fetchNumberCodec(ByteOrder.bigEndian);
	}

	@After
	public void tearDown() throws Exception {
		littleNumberCodec = null;
		bigNumberCodec = null;
	}

	@Test
	public void testLittleCodec() {
		//short
		Assert.assertEquals(littleNumberCodec.bytes2Short(littleNumberCodec.short2Bytes(s0, 2), 2), s0);
		Assert.assertEquals(littleNumberCodec.bytes2Short(littleNumberCodec.short2Bytes(s1, 2), 2), s1);
		
		//int
		Assert.assertEquals(littleNumberCodec.bytes2Int(littleNumberCodec.int2Bytes(i0, 4), 4), i0);
		Assert.assertEquals(littleNumberCodec.bytes2Int(littleNumberCodec.int2Bytes(i1, 4), 4), i1);
		
		//long
		Assert.assertEquals(littleNumberCodec.bytes2Long(littleNumberCodec.long2Bytes(l0, 8), 8), l0);
		Assert.assertEquals(littleNumberCodec.bytes2Long(littleNumberCodec.long2Bytes(l1, 8), 8), l1);
	}

	@Test
	public void testBigCodec() {
		//short
		Assert.assertEquals(bigNumberCodec.bytes2Short(bigNumberCodec.short2Bytes(s0, 2), 2), s0);
		Assert.assertEquals(bigNumberCodec.bytes2Short(bigNumberCodec.short2Bytes(s1, 2), 2), s1);
		
		//int
		Assert.assertEquals(bigNumberCodec.bytes2Int(bigNumberCodec.int2Bytes(i0, 4), 4), i0);
		Assert.assertEquals(bigNumberCodec.bytes2Int(bigNumberCodec.int2Bytes(i1, 4), 4), i1);
		
		//long
		Assert.assertEquals(bigNumberCodec.bytes2Long(bigNumberCodec.long2Bytes(l0, 8), 8), l0);
		Assert.assertEquals(bigNumberCodec.bytes2Long(bigNumberCodec.long2Bytes(l1, 8), 8), l1);
	}

}
