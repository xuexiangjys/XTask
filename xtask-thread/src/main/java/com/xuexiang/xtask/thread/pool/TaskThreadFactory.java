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

package com.xuexiang.xtask.thread.pool;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.xuexiang.xtask.logger.TaskLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程创建工厂
 *
 * @author xuexiang
 * @since 2021/10/9 4:01 PM
 */
public class TaskThreadFactory implements ThreadFactory {

    private static final String TAG = TaskLogger.getLogTag("TaskThreadFactory");

    /**
     * 线程池的编号池【静态全局】<优先级，线程池的编号>
     */
    private static final Map<Integer, AtomicInteger> POOL_NUMBER_MAP = new ConcurrentHashMap<>();
    /**
     * 线程组
     */
    private final ThreadGroup mThreadGroup;
    /**
     * 线程的编号
     */
    private final AtomicInteger mThreadNumber = new AtomicInteger(1);
    /**
     * 线程名前缀
     */
    private final String mNamePrefix;
    /**
     * 创建的线程优先级
     */
    private final int mPriority;

    /**
     * 获取线程创建工厂
     *
     * @param factoryName 工厂名
     * @return 线程创建工厂
     */
    public static TaskThreadFactory getFactory(@NonNull String factoryName) {
        return new TaskThreadFactory(factoryName, Thread.NORM_PRIORITY);
    }

    /**
     * 获取线程创建工厂
     *
     * @param factoryName 工厂名
     * @param priority    线程的优先级
     * @return 线程创建工厂
     */
    public static TaskThreadFactory getFactory(@NonNull String factoryName, @IntRange(from = Thread.MIN_PRIORITY, to = Thread.MAX_PRIORITY) int priority) {
        return new TaskThreadFactory(factoryName, priority);
    }

    /**
     * 构造方法
     *
     * @param factoryName 工厂名
     * @param priority    线程的优先级
     */
    private TaskThreadFactory(@NonNull String factoryName, @IntRange(from = Thread.MIN_PRIORITY, to = Thread.MAX_PRIORITY) int priority) {
        SecurityManager securityManager = System.getSecurityManager();
        mThreadGroup = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        mNamePrefix = factoryName + "-task-pool(" + priority + ") No." + getTaskPoolNumber(priority) + ", thread No.";
        mPriority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadName = mNamePrefix + mThreadNumber.getAndIncrement();
        TaskLogger.iTag(TAG, "Thread production, name is [" + threadName + "]");
        Thread thread = new Thread(mThreadGroup, r, threadName, 0);
        if (thread.isDaemon()) {
            // 设置为非守护线程
            thread.setDaemon(false);
        }
        if (thread.getPriority() != mPriority) {
            thread.setPriority(mPriority);
        }
        // 捕获多线程处理中的异常
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                TaskLogger.eTag(TAG, "Running task appeared exception! Thread [" + thread.getName() + "], because [" + ex.getMessage() + "]");
            }
        });
        return thread;
    }

    private int getTaskPoolNumber(int priority) {
        AtomicInteger poolNumber = POOL_NUMBER_MAP.get(priority);
        if (poolNumber == null) {
            poolNumber = new AtomicInteger(1);
            POOL_NUMBER_MAP.put(priority, poolNumber);
        }
        return poolNumber.getAndIncrement();
    }
}
