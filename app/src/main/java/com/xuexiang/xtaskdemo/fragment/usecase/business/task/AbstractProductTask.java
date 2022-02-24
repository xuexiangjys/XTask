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

package com.xuexiang.xtaskdemo.fragment.usecase.business.task;

import com.xuexiang.xtask.api.step.SimpleTaskStep;
import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 基础抽象任务
 *
 * @author xuexiang
 * @since 2/25/22 2:18 AM
 */
public abstract class AbstractProductTask extends SimpleTaskStep {

    protected LoggerTextView mLogger;

    public AbstractProductTask(LoggerTextView logger) {
        mLogger = logger;
    }

    public void log(String logContent) {
        if (mLogger != null) {
            mLogger.logSuccess(logContent);
        }
    }

    @Override
    protected boolean isAutoNotify() {
        // 这里进行手动控制
        return false;
    }
}
