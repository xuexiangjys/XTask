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

import com.xuexiang.xtask.core.param.ITaskResult;

/**
 * 任务链执行回调
 *
 * @author xuexiang
 * @since 2021/10/18 10:32 PM
 */
public interface ITaskChainCallback {

    /**
     * 回调是否返回主线程
     *
     * @return 是否返回主线程
     */
    boolean isCallBackOnMainThread();

    /**
     * 任务步骤开始执行
     *
     * @param engine 任务链
     */
    void onTaskChainStart(@NonNull ITaskChainEngine engine);

    /**
     * 任务步骤执行完毕
     *
     * @param engine 任务链
     * @param result 任务执行结果
     */
    void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result);

    /**
     * 任务步骤执行发生异常
     *
     * @param engine 任务链
     * @param result 任务执行结果
     */
    void onTaskChainError(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result);

    /**
     * 任务步骤执行被取消
     *
     * @param engine 任务链
     */
    void onTaskChainCancelled(@NonNull ITaskChainEngine engine);

}
