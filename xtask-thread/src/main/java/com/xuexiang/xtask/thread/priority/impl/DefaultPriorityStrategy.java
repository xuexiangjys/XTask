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

package com.xuexiang.xtask.thread.priority.impl;

import com.xuexiang.xtask.thread.priority.IPriority;
import com.xuexiang.xtask.thread.priority.IPriorityStrategy;

/**
 * 默认的优先级比较策略
 * 优先级值大的放前面，当优先值一样的，序号小的放前面
 *
 * @author xuexiang
 * @since 2021/10/9 3:05 PM
 */
public class DefaultPriorityStrategy implements IPriorityStrategy {
    @Override
    public int compare(IPriority priority, IPriority other) {
        if (priority == null || other == null) {
            return 0;
        }
        if (priority.priority() == other.priority()) {
            // ASC(升序)，序号小的放前面
            return priority.getId() < other.getId() ? -1 : 1;
        } else {
            // DESC(降序), 优先级大的放前面
            return priority.priority() > other.priority() ? -1 : 1;
        }
    }
}
