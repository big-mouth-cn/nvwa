package com.skymobi.market.commons.sap;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.channel.utils.Closure;
import com.google.common.collect.Maps;
import com.skymobi.market.commons.sap.DefaultSapRequest;
import com.skymobi.market.commons.sap.MutableSapRequest;
import com.skymobi.market.commons.sap.SapRequest;
import com.skymobi.market.commons.sap.SapResponse;
import com.skymobi.market.commons.sap.codec.SapReqRespCodecFactory;
import com.skymobi.market.commons.sap.namecode.NameCodeMapper;
import com.skymobi.market.commons.transport.CallbackSender;
import com.skymobi.market.commons.transport.DefaultTargetSessionLocatorBuilder;
import com.skymobi.market.commons.transport.MinaSender;
import com.skymobi.market.commons.transport.TcpConfig;

public class SapSenderTest {

	public static void main(String[] args) {

		NameCodeMapper ncMapper = NameCodeMapperTestUtils.getInstance();
		Map<String, IoFilter> filters = Maps.newHashMap();
		filters.put("SapCodec", new ProtocolCodecFilter(new SapReqRespCodecFactory(ncMapper)));

		CallbackSender sender = new MinaSender(new TcpConfig(), filters, new InetSocketAddress(12345),
				new DefaultTargetSessionLocatorBuilder(1, 10 * 1000), null);
		sender.init();
		sender.setCallback(new Closure() {

			@Override
			public void execute(Object packet) {
				SapResponse response = (SapResponse) packet;
				System.out.println("receive response:" + response);
			}
		});

		for (int i = 0; i < 10; i++) {
			sender.send(createSapRequest());
		}
	}

	private static SapRequest createSapRequest() {
		MutableSapRequest request = new DefaultSapRequest();
		request.setIdentification(UUID.randomUUID());

		request.setSourcePlugInName("app");
		request.setSourceServiceName("default");
		request.setTargetPlugInName("applist");
		request.setTargetServiceName("frame");

		request.setULP_beginOffset(101);
		request.setULP_endOffset(103);

		IoBuffer buf = IoBuffer.allocate(10);
		for (int i = 0; i < 10; i++) {
			buf.put((byte) i);
		}
		buf.flip();

		request.setContent(buf);
		return request;
	}
}
