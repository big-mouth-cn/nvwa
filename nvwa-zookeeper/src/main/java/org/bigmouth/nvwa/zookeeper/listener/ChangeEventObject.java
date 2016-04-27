/*
 * Copyright 2015 big-mouth.cn
 *
 * The Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.bigmouth.nvwa.zookeeper.listener;

import java.util.EventObject;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * @author Allen Hu 2015-6-1
 */
public class ChangeEventObject extends EventObject {

    private static final long serialVersionUID = 3947211309107126429L;

    public enum EventType {
        CHILD_ADDED, CHILD_REMOVED, CHILD_UPDATED;
    }

    private final EventType eventType;
    private final byte[] data;

    public static ChangeEventObject of(String path, EventType eventType, byte[] data) {
        return new ChangeEventObject(path, eventType, data);
    }

    private ChangeEventObject(Object source, EventType eventType, byte[] data) {
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
