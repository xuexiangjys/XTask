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
import com.xuexiang.xtask.thread.priority.IPriorityFuture;
import com.xuexiang.xtask.thread.utils.PriorityUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 具有优先级排序的Future
 *
 * @author xuexiang
 * @since 2021/10/9 11:16 AM
 */
public class DefaultPriorityFuture<V> extends FutureTask<V> implements IPriorityFuture<V> {

    /**
     * 优先级
     */
    private IPriority mPriority;

    public DefaultPriorityFuture(Callable<V> callable) {
        super(callable);
        if (callable instanceof IPriority) {
            mPriority = (IPriority) callable;
        }
        setId(PriorityUtils.generateId());
    }

    public DefaultPriorityFuture(Runnable runnable, V result) {
        super(runnable, result);
        if (runnable instanceof IPriority) {
            mPriority = (IPriority) runnable;
        }
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
    public String toString() {
        return "DefaultPriorityFuture{" +
                ", mPriority=" + mPriority +
                '}';
    }
}
