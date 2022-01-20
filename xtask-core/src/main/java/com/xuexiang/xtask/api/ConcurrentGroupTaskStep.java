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

package com.xuexiang.xtask.api;

import com.xuexiang.xtask.core.ITaskChainEngine;
import com.xuexiang.xtask.core.ITaskParam;
import com.xuexiang.xtask.core.ITaskStepHandler;
import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.core.impl.AbstractTaskStep;

/**
 * 并行任务组
 *
 * @author xuexiang
 * @since 2021/11/8 2:04 AM
 */
public abstract class ConcurrentGroupTaskStep extends AbstractTaskStep {

    public ConcurrentGroupTaskStep(ITaskChainEngine engine, ITaskParam taskParam, ITaskStepHandler taskHandler) {
        super(engine, taskParam, taskHandler);
    }

    public ConcurrentGroupTaskStep(ITaskChainEngine engine, ThreadType threadType, ITaskParam taskParam, ITaskStepHandler taskHandler) {
        super(engine, threadType, taskParam, taskHandler);
    }
}
