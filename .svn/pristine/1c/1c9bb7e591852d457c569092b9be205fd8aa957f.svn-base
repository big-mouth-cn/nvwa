package org.bigmouth.nvwa.dpl.service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface FunctionalService extends MutableService, ServiceFunctor {

	Object execute(long timeout, TimeUnit unit, Object... arguments) throws TimeoutException;
}
