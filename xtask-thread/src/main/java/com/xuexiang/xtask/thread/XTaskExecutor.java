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

package com.xuexiang.xtask.thread;

import com.xuexiang.xtask.thread.executor.IExecutorCore;
import com.xuexiang.xtask.thread.executor.impl.DefaultExecutorCore;

/**
 * XTask的执行者
 *
 * @author xuexiang
 * @since 2021/10/9 2:30 AM
 */
public class XTaskExecutor implements IExecutorCore {

    private static volatile XTaskExecutor sInstance = null;

    /**
     * 执行内核实现接口
     */
    private IExecutorCore mExecutorCore;

    /**
     * 获取XTask的执行者
     *
     * @return XTask的执行者
     */
    public static XTaskExecutor get() {
        if (sInstance == null) {
            synchronized (XTaskExecutor.class) {
                if (sInstance == null) {
                    sInstance = new XTaskExecutor();
                }
            }
        }
        return sInstance;
    }

    /**
     * 私有构造方法
     */
    private XTaskExecutor() {
        mExecutorCore = new DefaultExecutorCore();
    }

    /**
     * 设置执行内核实现接口
     *
     * @param executorCore 执行内核实现接口
     * @return this
     */
    public XTaskExecutor setExecutorCore(IExecutorCore executorCore) {
        mExecutorCore = executorCore;
        return this;
    }

    @Override
    public ICancelable submit(Runnable task, int priority) {
        return mExecutorCore.submit(task, priority);
    }

    @Override
    public ICancelable submit(String groupName, Runnable task, int priority) {
        return mExecutorCore.submit(groupName, task, priority);
    }

    @Override
    public boolean postToMain(Runnable task) {
        return mExecutorCore.postToMain(task);
    }

    @Override
    public void shutdown() {
        mExecutorCore.shutdown();
    }

}
