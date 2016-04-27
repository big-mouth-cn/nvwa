package org.bigmouth.nvwa.codec.tlv;

import org.bigmouth.nvwa.codec.CodecProvider;
import org.bigmouth.nvwa.codec.byteorder.ByteOrder;

public interface TLVEncoderProvider extends CodecProvider<TLVEncoder<?>> {

	public TLVConfig getTlvConfig();

	public int getIntByteLen();

	public void setIntByteLen(int byteLen);

	public ByteOrder getByteOrder();

	public void setByteOrder(ByteOrder byteOrder);

	public TLVEncoder<Object> getObjectEncoder();

}
