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
package org.bigmouth.nvwa.zookeeper.observers.exceptions;


/**
 * 
 * @author Allen Hu 
 * 2015-8-2
 */
public class CharacterException extends ObserverException {

    private static final long serialVersionUID = 2460101812712988770L;

    /**
     * 
     */
    public CharacterException() {
    }

    /**
     * @param message
     */
    public CharacterException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public CharacterException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public CharacterException(String message, Throwable cause) {
        super(message, cause);
    }

}
