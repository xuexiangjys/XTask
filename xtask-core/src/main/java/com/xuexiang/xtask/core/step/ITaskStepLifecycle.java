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

import com.xuexiang.xtask.core.param.ITaskResult;

/**
 * 任务步骤的生命周期管理
 *
 * @author xuexiang
 * @since 1/30/22 4:51 PM
 */
public interface ITaskStepLifecycle {

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
