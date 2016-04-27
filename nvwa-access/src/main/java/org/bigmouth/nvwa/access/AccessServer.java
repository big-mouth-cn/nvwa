package org.bigmouth.nvwa.access;

import org.bigmouth.nvwa.spring.boot.SpringBootstrap;

public final class AccessServer {

	private AccessServer() {
	}

	public static void main(String[] args) {
	    SpringBootstrap
		.bootUsingSpring(new String[] { "classpath:/config/applicationContext.xml" }, args);
	}
}
