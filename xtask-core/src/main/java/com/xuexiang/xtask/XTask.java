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

package com.xuexiang.xtask;

import androidx.annotation.NonNull;

import com.xuexiang.xtask.api.TaskChainEngine;
import com.xuexiang.xtask.api.step.ConcurrentGroupTaskStep;
import com.xuexiang.xtask.api.step.SerialGroupTaskStep;
import com.xuexiang.xtask.api.step.XTaskStep;
import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.core.step.impl.TaskCommand;
import com.xuexiang.xtask.logger.ILogger;
import com.xuexiang.xtask.logger.TaskLogger;
import com.xuexiang.xtask.thread.XTaskExecutor;
import com.xuexiang.xtask.thread.executor.ICategoryExecutorCore;
import com.xuexiang.xtask.thread.executor.IPriorityExecutorCore;
import com.xuexiang.xtask.thread.executor.IScheduledExecutorCore;
import com.xuexiang.xtask.thread.pool.cancel.ICancelable;
import com.xuexiang.xtask.thread.pool.cancel.ICancellerPool;
import com.xuexiang.xtask.utils.CancellerPoolUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * XTask对外统一API入口
 *
 * @author xuexiang
 * @since 2/7/22 1:14 PM
 */
public final class XTask {

    //========================TaskLogger===============================//

    /**
     * 设置是否打开调试
     *
     * @param isDebug 是否是调试模式
     */
    public static void debug(boolean isDebug) {
        TaskLogger.debug(isDebug);
    }

    /**
     * 设置调试模式
     *
     * @param tag tag,如果不为空是调试模式
     */
    public static void debug(String tag) {
        TaskLogger.debug(tag);
    }

    /**
     * 设置日志记录接口
     *
     * @param logger 日志记录接口
     */
    public static void setLogger(@NonNull ILogger logger) {
        TaskLogger.setLogger(logger);
    }

    /**
     * 设置是否打印任务执行所在的线程名
     *
     * @param isLogThreadName 是否打印任务执行所在的线程名
     */
    public static void setIsLogThreadName(boolean isLogThreadName) {
        TaskLogger.setIsLogThreadName(isLogThreadName);
    }

    //========================TaskChainEngine===============================//

    /**
     * 获取任务链执行引擎
     *
     * @return 任务链执行引擎
     */
    public static TaskChainEngine getTaskChain() {
        return new TaskChainEngine();
    }

    /**
     * 获取任务链执行引擎
     *
     * @param name 任务链名称
     * @return 任务链执行引擎
     */
    public static TaskChainEngine getTaskChain(String name) {
        return new TaskChainEngine(name);
    }

    //========================XTaskStep===============================//

    /**
     * 获取简化的任务
     *
     * @param command 任务执行内容
     * @return 简化任务的构建者
     */
    public static XTaskStep getTask(@NonNull TaskCommand command) {
        return XTaskStep.getTask(command);
    }

    /**
     * 获取简化的任务
     *
     * @param command      任务执行内容
     * @param isAutoNotify 是否自动通知任务成功或者失败
     * @return 简化任务的构建者
     */
    public static XTaskStep getTask(@NonNull TaskCommand command, boolean isAutoNotify) {
        return XTaskStep.getTask(command, isAutoNotify);
    }

    /**
     * 获取简化的任务
     *
     * @param command    任务执行内容
     * @param threadType 线程类型
     * @return 简化任务的构建者
     */
    public static XTaskStep getTask(@NonNull TaskCommand command, ThreadType threadType) {
        return XTaskStep.getTask(command, threadType);
    }

    /**
     * 获取简化的任务
     *
     * @param command   任务执行内容
     * @param taskParam 任务参数
     * @return 简化任务的构建者
     */
    public static XTaskStep getTask(@NonNull TaskCommand command, @NonNull ITaskParam taskParam) {
        return XTaskStep.getTask(command, taskParam);
    }

    /**
     * 获取简化的任务
     *
     * @param command    任务执行内容
     * @param threadType 线程类型
     * @param taskParam  任务参数
     * @return 简化任务的构建者
     */
    public static XTaskStep getTask(@NonNull TaskCommand command, ThreadType threadType, @NonNull ITaskParam taskParam) {
        return XTaskStep.getTask(command, threadType, taskParam);
    }

