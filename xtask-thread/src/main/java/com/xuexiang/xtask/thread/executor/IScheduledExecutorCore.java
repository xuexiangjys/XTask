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

package com.xuexiang.xtask.thread.executor;

import com.xuexiang.xtask.thread.pool.cancel.ICancelable;

import java.util.concurrent.TimeUnit;

/**
 * 拥有周期执行能力的内核实现接口
 *
 * @author xuexiang
 * @since 3/19/22 6:40 PM
 */
public interface IScheduledExecutorCore extends IExecutorCore {

    /**
     * 执行延期任务
     *
     * @param task  任务
     * @param delay 延迟时长
     * @param unit  时间单位
     * @return 取消接口
     */
    ICancelable schedule(Runnable task, long delay, TimeUnit unit);

    /**
     * 执行周期任务（以固定频率执行的任务）
     *
     * @param task         任务
     * @param initialDelay 初始延迟时长
     * @param period       间隔时长
     * @param unit         时间单位
     * @return 取消接口
     */
    ICancelable scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit);

    /**
     * 执行周期任务（以固定延时执行的任务，延时是相对当前任务结束为起点计算开始时间）
     *
     * @param task         任务
     * @param initialDelay 初始延迟时长
     * @param period       间隔时长
     * @param unit         时间单位
     * @return 取消接口
     */
    ICancelable scheduleWithFixedDelay(Runnable task, long initialDelay, long period, TimeUnit unit);

}
