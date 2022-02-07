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

package com.xuexiang.xtask.api.step;

import androidx.annotation.NonNull;

import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtask.core.step.ITaskStep;
import com.xuexiang.xtask.core.step.impl.AbstractGroupTaskStep;

/**
 * 并行任务组(不进行具体的任务）
 *
 * @author xuexiang
 * @since 2021/11/8 2:04 AM
 */
public class ConcurrentGroupTaskStep extends AbstractGroupTaskStep {

    public ConcurrentGroupTaskStep(String name) {
        super(name);
    }

    public ConcurrentGroupTaskStep(String name, @NonNull ITaskParam taskParam) {
        super(name, taskParam);
    }

    @Override
    public void doTask() throws Exception {

    }


    @Override
    public void onTaskStepCompleted(@NonNull ITaskStep step, @NonNull ITaskResult result) {

    }

    @Override
    public void onTaskStepError(@NonNull ITaskStep step, @NonNull ITaskResult result) {

    }
}
