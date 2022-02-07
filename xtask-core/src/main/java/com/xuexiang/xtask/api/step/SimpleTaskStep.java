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

package com.xuexiang.xtask.api.step;

import androidx.annotation.NonNull;

import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.core.step.ITaskStepHandler;
import com.xuexiang.xtask.core.step.impl.AbstractTaskStep;

/**
 * 简单的任务执行步骤
 *
 * @author xuexiang
 * @since 2/1/22 11:11 PM
 */
public abstract class SimpleTaskStep extends AbstractTaskStep {

    /**
     * 任务名称
     */
    private String mName;

    public SimpleTaskStep(String name) {
        mName = name;
    }

    public SimpleTaskStep(String name, @NonNull ITaskParam taskParam) {
        super(taskParam);
        mName = name;
    }

    public SimpleTaskStep(String name, ITaskStepHandler taskHandler) {
        super(taskHandler);
        mName = name;
    }

    public SimpleTaskStep(String name, @NonNull ITaskParam taskParam, ITaskStepHandler taskHandler) {
        super(taskParam, taskHandler);
        mName = name;
    }

    public SimpleTaskStep(String name, ThreadType threadType, @NonNull ITaskParam taskParam, ITaskStepHandler taskHandler) {
        super(threadType, taskParam, taskHandler);
        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }
}
