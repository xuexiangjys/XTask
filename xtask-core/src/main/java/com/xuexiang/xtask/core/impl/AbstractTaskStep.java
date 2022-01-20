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

import com.xuexiang.xtask.thread.ICancelable;
import com.xuexiang.xtask.core.ITaskChainEngine;
import com.xuexiang.xtask.core.ITaskParam;
import com.xuexiang.xtask.core.ITaskResult;
import com.xuexiang.xtask.core.ITaskStep;
import com.xuexiang.xtask.core.ITaskStepHandler;
import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.logger.TaskLogger;

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
     * 是否正在运行
     */
    private AtomicBoolean mIsRunning = new AtomicBoolean(false);

    /**
     * 是否取消
     */
    private AtomicBoolean mIsCanceled = new AtomicBoolean(false);

    /**
     * 任务链执行引擎
     */
    private ITaskChainEngine mEngine;
    /**
     * 线程执行类型
     */
    private ThreadType mThreadType;
    /**
     * 任务参数
     */
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
     *
     * @param engine      任务链执行引擎
     * @param taskParam   任务参数
     * @param taskHandler 任务执行处理者
     */
    public AbstractTaskStep(ITaskChainEngine engine, ITaskParam taskParam, ITaskStepHandler taskHandler) {
        this(engine, ThreadType.ASYNC, taskParam, taskHandler);
    }

    /**
     * 构造方法
     *
     * @param engine      任务链执行引擎
     * @param threadType  线程类型
     * @param taskParam   任务参数
     * @param taskHandler 任务执行处理者
     */
    public AbstractTaskStep(ITaskChainEngine engine, ThreadType threadType, ITaskParam taskParam, ITaskStepHandler taskHandler) {
        mEngine = engine;
        mThreadType = threadType;
        mTaskParam = taskParam;
        mTaskHandler = taskHandler;
    }

    @Override
    public ThreadType getThreadType() {
        return mThreadType;
    }

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
    public boolean accept() {
        if (mTaskHandler != null) {
            return mTaskHandler.accept(this);
        }
        return true;
    }

    @Override
    public void onTaskSucceed(ITaskResult result) {
        if (mTaskHandler != null) {
            mTaskHandler.handleTaskSucceed(this);
        }
        if (mEngine != null) {
            mEngine.onTaskStepCompleted(this, result);
        }
        mIsRunning.set(false);
    }

    @Override
    public void onTaskFailed(ITaskResult result) {
        if (mTaskHandler != null) {
            mTaskHandler.handleTaskFailed(this);
        }
        if (mEngine != null) {
            mEngine.onTaskStepError(this, result);
        }
        mIsRunning.set(false);
    }

    @Override
    public void cancel() {
        if (isCancelled() || !isRunning()) {
            return;
        }
        if (mCancelable != null) {
            mCancelable.cancel();
        }
        mIsCanceled.set(true);
    }

    @Override
    public boolean isCancelled() {
        return mIsCanceled.get();
    }

    @Override
    public void run() {
        if (isCancelled()) {
            TaskLogger.wTag(TAG, getTaskLogName() + " has canceled！");
            return;
        }
        setIsRunning(true);
        try {
            processTask();
        } catch (Exception e) {
            TaskLogger.eTag(TAG, getTaskLogName() + " has error！", e);
        }
        mTaskParam.addPath(getName());
        TaskLogger.dTag(TAG, getTaskLogName() + " has run: " + mTaskParam.getPath());
    }

    /**
     * 执行任务
     *
     * @throws Exception 异常
     */
    protected void processTask() throws Exception {
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
     * 获取任务的日志名称
     *
     * @return 任务的日志名称
     */
    protected String getTaskLogName() {
        return "Task step [" + getName() + "]";
    }

}
