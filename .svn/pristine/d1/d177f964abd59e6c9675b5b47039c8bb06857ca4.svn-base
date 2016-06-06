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
package org.apache.asyncweb.common.codec;


import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


/**
 * A codec factory which creates new encoders and decoders for HTTP requests
 * and responses, depending on the type of the IoService.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class HttpCodecFactory implements ProtocolCodecFactory
{
    /**
     * Gets an HttpResponseEncoder if the IoService is an acceptor for a
     * server, or an HttpRequestEncoder if the IoService is a connector for a
     * client.
     *
     * @see ProtocolCodecFactory#getEncoder(IoSession)
     */
    public ProtocolEncoder getEncoder( IoSession session ) throws Exception
    {
        if ( session.getService() instanceof IoAcceptor )
        {
            return new HttpResponseEncoder();
        }
        else
        {
            return new HttpRequestEncoder();
        }
    }


    /**
     * Gets an HttpRequestDecoder if the IoService is an acceptor for a
     * server, or an HttpResponseDecoder if the IoService is a connector for a
     * client.
     *
     * @see ProtocolCodecFactory#getDecoder(IoSession)
     */
    public ProtocolDecoder getDecoder( IoSession session ) throws Exception
    {
        if ( session.getService() instanceof IoAcceptor )
        {
            return new HttpRequestDecoder();
        }
        else
        {
            return new HttpResponseDecoder();
        }
    }
}
