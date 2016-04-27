package org.bigmouth.nvwa.access.response.standard.stream;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.util.List;
import java.util.Map;

import org.apache.asyncweb.common.HttpMessage;
import org.apache.asyncweb.common.HttpResponse;
import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.asyncweb.common.codec.HttpCodecUtils;
import org.apache.mina.core.buffer.IoBuffer;

public class HttpResponseHeader implements HttpResponseSegment {

	private final CharsetEncoder asciiEncoder = HttpCodecUtils.US_ASCII_CHARSET.newEncoder();
	private static final byte[] CRLF_BYTES = new byte[] { '\r', '\n' };
	private static boolean[] HTTP_CONTROLS = new boolean[128];

	private final HttpResponse header;

	public HttpResponseHeader(HttpResponse header) {
		this.header = header;
	}

	public HttpResponse getEntity() {
		return header;
	}

	@Override
	public IoBuffer getContent() {
		asciiEncoder.reset();

		IoBuffer buffer = IoBuffer.allocate(512);
		buffer.setAutoExpand(true);

		try {
			encodeStatusLine(header, buffer);
			encodeHeaders(header, buffer, asciiEncoder);
		} catch (CharacterCodingException e) {
			throw new RuntimeException("getContent:", e);
		}

		buffer.flip();
		return buffer;
	}

	private void encodeHeaders(HttpMessage message, IoBuffer buffer, CharsetEncoder encoder)
			throws CharacterCodingException {

		try {
			for (Map.Entry<String, List<String>> header : message.getHeaders().entrySet()) {
				byte[] key = header.getKey().getBytes(HttpCodecUtils.US_ASCII_CHARSET_NAME);

				for (String value : header.getValue()) {
					buffer.put(key);
					buffer.put((byte) ':');
					buffer.put((byte) ' ');
					buffer.putString(value, encoder);
					appendCRLF(buffer);
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new InternalError(HttpCodecUtils.US_ASCII_CHARSET_NAME + " should be available.");
		}

		appendCRLF(buffer);
	}

	private void encodeStatusLine(HttpResponse response, IoBuffer buffer)
			throws CharacterCodingException {
		// Write protocol version.
		buffer.putString(response.getProtocolVersion().toString(), asciiEncoder);
		buffer.put((byte) ' ');

		// Write status code.
		HttpResponseStatus status = response.getStatus();
		// TODO: Cached buffers for response codes / descriptions?
		appendString(buffer, String.valueOf(status.getCode()));
		buffer.put((byte) ' ');

		// Write reason phrase.
		appendString(buffer, response.getStatusReasonPhrase());
		appendCRLF(buffer);
	}

	private static boolean isHttpControl(byte b) {
		return HTTP_CONTROLS[b & 0x00FF];
	}

	private static void appendString(IoBuffer buffer, String string) {
		if (string == null) {
			return;
		}
		int len = string.length();

		for (int i = 0; i < len; i++) {
			byte b = (byte) string.charAt(i);
			if (isHttpControl(b) && b != '\t') {
				b = ' ';
			}
			buffer.put(b);
		}
	}

	private static void appendCRLF(IoBuffer buffer) {
		buffer.put(CRLF_BYTES);
	}
}
