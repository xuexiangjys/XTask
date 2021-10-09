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

package com.xuexiang.xtask.thread.priority.impl;

import com.xuexiang.xtask.thread.priority.IPriority;
import com.xuexiang.xtask.thread.priority.IPriorityRunnable;
import com.xuexiang.xtask.thread.utils.PriorityUtils;

/**
 * 具有优先级排序的Runnable
 *
 * @author xuexiang
 * @since 2021/10/9 11:32 AM
 */
public class DefaultPriorityRunnable implements IPriorityRunnable {
    /**
     * 优先级
     */
    private IPriority mPriority;
    /**
     * 执行任务
     */
    private Runnable mRunnable;

    public DefaultPriorityRunnable(IPriority priority, Runnable runnable) {
        mPriority = priority;
        mRunnable = runnable;
        setId(PriorityUtils.generateId());
    }

    @Override
    public void priority(int priority) {
        if (mPriority != null) {
            mPriority.priority(priority);
        }
    }

    @Override
    public long getId() {
        return mPriority != null ? mPriority.getId() : 0;
    }

    @Override
    public void setId(long id) {
        if (mPriority != null) {
            mPriority.setId(id);
        }
    }

    @Override
    public int priority() {
        return mPriority != null ? mPriority.priority() : 0;
    }

    @Override
    public int compareTo(IPriority other) {
        return PriorityUtils.compare(this, other);
    }

    @Override
    public void run() {
        if (mRunnable != null) {
            mRunnable.run();
        }
    }

    @Override
    public String toString() {
        return "DefaultPriorityRunnable{" +
                ", mPriority=" + mPriority +
                ", mRunnable=" + mRunnable +
                '}';
    }
}
