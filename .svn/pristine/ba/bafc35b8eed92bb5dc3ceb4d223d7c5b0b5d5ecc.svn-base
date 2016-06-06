package org.bigmouth.nvwa.zkadmin.entity;

import java.util.List;

import org.apache.zookeeper.data.Stat;
import org.bigmouth.nvwa.utils.StringHelper;


public class Node {

    private String path;
    private List<String> childrens;
    private byte[] data;
    private String data2String;
    private Stat stat;
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public List<String> getChildrens() {
        return childrens;
    }
    
    public void setChildrens(List<String> childrens) {
        this.childrens = childrens;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }
    
    public String getData2String() {
        if (null != data)
            data2String = StringHelper.convert(data);
        return data2String;
    }
    
    public void setData2String(String data2String) {
        this.data2String = data2String;
    }
    
    public Stat getStat() {
        return stat;
    }
    
    public void setStat(Stat stat) {
        this.stat = stat;
    }
}
