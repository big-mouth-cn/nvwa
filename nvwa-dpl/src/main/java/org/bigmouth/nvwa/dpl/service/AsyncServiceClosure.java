package org.bigmouth.nvwa.dpl.service;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO:
public class AsyncServiceClosure implements ServiceClosure {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncServiceClosure.class);

	private ExecutorService executor = null;
	private ServiceClosure impl;

	@Override
	public void execute(Object... params) {
		// TODO Auto-generated method stub
		// TODO:
	}
}
