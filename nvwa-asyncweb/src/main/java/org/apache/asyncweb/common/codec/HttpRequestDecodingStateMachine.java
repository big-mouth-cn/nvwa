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

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.asyncweb.common.DefaultHttpRequest;
import org.apache.asyncweb.common.HttpHeaderConstants;
import org.apache.asyncweb.common.HttpMethod;
import org.apache.asyncweb.common.HttpRequest;
import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.asyncweb.common.HttpVersion;
import org.apache.asyncweb.common.MutableHttpRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.statemachine.CrLfDecodingState;
import org.apache.mina.filter.codec.statemachine.DecodingState;
import org.apache.mina.filter.codec.statemachine.DecodingStateMachine;
import org.apache.mina.filter.codec.statemachine.FixedLengthDecodingState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses HTTP requests.
 * Clients should register a <code>HttpRequestParserListener</code>
 * in order to receive notifications at important stages of request
 * building.<br/>
 *
 * <code>HttpRequestParser</code>s should not be built for each request
 * as each parser constructs an underlying state machine which is
 * relatively costly to build.<br/> Instead, parsers should be pooled.<br/>
 *
 * Note, however, that a parser <i>must</i> be <code>prepare</code>d before
 * each new parse.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
abstract class HttpRequestDecodingStateMachine extends DecodingStateMachine {

    private static final Logger LOG = LoggerFactory
            .getLogger(HttpRequestDecodingStateMachine.class);

    /**
     * The request we are building
     */
    private MutableHttpRequest request;

    @Override
    protected DecodingState init() throws Exception {
        request = new DefaultHttpRequest();
        return SKIP_EMPTY_LINES;
    }

    @Override
    protected void destroy() throws Exception {
        request = null;
    }

    private final DecodingState SKIP_EMPTY_LINES = new CrLfDecodingState() {

        @Override
        protected DecodingState finishDecode(boolean foundCRLF,
                ProtocolDecoderOutput out) throws Exception {
            if (foundCRLF) {
                return this;
            } else {
                return READ_REQUEST_LINE;
            }
        }
    };

    private final DecodingState READ_REQUEST_LINE = new HttpRequestLineDecodingState() {
        @Override
        protected DecodingState finishDecode(List<Object> childProducts,
                ProtocolDecoderOutput out) throws Exception {
            URI requestUri = (URI) childProducts.get(1);
            request.setMethod((HttpMethod) childProducts.get(0));
            request.setRequestUri(requestUri);
            request.setProtocolVersion((HttpVersion) childProducts.get(2));
            request.setParameters(requestUri.getRawQuery());
            
            //TODO:
            READ_HEADERS.setHttpRequest(request);
            return READ_HEADERS;
        }
    };

