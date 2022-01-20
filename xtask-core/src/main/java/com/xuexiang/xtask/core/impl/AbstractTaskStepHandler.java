/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xtask.core.impl;

import com.xuexiang.xtask.core.ITaskStepHandler;
import com.xuexiang.xtask.core.ITaskStep;

/**
 * 抽象任务处理者
 *
 * @author xuexiang
 * @since 2021/11/2 1:48 AM
 */
public class AbstractTaskStepHandler implements ITaskStepHandler {
    @Override
    public void beforeTask(ITaskStep step) {

    }

    @Override
    public void afterTask(ITaskStep step) {

    }

    @Override
    public boolean accept(ITaskStep step) {
        return true;
    }

    @Override
    public void handleTaskSucceed(ITaskStep step) {

    }

    @Override
    public void handleTaskFailed(ITaskStep step) {

    }
}
