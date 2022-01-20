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

package com.xuexiang.xtask.thread;

/**
 * 可取消执行的实现接口
 *
 * @author xuexiang
 * @since 2021/11/2 12:20 AM
 */
public interface ICancelable {

    /**
     * 取消
     */
    void cancel();

    /**
     * 获取是否已取消
     *
     * @return 是否已取消
     */
    boolean isCancelled();
}
