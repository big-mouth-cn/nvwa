package com.skymobi.market.commons.transport;

import java.util.Map;

import org.apache.asyncweb.common.codec.HttpCodecFactory;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.google.common.collect.Maps;
import com.skymobi.market.commons.transport.MinaReceiver;
import com.skymobi.market.commons.transport.MinaReplier;

public class ReceiverTest {

	public static void main(String[] args) {
		Map<String, IoFilter> filters = Maps.newHashMap();
		filters.put("HttpCodec", new ProtocolCodecFilter(new HttpCodecFactory()));
		MinaReceiver mr = new MinaReceiver(new MinaReplier(), filters, 12345);
		mr.init();
	}
}
