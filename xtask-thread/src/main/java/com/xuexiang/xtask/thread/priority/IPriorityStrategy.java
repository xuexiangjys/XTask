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
 * 优先级策略实现接口
 *
 * @author xuexiang
 * @since 2021/10/9 12:15 PM
 */
public interface IPriorityStrategy {

    /**
     * 比较两个优先级
     *
     * @param priority 优先级
     * @param other    比较的优先级
     * @return 比较结果，如果priority比other的优先级高，那么结果<0;
     */
    int compare(IPriority priority, IPriority other);
}