    /**
     * 获取简化任务的构建者
     *
     * @param command 任务执行内容
     * @return 简化任务的构建者
     */
    public static XTaskStep.Builder getTaskBuilder(@NonNull TaskCommand command) {
        return XTaskStep.newBuilder(command);
    }

    //========================ConcurrentGroupTaskStep===============================//

    /**
     * 获取并行任务组
     *
     * @return 并行任务组
     */
    public static ConcurrentGroupTaskStep getConcurrentGroupTask() {
        return ConcurrentGroupTaskStep.get();
    }

    /**
     * 获取并行任务组
     *
     * @param name 任务组名称
     * @return 并行任务组
     */
    public static ConcurrentGroupTaskStep getConcurrentGroupTask(@NonNull String name) {
        return ConcurrentGroupTaskStep.get(name);
    }

    /**
     * 获取并行任务组
     *
     * @param threadType 线程类型
     * @return 并行任务组
     */
    public static ConcurrentGroupTaskStep getConcurrentGroupTask(@NonNull ThreadType threadType) {
        return ConcurrentGroupTaskStep.get(threadType);
    }

    //========================SerialGroupTaskStep===============================//

    /**
     * 获取串行任务组
     *
     * @return 串行任务组
     */
    public static SerialGroupTaskStep getSerialGroupTask() {
        return SerialGroupTaskStep.get();
    }

    /**
     * 获取串行任务组
     *
     * @param name 任务组名称
     * @return 串行任务组
     */
    public static SerialGroupTaskStep getSerialGroupTask(@NonNull String name) {
        return SerialGroupTaskStep.get(name);
    }

    /**
     * 获取串行任务组
     *
     * @param threadType 任务组名称
     * @return 串行任务组
     */
    public static SerialGroupTaskStep getSerialGroupTask(@NonNull ThreadType threadType) {
        return SerialGroupTaskStep.get(threadType);
    }

    //========================CancellerPoolUtils===============================//

    /**
     * 设置自定义的取消者订阅池
     *
     * @param cancellerPool 取消者订阅池
     */
    public static void setCancellerPool(ICancellerPool cancellerPool) {
        CancellerPoolUtils.setCancellerPool(cancellerPool);
    }

    /**
     * 取消指定任务链
     *
     * @param name 任务链名称
     * @return 是否执行成功
     */
    public static boolean cancelTaskChain(String name) {
        return CancellerPoolUtils.cancel(name);
    }

    /**
     * 取消指定任务链集合
     *
     * @param names 任务链名称集合
     */
    public static void cancelTaskChain(String... names) {
        CancellerPoolUtils.cancel(names);
    }


    /**
     * 取消指定任务链集合
     *
     * @param names 任务链名称集合
     */
    public static void cancelTaskChain(Collection<String> names) {
        CancellerPoolUtils.cancel(names);
    }

    /**
     * 取消所有任务链
     */
    public static void cancelAllTaskChain() {
        CancellerPoolUtils.cancelAll();
    }

    /**
     * 清除所有任务链
     *
     * @param ifNeedCancel 是否在清除前取消任务链
     */
    public static void clearTaskChain(boolean ifNeedCancel) {
        CancellerPoolUtils.clear(ifNeedCancel);
    }

    //========================XTaskExecutor===============================//

    /**
     * 设置优先级控制的执行内核实现接口
     *
     * @param priorityExecutorCore 优先级控制的执行内核实现接口
     */
    public static void setPriorityExecutorCore(@NonNull IPriorityExecutorCore priorityExecutorCore) {
        XTaskExecutor.get().setPriorityExecutorCore(priorityExecutorCore);
    }

    /**
     * 设置类别执行内核实现接口
     *
     * @param categoryExecutorCore 类别执行内核实现接口
     */
    public static void setCategoryExecutorCore(@NonNull ICategoryExecutorCore categoryExecutorCore) {
        XTaskExecutor.get().setCategoryExecutorCore(categoryExecutorCore);
    }

