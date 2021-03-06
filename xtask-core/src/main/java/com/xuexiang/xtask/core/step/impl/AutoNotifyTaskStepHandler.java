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

import androidx.annotation.NonNull;

import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtask.core.param.impl.TaskResult;
import com.xuexiang.xtask.core.step.ITaskStep;
import com.xuexiang.xtask.core.step.ITaskStepHandler;

/**
 * 自动通知任务执行结果处理者
 *
 * @author xuexiang
 * @since 2021/11/2 1:48 AM
 */
public class AutoNotifyTaskStepHandler implements ITaskStepHandler {
    @Override
    public void beforeTask(@NonNull ITaskStep step) {

    }

    @Override
    public void afterTask(@NonNull ITaskStep step) {
        step.notifyTaskSucceed(TaskResult.succeed());
    }

    @Override
    public void onTaskException(@NonNull ITaskStep step, Exception exception) {
        step.notifyTaskFailed(TaskResult.failed(ITaskResult.PROCESS_TASK_THROW_EXCEPTION, exception.getMessage()));
    }

    @Override
    public boolean accept(@NonNull ITaskStep step) {
        return true;
    }

    @Override
    public void handleTaskSucceed(@NonNull ITaskStep step) {

    }

    @Override
    public void handleTaskFailed(@NonNull ITaskStep step) {

    }

    @Override
    public void handleTaskCancelled(@NonNull ITaskStep step) {

    }
}
