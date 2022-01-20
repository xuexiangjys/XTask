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

package com.xuexiang.xtask.core;

import com.xuexiang.xtask.thread.ICancelable;

/**
 * 任务步骤
 *
 * @author xuexiang
 * @since 2021/10/18 9:11 PM
 */
public interface ITaskStep extends Runnable, ICancelable {

    /**
     * 获取任务步骤名称
     *
     * @return 任务步骤的名称
     */
    String getName();

    /**
     * 获取执行的线程类型
     *
     * @return 线程类型
     */
    ThreadType getThreadType();

    /**
     * 获取任务的参数
     *
     * @return 任务参数
     */
    ITaskParam getTaskParam();

    /**
     * 是否接收执行
     *
     * @return 是否执行，默认是true
     */
    boolean accept();

    /**
     * 设置任务取消接口
     *
     * @param cancelable 取消接口
     */
    void setCancelable(ICancelable cancelable);

    /**
     * 执行任务
     *
     * @throws Exception 异常
     */
    void doTask() throws Exception;

    /**
     * 设置是否正在运行
     *
     * @param isRunning 是否正在运行
     */
    void setIsRunning(boolean isRunning);

    /**
     * 是否正在运行
     *
     * @return 是否正在运行
     */
    boolean isRunning();

    /**
     * 任务步骤执行完毕
     *
     * @param result 任务执行结果
     */
    void onTaskSucceed(ITaskResult result);

    /**
     * 任务链执行发生异常
     *
     * @param result 任务执行结果
     */
    void onTaskFailed(ITaskResult result);

}
