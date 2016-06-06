package org.bigmouth.nvwa.access.response.standard;

import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.access.response.HttpResponseExtFactory;
import org.bigmouth.nvwa.access.response.HttpResponseSource;
import org.bigmouth.nvwa.access.response.Sap2HttpResponseTransformer;
import org.bigmouth.nvwa.access.service.ContentExtractor;
import org.bigmouth.nvwa.access.utils.HttpUtils;
import org.bigmouth.nvwa.sap.ContentType;


public class StandardSap2HttpResponseTransformer extends Sap2HttpResponseTransformer {

	public StandardSap2HttpResponseTransformer(HttpResponseExtFactory httpResponseExtFactory,
			ContentExtractor contentExtractor) {
		super(httpResponseExtFactory, contentExtractor);
	}

	@Override
	protected HttpResponseSource createHttpResponseSource(HttpResponseStatus httpRespStatus,
			ContentType contentType, IoBuffer contentBuf) {

		int totalLimit = contentBuf.limit();
		if (totalLimit < 20)
			throw new RuntimeException("Sap response data illegal length:" + totalLimit);

		// biz code
		int bizCode = slice(contentBuf, 8, 12).getInt();
		if (!isSuccess(bizCode))
			throw new RuntimeException("Sap response biz code:" + bizCode);

		// total length
		contentBuf.position(12);
		contentBuf.limit(16);
		int totalLength = slice(contentBuf, 12, 16).getInt();

		// begin offset
		contentBuf.position(16);
		contentBuf.limit(20);
		int beginOffset = slice(contentBuf, 16, 20).getInt();

		// TODO:data length
		contentBuf.position(20);
		contentBuf.limit(24);
		int length = slice(contentBuf, 20, 24).getInt();

		// TODO:
		// contentBuf = slice(contentBuf, 20, totalLimit);
		// data
		contentBuf = slice(contentBuf, 24, totalLimit);

		HttpResponseSourceImpl httpRespSource = new HttpResponseSourceImpl(httpRespStatus,
				contentType, contentBuf);

		if (isPartialContent(contentBuf, totalLength, beginOffset)) {
			String contentRangeValue = HttpUtils.genContentRange(beginOffset, beginOffset
					+ contentBuf.remaining() - 1, totalLength);
			httpRespSource.addHeader("Content-Range", contentRangeValue);
			httpRespSource.setStatus(HttpResponseStatus.PARTIAL_CONTENT);
		}

		httpRespSource.addHeader("Content-Disposition", "attachment;filename=skymobi.apk");

		return httpRespSource;
	}

	private boolean isPartialContent(IoBuffer contentBuf, int totalLength, int beginOffset) {
		return !(0 == beginOffset && contentBuf.remaining() == totalLength);
	}

	private boolean isSuccess(int bizCode) {
		return 2000 == bizCode;
	}

	private IoBuffer slice(IoBuffer data, int bOffset, int eOffset) {
		data.position(bOffset);
		data.limit(eOffset);
		return data.slice();
	}
}
