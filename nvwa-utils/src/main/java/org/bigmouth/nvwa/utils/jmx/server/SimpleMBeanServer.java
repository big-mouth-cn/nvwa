package org.bigmouth.nvwa.utils.jmx.server;

import javax.management.MBeanServer;

public interface SimpleMBeanServer {

	public static final SimpleMBeanServer DEFAULT = new SimpleMBeanServer() {

		@Override
		public int getMBeanCount() {
			return -1;
		}

		@Override
		public MBeanServer getNativeMBeanServer() {
			return null;
		}

		@Override
		public boolean isActive() {
			return false;
		}

		@Override
		public boolean isRegistered(String name) {
			return false;
		}

		@Override
		public void registMBean(Object o) {
		}

		@Override
		public void registMBean(Object o, String name) {
		}

		@Override
		public void start() {
		}

		@Override
		public void stop() {
		}
	};

	void start();

	void stop();

	boolean isRegistered(String name);

	boolean isActive();

	int getMBeanCount();

	void registMBean(Object o);

	void registMBean(Object o, String name);

	MBeanServer getNativeMBeanServer();
}
