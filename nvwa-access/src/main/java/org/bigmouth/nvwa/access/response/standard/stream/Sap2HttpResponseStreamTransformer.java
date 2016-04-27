package org.bigmouth.nvwa.access.response.standard.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.asyncweb.common.DefaultHttpResponse;
import org.apache.asyncweb.common.HttpResponse;
import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.access.response.HttpResponseExtFactory;
import org.bigmouth.nvwa.access.response.HttpResponseSource;
import org.bigmouth.nvwa.access.service.sharding.ContentStoreLocator;
import org.bigmouth.nvwa.access.service.sharding.ContentStoreNotFoundException;
import org.bigmouth.nvwa.access.utils.ConnectService;
import org.bigmouth.nvwa.access.utils.HttpUtils;
import org.bigmouth.nvwa.contentstore.ContentStore;
import org.bigmouth.nvwa.sap.ContentRange;
import org.bigmouth.nvwa.sap.ExtendedItem;
import org.bigmouth.nvwa.sap.ExtendedItemType;
import org.bigmouth.nvwa.sap.Identifiable;
import org.bigmouth.nvwa.sap.SapResponse;
import org.bigmouth.nvwa.utils.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

//TODO:
public class Sap2HttpResponseStreamTransformer implements Transformer<SapResponse, Identifiable> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Sap2HttpResponseStreamTransformer.class);

	private final HttpResponseExtFactory httpResponseExtFactory;
	private final Map<ExtendedItemType, ContentStoreLocator> contentStoreLocators;
	private Cache cache;

	public Sap2HttpResponseStreamTransformer(HttpResponseExtFactory httpResponseExtFactory,
			Map<ExtendedItemType, ContentStoreLocator> contentStoreLocators) {
		this.httpResponseExtFactory = httpResponseExtFactory;
		this.contentStoreLocators = contentStoreLocators;
	}

	@Override
	public Identifiable transform(final SapResponse input) {

		if (200 != input.getStatus().getCode()) {
			if (LOGGER.isErrorEnabled())
				LOGGER.error("ServiceLogic response error,status code:" + input.getStatus());
			return createIllegalHttpResponse(input);
		}

		IoBuffer bodyContent = input.getContent();
		List<ExtendedItem> items = input.getExtendedItems();

		List<Object> pieces = getPieces(input, bodyContent, items);
		
		if(0 == pieces.size()) {
			try {
				return createHttpResponse(input);
			} catch (Exception e) {
				if (LOGGER.isInfoEnabled())
					LOGGER.info("", e);
				DefaultHttpResponse httpResponseHeader = new DefaultHttpResponse();
				httpResponseHeader.setStatus(HttpResponseStatus.NOT_FOUND);
				List<HttpResponseSegment> body = Lists.newArrayList();
				return createHttpResponseStream(input, httpResponseHeader, body);
			}
		}
		
		if (0 > pieces.size()) {
			throw new RuntimeException("0 == pieces.size()");
		}

		HttpResponse httpResponseHeader = createHttpResponseHeader(pieces);
		List<HttpResponseSegment> bodySegments = createHttpResponseBodySegments(pieces);

		return createHttpResponseStream(input, httpResponseHeader, bodySegments);
	}

	private HttpResponseStream createHttpResponseStream(final SapResponse input,
			HttpResponse httpResponseHeader, List<HttpResponseSegment> bodySegments) {
		HttpResponseStream ret = new HttpResponseStream(new HttpResponseHeader(httpResponseHeader),
				input.getIdentification());
		ret.addBodySegments(bodySegments);
		return ret;
	}

	private List<HttpResponseSegment> createHttpResponseBodySegments(List<Object> pieces) {
		List<HttpResponseSegment> bodySegments = Lists.newArrayList();
		for (Object p : pieces) {
			if (p instanceof IoBuffer) {
				HttpResponseSegment segment = new HttpResponseBodySegment((IoBuffer) p);
				bodySegments.add(segment);
				continue;
			}
			if (p instanceof ExtendedItem) {
				ExtendedItem item = (ExtendedItem) p;
				if (0 == item.getContentFlagsCount())
					throw new RuntimeException("0 == item.getContentFlagsCount()");

				ContentStore contentStore = null;
				try {
					if(ExtendedItemType.GENERIC_SEG_RES == item.getType()) {
						contentStore = contentStoreLocators.get(ExtendedItemType.GENERIC_SEG_RES).lookup(item.getShardingMark());
					} else {
						contentStore = contentStoreLocators.get(ExtendedItemType.APK).lookup(item.getShardingMark());
					}
				} catch (ContentStoreNotFoundException e) {
					throw new RuntimeException("contentStoreLocator.lookup:", e);
				}

				ContentRange range = item.getContentRange();

				// 16K per block,for default.
				int rBeginOffset = range.getRelativeBeginOffset();
				int rLimit = -1;
				if (ExtendedItemType.APK == item.getType()) {
					// the limit of last block.
					rLimit = range.getLength() - (16 * 1024 * item.getContentFlagsCount())
							+ rBeginOffset;
				} else if (ExtendedItemType.APK_CEN == item.getType()) {
					rLimit = rBeginOffset + range.getLength();
				} else if(ExtendedItemType.GENERIC_SEG_RES == item.getType()) {
					rLimit = range.getLength() - (512 * 1024 * item.getContentFlagsCount())
							+ rBeginOffset;
				} else {
					throw new RuntimeException("Unkown ExtendedItemType.");
				}

				List<String> cfs = item.getContentFlags();
				for (int i = 0, size = cfs.size(); i < size; i++) {
					LazyHttpResponseBodySegment segment = new LazyHttpResponseBodySegment(
							contentStore);
					segment.addContentFlag(cfs.get(i));
					if (0 == i) {
						if (rBeginOffset > 0) {
							// TODO:fault-tolerant,if rBeginOffset <= 0?
							segment.setPosition(rBeginOffset);
						}
					}

					if (item.getContentFlagsCount() - 1 == i) {
						// TODO:if last block is not equals 16K.
						if (rLimit > 0) {

							segment.setLimit(rLimit);
						}
					}
					bodySegments.add(segment);
				}

				// TODO:整理segment的粒度

				continue;
			}
		}
		return bodySegments;
	}

	private HttpResponse createHttpResponseHeader(List<Object> pieces) {
		IoBuffer firstPiece = (IoBuffer) pieces.get(0);
		if (firstPiece.remaining() < 24) {
			throw new RuntimeException("ppp");
		}
		firstPiece.position(0);
		firstPiece.limit(24);
		IoBuffer meta = firstPiece.slice();

		HttpResponse httpResponseHeader = createHttpResponseHeader0(meta);

		if (firstPiece.limit() == firstPiece.capacity()) {
			pieces.remove(0);
		} else {
			firstPiece.position(firstPiece.limit());
			firstPiece.limit(firstPiece.capacity());
			IoBuffer updatedFirstPiece = firstPiece.slice();
			pieces.set(0, updatedFirstPiece);
		}
		return httpResponseHeader;
	}

	private DefaultHttpResponse createHttpResponseHeader0(IoBuffer meta) {
		// biz code
		int bizCode = slice(meta, 8, 12).getInt();
		if (!isSuccess(bizCode))
			throw new RuntimeException("Sap response biz code:" + bizCode);
		// total length
		int totalLength = slice(meta, 12, 16).getInt();
		// begin offset
		int beginOffset = slice(meta, 16, 20).getInt();
		// TODO:data length
		int length = slice(meta, 20, 24).getInt();

		DefaultHttpResponse httpResponseHeader = new DefaultHttpResponse();
		httpResponseHeader.setStatus(HttpResponseStatus.OK);

		httpResponseHeader.setHeader("Content-Length", String.valueOf(length));
		httpResponseHeader.setHeader("Date", HttpUtils.genGMTString());
		httpResponseHeader.setHeader("SkyId", "SkyMarket");
		httpResponseHeader.addHeader("Content-Disposition", "attachment;filename=skymobiapp-"
				+ totalLength + ".apk");

		if (isPartialContent(length, totalLength, beginOffset)) {
			String contentRangeValue = HttpUtils.genContentRange(beginOffset, beginOffset + length
					- 1, totalLength);
			httpResponseHeader.setHeader("Content-Range", contentRangeValue);
			httpResponseHeader.setStatus(HttpResponseStatus.PARTIAL_CONTENT);
		}
		return httpResponseHeader;
	}

	private List<Object> getPieces(final SapResponse input, IoBuffer bodyContent,
			List<ExtendedItem> items) {
		List<Object> pieces = Lists.newArrayList();
		for (ExtendedItem item : items) {

			if (bodyContent.position() != item.getMergeOffset()) {
				int segLimit = item.getMergeOffset();
				if (-1 == segLimit) {// append end of body content.
					segLimit = input.getContentLength();
				}
				bodyContent.limit(segLimit);
				IoBuffer seg = bodyContent.slice();
				pieces.add(seg);

				bodyContent.position(segLimit);
			}
			pieces.add(item);
		}

		if (bodyContent.capacity() > bodyContent.limit()) {
			bodyContent.limit(bodyContent.capacity());
			IoBuffer lastSeg = bodyContent.slice();

			pieces.add(lastSeg);
		}
		return pieces;
	}

	private Identifiable createIllegalHttpResponse(final SapResponse input) {
		return httpResponseExtFactory.create(new HttpResponseSource() {

			@Override
			public HttpResponseStatus getStatus() {
				return HttpResponseStatus.forId(input.getStatus().getCode());
			}

			@Override
			public Map<String, String> getHeaders() {
				Map<String, String> ret = Maps.newHashMap();
				ret.put("Date", HttpUtils.genGMTString());
				ret.put("SkyId", "SkyMarket");

				return ret;
			}

			@Override
			public String getContentType() {
				return null;
			}

			@Override
			public IoBuffer getContent() {
				return null;
			}

		}, input.getIdentification());
	}

	private boolean isPartialContent(int length, int totalLength, int beginOffset) {
		return !(0 == beginOffset && length == totalLength);
	}

	private boolean isSuccess(int bizCode) {
		return 2000 == bizCode;
	}

	private IoBuffer slice(IoBuffer data, int bOffset, int eOffset) {
		data.position(bOffset);
		data.limit(eOffset);
		return data.slice();
	}
	
	private HttpResponseStream createHttpResponse(SapResponse input) {

		IoBuffer bodyContent = input.getContent();
		if(bodyContent.limit() < 40)
			throw new RuntimeException("pic url not found!");
		int length = slice(bodyContent, 36, 40).getInt();
		if(length > Short.MAX_VALUE)
			throw new RuntimeException("pic url not found!");
		byte[] urlArr = new byte[length];
		try {
			slice(bodyContent, 40, 40 + length).asInputStream().read(urlArr);
		} catch (IOException e) {
			throw new RuntimeException("pic url not found!", e);
		}
		String url = new String(urlArr);
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug(url);
		}
		ByteArrayOutputStream content = null;
		Element element = cache.get(url);
		if (element != null) {
			content = (ByteArrayOutputStream) element.getObjectValue();
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("pic from cache!");
		} else {
			content = ConnectService.connect(url);
			cache.put(new Element(url, content));
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("pic from http, and saved to cache!");
		}
		
		DefaultHttpResponse httpResponseHeader = new DefaultHttpResponse();
		httpResponseHeader.setStatus(HttpResponseStatus.OK);

		httpResponseHeader.setHeader("Content-Length", String.valueOf(content.size()));
		httpResponseHeader.setHeader("Date", HttpUtils.genGMTString());
		
		HttpResponseStream responseStream = new HttpResponseStream(new HttpResponseHeader(httpResponseHeader), input.getIdentification());
		IoBuffer body = IoBuffer.wrap(content.toByteArray());
		HttpResponseSegment segment = new HttpResponseBodySegment(body);
		responseStream.addBodySegment(segment);
		
		return responseStream;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}
}
