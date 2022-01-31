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
import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtask.core.param.impl.TaskResult;
import com.xuexiang.xtask.core.step.ITaskStep;
import com.xuexiang.xtask.logger.TaskLogger;
import com.xuexiang.xtask.thread.pool.ICancelable;
import com.xuexiang.xtask.utils.CommonUtils;
import com.xuexiang.xtask.utils.TaskUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 默认实现的任务链执行引擎
 *
 * @author xuexiang
 * @since 2021/11/2 1:46 AM
 */
public class TaskChainEngine implements ITaskChainEngine {

    private static final String TAG = TaskLogger.getLogTag("TaskChainEngine");

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
    private List<ITaskStep> mTasks = new CopyOnWriteArrayList<>();

    /**
     * 任务链执行回调
     */
    private ITaskChainCallback mTaskChainCallback;

    /**
     * 获取任务链执行引擎
     *
     * @return 任务链执行引擎
     */
    public static TaskChainEngine get() {
        return new TaskChainEngine();
    }

    /**
     * 获取任务链执行引擎
     *
     * @param name 任务链名称
     * @return 任务链执行引擎
     */
    public static TaskChainEngine get(String name) {
        return new TaskChainEngine(name);
    }

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
        if (isDestroy()) {
            TaskLogger.eTag(TAG, "task chain setTaskChainCallback failed, task chain has destroyed!");
            return this;
        }
        mTaskChainCallback = iTaskChainCallback;
        return this;
    }

    @Override
    public ITaskChainEngine addTask(ITaskStep taskStep) {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, "task chain addTask failed, task chain has destroyed!");
            return this;
        }
        if (taskStep != null) {
            taskStep.setTaskStepLifecycle(this);
            mTasks.add(taskStep);
        }
        return this;
    }

    @Override
    public ITaskChainEngine addTasks(List<ITaskStep> taskStepList) {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, "task chain addTasks failed, task chain has destroyed!");
            return this;
        }
        if (!CommonUtils.isEmpty(taskStepList)) {
            for (ITaskStep taskStep : taskStepList) {
                addTask(taskStep);
            }
        }
        return this;
    }

    @Override
    public void clearTask() {
        if (isDestroy()) {
            return;
        }
        if (CommonUtils.isEmpty(mTasks)) {
            return;
        }
        for (ITaskStep taskStep : mTasks) {
            taskStep.recycle();
        }
        mTasks.clear();
    }

    @Override
    public void start() {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, "task chain start failed, task chain has destroyed!");
            return;
        }
        onTaskChainStart();
        ITaskStep taskStep = TaskUtils.findNextTaskStep(mTasks, null);
        if (taskStep != null) {
            ICancelable cancelable = TaskUtils.executeTask(taskStep);
            taskStep.setCancelable(cancelable);
        } else {
            onTaskChainCompleted(mResult);
        }
    }

    @Override
    public void reset() {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, "task chain reset failed, task chain has destroyed!");
            return;
        }
        mIsCanceled.set(false);
        mResult.clear();
        clearTask();
    }

    @Override
    public void destroy() {
        if (isDestroy()) {
            return;
        }
        reset();
        mTaskChainCallback = null;
        mResult = null;
        mTasks = null;
    }

    /**
     * 是否已经销毁
     *
     * @return 是否已经销毁
     */
    private boolean isDestroy() {
        return mResult == null || mTasks == null;
    }

    @Override
    public void cancel() {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, "task chain cancel failed, task chain has destroyed!");
            return;
        }
        if (isCancelled()) {
            return;
        }
        for (ITaskStep taskStep : mTasks) {
            taskStep.cancel();
        }
        mIsCanceled.set(true);
        onTaskChainCancelled();
    }

    @Override
    public boolean isCancelled() {
        return mIsCanceled.get();
    }

    @Override
    public void onTaskStepCompleted(@NonNull ITaskStep step, @NonNull ITaskResult result) {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, "task chain onTaskStepCompleted failed, task chain has destroyed!");
            return;
        }
        mResult.saveResult(result);
        ITaskStep nextTaskStep = TaskUtils.findNextTaskStep(mTasks, step);
        if (nextTaskStep != null) {
            // 更新数据，将上一个task的结果更新到下一个task
            nextTaskStep.prepareTask(mResult);
            ICancelable cancelable = TaskUtils.executeTask(nextTaskStep);
            nextTaskStep.setCancelable(cancelable);
        } else {
            onTaskChainCompleted(result);
        }
    }

    @Override
    public void onTaskStepError(@NonNull ITaskStep step, @NonNull ITaskResult result) {
        if (isDestroy()) {
            TaskLogger.eTag(TAG, "task chain onTaskStepError failed, task chain has destroyed!");
            return;
        }
        onTaskChainError(result);
    }

    private void onTaskChainStart() {
        TaskLogger.dTag(TAG, "task chain(size=" + CommonUtils.getSize(mTasks) + ") start...");
        if (mTaskChainCallback != null) {
            mTaskChainCallback.onTaskChainStart(this);
        }
    }

    private void onTaskChainCancelled() {
        TaskLogger.dTag(TAG, "task chain cancelled!");
        if (mTaskChainCallback != null) {
            mTaskChainCallback.onTaskChainCancelled(this);
        }
    }

    private void onTaskChainCompleted(ITaskResult result) {
        TaskLogger.dTag(TAG, "task chain completed!");
        if (mTaskChainCallback != null) {
            mTaskChainCallback.onTaskChainCompleted(this, result);
        }
    }

    private void onTaskChainError(ITaskResult result) {
        TaskLogger.dTag(TAG, "task chain error!");
        if (mTaskChainCallback != null) {
            mTaskChainCallback.onTaskChainError(this, result);
        }
    }
}
