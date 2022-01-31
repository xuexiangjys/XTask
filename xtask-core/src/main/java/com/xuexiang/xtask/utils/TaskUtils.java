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

package com.xuexiang.xtask.utils;

import android.os.Looper;

import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.core.step.ITaskStep;
import com.xuexiang.xtask.logger.TaskLogger;
import com.xuexiang.xtask.thread.XTaskExecutor;
import com.xuexiang.xtask.thread.pool.ICancelable;

import java.util.List;

/**
 * XTask内部工具类
 *
 * @author xuexiang
 * @since 1/31/22 8:40 PM
 */
public final class TaskUtils {

    private static final String TAG = TaskLogger.getLogTag("TaskUtils");

    private TaskUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 是否是主线程
     *
     * @return 是否是主线程
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 切到主线程运行
     *
     * @param runnable 命令
     */
    public static void runOnMainThread(Runnable runnable) {
        XTaskExecutor.get().postToMain(runnable);
    }

    /**
     * 查找下一条需要执行的任务
     *
     * @param taskStepList 执行任务集合
     * @param taskStep     当前任务
     * @return 下一条执行任务
     */
    public static ITaskStep findNextTaskStep(List<ITaskStep> taskStepList, ITaskStep taskStep) {
        if (CommonUtils.isEmpty(taskStepList)) {
            return null;
        }
        int index = 0;
        if (taskStep != null) {
            index = taskStepList.indexOf(taskStep) + 1;
        }
        for (; index < taskStepList.size(); index++) {
            ITaskStep target = taskStepList.get(index);
            if (target != null && target.accept()) {
                return target;
            }
        }
        return null;
    }

    /**
     * 执行任务
     *
     * @param taskStep 需要执行的任务
     * @return 取消接口
     */
    public static ICancelable executeTask(ITaskStep taskStep) {
        if (taskStep == null) {
            TaskLogger.eTag(TAG, "execute task failed, taskStep is null!");
            return null;
        }
        ThreadType type = taskStep.getThreadType();
        if (type == ThreadType.MAIN) {
            XTaskExecutor.get().postToMain(taskStep);
            return null;
        } else if (type == ThreadType.ASYNC_EMERGENT) {
            return XTaskExecutor.get().emergentSubmit(taskStep);
        } else if (type == ThreadType.ASYNC) {
            return XTaskExecutor.get().submit(taskStep);
        } else if (type == ThreadType.ASYNC_IO) {
            return XTaskExecutor.get().ioSubmit(taskStep);
        } else if (type == ThreadType.ASYNC_BACKGROUND) {
            return XTaskExecutor.get().backgroundSubmit(taskStep);
        } else {
            taskStep.run();
            return null;
        }
    }

}
