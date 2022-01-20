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

package com.xuexiang.xtask.utils;

import com.xuexiang.xtask.core.ITaskStep;
import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.logger.TaskLogger;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * XTask内部工具类
 *
 * @author xuexiang
 * @since 2021/10/26 1:47 AM
 */
public final class TaskUtils {

    private static final String TAG = TaskLogger.getLogTag("TaskUtils");

    private TaskUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 类型强转
     *
     * @param object 需要强转的对象
     * @param clazz  需要强转的类型
     * @param <T>
     * @return 类型强转结果
     */
    public static <T> T cast(final Object object, Class<T> clazz) {
        return clazz != null && clazz.isInstance(object) ? (T) object : null;
    }

    /**
     * 类型强转
     *
     * @param object       需要强转的对象
     * @param defaultValue 强转的默认值
     * @param <T>
     * @return 类型强转结果
     */
    public static <T> T cast(Object object, T defaultValue) {
        if (defaultValue == null) {
            return null;
        } else if (object == null) {
            return null;
        } else {
            return defaultValue.getClass() == object.getClass() ? (T) object : defaultValue;
        }
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return true: 为空，false：不为空
     */
    public static <E> boolean isEmpty(final Collection<E> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Map是否为空
     *
     * @param obj Map
     * @return true: 为空，false：不为空
     */
    public static boolean isEmpty(final Map obj) {
        return obj == null || obj.isEmpty();
    }

    /**
     * 查找下一条需要执行的任务
     *
     * @param taskStepList 执行任务集合
     * @param taskStep     当前任务
     * @return 下一条执行任务
     */
    public static ITaskStep findNextTaskStep(List<ITaskStep> taskStepList, ITaskStep taskStep) {
        if (isEmpty(taskStepList)) {
            return null;
        }
        int index = 0;
        if (taskStep != null) {
            index = taskStepList.indexOf(taskStep) + 1;
        }
        for (; index < taskStepList.size(); index++) {
            ITaskStep target = taskStepList.get(index);
            if (target != null && target.accept()) {
                return target;
            }
        }
        return null;
    }

    /**
     * 执行任务
     *
     * @param taskStep 需要执行的任务
     */
    public static void executeTask(ITaskStep taskStep) {
        if (taskStep == null) {
            TaskLogger.eTag(TAG, "execute task failed, taskStep is null!");
            return;
        }
        ThreadType type = taskStep.getThreadType();
        if (type == ThreadType.MAIN) {

        } else if (type == ThreadType.ASYNC) {

        } else {

        }
    }

}
