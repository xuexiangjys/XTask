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

import com.xuexiang.xtask.thread.pool.base.BaseScheduledThreadPoolExecutor;
import com.xuexiang.xtask.thread.pool.cancel.IScheduledFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 默认的定时线程池
 *
 * @author xuexiang
 * @since 3/19/22 5:41 PM
 */
public class DefaultScheduledThreadPoolExecutor extends BaseScheduledThreadPoolExecutor {

    /**
     * 获取默认配置的优先级线程池
     *
     * @return 线程池
     */
    public static DefaultScheduledThreadPoolExecutor getDefault() {
        return new Builder().build();
    }

    /**
     * 获取优先级线程池的构建者
     *
     * @return 构建者
     */
    public static Builder newBuilder() {
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
     * 构造方法
     *
     * @param corePoolSize  核心(执行)线程池的线程数
     * @param threadFactory 线程创建工厂
     * @param handler       拒绝执行的处理
     */
    public DefaultScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
        return new DefaultScheduledFuture<>(callable, task);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        return new DefaultScheduledFuture<>(runnable, task);
    }

    @Override
    public IScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return (IScheduledFuture<?>) super.schedule(command, delay, unit);
    }

    @Override
    public IScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return (IScheduledFuture<?>) super.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @Override
    public IScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return (IScheduledFuture<?>) super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    /**
     * 默认ScheduledFuture
     *
     * @author xuexiang
     * @since 3/19/22 5:52 PM
     */
    public static class DefaultScheduledFuture<V> extends FutureTask<V> implements IScheduledFuture<V> {

        private RunnableScheduledFuture<V> mTask;

        DefaultScheduledFuture(Callable<V> callable, RunnableScheduledFuture<V> task) {
            super(callable);
            mTask = task;
        }

        DefaultScheduledFuture(Runnable runnable, RunnableScheduledFuture<V> task) {
            super(runnable, null);
            mTask = task;
        }

        @Override
        public void cancel() {
            cancel(true);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return mTask.getDelay(unit);
        }

        @Override
        public int compareTo(Delayed o) {
            return mTask.compareTo(o);
        }

        @Override
        public boolean isPeriodic() {
            return mTask.isPeriodic();
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return mTask.cancel(mayInterruptIfRunning);
        }

        @Override
        public void run() {
            mTask.run();
        }
    }

    //==============================构建者===================================//

    /**
     * 默认线程池构建者
     */
    public static final class Builder {
        /**
         * 默认核心线程数
         */
        private static final int DEFAULT_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
        /**
         * 默认线程工厂名
         */
        private static final String DEFAULT_FACTORY_NAME = "DefaultScheduled";
        /**
         * 核心(执行)线程池的线程数
         */
        int corePoolSize;
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
            this.corePoolSize = corePoolSize;
        }

        public Builder setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
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
        public DefaultScheduledThreadPoolExecutor build() {
            if (threadFactory == null) {
                threadFactory = TaskThreadFactory.getFactory(DEFAULT_FACTORY_NAME);
            }
            if (handler == null) {
                handler = new TaskRecordPolicy();
            }
            return new DefaultScheduledThreadPoolExecutor(corePoolSize, threadFactory, handler);
        }
    }

}
