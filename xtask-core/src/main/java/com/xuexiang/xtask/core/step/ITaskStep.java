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

package com.xuexiang.xtask.core.step;

import androidx.annotation.NonNull;

import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.core.param.impl.TaskParam;
import com.xuexiang.xtask.thread.pool.ICancelable;

/**
 * 任务步骤
 *
 * @author xuexiang
 * @since 2021/10/18 9:11 PM
 */
public interface ITaskStep extends Runnable, ICancelable, ITaskStepController {

    /**
     * 设置任务步骤的生命周期
     *
     * @param taskStepLifecycle 任务步骤的生命周期
     * @return 任务步骤
     */
    ITaskStep setTaskStepLifecycle(@NonNull ITaskStepLifecycle taskStepLifecycle);

    /**
     * 设置任务处理者
     *
     * @param taskStepHandler 任务处理者
     * @return 任务步骤
     */
    ITaskStep setTaskStepHandler(@NonNull ITaskStepHandler taskStepHandler);

    /**
     * 设置执行的线程类型
     *
     * @param threadType 线程类型
     * @return 任务步骤
     */
    ITaskStep setThreadType(@NonNull ThreadType threadType);

    /**
     * 设置执行的任务参数
     *
     * @param taskParam 任务参数
     * @return 任务步骤
     */
    ITaskStep setTaskParam(@NonNull ITaskParam taskParam);

    /**
     * 获取执行的线程类型
     *
     * @return 线程类型
     */
    @NonNull
    ThreadType getThreadType();

    /**
     * 是否接收执行
     *
     * @return 是否执行，默认是true
     */
    boolean accept();

    /**
     * 任务准备工作
     *
     * @param taskParam 任务参数
     */
    void prepareTask(TaskParam taskParam);

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
     * 是否正在等待
     *
     * @return 是否正在等待
     */
    boolean isPending();

}
