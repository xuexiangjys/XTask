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

package com.xuexiang.xtask.thread.executor;

import com.xuexiang.xtask.thread.pool.ICancelable;

/**
 * 拥有优先级控制的执行者内核实现接口
 *
 * @author xuexiang
 * @since 2021/11/10 1:04 AM
 */
public interface IPriorityExecutorCore extends IExecutorCore {

    /**
     * 执行异步任务
     *
     * @param task     任务
     * @param priority 优先级
     * @return 取消接口
     */
    ICancelable submit(Runnable task, int priority);

    /**
     * 按组执行异步任务
     *
     * @param groupName 任务组名
     * @param task      任务
     * @param priority  优先级
     * @return 取消接口
     */
    ICancelable submit(String groupName, Runnable task, int priority);

}
