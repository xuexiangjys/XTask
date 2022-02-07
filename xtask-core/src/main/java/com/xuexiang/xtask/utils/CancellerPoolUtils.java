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

package com.xuexiang.xtask.utils;

import com.xuexiang.xtask.thread.pool.ICancelable;
import com.xuexiang.xtask.thread.pool.ICancellerPool;
import com.xuexiang.xtask.thread.pool.TaskCancellerPool;

import java.util.Collection;

/**
 * 取消者订阅池工具类
 *
 * @author xuexiang
 * @since 2/7/22 1:17 PM
 */
public final class CancellerPoolUtils {

    private static ICancellerPool sCancellerPool = new TaskCancellerPool();

    private CancellerPoolUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 设置自定义的取消者订阅池
     *
     * @param sCancellerPool 取消者订阅池
     */
    public static void setCancellerPool(ICancellerPool sCancellerPool) {
        CancellerPoolUtils.sCancellerPool = sCancellerPool;
    }

    /**
     * 添加取消者
     *
     * @param name       取消者名称
     * @param cancelable 取消接口
     * @return 是否执行成功
     */
    public static boolean add(String name, ICancelable cancelable) {
        return sCancellerPool.add(name, cancelable);
    }

    /**
     * 去除取消者
     *
     * @param name 取消者名称
     * @return 是否执行成功
     */
    public static boolean remove(String name) {
        return sCancellerPool.remove(name);
    }

    /**
     * 指定取消者执行
     *
     * @param name 取消者名称
     * @return 是否执行成功
     */
    public static boolean cancel(String name) {
        return sCancellerPool.cancel(name);
    }

    /**
     * 指定取消者集合执行
     *
     * @param names 取消者集合
     */
    public static void cancel(String... names) {
        sCancellerPool.cancel(names);
    }


    /**
     * 指定取消者集合执行
     *
     * @param names 取消者集合
     */
    public static void cancel(Collection<String> names) {
        sCancellerPool.cancel(names);
    }

    /**
     * 所有取消者执行
     */
    public static void cancelAll() {
        sCancellerPool.cancelAll();
    }

    /**
     * 清楚所有
     *
     * @param ifNeedCancel 是否在清除前取消
     */
    public static void clear(boolean ifNeedCancel) {
        sCancellerPool.clear(ifNeedCancel);
    }

}
