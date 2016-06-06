package com.skymobi.market.commons.dpl;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DplServer {

	public static void main(String[] args) {
		long beginMTime = System.currentTimeMillis();
		try {
			new ClassPathXmlApplicationContext(
					new String[] { "classpath:/com/skymobi/market/commons/dpl/applicationContext-dpl-test.xml" });
			System.out.println("Misg Platform startup in "
					+ (System.currentTimeMillis() - beginMTime) + " ms");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
