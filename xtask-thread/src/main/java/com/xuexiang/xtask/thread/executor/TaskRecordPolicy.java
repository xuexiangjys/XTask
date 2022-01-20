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

package com.xuexiang.xtask.thread.executor;

import com.xuexiang.xtask.logger.TaskLogger;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 记录日志的拒绝策略
 *
 * @author xuexiang
 * @since 2021/12/16 2:02 AM
 */
public class TaskRecordPolicy implements RejectedExecutionHandler {

    private static final String TAG = TaskLogger.getLogTag("RecordPolicy");

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        TaskLogger.eTag(TAG, "Runnable task has been rejected! Thread [" + Thread.currentThread().getName() + "], Runnable: " + r + ", ThreadPoolExecutor: " + e);
    }
}
