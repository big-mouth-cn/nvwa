package org.bigmouth.nvwa.codec.tlv;

import org.bigmouth.nvwa.codec.CodecProvider;
import org.bigmouth.nvwa.codec.byteorder.ByteOrder;

public interface TLVDecoderProvider extends CodecProvider<TLVDecoder<?>> {

	public TLVConfig getTlvConfig();

	public ByteOrder getByteOrder();

	public void setByteOrder(ByteOrder byteOrder);

	public int getIntByteLen();

	public void setIntByteLen(int byteLen);

	public TLVDecoder<Object> getObjectDecoder();
	
	public String getCharset();

}
