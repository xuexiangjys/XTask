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

import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.core.param.ITaskResult;

/**
 * 任务步骤执行控制器
 *
 * @author xuexiang
 * @since 1/30/22 6:33 PM
 */
public interface ITaskStepController {

    /**
     * 获取任务步骤名称
     *
     * @return 任务步骤的名称
     */
    String getName();

    /**
     * 获取任务的参数
     *
     * @return 任务参数
     */
    @NonNull
    ITaskParam getTaskParam();

    /**
     * 任务步骤执行完毕
     *
     * @param result 任务执行结果
     */
    void onTaskSucceed(@NonNull ITaskResult result);

    /**
     * 任务链执行发生异常
     *
     * @param result 任务执行结果
     */
    void onTaskFailed(@NonNull ITaskResult result);

    /**
     * 资源释放
     */
    void recycle();
}
