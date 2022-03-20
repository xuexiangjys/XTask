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

import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtask.core.param.impl.TaskParam;
import com.xuexiang.xtask.core.param.impl.TaskResult;
import com.xuexiang.xtask.core.step.ITaskStep;
import com.xuexiang.xtask.core.step.ITaskStepHandler;
import com.xuexiang.xtask.core.step.ITaskStepLifecycle;
import com.xuexiang.xtask.logger.TaskLogger;
import com.xuexiang.xtask.thread.pool.cancel.ICancelable;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象任务执行步骤
 *
 * @author xuexiang
 * @since 2021/11/2 1:22 AM
 */
public abstract class AbstractTaskStep implements ITaskStep {

    private static final String TAG = TaskLogger.getLogTag("AbstractTaskStep");

    /**
     * 是否正在等待
     */
    private AtomicBoolean mIsPending = new AtomicBoolean(true);
    /**
     * 是否正在运行
     */
    private AtomicBoolean mIsRunning = new AtomicBoolean(false);
    /**
     * 是否取消
     */
    private AtomicBoolean mIsCancelled = new AtomicBoolean(false);
    /**
     * 是否已经通知执行结果
     */
    private AtomicBoolean mIsNotified = new AtomicBoolean(false);
    /**
     * 任务步骤的生命周期管理
     */
    private ITaskStepLifecycle mTaskStepLifecycle;
    /**
     * 线程执行类型
     */
    private ThreadType mThreadType;
    /**
     * 任务参数
     */
    @NonNull
    private ITaskParam mTaskParam;
    /**
     * 任务处理
     */
    private ITaskStepHandler mTaskHandler;
    /**
     * 任务取消接口
     */
    private ICancelable mCancelable;

    /**
     * 构造方法
     */
    public AbstractTaskStep() {
        this(ThreadType.ASYNC, TaskParam.get());
    }

    /**
     * 构造方法
     *
     * @param threadType 线程类型
     */
    public AbstractTaskStep(ThreadType threadType) {
        this(threadType, TaskParam.get());
    }

    /**
     * 构造方法
     *
     * @param taskParam 任务参数
     */
    public AbstractTaskStep(@NonNull ITaskParam taskParam) {
        this(ThreadType.ASYNC, taskParam);
    }

    /**
     * 构造方法
     *
     * @param threadType 线程类型
     * @param taskParam  任务参数
     */
    public AbstractTaskStep(ThreadType threadType, @NonNull ITaskParam taskParam) {
        mThreadType = threadType;
        mTaskParam = taskParam;
    }

    @Override
    public AbstractTaskStep setTaskStepLifecycle(@NonNull ITaskStepLifecycle taskStepLifecycle) {
        mTaskStepLifecycle = taskStepLifecycle;
        return this;
    }

    @Override
    public AbstractTaskStep setTaskStepHandler(@NonNull ITaskStepHandler taskStepHandler) {
        mTaskHandler = taskStepHandler;
        return this;
    }

    @Override
    public AbstractTaskStep setThreadType(@NonNull ThreadType threadType) {
        mThreadType = threadType;
        return this;
    }

    @Override
    public AbstractTaskStep setTaskParam(@NonNull ITaskParam taskParam) {
        mTaskParam = taskParam;
        return this;
    }

    @NonNull
    @Override
    public ThreadType getThreadType() {
        return mThreadType;
    }

    @NonNull
    @Override
    public ITaskParam getTaskParam() {
        return mTaskParam;
    }

    @Override
    public void setCancelable(ICancelable cancelable) {
        mCancelable = cancelable;
    }

    @Override
    public void setIsRunning(boolean isRunning) {
        mIsRunning.set(isRunning);
    }

    @Override
    public boolean isRunning() {
        return mIsRunning.get();
    }

    @Override
    public boolean isPending() {
        return mIsPending.get();
    }

    @Override
    public boolean accept() {
        if (mTaskHandler != null) {
            return mTaskHandler.accept(this);
        }
        return true;
    }

    @Override
    public void prepareTask(TaskParam taskParam) {
        getTaskParam().updateParam(taskParam);
    }

