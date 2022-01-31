/*
 * Copyright (C) 2022 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xtask.core.step.impl;

import com.xuexiang.xtask.core.ITaskChainCallback;
import com.xuexiang.xtask.core.ITaskChainEngine;
import com.xuexiang.xtask.core.param.ITaskResult;

/**
 * 任务链执行回调适配器【默认回主线程的回调】
 *
 * @author xuexiang
 * @since 1/31/22 9:11 PM
 */
public class TaskChainCallbackAdapter implements ITaskChainCallback {

    @Override
    public boolean isCallBackOnMainThread() {
        return true;
    }

    @Override
    public void onTaskChainStart(ITaskChainEngine engine) {

    }

    @Override
    public void onTaskChainCompleted(ITaskChainEngine engine, ITaskResult result) {

    }

    @Override
    public void onTaskChainError(ITaskChainEngine engine, ITaskResult result) {

    }

    @Override
    public void onTaskChainCancelled(ITaskChainEngine engine) {

    }
}
