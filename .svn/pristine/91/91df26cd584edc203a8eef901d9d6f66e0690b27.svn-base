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
package org.bigmouth.nvwa.transport.jmx;

import org.bigmouth.nvwa.jmx.model.GenericModelMBean;
import org.bigmouth.nvwa.transport.DelegatedSender;



/**
 * 
 * @since 1.0
 * @author Allen Hu - (big-mouth.cn)
 */
public class DelegatedSenderMBean extends GenericModelMBean<DelegatedSender> {

    public DelegatedSenderMBean(DelegatedSender source) {
        super(source);
    }

    @Override
    protected boolean isAttribute(String attrName, Class<?> attrType) {
        if (attrName.matches("(pendingSendMessages|processors)"))
            return true;
        return false;
    }

    @Override
    protected boolean isOperation(String methodName, Class<?>[] paramTypes) {
        return false;
    }
}
