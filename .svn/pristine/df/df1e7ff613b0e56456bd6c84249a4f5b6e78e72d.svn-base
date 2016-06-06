/*
 * Copyright 2016 mopote.com
 *
 * The Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.bigmouth.nvwa.transport.failover;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.bigmouth.nvwa.distributed.Endpoint;
import org.bigmouth.nvwa.distributed.EndpointKeyHost;
import org.bigmouth.nvwa.transport.Sender;
import org.bigmouth.nvwa.transport.SenderStatus;
import org.bigmouth.nvwa.utils.CyclicCounter;
import org.bigmouth.nvwa.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * 
 * @since 1.0
 * @author Allen Hu - (big-mouth.cn)
 */
public abstract class SenderLocator {

    private static final Logger LOG = LoggerFactory.getLogger(SenderLocator.class);
    private static final String LOCAL_ADDRESS;
    private static final Map<String, SenderLocator> KEY2LOCATOR = Maps.newHashMap();

    public abstract Sender lookup(ListMultimap<EndpointKeyHost, Pair<Endpoint, Sender>> senders);

    public abstract String getKey();

    public static SenderLocator create() {
        return LOCAL_FIRST;
    }

    public static SenderLocator create(String key) {
        Preconditions.checkNotNull(StringUtils.isNotBlank(key), "key is blank.");

        SenderLocator ret = KEY2LOCATOR.get(key);
        if (null == ret)
            throw new RuntimeException("unkown SenderLocator key:" + key);
        return ret;
    }

    private static final SenderLocator AVG = new SenderLocator() {

        private final CyclicCounter idx = new CyclicCounter();

        @Override
        public Sender lookup(ListMultimap<EndpointKeyHost, Pair<Endpoint, Sender>> senders) {

            // average distributiond
            List<Pair<Endpoint, Sender>> als = Lists.newArrayList(senders.values());

            int tryCount = als.size();
            do {
                tryCount--;
                int targetIdx = idx.get() % als.size();
                Pair<Endpoint, Sender> targetPair = als.get(targetIdx);
                if (SenderStatus.UNAVAILABLE == targetPair.getSecond().getStatus()) {
                    if (LOG.isInfoEnabled())
                        LOG.info("{},{} is unavailable,ignore.", getKey(), targetPair.getFirst());
                    continue;
                }

                return targetPair.getSecond();

            } while (tryCount > 0);

            throw new NotFoundAvailableSenderException("all senders unavailable.");
        }

        @Override
        public String getKey() {
            return "avg";
        }
    };
    
    private static final SenderLocator RANDOM = new SenderLocator() {
        
        @Override
        public Sender lookup(ListMultimap<EndpointKeyHost, Pair<Endpoint, Sender>> senders) {
            List<Pair<Endpoint, Sender>> als = Lists.newArrayList(senders.values());
            int size = als.size();
            Pair<Endpoint, Sender> targetPair = als.get(RandomUtils.nextInt(size));
            if (LOG.isInfoEnabled())
                LOG.info("random,message route:{}", targetPair.getFirst());
            Sender targetSender = targetPair.getSecond();
            if (SenderStatus.UNAVAILABLE == targetSender.getStatus()) {
                throw new NotFoundAvailableSenderException(getKey());
            }
            return targetSender;
        }
        
        @Override
        public String getKey() {
            return "random";
        }
    };

