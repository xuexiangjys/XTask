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

package com.xuexiang.xtask.thread.executor.impl;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xuexiang.xtask.thread.executor.ICategoryExecutorCore;
import com.xuexiang.xtask.thread.pool.DefaultThreadPoolExecutor;
import com.xuexiang.xtask.thread.pool.TaskThreadFactory;
import com.xuexiang.xtask.thread.pool.cancel.CancelHandlerRunnable;
import com.xuexiang.xtask.thread.pool.cancel.ICancelable;
import com.xuexiang.xtask.thread.utils.ExecutorUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

/**
 * 不同类别的执行者内核实现
 * <p>
 * Emergent: 核心线程数为2，最大线程为∞，60s keepTime，SynchronousQueue（不阻塞），线程优先级10
 * Normal: 核心线程数和最大线程为CPU数，0s keepTime，LinkedBlockingQueue（128），线程优先级5
 * Background: 核心线程数和最大线程为2，0s keepTime，LinkedBlockingQueue（128），线程优先级1
 * <p>
 * 线程组: 核心线程数和最大线程为2～4之间，30s keepTime，LinkedBlockingQueue（128），线程优先级5
 * io: 核心线程数和最大线程为(2*CPU数+1)，30s keepTime，LinkedBlockingQueue（128），线程优先级5
 *
 * @author xuexiang
 * @since 1/26/22 2:33 AM
 */
public class CategoryExecutorCore implements ICategoryExecutorCore {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = 2;
    private static final int GROUP_CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int IO_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int EMERGENT_KEEP_ALIVE_SECONDS = 60;
    private static final String GROUP_FACTORY_NAME_PREFIX = "CategoryGroup-";

    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
    /**
     * 紧急线程池，核心线程数为2，最大线程为∞，60s keepTime，SynchronousQueue（不阻塞），线程优先级10
     */
    private DefaultThreadPoolExecutor mEmergentExecutor;
    /**
     * 普通线程池，核心线程数和最大线程为CPU数，0s keepTime，LinkedBlockingQueue（128），线程优先级5
     */
    private DefaultThreadPoolExecutor mNormalExecutor;
    /**
     * 后台线程池，核心线程数和最大线程为2，0s keepTime，LinkedBlockingQueue（128），线程优先级1
     */
    private DefaultThreadPoolExecutor mBackgroundExecutor;
    /**
     * 组线程池，核心线程数和最大线程为2～4之间，30s keepTime，LinkedBlockingQueue（128），线程优先级5
     */
    private Map<String, DefaultThreadPoolExecutor> mGroupExecutorMap = new ConcurrentHashMap<>();
    /**
     * io线程池，核心线程数和最大线程为(2*CPU数+1)，30s keepTime，LinkedBlockingQueue（128），线程优先级5
     */
    private DefaultThreadPoolExecutor mIoExecutor;

    @Override
    public boolean postToMain(Runnable task) {
        if (ExecutorUtils.isMainThread()) {
            task.run();
            return true;
        }
        return mMainHandler.post(task);
    }

    @Override
    public ICancelable postToMainDelay(Runnable task, long delayMillis) {
        return CancelHandlerRunnable.get(mMainHandler, task).startDelayed(delayMillis);
    }

    @Override
    public ICancelable emergentSubmit(Runnable task) {
        return submitTask(getTargetExecutor(Thread.MAX_PRIORITY), task);
    }

    @Override
    public ICancelable submit(Runnable task) {
        return submitTask(getTargetExecutor(Thread.NORM_PRIORITY), task);
    }

    @Override
    public ICancelable backgroundSubmit(Runnable task) {
        return submitTask(getTargetExecutor(Thread.MIN_PRIORITY), task);
    }

    @Override
    public ICancelable ioSubmit(Runnable task) {
        return submitTask(getIoExecutor(), task);
    }

    @Override
    public ICancelable groupSubmit(String groupName, Runnable task) {
        return submitTask(getGroupExecutor(groupName), task);
    }

