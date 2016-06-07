package org.bigmouth.nvwa.cluster.node;

import java.io.IOException;


public interface Wrapper {

    byte[] getData(String path);
    
    byte[] getData(String path, byte[] defaultValue);
    
    boolean setData(String path, byte[] value);
    
    byte[] registerDataChangeListener(String path, DataChangeListener listener);
    
    void init() throws IOException;
    
    void destroy();
}
