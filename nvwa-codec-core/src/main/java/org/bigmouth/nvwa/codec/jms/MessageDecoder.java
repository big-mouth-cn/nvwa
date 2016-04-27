package org.bigmouth.nvwa.codec.jms;

import org.bigmouth.nvwa.codec.jms.bean.TLVMessageBean;


public interface MessageDecoder {

	public TLVMessageBean decode(byte[] bytes);

}
