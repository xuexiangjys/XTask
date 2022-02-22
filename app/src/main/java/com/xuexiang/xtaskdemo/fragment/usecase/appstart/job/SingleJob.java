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

package com.xuexiang.xtaskdemo.fragment.usecase.appstart.job;

import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 单独的任务，没有执行上的先后顺序. 例如：非核心数据的加载。
 *
 * @author xuexiang
 * @since 2/23/22 1:16 AM
 */
public class SingleJob extends AbstractMockJob {

    private int mIndex;

    public SingleJob(int index, LoggerTextView logger) {
        super(logger);
        mIndex = index;
    }

    @Override
    public void doJob() {
        log("[单独的任务" + mIndex + "]开始执行...");
        mockProcess(200);
        log("[单独的任务" + mIndex + "]执行完毕!");
    }
}
