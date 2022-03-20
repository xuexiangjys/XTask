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

package com.xuexiang.xtask.thread.pool.cancel;

import com.xuexiang.xtask.thread.utils.CancelUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务取消者池
 *
 * @author xuexiang
 * @since 2/7/22 12:43 PM
 */
public final class TaskCancellerPool implements ICancellerPool {

    private Map<String, ICancelable> mPool = new ConcurrentHashMap<>();

    @Override
    public boolean add(String name, ICancelable cancelable) {
        if (name == null || cancelable == null) {
            return false;
        }
        return mPool.put(name, cancelable) != null;
    }

    @Override
    public boolean remove(String name) {
        if (name == null) {
            return false;
        }
        return mPool.remove(name) != null;
    }

    @Override
    public boolean cancel(String name) {
        if (name == null) {
            return false;
        }
        ICancelable cancelable = mPool.get(name);
        boolean result = CancelUtils.cancel(cancelable);
        mPool.remove(name);
        return result;
    }

    @Override
    public void cancel(String... names) {
        for (String name : names) {
            cancel(name);
        }
    }

    @Override
    public void cancel(Collection<String> names) {
        for (String name : names) {
            cancel(name);
        }
    }

    @Override
    public void cancelAll() {
        if (mPool.isEmpty()) {
            return;
        }
        CancelUtils.cancel(mPool.values());
        mPool.clear();
    }

    @Override
    public void clear(boolean ifNeedCancel) {
        if (ifNeedCancel) {
            cancelAll();
        } else {
            mPool.clear();
        }
    }

}
