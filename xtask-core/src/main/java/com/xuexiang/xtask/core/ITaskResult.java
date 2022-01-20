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
 * 任务执行结果
 *
 * @author xuexiang
 * @since 2021/10/27 2:10 AM
 */
public interface ITaskResult {

    /**
     * 获取任务结果码
     *
     * @return 任务结果码
     */
    int getCode();

    /**
     * 获取任务执行信息
     *
     * @return 任务执行信息
     */
    String getMessage();

    /**
     * 设置结果信息
     *
     * @param code    任务结果码
     * @param message 任务执行信息
     */
    void setResult(int code, String message);

    /**
     * 保存执行结果
     *
     * @param taskResult 执行结果
     */
    void saveResult(ITaskResult taskResult);

    /**
     * 获取数据存储仓库
     *
     * @return 数据存储仓库
     */
    IDataStore getDataStore();

}
