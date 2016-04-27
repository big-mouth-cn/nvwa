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

import org.apache.asyncweb.common.codec.HttpResponseDecoder;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolCodecSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.junit.Test;
import static org.junit.Assert.*;

public class HttpResponseDecoderTest {

    @Test
    public void testEmpyContentDecoding() throws Exception {
        HttpResponseDecoder decoder = new HttpResponseDecoder();
        ProtocolCodecSession session = new ProtocolCodecSession();
        ProtocolDecoderOutput output = session.getDecoderOutput();

        decoder.decode(session, IoBuffer.wrap("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n".getBytes()), output);
        decoder.finishDecode(session, output); // HttpResonse is produced and sent to DecoderOutput
        //decoder.dispose(session);
        assertTrue(session.getDecoderOutputQueue().size()!=0);
        System.out.println("content decoded : ["+session.getDecoderOutputQueue().poll()+"]");
        
    }
}
