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

package com.xuexiang.xtask.core.param;

/**
 * 任务参数信息实现接口
 *
 * @author xuexiang
 * @since 2021/10/19 1:44 AM
 */
public interface ITaskParam extends IDataStore {

    /**
     * 增加任务路径
     *
     * @param path 任务路径
     */
    void addPath(String path);

    /**
     * 获取当前任务执行的全路径
     *
     * @return 当前任务执行的全路径
     */
    String getPath();

    /**
     * 更新路径
     *
     * @param path 任务全路径
     */
    void updatePath(String path);

    /**
     * 获取数据存储仓库
     *
     * @return 数据存储仓库
     */
    IDataStore getDataStore();

    /**
     * 更新数据
     *
     * @param iDataStore 数据存储仓库
     */
    void updateData(IDataStore iDataStore);

    /**
     * 更新参数
     *
     * @param path       任务全路径
     * @param iDataStore 数据存储仓库
     */
    void updateParam(String path, IDataStore iDataStore);

    /**
     * 更新参数
     *
     * @param taskParam 任务参数
     */
    void updateParam(ITaskParam taskParam);

}
