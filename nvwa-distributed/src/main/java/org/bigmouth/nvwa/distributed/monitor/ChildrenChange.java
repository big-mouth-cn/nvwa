package org.bigmouth.nvwa.distributed.monitor;


public interface ChildrenChange {

    void add(String path, byte[] data);
    
    void update(String path, byte[] data);
    
    void remove(String path, byte[] data);
}
