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

import androidx.annotation.NonNull;

import com.xuexiang.xtask.thread.ICancelable;

import java.util.List;

/**
 * 任务链执行引擎实现接口
 *
 * @author xuexiang
 * @since 2021/10/19 1:43 AM
 */
public interface ITaskChainEngine extends ICancelable {

    /**
     * 获取任务链的名称
     *
     * @return 任务链的名称
     */
    String getName();

    /**
     * 设置任务链执行回调
     *
     * @param iTaskChainCallback 任务链执行回调
     * @return 任务链执行引擎
     */
    ITaskChainEngine setTaskChainCallback(ITaskChainCallback iTaskChainCallback);

    /**
     * 增加执行任务
     *
     * @param taskStep 执行任务
     * @return 任务链执行引擎
     */
    ITaskChainEngine addTask(ITaskStep taskStep);

    /**
     * 增加执行任务集合
     *
     * @param taskStepList 执行任务集合
     * @return 任务链执行引擎
     */
    ITaskChainEngine addTasks(List<ITaskStep> taskStepList);

    /**
     * 开始任务
     */
    void start();

    /**
     * 任务步骤执行完毕
     *
     * @param step   任务步骤
     * @param result 任务执行结果
     */
    void onTaskStepCompleted(@NonNull ITaskStep step, @NonNull ITaskResult result);

    /**
     * 任务步骤执行发生异常
     *
     * @param step   任务步骤
     * @param result 任务执行结果
     */
    void onTaskStepError(@NonNull ITaskStep step, @NonNull ITaskResult result);

}
