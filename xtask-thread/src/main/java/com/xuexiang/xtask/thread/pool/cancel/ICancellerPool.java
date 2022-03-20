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

import java.util.Collection;

/**
 * 取消者订阅池
 *
 * @author xuexiang
 * @since 2/7/22 1:03 PM
 */
public interface ICancellerPool {

    /**
     * 添加取消者
     *
     * @param name       取消者名称
     * @param cancelable 取消接口
     * @return 是否执行成功
     */
    boolean add(String name, ICancelable cancelable);

    /**
     * 去除取消者
     *
     * @param name 取消者名称
     * @return 是否执行成功
     */
    boolean remove(String name);

    /**
     * 指定取消者执行
     *
     * @param name 取消者名称
     * @return 是否执行成功
     */
    boolean cancel(String name);

    /**
     * 指定取消者集合执行
     *
     * @param names 取消者集合
     */
    void cancel(String... names);

    /**
     * 指定取消者集合执行
     *
     * @param names 取消者集合
     */
    void cancel(Collection<String> names);

    /**
     * 所有取消者执行
     */
    void cancelAll();

    /**
     * 清楚所有
     *
     * @param ifNeedCancel 是否在清除前取消
     */
    void clear(boolean ifNeedCancel);

}
