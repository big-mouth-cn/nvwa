/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.asyncweb.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * Base message type of {@link HttpRequest} and {@link HttpResponse}.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public interface HttpMessage extends Serializable {

    /**
     * Returns the version of the protocol associated with this request.
     */
    HttpVersion getProtocolVersion();

    /**
     * Gets the <tt>Content-Type</tt> header of the message.
     * @return The content type.
     */
    String getContentType();

    /**
     * Returns <tt>true</tt> if this message enables keep-alive connection.
     */
    boolean isKeepAlive();

    /**
     * Returns the value of the HTTP header with the specified name.
     * If more than one header with the given name is associated with
     * this request, one is selected and returned.
     *
     * @param name  The name of the desired header
     * @return      The header value - or null if no header is found
     *              with the specified name
     */
    String getHeader(String name);

    /**
     * Returns <tt>true</tt> if the HTTP header with the specified name exists in this request.
     */
    boolean containsHeader(String name);

    /**
     * Returns a read-only {@link Map} of HTTP headers whose key is a {@link String} and whose value
     * is a {@link List} of {@link String}s.
     */
    Map<String, List<String>> getHeaders();

    /**
     * Returns a read-only {@link Map} of cookies whose key is a {@link String} and whose value is
     * a {@link Cookie}.
     */
    Set<Cookie> getCookies();

    /**
     * Returns the content of the request body.
     * @return the data contained in the message in the form of a buffer of bytes
     */
    IoBuffer getContent();
}