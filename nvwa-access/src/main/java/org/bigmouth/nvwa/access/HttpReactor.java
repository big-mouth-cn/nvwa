package org.bigmouth.nvwa.access;

import java.util.Map;
import java.util.UUID;

import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.access.request.HttpRequestExt;
import org.bigmouth.nvwa.access.request.IllegalAccessProtocolException;
import org.bigmouth.nvwa.access.request.IllegalRequestHandler;
import org.bigmouth.nvwa.access.request.UnsupportHttpMethodException;
import org.bigmouth.nvwa.access.response.HttpResponseExt;
import org.bigmouth.nvwa.access.response.HttpResponseExtFactory;
import org.bigmouth.nvwa.access.response.HttpResponseSource;
import org.bigmouth.nvwa.sap.SapRequest;
import org.bigmouth.nvwa.sap.namecode.NameCodeMapper;
import org.bigmouth.nvwa.sap.namecode.NoSuchNameCodeMappingException;
import org.bigmouth.nvwa.transport.Replier;
import org.bigmouth.nvwa.transport.Sender;
import org.bigmouth.nvwa.utils.Closure;
import org.bigmouth.nvwa.utils.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpReactor implements Closure {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpReactor.class);
	private static final IllegalRequestHandler DEFAULT_ILLEGAL_REQ_HANDLER = IllegalRequestHandler.DEFAULT;

	private final Transformer<HttpRequestExt, SapRequest> http2SapRequestTransformer;
	private final Sender sender;

	private final Replier replier;
	private final HttpResponseExtFactory httpResponseExtFactory;
	private final IllegalRequestHandler illegalRequestHandler;

	private NameCodeMapper ncMapper;

	public HttpReactor(Transformer<HttpRequestExt, SapRequest> http2SapRequestTransformer,
			Sender sender, Replier replier, HttpResponseExtFactory httpResponseExtFactory) {
		this(http2SapRequestTransformer, sender, replier, httpResponseExtFactory,
				DEFAULT_ILLEGAL_REQ_HANDLER);
	}

	public HttpReactor(Transformer<HttpRequestExt, SapRequest> http2SapRequestTransformer,
			Sender sender, Replier replier, HttpResponseExtFactory httpResponseExtFactory,
			IllegalRequestHandler illegalRequestHandler) {
		this.http2SapRequestTransformer = http2SapRequestTransformer;
		this.sender = sender;
		this.replier = replier;
		this.httpResponseExtFactory = httpResponseExtFactory;
		this.illegalRequestHandler = illegalRequestHandler;
	}

	protected boolean filteRequest(HttpRequestExt httpRequest) {
		return true;
	}

	@Override
	public void execute(Object message) {
		if (!(message instanceof HttpRequestExt)) {
			throw new IllegalArgumentException("message expect HttpRequestExt,but "
					+ message.getClass());
		}

		HttpRequestExt httpRequest = (HttpRequestExt) message;
		boolean goon = filteRequest(httpRequest);
		if (!goon) {
			return;
		}

		SapRequest sapRequest = null;
		try {
			sapRequest = http2SapRequestTransformer.transform(httpRequest);
		} catch (UnsupportHttpMethodException e) {
			LOGGER.error("http2SapRequestTransformer.transform:", e);
			onRequestException(HttpResponseStatus.METHOD_NOT_ALLOWED, httpRequest);
			return;
		} catch (IllegalAccessProtocolException e) {
			LOGGER.error("http2SapRequestTransformer.transform:", e);
			onRequestException(HttpResponseStatus.NOT_FOUND, httpRequest);
			return;
		}

		// TODO:hack:check whether exists name code mapping here
		try {
			ncMapper.getCodeOf(sapRequest.getTargetPlugInServiceNamePair());
		} catch (NoSuchNameCodeMappingException e) {
			LOGGER.error("Check NameCodeMapping: " + e.getMessage());
			onRequestException(HttpResponseStatus.NOT_FOUND, httpRequest);
			return;
		}

		sender.send(sapRequest);
	}

	protected void onRequestException(final HttpResponseStatus status, HttpRequestExt httpRequest) {
		reply(new HttpResponseSource() {

			@Override
			public HttpResponseStatus getStatus() {
				return status;
			}

			@Override
			public Map<String, String> getHeaders() {
				return null;
			}

			@Override
			public String getContentType() {
				return null;
			}

			@Override
			public IoBuffer getContent() {
				return null;
			}
		}, httpRequest.getIdentification());
		handleIllegalRequest(httpRequest);
	}

	private void handleIllegalRequest(HttpRequestExt httpRequest) {
		illegalRequestHandler.handle(httpRequest);
	}

	protected void reply(HttpResponseSource httpResponse, UUID tid) {
		HttpResponseExt httpResponseExt = createHttpResponseExt(httpResponse, tid);
		replier.reply(httpResponseExt);
	}

	private HttpResponseExt createHttpResponseExt(HttpResponseSource httpResponseSource, UUID tid) {
		return httpResponseExtFactory.create(httpResponseSource, tid);
	}

	public void setNcMapper(NameCodeMapper ncMapper) {
		this.ncMapper = ncMapper;
	}
}
