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

import android.util.Log;

import com.xuexiang.xtask.core.param.impl.TaskResult;
import com.xuexiang.xtask.core.step.impl.AbstractTaskStep;
import com.xuexiang.xtaskdemo.fragment.CustomTaskFragment;

/**
 * 步骤B任务
 *
 * @author xuexiang
 * @since 2/9/22 1:42 AM
 */
public class StepBTask extends AbstractTaskStep {

    @Override
    public void doTask() throws Exception {
        Log.e(CustomTaskFragment.TAG, "doing StepB task!");
        // 获取参数进行处理
        int total = getTaskParam().getInt(StepATask.KEY_TOTAL) + 20;
        Log.e(CustomTaskFragment.TAG, "total:" + total);
        // 更新结果
        getTaskParam().put(StepATask.KEY_TOTAL, total);
        notifyTaskSucceed(TaskResult.succeed());
    }

    @Override
    public String getName() {
        return "StepBTask";
    }
}
