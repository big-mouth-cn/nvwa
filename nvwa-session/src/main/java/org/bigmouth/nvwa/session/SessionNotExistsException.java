/*
 * Copyright 2015 mopote.com
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
package org.bigmouth.nvwa.session;

/**
 * Session already not exists or timeout.
 * 
 * @author Allen Hu - (big-mouth.cn) 2015-7-9
 */
public class SessionNotExistsException extends RuntimeException {

    private static final long serialVersionUID = 8804484388502420351L;

    private Trackable trackable;

    /**
     * 
     */
    public SessionNotExistsException() {
        super();
    }

    /**
     * @param trackable
     */
    public SessionNotExistsException(Trackable trackable) {
        super();
        this.trackable = trackable;
    }

    /**
     * @param message
     * @param cause
     */
    public SessionNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public SessionNotExistsException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public SessionNotExistsException(Throwable cause) {
        super(cause);
    }

    public Trackable getTrackable() {
        return trackable;
    }

    public void setTrackable(Trackable trackable) {
        this.trackable = trackable;
    }
}
