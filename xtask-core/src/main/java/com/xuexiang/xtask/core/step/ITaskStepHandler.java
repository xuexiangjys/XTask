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

/**
 * 任务处理者
 *
 * @author xuexiang
 * @since 2021/10/18 9:10 PM
 */
public interface ITaskStepHandler {

    /**
     * 任务执行之前的处理
     *
     * @param step 任务
     */
    void beforeTask(@NonNull ITaskStep step);

    /**
     * 任务执行完毕的处理
     *
     * @param step 任务
     */
    void afterTask(@NonNull ITaskStep step);

    /**
     * 任务执行发生异常
     *
     * @param step      任务
     * @param exception 异常
     */
    void onTaskException(@NonNull ITaskStep step, Exception exception);

    /**
     * 是否接收执行任务
     *
     * @param step 任务
     * @return true：执行；false：不执行
     */
    boolean accept(@NonNull ITaskStep step);

    /**
     * 任务执行完成的处理
     *
     * @param step 任务
     */
    void handleTaskSucceed(@NonNull ITaskStep step);

    /**
     * 任务执行失败的处理
     *
     * @param step 任务
     */
    void handleTaskFailed(@NonNull ITaskStep step);

    /**
     * 任务执行被取消的处理
     *
     * @param step 任务
     */
    void handleTaskCancelled(@NonNull ITaskStep step);

}
