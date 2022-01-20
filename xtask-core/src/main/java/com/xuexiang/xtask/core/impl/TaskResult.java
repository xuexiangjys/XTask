/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xtask.core.impl;

import com.xuexiang.xtask.core.ITaskResult;
import com.xuexiang.xtask.logger.TaskLogger;

/**
 * 任务执行结果
 *
 * @author xuexiang
 * @since 2021/10/27 2:11 AM
 */
public class TaskResult extends TaskParam implements ITaskResult {

    private static final String TAG = TaskLogger.getLogTag("TaskResult");

    /**
     * 任务执行结果码
     */
    private int mCode;

    /**
     * 任务执行信息
     */
    private String mMessage;

    /**
     * 空构造方法
     */
    public TaskResult() {

    }

    /**
     * 构造方法
     *
     * @param code    结果码
     * @param message 消息
     */
    public TaskResult(int code, String message) {
        setResult(code, message);
    }

    @Override
    public int getCode() {
        return mCode;
    }

    public TaskResult setCode(int code) {
        mCode = code;
        return this;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }

    public TaskResult setMessage(String message) {
        mMessage = message;
        return this;
    }

    /**
     * 设置结果
     *
     * @param code    结果码
     * @param message 消息
     */
    @Override
    public void setResult(int code, String message) {
        mCode = code;
        mMessage = message;
    }

    @Override
    public void saveResult(ITaskResult taskResult) {
        if (taskResult == null) {
            TaskLogger.eTag(TAG, "saveResult error, taskResult is null!");
            return;
        }
        saveData(taskResult.getDataStore());
        setResult(taskResult.getCode(), taskResult.getMessage());
    }
}
