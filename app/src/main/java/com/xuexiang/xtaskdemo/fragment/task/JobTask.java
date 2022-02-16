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

package com.xuexiang.xtaskdemo.fragment.task;

import com.xuexiang.xtask.api.step.SimpleTaskStep;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟复杂任务
 *
 * @author xuexiang
 * @since 2/9/22 11:22 PM
 */
public class JobTask extends SimpleTaskStep {

    private static final AtomicInteger TASK_NUMBER = new AtomicInteger(1);

    /**
     * 构造方法
     */
    public JobTask() {
        super("CallBackTask-" + TASK_NUMBER.getAndIncrement());
    }


    @Override
    public void doTask() throws Exception {
        new Job(this).run();
    }

    @Override
    protected boolean isAutoNotify() {
        return false;
    }
}
