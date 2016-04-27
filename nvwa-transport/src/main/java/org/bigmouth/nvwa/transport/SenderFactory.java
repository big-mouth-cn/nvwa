package org.bigmouth.nvwa.transport;

import java.net.InetSocketAddress;

public interface SenderFactory {

	Sender create(InetSocketAddress targetAddress);
}
