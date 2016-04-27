package org.bigmouth.nvwa.servicelogic.statistics.cache;

import org.bigmouth.nvwa.cache.xmc.XmcFetchService;
import org.bigmouth.nvwa.dpl.utils.RuntimeUtils;
import org.bigmouth.nvwa.servicelogic.TransactionInvocation;
import org.bigmouth.nvwa.servicelogic.statistics.ExtendedServiceStatistics;

public class StatisticsXmcFetchService extends XmcFetchService {

	@Override
	protected Object returnFromStore(Object fromStoreValue) {
		ExtendedServiceStatistics ess = (ExtendedServiceStatistics) RuntimeUtils
				.getServiceAttachment();

		if (null == ess)
			return super.returnFromStore(fromStoreValue);

		ess.increaseHitStorage();

		TransactionInvocation iv = (TransactionInvocation) RuntimeUtils.getInvocation();
		ess.recordInvocation(iv);

		return super.returnFromStore(fromStoreValue);
	}

	@Override
	protected Object returnFromCache(Object fromCacheValue) {
		ExtendedServiceStatistics ess = (ExtendedServiceStatistics) RuntimeUtils
				.getServiceAttachment();

		if (null == ess)
			return super.returnFromCache(fromCacheValue);

		ess.increaseHitCache();
		return super.returnFromCache(fromCacheValue);
	}
}
