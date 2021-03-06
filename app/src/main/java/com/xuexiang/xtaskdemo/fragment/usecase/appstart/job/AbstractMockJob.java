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
 * 模拟任务
 *
 * @author xuexiang
 * @since 2/23/22 12:47 AM
 */
public abstract class AbstractMockJob {

    private LoggerTextView mLogger;

    public AbstractMockJob(LoggerTextView logger) {
        mLogger = logger;
    }

    /**
     * 模拟执行任务
     */
    public abstract void doJob();

    public void log(String logContent) {
        if (mLogger != null) {
            mLogger.logNormal(logContent);
        }
    }


    /**
     * 模拟执行
     *
     * @param time 模拟执行所需要的时间
     */
    public void mockProcess(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
