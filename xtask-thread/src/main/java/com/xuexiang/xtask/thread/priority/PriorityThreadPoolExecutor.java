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

package com.xuexiang.xtask.thread.priority;

import com.xuexiang.xtask.logger.TaskLogger;
import com.xuexiang.xtask.thread.priority.impl.DefaultPriorityCallable;
import com.xuexiang.xtask.thread.priority.impl.DefaultPriorityFuture;
import com.xuexiang.xtask.thread.priority.impl.DefaultPriorityRunnable;
import com.xuexiang.xtask.thread.priority.impl.Priority;
import com.xuexiang.xtask.thread.utils.PriorityUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 一个具有指定和动态调整任务优先级能力的Java线程池
 *
 * @author xuexiang
 * @since 2021/10/9 2:28 AM
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    private static final String TAG = TaskLogger.getLogTag("PriorityThreadPoolExecutor");

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
     * 获取优先级线程池的构建者
     *
     * @param corePoolSize    核心(执行)线程池的线程数
     * @param maximumPoolSize 线程池最大能容纳的线程数
     * @param keepAliveTime   当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
     * @param unit            等待时长的单位
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
    private PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, PriorityBlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * 指定优先级执行Runnable
     *
     * @param command  命令
     * @param priority 优先级
     * @return IPriorityRunnable
     */
    public IPriorityRunnable execute(Runnable command, int priority) {
        if (command instanceof IPriorityRunnable) {
            this.execute(command);
            return (IPriorityRunnable) command;
        }
        IPriorityRunnable runnable = new DefaultPriorityRunnable(new Priority(priority), command);
        this.execute(runnable);
        return runnable;
    }

    /**
     * 指定优先级执行Runnable
     *
     * @param task     任务
     * @param priority 优先级
     * @return IPriorityFuture
     */
    public IPriorityFuture<?> submit(Runnable task, int priority) {
        if (task instanceof IPriority) {
            return (IPriorityFuture<?>) this.submit(task);
        }
        return (IPriorityFuture<?>) this.submit(new DefaultPriorityRunnable(new Priority(priority), task));
    }

    /**
     * 指定优先级执行Runnable
     *
     * @param task     任务
     * @param result   结果
     * @param priority 优先级
     * @return IPriorityFuture
     */
    public <T> IPriorityFuture<T> submit(Runnable task, T result, int priority) {
        if (task instanceof IPriority) {
            return (IPriorityFuture<T>) this.submit(task, result);
        }
        return (IPriorityFuture<T>) this.submit(new DefaultPriorityRunnable(new Priority(priority), task),
                result);
    }

    /**
     * 指定优先级执行Callable
     *
     * @param task     任务
     * @param priority 优先级
     * @return IPriorityFuture
     */
    public <T> IPriorityFuture<T> submit(Callable<T> task, int priority) {
        if (task instanceof IPriority) {
            return (IPriorityFuture<T>) this.submit(task);
        }
        return (IPriorityFuture<T>) this.submit(new DefaultPriorityCallable<>(new Priority(priority), task));
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new DefaultPriorityFuture<>(callable);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new DefaultPriorityFuture<>(runnable, value);
    }

    /**
     * 传递子类PriorityRunnable，传递Runnable而非PriorityRunnable的话，将不支持优先级调整
     * 如果要使用Runnable又需要支持优先级可用扩展方法{{@link #execute(Runnable, int)}}并使用其返回值进行优先级调整
     *
     * @param command 命令
     */
    @Override
    public void execute(Runnable command) {
        if (command instanceof IPriorityComparable) {
            super.execute(command);
            return;
        }
        if (command instanceof IPriority) {
            super.execute(new DefaultPriorityRunnable((IPriority) command, command));
            return;
        }
        super.execute(new DefaultPriorityRunnable(new Priority(), command));
    }

    /**
     * 传递子类PriorityRunnable，传递Runnable而非PriorityRunnable的话，将不支持优先级调整
     * 如果要使用Runnable又需要支持优先级可用扩展方法{{@link #submit(Runnable, int)}}并使用其返回值进行优先级调整
     *
     * @param task 任务
     * @return Future
     */
    @Override
    public Future<?> submit(Runnable task) {
        if (task instanceof IPriorityComparable) {
            return super.submit(task);
        }
        if (task instanceof IPriority) {
            return super.submit(new DefaultPriorityRunnable((IPriority) task, task));
        }
        return super.submit(new DefaultPriorityRunnable(new Priority(), task));
    }

    /**
     * 传递子类PriorityRunnable，传递Runnable而非PriorityRunnable的话，将不支持优先级调整
     * 如果要使用Runnable又需要支持优先级可用扩展方法{{@link #submit(Runnable, Object, int)}}并使用其返回值进行优先级调整
     *
     * @param task   任务
     * @param result 结果
     * @return Future
     */
    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        if (task instanceof IPriorityComparable) {
            return super.submit(task, result);
        }
        if (task instanceof IPriority) {
            return super.submit(new DefaultPriorityRunnable((IPriority) task, task), result);
        }
        return super.submit(new DefaultPriorityRunnable(new Priority(), task), result);
    }

    /**
     * 传递子类PriorityCallable，传递Callable而非PriorityCallable的话，将不支持优先级调整
     * 如果要使用Callable又需要支持优先级可用扩展方法{{@link #submit(Callable, int)}}并使用其返回值进行优先级调整
     *
     * @param task 任务
     * @return Future
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (task instanceof IPriorityComparable) {
            return super.submit(task);
        }
        if (task instanceof IPriority) {
            return super.submit(new DefaultPriorityCallable<>((IPriority) task, task));
        }
        return super.submit(new DefaultPriorityCallable<>(new Priority(), task));
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
                // ignore/reset
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            TaskLogger.wTag(TAG, "Running task appeared exception! Thread [" + Thread.currentThread().getName() + "], because [" + t.getMessage() + "]\n" + PriorityUtils.formatStackTrace(t.getStackTrace()));
        }
    }

    //==============================构建者===================================//

    /**
     * 优先级线程池构建者
     */
    public static final class Builder {
        /**
         * 默认核心线程数
         */
        private static final int DEFAULT_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
        /**
         * 默认的等待时长
         */
        private static final long DEFAULT_KEEP_ALIVE_TIME = 30;
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
        PriorityBlockingQueue<Runnable> workQueue;
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

        public Builder setWorkQueue(PriorityBlockingQueue<Runnable> workQueue) {
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
        public PriorityThreadPoolExecutor build() {
            if (workQueue == null) {
                workQueue = new PriorityBlockingQueue<>();
            }
            if (threadFactory == null) {
                threadFactory = new PriorityThreadFactory();
            }
            if (handler == null) {
                handler = new RecordPolicy();
            }
            return new PriorityThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }
    }

    /**
     * 记录日志的拒绝策略
     *
     * @author xuexiang
     * @since 2021/10/10 2:16 AM
     */
    public static class RecordPolicy implements RejectedExecutionHandler {

        private static final String TAG = TaskLogger.getLogTag("RecordPolicy");

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            TaskLogger.eTag(TAG, "Runnable task has been rejected! Thread [" + Thread.currentThread().getName() + "], Runnable: " + r + ", ThreadPoolExecutor: " + e);
        }
    }

}