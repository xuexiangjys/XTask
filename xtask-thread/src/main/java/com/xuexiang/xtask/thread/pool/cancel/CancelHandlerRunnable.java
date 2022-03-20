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

import android.os.Handler;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 可以取消的供Handler使用的Runnable
 *
 * @author xuexiang
 * @since 3/21/22 1:34 AM
 */
public class CancelHandlerRunnable implements Runnable, ICancelable {

    /**
     * 获取可以取消的Runnable
     *
     * @param handler  handler
     * @param runnable 执行任务
     * @return 可以取消的Runnable
     */
    public static CancelHandlerRunnable get(@NonNull Handler handler, @NonNull Runnable runnable) {
        return new CancelHandlerRunnable(handler, runnable);
    }

    private WeakReference<Handler> mHandler;

    private Runnable mRunnable;

    private AtomicBoolean mIsCancelled = new AtomicBoolean(false);

    /**
     * 构造方法
     *
     * @param handler  handler
     * @param runnable 执行任务
     */
    private CancelHandlerRunnable(@NonNull Handler handler, @NonNull Runnable runnable) {
        mHandler = new WeakReference<>(handler);
        mRunnable = runnable;
    }

    /**
     * 开始延期执行
     *
     * @param delayMillis 延期时间
     * @return 取消接口
     */
    public ICancelable startDelayed(long delayMillis) {
        final Handler handler = mHandler.get();
        if (handler != null) {
            handler.postDelayed(this, delayMillis);
        }
        return this;
    }

    @Override
    public void cancel() {
        if (isCancelled()) {
            return;
        }
        final Handler handler = mHandler.get();
        if (handler == null) {
            return;
        }
        handler.removeCallbacks(this);
        mIsCancelled.set(true);
    }

    @Override
    public boolean isCancelled() {
        return mIsCancelled.get();
    }

    @Override
    public void run() {
        if (mRunnable != null) {
            mRunnable.run();
        }
    }
}
