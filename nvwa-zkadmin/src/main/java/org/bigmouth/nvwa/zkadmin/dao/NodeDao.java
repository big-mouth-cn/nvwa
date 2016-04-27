package org.bigmouth.nvwa.zkadmin.dao;

import java.util.Collections;
import java.util.List;

import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.data.Stat;
import org.bigmouth.nvwa.zkadmin.entity.Node;
import org.bigmouth.nvwa.zookeeper.ZkClientHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

@Repository
public class NodeDao {

    @Autowired
    private ZkClientHolder zkClientHolder;
    
    public List<String> getChildrens(String path) throws Exception {
        List<String> childrens = zkClientHolder.get().getChildren().forPath(path);
        Collections.sort(childrens);
        List<String> result = Lists.newArrayList();
        for (String string : childrens) {
            result.add(ZKPaths.makePath(path, string));
        }
        return result;
    }
    
    public Node getNode(String path) throws Exception {
        Node node = new Node();
        Stat stat = zkClientHolder.get().checkExists().forPath(path);
        if (null == stat) 
            return null;
        
        byte[] data = zkClientHolder.get().getData().forPath(path);
        List<String> childrens = getChildrens(path);
        
        node.setPath(path);
        node.setStat(stat);
        node.setData(data);
        node.setChildrens(childrens);
        
        return node;
    }
    
    public void save(String path, byte[] data) throws Exception {
        Stat stat = zkClientHolder.get().checkExists().forPath(path);
        if (null == stat) 
            zkClientHolder.get().create().creatingParentsIfNeeded().forPath(path, data);
        else 
            zkClientHolder.get().setData().forPath(path, data);
    }
    
    public void remove(String path) throws Exception {
        zkClientHolder.get().delete().deletingChildrenIfNeeded().forPath(path);
    }

    public void setZkClientHolder(ZkClientHolder zkClientHolder) {
        this.zkClientHolder = zkClientHolder;
    }
}
