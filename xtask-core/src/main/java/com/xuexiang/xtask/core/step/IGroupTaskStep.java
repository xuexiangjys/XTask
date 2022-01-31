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

import java.util.List;

/**
 * 组任务步骤
 *
 * @author xuexiang
 * @since 1/30/22 6:18 PM
 */
public interface IGroupTaskStep {

    /**
     * 增加执行任务
     *
     * @param taskStep 执行任务
     * @return 任务链执行引擎
     */
    IGroupTaskStep addTask(ITaskStep taskStep);

    /**
     * 增加执行任务集合
     *
     * @param taskStepList 执行任务集合
     * @return 任务链执行引擎
     */
    IGroupTaskStep addTasks(List<ITaskStep> taskStepList);

    /**
     * 清理任务
     */
    void clearTask();
}
