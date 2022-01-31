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

package com.xuexiang.xtask.core.param.impl;

import com.xuexiang.xtask.core.param.IDataStore;
import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.logger.TaskLogger;
import com.xuexiang.xtask.utils.CommonUtils;

import java.util.Map;

/**
 * 任务参数信息[数据+任务路径]
 *
 * @author xuexiang
 * @since 2021/10/26 1:36 AM
 */
public class TaskParam implements ITaskParam {

    private static final String TAG = TaskLogger.getLogTag("TaskParam");

    /**
     * 路径箭头
     */
    private static final String PATH_ARROW = "->";

    /**
     * 数据存储仓库
     */
    private final IDataStore mDataStore = new MapDataStore();

    /**
     * 任务执行路径
     */
    private StringBuilder mPath = new StringBuilder();

    /**
     * 构建一个空的任务参数
     *
     * @return 任务参数
     */
    public static TaskParam get() {
        return new TaskParam();
    }

    /**
     * 构建一个任务参数
     *
     * @param key   键
     * @param value 值
     * @return 任务参数
     */
    public static TaskParam get(String key, Object value) {
        TaskParam taskParam = new TaskParam();
        taskParam.put(key, value);
        return taskParam;
    }

    @Override
    public void addPath(String path) {
        mPath.append(PATH_ARROW).append(path);
    }

    @Override
    public String getPath() {
        return mPath.toString();
    }

    @Override
    public void updatePath(String path) {
        mPath = new StringBuilder(path);
    }

    @Override
    public IDataStore getDataStore() {
        return mDataStore;
    }

    @Override
    public void updateData(IDataStore iDataStore) {
        if (iDataStore == null) {
            TaskLogger.wTag(TAG, "updateData ignore, iDataStore is null!");
            return;
        }
        if (CommonUtils.isEmpty(iDataStore.getData())) {
            return;
        }
        for (Map.Entry<String, Object> entry : iDataStore.getData().entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void updateParam(String path, IDataStore iDataStore) {
        updatePath(path);
        updateData(iDataStore);
    }

    @Override
    public void updateParam(ITaskParam taskParam) {
        if (taskParam == null) {
            TaskLogger.wTag(TAG, "updateParam ignore, taskParam is null!");
            return;
        }
        updateParam(taskParam.getPath(), taskParam.getDataStore());
    }

    @Override
    public Object get(String key) {
        return mDataStore.get(key);
    }

    @Override
    public TaskParam put(String key, Object value) {
        mDataStore.put(key, value);
        return this;
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        return mDataStore.getObject(key, clazz);
    }

    @Override
    public <T> T getObject(String key, T defaultValue) {
        return mDataStore.getObject(key, defaultValue);
    }

    @Override
    public String getString(String key) {
        return mDataStore.getString(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return mDataStore.getString(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key) {
        return mDataStore.getBoolean(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return mDataStore.getBoolean(key, defaultValue);
    }

    @Override
    public int getInt(String key) {
        return mDataStore.getInt(key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return mDataStore.getInt(key, defaultValue);
    }

    @Override
    public Map<String, Object> getData() {
        return mDataStore.getData();
    }

    @Override
    public void clear() {
        mDataStore.clear();
        mPath.delete(0, mPath.length());
    }
}
