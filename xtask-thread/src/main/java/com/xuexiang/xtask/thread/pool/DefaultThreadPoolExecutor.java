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

import androidx.annotation.NonNull;

import com.xuexiang.xtask.thread.pool.base.BaseThreadPoolExecutor;
import com.xuexiang.xtask.thread.pool.cancel.IFuture;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 默认线程池
 *
 * @author xuexiang
 * @since 1/25/22 2:00 AM
 */
public class DefaultThreadPoolExecutor extends BaseThreadPoolExecutor {

    /**
     * 获取默认配置的优先级线程池
     *
     * @return 线程池
     */
    public static DefaultThreadPoolExecutor getDefault() {
        return new Builder().build();
    }

    /**
     * 获取优先级线程池的构建者
     *
     * @return 构建者
     */
    public static DefaultThreadPoolExecutor.Builder newBuilder() {
        return new Builder();
    }

    /**
     * 获取优先级线程池的构建者
     *
     * @param corePoolSize 核心(执行)线程池的线程数
     * @return 构建者
     */
    public static Builder newBuilder(int corePoolSize) {
        return new Builder(corePoolSize);
    }

    /**
     * 获取优先级线程池的构建者
     *
     * @param corePoolSize  核心(执行)线程池的线程数
     * @param maximumPoolSize 线程池最大能容纳的线程数
     * @param keepAliveTime 当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
     * @param unit          等待时长的单位
     * @return 构建者
     */
    public static Builder newBuilder(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        return new Builder(corePoolSize, maximumPoolSize, keepAliveTime, unit);
    }

    /**
     * 构造方法
     *
     * @param corePoolSize    核心(执行)线程池的线程数
     * @param maximumPoolSize 线程池最大能容纳的线程数
     * @param keepAliveTime   当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
     * @param unit            等待时长的单位
     * @param workQueue       线程池工作队列，用于在任务完成之前保留任务的队列执行
     * @param threadFactory   线程创建工厂
     * @param handler         拒绝执行的处理
     */
    private DefaultThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new DefaultFuture<>(callable);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new DefaultFuture<>(runnable, value);
    }

    @Override
    public IFuture<?> submit(@NonNull Runnable task) {
        return (IFuture<?>) super.submit(task);
    }

    /**
     * 默认Future
     *
     * @author xuexiang
     * @since 1/25/22 2:09 AM
     */
    public static class DefaultFuture<V> extends FutureTask<V> implements IFuture<V> {

        DefaultFuture(Callable<V> callable) {
            super(callable);
        }

        DefaultFuture(Runnable runnable, V result) {
            super(runnable, result);
        }

        @Override
        public void cancel() {
            cancel(true);
        }
    }


    //==============================构建者===================================//

    /**
     * 默认线程池构建者
     */
    public static final class Builder {
        /**
         * 默认核心线程数【非IO操作】
         */
        private static final int DEFAULT_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
        /**
         * 默认的等待时长
         */
        private static final long DEFAULT_KEEP_ALIVE_TIME = 30;
        /**
         * 默认线程工厂名
         */
        private static final String DEFAULT_FACTORY_NAME = "Default";
        /**
         * 默认阻塞队列的大小
         */
        private static final int DEFAULT_BLOCKING_QUEUE_SIZE = 128;
        /**
         * 核心(执行)线程池的线程数
         */
        int corePoolSize;
        /**
         * 线程池最大能容纳的线程数
         */
        int maximumPoolSize;
        /**
         * 当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
         */
        long keepAliveTime;
        /**
         * 等待时长的单位
         */
        TimeUnit unit;
        /**
         * 线程池工作队列，用于在任务完成之前保留任务的队列执行
         */
        BlockingQueue<Runnable> workQueue;
        /**
         * 线程创建工厂
         */
        ThreadFactory threadFactory;
        /**
         * 拒绝执行的处理
         */
        RejectedExecutionHandler handler;

        /**
         * 构造方法
         */
        public Builder() {
            this(DEFAULT_CORE_POOL_SIZE);
        }

        /**
         * 构造方法
         *
         * @param corePoolSize 核心(执行)线程池的线程数
         */
        public Builder(int corePoolSize) {
            this(corePoolSize, corePoolSize, DEFAULT_KEEP_ALIVE_TIME, TimeUnit.SECONDS);
        }

        /**
         * 构造方法
         *
         * @param corePoolSize    核心(执行)线程池的线程数
         * @param maximumPoolSize 线程池最大能容纳的线程数
         * @param keepAliveTime   当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
         * @param unit            等待时长的单位
         */
        public Builder(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
            this.unit = unit;
        }

        public Builder setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }

        public Builder setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
            return this;
        }

        public Builder setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
            return this;
        }

        public Builder setUnit(TimeUnit unit) {
            this.unit = unit;
            return this;
        }

        public Builder setWorkQueue(BlockingQueue<Runnable> workQueue) {
            this.workQueue = workQueue;
            return this;
        }

        public Builder setThreadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        public Builder setHandler(RejectedExecutionHandler handler) {
            this.handler = handler;
            return this;
        }

        /**
         * 构建
         *
         * @return 优先级线程池
         */
        public DefaultThreadPoolExecutor build() {
            if (workQueue == null) {
                workQueue = new LinkedBlockingQueue<>(DEFAULT_BLOCKING_QUEUE_SIZE);
            }
            if (threadFactory == null) {
                threadFactory = TaskThreadFactory.getFactory(DEFAULT_FACTORY_NAME);
            }
            if (handler == null) {
                handler = new TaskRecordPolicy();
            }
            return new DefaultThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }
    }
}
