package com.skymobi.market.commons.sap;

import java.util.Map;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.channel.utils.Closure;
import com.google.common.collect.Maps;
import com.skymobi.market.commons.sap.SapMessage;
import com.skymobi.market.commons.sap.codec.SapReqRespCodecFactory;
import com.skymobi.market.commons.sap.namecode.NameCodeMapper;
import com.skymobi.market.commons.transport.MinaReceiver;
import com.skymobi.market.commons.transport.MinaReplier;

public class SapMessageReceiverTest {

	public static void main(String[] args) {
		final MinaReplier replier = new MinaReplier();
		NameCodeMapper ncMapper = NameCodeMapperTestUtils.getInstance();
		Map<String, IoFilter> filters = Maps.newHashMap();
		filters.put("SapCodec", new ProtocolCodecFilter(new SapReqRespCodecFactory(ncMapper)));
		MinaReceiver mr = new MinaReceiver(replier, filters, 1234);

		mr.setReactor(new Closure() {

			@Override
			public void execute(Object message) {
				SapMessage sapMessage = (SapMessage) message;
				System.out.println("receive sapPacket:" + sapMessage);

				// sendback
				// sapMessage.getContent().flip();
				replier.reply(sapMessage);
				System.out.println("send back:" + sapMessage);
			}
		});
		mr.init();
	}
}
