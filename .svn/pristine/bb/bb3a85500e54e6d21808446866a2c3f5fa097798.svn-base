package org.bigmouth.nvwa.codec.tlv.decoders;

import org.bigmouth.nvwa.codec.error.IllegalTLVContentHandler;
import org.bigmouth.nvwa.codec.tlv.TLVDecoder;


public abstract class AbstractTLVDecoder<R extends Object> implements TLVDecoder<R> {
    
    protected IllegalTLVContentHandler illegalTLVContentHandler;

    @Override
    public void setIllegalTLVContentHandler(IllegalTLVContentHandler illegalTLVContentHandler) {
        this.illegalTLVContentHandler = illegalTLVContentHandler;
    }
}
