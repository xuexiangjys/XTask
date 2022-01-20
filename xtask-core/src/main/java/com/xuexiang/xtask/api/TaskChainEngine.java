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

package com.xuexiang.xtask.api;

import androidx.annotation.NonNull;

import com.xuexiang.xtask.core.ITaskChainCallback;
import com.xuexiang.xtask.core.ITaskChainEngine;
import com.xuexiang.xtask.core.ITaskResult;
import com.xuexiang.xtask.core.ITaskStep;
import com.xuexiang.xtask.core.impl.TaskResult;
import com.xuexiang.xtask.utils.TaskUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 默认实现的任务链执行引擎
 *
 * @author xuexiang
 * @since 2021/11/2 1:46 AM
 */
public class TaskChainEngine implements ITaskChainEngine {

    /**
     * 任务链名前缀
     */
    private static final String TASK_CHAIN_NAME_PREFIX = "TaskChain-";

    /**
     * 是否取消
     */
    private AtomicBoolean mIsCanceled = new AtomicBoolean(false);

    /**
     * 任务链名称
     */
    private String mName;

    /**
     * 任务结果
     */
    private TaskResult mResult = new TaskResult();

    /**
     * 执行任务集合
     */
    private List<ITaskStep> mTasks = new ArrayList<>();

    /**
     * 任务链执行回调
     */
    private ITaskChainCallback mTaskChainCallback;

    /**
     * 构造方法
     */
    public TaskChainEngine() {
        this(TASK_CHAIN_NAME_PREFIX + UUID.randomUUID().toString());
    }

    /**
     * 构造方法
     *
     * @param name 任务链名称
     */
    public TaskChainEngine(String name) {
        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public ITaskChainEngine setTaskChainCallback(ITaskChainCallback iTaskChainCallback) {
        mTaskChainCallback = iTaskChainCallback;
        return this;
    }

    @Override
    public ITaskChainEngine addTask(ITaskStep taskStep) {
        if (taskStep != null) {
            mTasks.add(taskStep);
        }
        return this;
    }

    @Override
    public ITaskChainEngine addTasks(List<ITaskStep> taskStepList) {
        if (!TaskUtils.isEmpty(taskStepList)) {
            for (ITaskStep taskStep : taskStepList) {
                addTask(taskStep);
            }
        }
        return this;
    }

    @Override
    public void start() {
        ITaskStep taskStep = TaskUtils.findNextTaskStep(mTasks, null);
        if (taskStep != null) {
            TaskUtils.executeTask(taskStep);
        } else {

        }
    }

    @Override
    public void cancel() {
        if (isCancelled()) {
            return;
        }
        for (ITaskStep taskStep : mTasks) {
            taskStep.cancel();
        }
        mIsCanceled.set(true);
        if (mTaskChainCallback != null) {
            mTaskChainCallback.onTaskChainCanceled(this);
        }
    }

    @Override
    public boolean isCancelled() {
        return mIsCanceled.get();
    }

    @Override
    public void onTaskStepCompleted(@NonNull ITaskStep step, @NonNull ITaskResult result) {
        mResult.saveResult(result);
        ITaskStep nextTaskStep = TaskUtils.findNextTaskStep(mTasks, step);
        if (nextTaskStep != null) {

        } else {
            onTaskChainCompleted(result);
        }
    }

    private void onTaskChainCompleted(ITaskResult result) {
        if (mTaskChainCallback != null) {
            mTaskChainCallback.onTaskChainCompleted(this, result);
        }
    }

    @Override
    public void onTaskStepError(@NonNull ITaskStep step, @NonNull ITaskResult result) {


    }

    private void onTaskChainError(ITaskResult result) {
        if (mTaskChainCallback != null) {
            mTaskChainCallback.onTaskChainError(this, result);
        }
    }

}
