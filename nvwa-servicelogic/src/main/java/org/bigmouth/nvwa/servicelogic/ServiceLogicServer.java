package org.bigmouth.nvwa.servicelogic;

import org.bigmouth.nvwa.spring.boot.SpringBootstrap;

public final class ServiceLogicServer {

	private ServiceLogicServer() {
	}

	public static void main(String[] args) {
	    SpringBootstrap.
		bootUsingSpring(new String[] { "classpath:/config/applicationContext.xml" }, args);
	}
}
