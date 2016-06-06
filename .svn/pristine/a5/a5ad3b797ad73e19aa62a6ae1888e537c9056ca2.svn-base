package org.bigmouth.nvwa.distributed.monitor;

import java.util.EventObject;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class ChildrenChangeEvent extends EventObject {

    private static final long serialVersionUID = 4969547009590997132L;
    
    public enum EventType {
        CHILD_ADDED, CHILD_REMOVED, CHILD_UPDATED;
    }
    
    private final EventType eventType;
    private final byte[] data;
    
    public static ChildrenChangeEvent of(String path, EventType eventType, byte[] data) {
        return new ChildrenChangeEvent(path, eventType, data);
    }
    
    private ChildrenChangeEvent(Object source, EventType eventType, byte[] data) {
        super(source);
        if (!(source instanceof String))
            throw new IllegalArgumentException("path expect String,but " + source.getClass());
        this.eventType = eventType;
        this.data = data;
    }
    
    public String getPath() {
        return (String) getSource();
    }

    public EventType getEventType() {
        return eventType;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
