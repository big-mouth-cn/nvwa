package com.skymobi.market.commons.sap;

import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.channel.utils.Closure;
import com.google.common.collect.Maps;
import com.skymobi.market.commons.sap.SapMessage;
import com.skymobi.market.commons.sap.codec.SapReqRespCodecFactory;
import com.skymobi.market.commons.sap.namecode.NameCodeMapper;
import com.skymobi.market.commons.transport.CallbackSender;
import com.skymobi.market.commons.transport.DefaultTargetSessionLocatorBuilder;
import com.skymobi.market.commons.transport.MinaReceiver;
import com.skymobi.market.commons.transport.MinaReplier;
import com.skymobi.market.commons.transport.MinaSender;
import com.skymobi.market.commons.transport.TcpConfig;

public class SapMessageProxyTest {

	public static void main(String[] args) {
		final MinaReplier replier = new MinaReplier();

		// create sender
		final CallbackSender sender = new MinaSender(new TcpConfig(), null, new InetSocketAddress(
				12345), new DefaultTargetSessionLocatorBuilder(1, 10 * 1000), null);
		sender.init();
		sender.setCallback(new Closure() {

			@Override
			public void execute(Object packet) {
				SapMessage sapMessage = (SapMessage) packet;
				System.out.println("proxy response:" + sapMessage);
				replier.reply(sapMessage);
			}
		});

		// create receiver
		NameCodeMapper ncMapper = NameCodeMapperTestUtils.getInstance();
		Map<String, IoFilter> filters = Maps.newHashMap();
		filters.put("SapCodec", new ProtocolCodecFilter(new SapReqRespCodecFactory(ncMapper)));
		MinaReceiver mr = new MinaReceiver(replier, filters, 1234);

		mr.setReactor(new Closure() {

			@Override
			public void execute(Object packet) {
				SapMessage sapMessage = (SapMessage) packet;
				sender.send(sapMessage);
				System.out.println("proxy request:" + sapMessage);
			}
		});
		mr.init();
	}
}
