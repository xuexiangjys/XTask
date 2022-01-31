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

import com.xuexiang.xtask.core.step.IGroupTaskStep;
import com.xuexiang.xtask.core.step.ITaskStepLifecycle;
import com.xuexiang.xtask.thread.pool.ICancelable;

/**
 * 任务链执行引擎实现接口
 *
 * @author xuexiang
 * @since 2021/10/19 1:43 AM
 */
public interface ITaskChainEngine extends ITaskStepLifecycle, IGroupTaskStep, ICancelable {

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
     * 开始任务
     */
    void start();

    /**
     * 重置任务链（可继续使用)
     */
    void reset();

    /**
     * 销毁任务链（不可使用)
     */
    void destroy();

}
