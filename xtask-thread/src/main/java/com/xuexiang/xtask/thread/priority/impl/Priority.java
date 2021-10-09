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

/**
 * 优先级【先比较优先级，再比较序号】
 *
 * @author xuexiang
 * @since 2021/10/9 11:29 AM
 */
public class Priority implements IPriority {

    /**
     * 优先级
     */
    private int mPriority;

    /**
     * 序号
     */
    private long mId;

    public Priority() {

    }

    public Priority(int priority) {
        mPriority = priority;
    }

    @Override
    public int priority() {
        return mPriority;
    }

    @Override
    public void priority(int priority) {
        mPriority = priority;
    }

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public void setId(long id) {
        mId = id;
    }

    @Override
    public String toString() {
        return "Priority{" +
                "mPriority=" + mPriority +
                ", mId=" + mId +
                '}';
    }
}
