package org.bigmouth.nvwa.transport.jmx;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.service.IoServiceStatistics;
import org.bigmouth.nvwa.utils.CyclicCounter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class MinaStatisticsUpdater {

    private static final int DEFAULT_INTERVAL = 3;
    private static final CyclicCounter threadId = new CyclicCounter();

    private final ConcurrentMap<InetSocketAddress, IoServiceStatistics> stats = Maps
            .newConcurrentMap();
    private final int interval;

    private volatile ScheduledExecutorService executor;

    public MinaStatisticsUpdater() {
        this(DEFAULT_INTERVAL);
    }

    public MinaStatisticsUpdater(int interval) {
        if (interval <= 0)
            throw new IllegalArgumentException("interval:" + interval);
        this.interval = interval;
    }

    /**
     * 
     * @param ia
     *            if acceptor,then listening address;<br>
     *            if connector,then peer address.
     * @param sts
     */
    public void register(InetSocketAddress ia, IoServiceStatistics sts) {
        Preconditions.checkNotNull(ia, "InetSocketAddress");
        Preconditions.checkNotNull(ia, "IoServiceStatistics");
        stats.put(ia, sts);
    }

    public void unregister(InetSocketAddress ia) {
        Preconditions.checkNotNull(ia, "InetSocketAddress");
        stats.remove(ia);
    }

    public void clear() {
        stats.clear();
    }

    public void start() {
        executor = Executors.newScheduledThreadPool(1, new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Mina-statistics-updater-thread-" + threadId.get());
            }
        });
        executor.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                for (IoServiceStatistics sts : stats.values()) {
                    sts.updateThroughput(System.currentTimeMillis());
                }
            }
        }, interval, interval, TimeUnit.SECONDS);
    }

    public void stop() {
        executor.shutdownNow();
    }
}
