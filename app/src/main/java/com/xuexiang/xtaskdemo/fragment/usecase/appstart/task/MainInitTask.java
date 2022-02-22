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

package com.xuexiang.xtaskdemo.fragment.usecase.appstart.task;

import androidx.annotation.NonNull;

import com.xuexiang.xtask.api.step.SimpleTaskStep;
import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtaskdemo.fragment.usecase.appstart.job.TopPriorityJob;
import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 主要初始化任务，放在第一位执行, 执行最高优先级的事务
 *
 * @author xuexiang
 * @since 2/23/22 1:34 AM
 */
public class MainInitTask extends SimpleTaskStep {

    private LoggerTextView mLogger;

    public MainInitTask(LoggerTextView logger) {
        mLogger = logger;
    }

    @Override
    public void doTask() throws Exception {
        // 执行最高优先级的事务
        new TopPriorityJob(mLogger).doJob();
    }


    @Override
    public String getName() {
        return "MainInitTask";
    }

    @NonNull
    @Override
    public ThreadType getThreadType() {
        // 任务优先级较高，执行有前后依赖，因此将任务放在第一位使用同步主线程执行
        return ThreadType.SYNC;
    }
}
