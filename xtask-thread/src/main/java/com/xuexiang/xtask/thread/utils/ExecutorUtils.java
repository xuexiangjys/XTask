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

package com.xuexiang.xtask.thread.utils;

import android.os.Looper;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

/**
 * 线程池工具
 *
 * @author xuexiang
 * @since 1/21/22 2:11 AM
 */
public final class ExecutorUtils {

    private ExecutorUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 关闭线程池
     *
     * @param executor 线程池
     */
    public static void shutdown(ExecutorService executor) {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    /**
     * 关闭线程池
     *
     * @param executors 线程池集合
     */
    public static void shutdown(Collection<? extends ExecutorService> executors) {
        if (executors == null || executors.isEmpty()) {
            return;
        }
        for (ExecutorService executor : executors) {
            shutdown(executor);
        }
    }

    /**
     * 是否是主线程
     *
     * @return 是否是主线程
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

}
