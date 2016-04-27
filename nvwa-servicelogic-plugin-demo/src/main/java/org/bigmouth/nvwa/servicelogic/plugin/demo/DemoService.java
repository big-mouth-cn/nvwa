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
package org.bigmouth.nvwa.servicelogic.plugin.demo;

import org.bigmouth.nvwa.dpl.factory.annotation.PlugIn;
import org.bigmouth.nvwa.servicelogic.factory.annotation.TransactionService;
import org.bigmouth.nvwa.servicelogic.handler.ResourceNotFoundException;
import org.bigmouth.nvwa.servicelogic.handler.TransactionException;
import org.bigmouth.nvwa.servicelogic.handler.TransactionHandler;


@PlugIn(name = "demo", code = "demo")
@TransactionService(name = "demo", code = "demo")
public class DemoService implements TransactionHandler<DemoRequest, DemoResponse> {

    @Override
    public void handle(DemoRequest requestModel, DemoResponse responseModel) throws ResourceNotFoundException,
            TransactionException {
        String name = requestModel.getName();
        responseModel.setEcho("Hello " + name);
    }
}
