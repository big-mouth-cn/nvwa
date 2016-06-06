package com.skymobi.market.commons.dpl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bigmouth.nvwa.dpl.PlugInServiceBus;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.bigmouth.nvwa.dpl.service.DefaultFunctionalService;
import org.bigmouth.nvwa.dpl.service.DefaultProceduralService;
import org.bigmouth.nvwa.dpl.service.Service;


public class ServiceInvokeTest {

	private final PlugInServiceBus bus;

	public ServiceInvokeTest(PlugInServiceBus bus) {
		super();
		this.bus = bus;
	}

	public void start() {
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		ses.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					testClosureService();
					testFunctorService();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1, 5, TimeUnit.SECONDS);
	}

	public void stop() {

	}

	private void testClosureService() {
		PlugIn demo0PlugIn = bus.lookupPlugIn("demo");
		if (null == demo0PlugIn) {
			System.out.println("demo0PlugIn is null.");
			return;
		}
		Service service = demo0PlugIn.lookupService("say");
		if (service instanceof DefaultProceduralService) {
            DefaultProceduralService closureService = (DefaultProceduralService) service;
		    closureService.execute(1, "doug");
		}
	}

	private void testFunctorService() {
		PlugIn demo0PlugIn = bus.lookupPlugIn("demo");
		if (null == demo0PlugIn) {
			System.out.println("demo0PlugIn is null.");
			return;
		}
		Service service = demo0PlugIn.lookupService("say");
        DefaultFunctionalService functorService = (DefaultFunctionalService) service;
		String ret = (String) functorService.execute("say");
		System.out.println("FunctorService ret:" + ret);
	}
}
