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

package com.xuexiang.xtask.thread.priority;

/**
 * 优先级排序接口
 *
 * @author xuexiang
 * @since 2021/10/9 2:32 AM
 */
public interface IPriority {

    /**
     * 获取优先级，越大越靠前
     *
     * @return 优先级
     */
    int priority();

    /**
     * 改变优先级，越大越靠前
     *
     * @param priority 优先级
     */
    void priority(int priority);

    /**
     * 获取序号
     *
     * @return 序号
     */
    long getId();

    /**
     * 设置序号
     *
     * @param id 序号
     */
    void setId(long id);
}
