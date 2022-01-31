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

package com.xuexiang.xtask.core;

/**
 * 线程执行类型
 *
 * @author xuexiang
 * @since 2021/10/27 2:01 AM
 */
public enum ThreadType {

    /**
     * 主线程（UI线程）
     */
    MAIN,

    /**
     * 异步线程（开子线程，普通线程池）
     */
    ASYNC,

    /**
     * 异步线程（开子线程，io线程池）
     */
    ASYNC_IO,

    /**
     * 异步线程（开子线程，紧急线程池）
     */
    ASYNC_EMERGENT,

    /**
     * 异步线程（开子线程，优先级较低线程池）
     */
    ASYNC_BACKGROUND,

    /**
     * 同步线程（直接执行）
     */
    SYNC

}