    /**
     * 设置周期执行内核的实现接口
     *
     * @param scheduledExecutorCore 周期执行内核的实现接口
     */
    public static void setScheduledExecutorCore(@NonNull IScheduledExecutorCore scheduledExecutorCore) {
        XTaskExecutor.get().setScheduledExecutorCore(scheduledExecutorCore);
    }

    /**
     * 停止工作
     */
    public static void shutdown() {
        XTaskExecutor.get().shutdown();
    }

    //================PriorityExecutorCore==================//

    /**
     * 按优先级执行异步任务
     *
     * @param task     任务
     * @param priority 优先级
     * @return 取消接口
     */
    public static ICancelable submit(Runnable task, int priority) {
        return XTaskExecutor.get().submit(task, priority);
    }

    /**
     * 分组按优先级执行异步任务
     *
     * @param groupName 任务组名
     * @param task      任务
     * @param priority  优先级
     * @return 取消接口
     */
    public static ICancelable submit(String groupName, Runnable task, int priority) {
        return XTaskExecutor.get().submit(groupName, task, priority);
    }

    //================CategoryExecutorCore==================//

    /**
     * 执行任务到主线程
     *
     * @param task 任务
     * @return 是否执行成功
     */
    public static boolean postToMain(Runnable task) {
        return XTaskExecutor.get().postToMain(task);
    }

    /**
     * 延迟执行任务到主线程
     *
     * @param task        任务
     * @param delayMillis 延迟时间
     * @return 是否执行成功
     */
    public static ICancelable postToMainDelay(Runnable task, long delayMillis) {
        return XTaskExecutor.get().postToMainDelay(task, delayMillis);
    }

    /**
     * 执行紧急异步任务【线程的优先级默认是10】
     *
     * @param task 任务
     * @return 取消接口
     */
    public static ICancelable emergentSubmit(Runnable task) {
        return XTaskExecutor.get().emergentSubmit(task);
    }

    /**
     * 执行普通异步任务【线程的优先级是5】
     *
     * @param task 任务
     * @return 取消接口
     */
    public static ICancelable submit(Runnable task) {
        return XTaskExecutor.get().submit(task);
    }

    /**
     * 执行后台异步任务【线程的优先级是1】
     *
     * @param task 任务
     * @return 取消接口
     */
    public static ICancelable backgroundSubmit(Runnable task) {
        return XTaskExecutor.get().backgroundSubmit(task);
    }

    /**
     * 执行io耗时的异步任务【线程的优先级是5】
     *
     * @param task 任务
     * @return 取消接口
     */
    public static ICancelable ioSubmit(Runnable task) {
        return XTaskExecutor.get().ioSubmit(task);
    }

    /**
     * 执行分组异步任务【线程的优先级是5】
     *
     * @param groupName 任务组名
     * @param task      任务
     * @return 取消接口
     */
    public static ICancelable groupSubmit(String groupName, Runnable task) {
        return XTaskExecutor.get().groupSubmit(groupName, task);
    }

    //================ScheduledExecutorCore==================//

    /**
     * 执行延期任务
     *
     * @param task  任务
     * @param delay 延迟时长
     * @param unit  时间单位
     * @return 取消接口
     */
    public static ICancelable schedule(Runnable task, long delay, TimeUnit unit) {
        return XTaskExecutor.get().schedule(task, delay, unit);
    }

    /**
     * 执行周期任务（以固定频率执行的任务）
     *
     * @param task         任务
     * @param initialDelay 初始延迟时长
     * @param period       间隔时长
     * @param unit         时间单位
     * @return 取消接口
     */
    public static ICancelable scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return XTaskExecutor.get().scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    /**
     * 执行周期任务（以固定延时执行的任务，延时是相对当前任务结束为起点计算开始时间）
     *
     * @param task         任务
     * @param initialDelay 初始延迟时长
     * @param period       间隔时长
     * @param unit         时间单位
     * @return 取消接口
     */
    public static ICancelable scheduleWithFixedDelay(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return XTaskExecutor.get().scheduleWithFixedDelay(task, initialDelay, period, unit);
    }

}