    @Override
    public void shutdown() {
        mMainHandler.removeCallbacksAndMessages(null);
        ExecutorUtils.shutdown(mEmergentExecutor);
        mEmergentExecutor = null;
        ExecutorUtils.shutdown(mNormalExecutor);
        mNormalExecutor = null;
        ExecutorUtils.shutdown(mBackgroundExecutor);
        mBackgroundExecutor = null;
        ExecutorUtils.shutdown(mIoExecutor);
        mIoExecutor = null;
        ExecutorUtils.shutdown(mGroupExecutorMap.values());
        mGroupExecutorMap.clear();
    }

    /**
     * 提交任务
     *
     * @param executor 线程池
     * @param task     任务
     * @return 取消执行的接口
     */
    private ICancelable submitTask(@NonNull DefaultThreadPoolExecutor executor, Runnable task) {
        return executor.submit(task);
    }

    /**
     * 获取线程池
     *
     * @param priority 优先级
     * @return 线程池
     */
    @NonNull
    private DefaultThreadPoolExecutor getTargetExecutor(int priority) {
        if (priority == Thread.MAX_PRIORITY) {
            return getEmergentExecutor();
        } else if (priority == Thread.NORM_PRIORITY) {
            return getNormalExecutor();
        } else {
            return getBackgroundExecutor(priority);
        }
    }

    private DefaultThreadPoolExecutor getEmergentExecutor() {
        if (mEmergentExecutor == null) {
            mEmergentExecutor = DefaultThreadPoolExecutor.newBuilder(CORE_POOL_SIZE)
                    .setMaximumPoolSize(Integer.MAX_VALUE)
                    .setKeepAliveTime(EMERGENT_KEEP_ALIVE_SECONDS)
                    .setWorkQueue(new SynchronousQueue<Runnable>())
                    .setThreadFactory(TaskThreadFactory.getFactory("Emergent", Thread.MAX_PRIORITY))
                    .build();
        }
        return mEmergentExecutor;
    }

    private DefaultThreadPoolExecutor getNormalExecutor() {
        if (mNormalExecutor == null) {
            mNormalExecutor = DefaultThreadPoolExecutor.newBuilder(CPU_COUNT)
                    .setKeepAliveTime(0)
                    .setThreadFactory(TaskThreadFactory.getFactory("Normal", Thread.NORM_PRIORITY))
                    .build();
        }
        return mNormalExecutor;
    }

    private DefaultThreadPoolExecutor getBackgroundExecutor(int priority) {
        if (mBackgroundExecutor == null) {
            mBackgroundExecutor = DefaultThreadPoolExecutor.newBuilder(CORE_POOL_SIZE)
                    .setKeepAliveTime(0)
                    .setThreadFactory(TaskThreadFactory.getFactory("Background", priority))
                    .build();
        }
        return mBackgroundExecutor;
    }

    private DefaultThreadPoolExecutor getIoExecutor() {
        if (mIoExecutor == null) {
            mIoExecutor = DefaultThreadPoolExecutor.newBuilder(IO_POOL_SIZE)
                    .setThreadFactory(TaskThreadFactory.getFactory("Io", Thread.NORM_PRIORITY))
                    .build();
        }
        return mIoExecutor;
    }

    /**
     * 获取组线程池
     *
     * @param groupName 组名称
     * @return 线程池
     */
    @NonNull
    private DefaultThreadPoolExecutor getGroupExecutor(String groupName) {
        if (TextUtils.isEmpty(groupName)) {
            return getTargetExecutor(Thread.NORM_PRIORITY);
        } else {
            DefaultThreadPoolExecutor executor = mGroupExecutorMap.get(groupName);
            if (executor == null) {
                executor = DefaultThreadPoolExecutor.newBuilder(GROUP_CORE_POOL_SIZE)
                        .setThreadFactory(TaskThreadFactory.getFactory(GROUP_FACTORY_NAME_PREFIX + groupName))
                        .build();
                mGroupExecutorMap.put(groupName, executor);
            }
            return executor;
        }
    }

}
