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
 * 耗时任务，比如第三方依赖库的初始化、大数据的预加载、磁盘读写操作等
 *
 * @author xuexiang
 * @since 2/23/22 1:02 AM
 */
public class LongTimeJob extends AbstractMockJob {

    public LongTimeJob(LoggerTextView logger) {
        super(logger);
    }

    @Override
    public void doJob() {
        log("[耗时任务]开始执行...");
        mockProcess(1000);
        log("[耗时任务]执行完毕!");
    }
}