    @Override
    public void notifyTaskSucceed(@NonNull ITaskResult result) {
        if (mIsNotified.get()) {
            TaskLogger.wTag(TAG, getTaskLogName() + " has notified！");
            return;
        }
        mIsNotified.set(true);
        mIsRunning.set(false);
        if (isCancelled()) {
            TaskLogger.wTag(TAG, getTaskLogName() + " has cancelled！");
            return;
        }
        TaskLogger.dTag(TAG, getTaskLogName() + " succeed!");
        if (mTaskHandler != null) {
            mTaskHandler.handleTaskSucceed(this);
        }
        if (mTaskStepLifecycle != null) {
            result.updateParam(getTaskParam());
            mTaskStepLifecycle.onTaskStepCompleted(this, result);
        }
    }

    @Override
    public void notifyTaskFailed(@NonNull ITaskResult result) {
        if (mIsNotified.get()) {
            TaskLogger.wTag(TAG, getTaskLogName() + " has notified！");
            return;
        }
        mIsNotified.set(true);
        mIsRunning.set(false);
        TaskLogger.eTag(TAG, getTaskLogName() + " failed, " + result.getDetailMessage());
        if (mTaskHandler != null) {
            mTaskHandler.handleTaskFailed(this);
        }
        if (mTaskStepLifecycle != null) {
            result.updateParam(getTaskParam());
            mTaskStepLifecycle.onTaskStepError(this, result);
        }
    }

    @Override
    public void recycle() {
        TaskLogger.dTag(TAG, getTaskLogName() + " recycle...");
        if (isRunning() && !isCancelled()) {
            cancel();
        }
        mTaskParam.clear();
        mTaskStepLifecycle = null;
        mTaskHandler = null;
        mCancelable = null;
    }

    @Override
    public void cancel() {
        if (isCancelled()) {
            return;
        }
        if (isPending() || isRunning()) {
            TaskLogger.dTag(TAG, getTaskLogName() + " cancel...");
        }
        if (mCancelable != null) {
            mCancelable.cancel();
        }
        mIsCancelled.set(true);
        if (mTaskHandler != null) {
            mTaskHandler.handleTaskCancelled(this);
        }
    }

    @Override
    public boolean isCancelled() {
        return mIsCancelled.get();
    }

    @Override
    public void run() {
        if (isCancelled()) {
            TaskLogger.wTag(TAG, getTaskLogName() + " has cancelled, do not need to run！");
            return;
        }
        setIsRunning(true);
        try {
            processTask();
        } catch (Exception e) {
            TaskLogger.eTag(TAG, getTaskLogName() + " has error！", e);
            if (mTaskHandler != null) {
                mTaskHandler.onTaskException(this, e);
            }
        }
    }

    /**
     * 执行任务
     *
     * @throws Exception 异常
     */
    protected void processTask() throws Exception {
        updateProcessTaskPath();
        if (mTaskHandler != null) {
            mTaskHandler.beforeTask(this);
        }
        if (isRunning()) {
            doTask();
        }
        if (mTaskHandler != null) {
            mTaskHandler.afterTask(this);
        }
    }

    /**
     * 更新任务处理的路径
     */
    private void updateProcessTaskPath() {
        getTaskParam().addPath(getName());
        TaskLogger.dTag(TAG, getTaskRunningDetailLogInfo());
        mIsPending.set(false);
    }

    private String getTaskRunningDetailLogInfo() {
        if (TaskLogger.isLogThreadName()) {
            return getTaskLogName() + " has run, thread: " + Thread.currentThread().getName() + ", path:" + getTaskParam().getPath();
        } else {
            return getTaskLogName() + " has run, path: " + getTaskParam().getPath();
        }
    }

    /**
     * 获取任务的日志名称
     *
     * @return 任务的日志名称
     */
    protected String getTaskLogName() {
        return "Task step [" + getName() + "]";
    }

    // ==================默认提供的通知方法=========================//

    /**
     * 通知任务链任务步骤执行完毕
     */
    public void notifyTaskSucceed() {
        notifyTaskSucceed(TaskResult.succeed());
    }

    /**
     * 通知任务链任务步骤执行失败
     */
    public void notifyTaskFailed() {
        notifyTaskFailed(TaskResult.failed());
    }

    /**
     * 通知任务链任务步骤执行失败
     *
     * @param code 失败的错误码
     */
    public void notifyTaskFailed(int code) {
        notifyTaskFailed(TaskResult.failed(code));
    }

    /**
     * 通知任务链任务步骤执行失败
     *
     * @param code    失败的错误码
     * @param message 错误信息
     */
    public void notifyTaskFailed(int code, String message) {
        notifyTaskFailed(TaskResult.failed(code, message));
    }

}
