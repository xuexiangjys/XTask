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

import androidx.annotation.NonNull;

import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.core.param.impl.TaskResult;
import com.xuexiang.xtask.core.step.ITaskStepController;
import com.xuexiang.xtaskdemo.fragment.CustomTaskFragment;

/**
 * 模拟复杂的任务
 *
 * @author xuexiang
 * @since 2/9/22 11:22 PM
 */
public class Job implements Runnable {

    public static final String KEY_JOB_TARGET = "key_job_target";

    private ITaskStepController mController;

    public Job(@NonNull ITaskStepController controller) {
        mController = controller;
    }

    @Override
    public void run() {
        ITaskParam taskParam = mController.getTaskParam();
        int target = taskParam.getInt(KEY_JOB_TARGET);
        Log.e(CustomTaskFragment.TAG, "Job is running..., target:" + target);
        // 模拟耗时
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        taskParam.put(KEY_JOB_TARGET, target + 10);
        mController.notifyTaskSucceed(TaskResult.succeed());
    }
}
