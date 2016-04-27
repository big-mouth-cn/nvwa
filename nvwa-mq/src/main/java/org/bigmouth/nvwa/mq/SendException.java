/*
 * Copyright 2016 big-mouth.cn
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
package org.bigmouth.nvwa.mq;


public class SendException extends RuntimeException {

    private static final long serialVersionUID = 6484824315128060157L;

    public SendException() {
        super();
    }

    public SendException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendException(String message) {
        super(message);
    }

    public SendException(Throwable cause) {
        super(cause);
    }
}
