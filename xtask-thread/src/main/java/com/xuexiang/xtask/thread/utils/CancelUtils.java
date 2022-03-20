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

import com.xuexiang.xtask.thread.pool.cancel.ICancelable;

import java.util.Collection;

/**
 * 取消任务工具类
 *
 * @author xuexiang
 * @since 3/20/22 2:34 AM
 */
public final class CancelUtils {


    /**
     * 取消执行接口
     *
     * @param cancelable 可取消执行的实现接口
     * @return 取消执行的结果
     */
    public static boolean cancel(ICancelable cancelable) {
        if (cancelable != null && !cancelable.isCancelled()) {
            cancelable.cancel();
            return true;
        }
        return false;
    }

    /**
     * 取消执行接口集合
     *
     * @param cancelables 可取消执行的实现接口集合
     */
    public static void cancel(Collection<ICancelable> cancelables) {
        if (cancelables == null || cancelables.isEmpty()) {
            return;
        }
        for (ICancelable cancelable : cancelables) {
            cancel(cancelable);
        }
    }

    /**
     * 取消执行接口集合
     *
     * @param cancelables 可取消执行的实现接口集合
     */
    public static void cancel(ICancelable... cancelables) {
        if (cancelables == null || cancelables.length == 0) {
            return;
        }
        for (ICancelable cancelable : cancelables) {
            cancel(cancelable);
        }
    }

}
