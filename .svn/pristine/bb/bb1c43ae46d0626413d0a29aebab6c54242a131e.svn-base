package com.skymobi.market.commons.sap;

import java.net.InetSocketAddress;
import java.util.UUID;

import org.apache.mina.core.buffer.IoBuffer;

import com.channel.utils.Closure;
import com.skymobi.market.commons.sap.DefaultSapMessage;
import com.skymobi.market.commons.sap.MutableSapMessage;
import com.skymobi.market.commons.sap.SapMessage;
import com.skymobi.market.commons.transport.CallbackSender;
import com.skymobi.market.commons.transport.DefaultTargetSessionLocatorBuilder;
import com.skymobi.market.commons.transport.MinaSender;
import com.skymobi.market.commons.transport.TcpConfig;

public class SapMessageSenderTest {

	public static void main(String[] args) {
		CallbackSender sender = new MinaSender(new TcpConfig(), null, new InetSocketAddress(12345),
				new DefaultTargetSessionLocatorBuilder(1, 10 * 1000), null);
		sender.init();
		sender.setCallback(new Closure() {

			@Override
			public void execute(Object packet) {
				SapMessage response = (SapMessage) packet;
				System.out.println("receive response:" + response);
			}
		});

		for (int i = 0; i < 10; i++) {
			sender.send(createSapPacket());
		}
	}

	private static SapMessage createSapPacket() {
		MutableSapMessage message = new DefaultSapMessage();
		message.setIdentification(UUID.randomUUID());
		IoBuffer buf = IoBuffer.allocate(10);
		for (int i = 0; i < 10; i++) {
			buf.put((byte) i);
		}
		buf.flip();

		message.setContent(buf);
		return message;
	}
}
