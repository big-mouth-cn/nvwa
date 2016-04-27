package org.bigmouth.nvwa.codec.tlv;

import java.util.List;

import org.bigmouth.nvwa.codec.Codec;


public interface TLVEncoder<T extends Object> extends
		Codec<T, List<byte[]>, TLVEncoderProvider, Object> {

}
