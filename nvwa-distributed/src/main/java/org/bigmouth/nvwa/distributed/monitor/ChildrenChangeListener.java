package org.bigmouth.nvwa.distributed.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ChildrenChangeListener implements ChangeListener<ChildrenChangeEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChildrenChangeListener.class);

    private final ChildrenChange childrenChange;

    public ChildrenChangeListener(ChildrenChange childrenChange) {
        if (null == childrenChange)
            throw new NullPointerException("childrenChange");
        this.childrenChange = childrenChange;
    }

    @Override
    public void onChanged(ChildrenChangeEvent event) {
        String path = event.getPath();
        byte[] data = event.getData();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("ZooKeeper: " + event.getEventType() + " Path " + path);
        }
        switch (event.getEventType()) {
            case CHILD_ADDED: {
                childrenChange.add(path, data);
                break;
            }
            case CHILD_UPDATED: {
                childrenChange.update(path, data);
                break;
            }
            case CHILD_REMOVED: {
                childrenChange.remove(path, data);
                break;
            }
        }
    }
}
