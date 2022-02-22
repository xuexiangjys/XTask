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
import com.xuexiang.xtaskdemo.fragment.usecase.appstart.job.LongTimeJob;
import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 异步初始化任务
 * 放一些优先级不是很高的、耗时的初始化任务
 *
 * @author xuexiang
 * @since 2/23/22 1:42 AM
 */
public class AsyncInitTask extends SimpleTaskStep {

    private LoggerTextView mLogger;

    public AsyncInitTask(LoggerTextView logger) {
        mLogger = logger;
    }

    @Override
    public void doTask() throws Exception {
        // 执行耗时任务
        new LongTimeJob(mLogger).doJob();
    }

    @Override
    public String getName() {
        return "AsyncInitTask";
    }

    @NonNull
    @Override
    public ThreadType getThreadType() {
        // 任务的优先级不高，使用异步子线程执行
        return ThreadType.ASYNC;
    }
}