    private final HttpHeaderDecodingState READ_HEADERS = new HttpHeaderDecodingState() {
        @Override
        @SuppressWarnings("unchecked")
        protected DecodingState finishDecode(List<Object> childProducts,
                ProtocolDecoderOutput out) throws Exception {
            Map<String, List<String>> headers =
                (Map<String, List<String>>) childProducts.get(0);
            
            // Set cookies.
            List<String> cookies = headers.get(
                    HttpHeaderConstants.KEY_COOKIE);
            if (cookies != null && !cookies.isEmpty()) {
                if (cookies.size() > 1) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Ignoring extra cookie headers: "
                                + cookies.subList(1, cookies.size()));
                    }
                }
                request.setCookies(cookies.get(0));
            }

            // Set headers.
            request.setHeaders(headers);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Decoded header: " + request.getHeaders());
            }

            // Select appropriate body decoding state.
            boolean isChunked = false;
            
            if (request.getProtocolVersion() == HttpVersion.HTTP_1_1) {
                LOG.debug("Request is HTTP 1/1. Checking for transfer coding");
              
                //TODO:临时方案，解决线网chunked问题
                isChunked = isChunked_0(request);
//                isChunked = isChunked(request);
            } else {
                LOG.debug("Request is not HTTP 1/1. Using content length");
            }
            DecodingState nextState;
            if (isChunked) {
				LOG.debug("Using chunked decoder for request");
				// TODO:refuse all request which not exists Content-Length,return HTTP 200 and BIZCODE 2000.
				DefaultHttpRequest r = new DefaultHttpRequest();
				r.setRequestUri(new URI("/fortest/killchunked"));
				r.setKeepAlive(false);
				r.setContent(IoBuffer.wrap(new byte[0]));

				out.write(r);

				return null;
//                nextState = new ChunkedBodyDecodingState() {
//                    @Override
//                    protected DecodingState finishDecode(
//                            List<Object> childProducts,
//                            ProtocolDecoderOutput out) throws Exception {
//                        if (childProducts.size() != 1) {
//                            int chunkSize = 0;
//                            for (Object product : childProducts) {
//                                IoBuffer chunk = (IoBuffer) product;
//                                chunkSize += chunk.remaining();
//                            }
//
//                            IoBuffer body = IoBuffer.allocate(chunkSize);
//                            for (Object product : childProducts) {
//                                IoBuffer chunk = (IoBuffer) product;
//                                body.put(chunk);
//                            }
//                            body.flip();
//                            request.setContent(body);
//                        } else {
//                            request.setContent((IoBuffer) childProducts.get(0));
//                        }
//
//                        out.write(request);
//                        return null;
//                    }
//                };
            } else {
                int length = getContentLength(request);
                if (length > 0) {
                    if (LOG.isDebugEnabled()) {
                        LOG
                                .debug("Using fixed length decoder for request with length "
                                        + length);
                    }
                    nextState = new FixedLengthDecodingState(length) {
                        @Override
                        protected DecodingState finishDecode(IoBuffer readData,
                                ProtocolDecoderOutput out) throws Exception {
                            request.setContent(readData);
                            out.write(request);
                            return null;
                        }
                    };
                } else {
                    LOG.debug("No entity body for this request");
                    out.write(request);
                    nextState = null;
                }
            }
            return nextState;
        }

        /**
         * Obtains the content length from the specified request
         *
         * @param request  The request
         * @return         The content length, or 0 if not specified
         * @throws HttpDecoderException If an invalid content length is specified
         */
        private int getContentLength(HttpRequest request)
                throws ProtocolDecoderException {
            int length = 0;
            String lengthValue = request.getHeader(
                    HttpHeaderConstants.KEY_CONTENT_LENGTH);
            if (lengthValue != null) {
                try {
                    length = Integer.parseInt(lengthValue);
                } catch (NumberFormatException e) {
                    HttpCodecUtils.throwDecoderException(
                            "Invalid content length: " + length,
                            HttpResponseStatus.BAD_REQUEST);
                }
            }
            return length;
        }

        /**
         * Determines whether a specified request employs a chunked
         * transfer coding
         *
         * @param request  The request
         * @return         <code>true</code> iff the request employs a
         *                 chunked transfer coding
         * @throws HttpDecoderException
         *                 If the request employs an unsupported coding
         */
        private boolean isChunked(HttpRequest request)
                throws ProtocolDecoderException {
            boolean isChunked = false;
            String coding = request.getHeader(
                    HttpHeaderConstants.KEY_TRANSFER_ENCODING);
            if (coding == null) {
                coding = request.getHeader(
                        HttpHeaderConstants.KEY_TRANSFER_CODING);
            }

            if (coding != null) {
                int extensionIndex = coding.indexOf(';');
                if (extensionIndex != -1) {
                    coding = coding.substring(0, extensionIndex);
                }
                if (HttpHeaderConstants.VALUE_CHUNKED.equalsIgnoreCase(coding)) {
                    isChunked = true;
                } else {
                    // As we only support chunked encoding, any other encoding
                    // is unsupported
                    HttpCodecUtils.throwDecoderException(
                            "Unknown transfer coding " + coding,
                            HttpResponseStatus.NOT_IMPLEMENTED);
                }
            }
            return isChunked;
        }
        
        
        /**
         * 临时方案
         * @param request
         * @return
         * @throws ProtocolDecoderException
         */
		private boolean isChunked_0(HttpRequest request) throws ProtocolDecoderException {
			/*-----BEGIN-----*/
			String contentLength = request.getHeader(HttpHeaderConstants.KEY_CONTENT_LENGTH);
			return (HttpMethod.POST == request.getMethod()) && (StringUtils.isBlank(contentLength));
		}
    };
}
