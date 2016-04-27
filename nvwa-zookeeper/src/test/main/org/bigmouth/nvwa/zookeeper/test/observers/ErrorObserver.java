/*
 * Copyright 2015 big-mouth.cn
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
package org.bigmouth.nvwa.zookeeper.test.observers;

import org.bigmouth.nvwa.zookeeper.ZkClientHolder;
import org.bigmouth.nvwa.zookeeper.observers.AbstractObserver;


/**
 * 
 * @author Allen Hu 
 * 2015-8-2
 */
public class ErrorObserver extends AbstractObserver<Error> {

    public ErrorObserver(ZkClientHolder zkClientHolder, String subjectType, String name) {
        super(zkClientHolder, subjectType, name);
    }

    @Override
    public void add(Error message) {
        System.out.println(message);
    }

    @Override
    public void update(Error message) {
        System.out.println(message);
    }

    @Override
    public void remove(Error message) {
        System.out.println(message);
    }

    @Override
    public Class<Error> getClassType() {
        return Error.class;
    }

    @Override
    public String getObserverName() {
        return getName();
    }
}
