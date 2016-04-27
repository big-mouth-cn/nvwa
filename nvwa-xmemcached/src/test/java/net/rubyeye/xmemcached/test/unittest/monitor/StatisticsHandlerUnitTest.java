package net.rubyeye.xmemcached.test.unittest.monitor;

import java.util.HashMap;
import java.util.Map;

import net.rubyeye.xmemcached.command.CommandType;
import net.rubyeye.xmemcached.monitor.StatisticsHandler;
import junit.framework.TestCase;

public class StatisticsHandlerUnitTest extends TestCase {
	StatisticsHandler handler;

	@Override
	public void setUp() {
		handler = new StatisticsHandler();
		handler.setStatistics(true);

	}

	public void testStatistics() {
		Map<CommandType, Long> map = new HashMap<CommandType, Long>();
		long i = 0;
		for (CommandType cmdType : CommandType.values()) {
			map.put(cmdType, i++);
		}
		for (CommandType cmdType : CommandType.values()) {
			for (int j = 0; j < map.get(cmdType); j++) {
				handler.statistics(cmdType);
			}
		}

		assertEquals((long) map.get(CommandType.GET_MANY), handler
				.getMultiGetCount());
		assertEquals((long) map.get(CommandType.GETS_MANY), handler
				.getMultiGetsCount());
		assertEquals((long) map.get(CommandType.SET), handler
				.getSetCount());
		assertEquals((long) map.get(CommandType.ADD), handler
				.getAddCount());
		assertEquals((long) map.get(CommandType.CAS), handler
				.getCASCount());
		assertEquals((long) map.get(CommandType.REPLACE), handler
				.getReplaceCount());
		assertEquals((long) map.get(CommandType.APPEND), handler
				.getAppendCount());
		assertEquals((long) map.get(CommandType.PREPEND), handler
				.getPrependCount());
		assertEquals((long) map.get(CommandType.DELETE), handler
				.getDeleteCount());
	}
}
