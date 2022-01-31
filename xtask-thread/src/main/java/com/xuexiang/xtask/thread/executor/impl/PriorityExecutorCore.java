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

package com.xuexiang.xtask.thread.executor.impl;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xuexiang.xtask.thread.executor.IExecutorCore;
import com.xuexiang.xtask.thread.executor.IPriorityExecutorCore;
import com.xuexiang.xtask.thread.pool.ICancelable;
import com.xuexiang.xtask.thread.pool.PriorityThreadPoolExecutor;
import com.xuexiang.xtask.thread.pool.TaskThreadFactory;
import com.xuexiang.xtask.thread.utils.ExecutorUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用PriorityThreadPoolExecutor实现的线程执行内核，通过阻塞队列(PriorityBlockingQueue)来实现优先级的控制。
 *
 * @author xuexiang
 * @since 2021/11/10 1:17 AM
 */
public class PriorityExecutorCore implements IPriorityExecutorCore, IExecutorCore {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int GROUP_CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final String GROUP_FACTORY_NAME_PREFIX = "PriorityGroup-";

    private PriorityThreadPoolExecutor mExecutor;

    private Map<String, PriorityThreadPoolExecutor> mGroupExecutorMap = new ConcurrentHashMap<>();

    @Override
    public ICancelable submit(Runnable task, int priority) {
        return submitTask(getThreadPoolExecutor(null), task, priority);
    }

    @Override
    public ICancelable submit(String groupName, Runnable task, int priority) {
        return submitTask(getThreadPoolExecutor(groupName), task, priority);
    }

    @Override
    public void shutdown() {
        ExecutorUtils.shutdown(mExecutor);
        mExecutor = null;
        ExecutorUtils.shutdown(mGroupExecutorMap.values());
        mGroupExecutorMap.clear();
    }

    /**
     * 提交任务
     *
     * @param executor 线程池
     * @param task     任务
     * @param priority 优先级
     * @return 取消执行的接口
     */
    private ICancelable submitTask(@NonNull PriorityThreadPoolExecutor executor, Runnable task, int priority) {
        return executor.submit(task, priority);
    }

    /**
     * 获取线程池
     *
     * @param groupName 组名称
     * @return 线程池
     */
    @NonNull
    private PriorityThreadPoolExecutor getThreadPoolExecutor(String groupName) {
        if (TextUtils.isEmpty(groupName)) {
            if (mExecutor == null) {
                mExecutor = PriorityThreadPoolExecutor.getDefault();
            }
            return mExecutor;
        } else {
            PriorityThreadPoolExecutor executor = mGroupExecutorMap.get(groupName);
            if (executor == null) {
                executor = PriorityThreadPoolExecutor.newBuilder(GROUP_CORE_POOL_SIZE)
                        .setThreadFactory(TaskThreadFactory.getFactory(GROUP_FACTORY_NAME_PREFIX + groupName))
                        .build();
                mGroupExecutorMap.put(groupName, executor);
            }
            return executor;
        }
    }

}
