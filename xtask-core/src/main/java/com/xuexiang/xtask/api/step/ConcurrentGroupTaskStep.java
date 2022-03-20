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
import com.xuexiang.xtask.thread.pool.cancel.ICancelable;
import com.xuexiang.xtask.utils.TaskUtils;

/**
 * 并行任务组(不进行具体的任务）
 *
 * @author xuexiang
 * @since 2021/11/8 2:04 AM
 */
public class ConcurrentGroupTaskStep extends AbstractGroupTaskStep {

    /**
     * 获取并行任务组
     *
     * @return 并行任务组
     */
    public static ConcurrentGroupTaskStep get() {
        return new ConcurrentGroupTaskStep();
    }

    /**
     * 获取并行任务组
     *
     * @param name 任务组名称
     * @return 并行任务组
     */
    public static ConcurrentGroupTaskStep get(String name) {
        return new ConcurrentGroupTaskStep(name);
    }

    public ConcurrentGroupTaskStep() {
        super();
    }

    public ConcurrentGroupTaskStep(String name) {
        super(name);
    }

    public ConcurrentGroupTaskStep(String name, @NonNull ITaskParam taskParam) {
        super(name, taskParam);
    }

    @Override
    public void doTask() throws Exception {
        initGroupTask();
        if (mTaskTotalSize > 0) {
            for (ITaskStep taskStep : getTasks()) {
                if (taskStep != null && taskStep.accept()) {
                    taskStep.prepareTask(getResult());
                    ICancelable cancelable = TaskUtils.executeTask(taskStep);
                    taskStep.setCancelable(cancelable);
                }
            }
        } else {
            notifyTaskSucceed(getResult());
        }
    }

    @Override
    public void onTaskStepCompleted(@NonNull ITaskStep step, @NonNull ITaskResult result) {
        getResult().saveResultNotPath(result);
        getResult().addGroupPath(step.getName(), mTaskIndex.getAndIncrement(), mTaskTotalSize);
        if (mTaskIndex.get() == mTaskTotalSize) {
            notifyTaskSucceed(result);
        }
    }

    @Override
    public void onTaskStepError(@NonNull ITaskStep step, @NonNull ITaskResult result) {
        if (mTaskIndex.get() != -1) {
            // 并行任务，只通知一次失败
            notifyTaskFailed(result);
            mTaskIndex.set(-1);
        }
    }
}
