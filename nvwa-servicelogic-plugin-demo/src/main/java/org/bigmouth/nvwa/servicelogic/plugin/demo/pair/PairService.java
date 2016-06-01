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
package org.bigmouth.nvwa.servicelogic.plugin.demo.pair;

import java.util.List;

import org.bigmouth.nvwa.mybatis.page.PageInfo;
import org.bigmouth.nvwa.mybatis.support.MyBatisServiceSupport;
import org.bigmouth.nvwa.servicelogic.factory.annotation.TransactionService;
import org.bigmouth.nvwa.servicelogic.handler.ResourceNotFoundException;
import org.bigmouth.nvwa.servicelogic.handler.TransactionException;
import org.bigmouth.nvwa.servicelogic.handler.TransactionHandler;
import org.bigmouth.nvwa.servicelogic.plugin.demo.pair.dao.PairDao;

@TransactionService(name = "pair", code = "pair")
public class PairService extends MyBatisServiceSupport<Pair, Long, PairDao> implements TransactionHandler<PairRequest, PairResponse> {

    private PairDao pairDao;
    
    @Override
    public void handle(PairRequest requestModel, PairResponse responseModel) throws ResourceNotFoundException,
            TransactionException {
        PageInfo<Pair> page = new PageInfo<Pair>(1, 5);
        List<Pair> pairs = pairDao.queryAll(page);
        responseModel.setPairs(pairs);
        
        Pair pair = pairDao.queryByKey("UNUSABLE_REGION");
        responseModel.setPair(pair);
    }

    @Override
    protected PairDao getDao() {
        return pairDao;
    }

    public void setPairDao(PairDao pairDao) {
        this.pairDao = pairDao;
    }
}