    private static final SenderLocator LOCAL_ONLY = new SenderLocator() {

        private final CyclicCounter idx = new CyclicCounter();

        @Override
        public Sender lookup(ListMultimap<EndpointKeyHost, Pair<Endpoint, Sender>> senders) {

            // local only
            List<Pair<Endpoint, Sender>> ls = senders.get(EndpointKeyHost.of(Endpoint.of(
                    LOCAL_ADDRESS, 1)));
            Sender targetSender = null;
            if (null != ls) {
                if (1 == ls.size()) {
                    Pair<Endpoint, Sender> targetPair = ls.get(0);
                    if (LOG.isInfoEnabled())
                        LOG.info("local only,message route:{}", targetPair.getFirst());
                    targetSender = targetPair.getSecond();
                    if (SenderStatus.UNAVAILABLE == targetSender.getStatus()) {
                        throw new NotFoundAvailableSenderException(getKey());
                    }
                    return targetSender;
                } else {
                    int tryCount = ls.size();
                    do {
                        tryCount--;
                        int targetIdx = idx.get() % ls.size();
                        Pair<Endpoint, Sender> targetPair = ls.get(targetIdx);
                        if (LOG.isInfoEnabled())
                            LOG.info("local only,message route:{}", targetPair.getFirst());
                        targetSender = targetPair.getSecond();
                        if (SenderStatus.UNAVAILABLE == targetSender.getStatus()) {
                            if (LOG.isInfoEnabled())
                                LOG.info("{},{} is unavailable,ignore.", getKey(),
                                        targetPair.getFirst());
                            continue;
                        }
                        return targetSender;
                    } while (tryCount > 0);
                }
            }

            throw new NotFoundAvailableSenderException(
                    "local only,not found available local sender.");
        }

        @Override
        public String getKey() {
            return "localOnly";
        }
    };

    private static final SenderLocator LOCAL_FIRST = new SenderLocator() {

        @Override
        public Sender lookup(ListMultimap<EndpointKeyHost, Pair<Endpoint, Sender>> senders) {

            Sender targetSender = null;
            try {
                targetSender = LOCAL_ONLY.lookup(senders);
            } catch (NotFoundAvailableSenderException e) {
                // TODO:ignore
            }
            if (null != targetSender) {
                return targetSender;
            }

            try {
                targetSender = AVG.lookup(senders);
            } catch (NotFoundAvailableSenderException e) {
                throw e;
            }

            if (null != targetSender) {
                return targetSender;
            }

            throw new NotFoundAvailableSenderException(getKey());
        }

        @Override
        public String getKey() {
            return "localFirst";
        }
    };

    private static final SenderLocator WEIGHTS = new SenderLocator() {

        private final CyclicCounter idx = new CyclicCounter();

        @Override
        public Sender lookup(ListMultimap<EndpointKeyHost, Pair<Endpoint, Sender>> senders) {

            int total = getTotal(senders);
            int curIdx = idx.get() % total;
            int tmp = 0;
            Sender targetSender = null;
            for (Pair<Endpoint, Sender> p : senders.values()) {
                if (curIdx >= tmp && curIdx < (tmp += p.getFirst().getWeights())) {
                    if (LOG.isInfoEnabled())
                        LOG.info("{},message route:{}", getKey(), p.getFirst());
                    targetSender = p.getSecond();
                    if (SenderStatus.UNAVAILABLE == targetSender.getStatus()) {
                        if (LOG.isInfoEnabled())
                            LOG.info("{},{} is unavailable,ignore.", getKey(), p.getFirst());
                        continue;
                    }
                    return targetSender;
                }
            }

            throw new NotFoundAvailableSenderException(getKey());
        }

        private int getTotal(ListMultimap<EndpointKeyHost, Pair<Endpoint, Sender>> senders) {
            int total = 0;
            for (Pair<Endpoint, Sender> p : senders.values()) {
                total += p.getFirst().getWeights() + 1;
            }
            return total;
        }

        @Override
        public String getKey() {
            return "weights";
        }
    };

    static {
        try {
            LOCAL_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("SenderLocator:", e);
        }

        KEY2LOCATOR.put(AVG.getKey(), AVG);
        KEY2LOCATOR.put(RANDOM.getKey(), RANDOM);
        KEY2LOCATOR.put(LOCAL_ONLY.getKey(), LOCAL_ONLY);
        KEY2LOCATOR.put(LOCAL_FIRST.getKey(), LOCAL_FIRST);
        KEY2LOCATOR.put(WEIGHTS.getKey(), WEIGHTS);
    }
}
