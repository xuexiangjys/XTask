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

package com.xuexiang.xtask.thread.pool.base;

import com.xuexiang.xtask.logger.TaskLogger;
import com.xuexiang.xtask.thread.priority.IPriority;
import com.xuexiang.xtask.thread.utils.PriorityUtils;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * ScheduledThreadPoolExecutor线程池基类
 * <p>
 * maximumPoolSize：Integer.MAX_VALUE
 * keepAliveTime：10L
 * unit：MILLISECONDS
 * workQueue：DelayedWorkQueue
 *
 * @author xuexiang
 * @since 3/19/22 4:49 PM
 */
public class BaseScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    private static final String TAG = TaskLogger.getLogTag("BaseScheduledThreadPoolExecutor");

    /**
     * 构造方法
     *
     * @param corePoolSize 核心(执行)线程池的线程数
     */
    public BaseScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    /**
     * 构造方法
     *
     * @param corePoolSize  核心(执行)线程池的线程数
     * @param threadFactory 线程创建工厂
     */
    public BaseScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    /**
     * 构造方法
     *
     * @param corePoolSize 核心(执行)线程池的线程数
     * @param handler      拒绝执行的处理
     */
    public BaseScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    /**
     * 构造方法
     *
     * @param corePoolSize  核心(执行)线程池的线程数
     * @param threadFactory 线程创建工厂
     * @param handler       拒绝执行的处理
     */
    public BaseScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        if (t == null) {
            return;
        }
        if (!TaskLogger.isLogThreadName()) {
            return;
        }
        if (r instanceof IPriority) {
            TaskLogger.dTag(TAG, "Running task start execute, id:" + ((IPriority) r).getId() + ", priority:" + ((IPriority) r).priority() + ", in Thread [" + Thread.currentThread().getName() + "]");
        } else {
            TaskLogger.dTag(TAG, "Running task start execute, in Thread [" + Thread.currentThread().getName() + "]");
        }
    }

    /**
     * 线程执行结束，顺便看一下有么有什么乱七八糟的异常
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t == null && r instanceof Future<?> && ((Future<?>) r).isDone()) {
            try {
                ((Future<?>) r).get();
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            TaskLogger.wTag(TAG, "Running task appeared exception! Thread [" + Thread.currentThread().getName() + "], because [" + t.getMessage() + "]\n" + PriorityUtils.formatStackTrace(t.getStackTrace()));
        }
    }
}
