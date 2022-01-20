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

package com.xuexiang.xtask.core.impl;

import com.xuexiang.xtask.core.IDataStore;
import com.xuexiang.xtask.logger.TaskLogger;
import com.xuexiang.xtask.utils.TaskUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认数据存储仓库， 使用ConcurrentHashMap实现
 *
 * @author xuexiang
 * @since 2021/11/13 4:09 PM
 */
public class MapDataStore implements IDataStore {

    private static final String TAG = TaskLogger.getLogTag("MapDataStore");

    /**
     * 数据存储
     */
    private Map<String, Object> mData = new ConcurrentHashMap<>();

    @Override
    public Object get(String key) {
        if (key == null) {
            return null;
        }
        return mData.get(key);
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        return TaskUtils.cast(get(key), clazz);
    }

    @Override
    public <T> T getObject(String key, T defaultValue) {
        return TaskUtils.cast(get(key), defaultValue);
    }

    @Override
    public String getString(String key) {
        return getString(key, DEFAULT_STRING);
    }

    @Override
    public String getString(String key, String defaultValue) {
        String value = getObject(key, String.class);
        return value != null ? value : defaultValue;
    }

    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, DEFAULT_BOOLEAN);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        Boolean value = getObject(key, Boolean.class);
        return value != null ? value : defaultValue;
    }

    @Override
    public int getInt(String key) {
        return getInt(key, DEFAULT_INTEGER);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        Integer value = getObject(key, Integer.class);
        return value != null ? value : defaultValue;
    }

    @Override
    public Map<String, Object> getData() {
        return mData;
    }

    @Override
    public void put(String key, Object value) {
        if (key == null || value == null) {
            TaskLogger.eTag(TAG, "put param error, key or value is null!");
            return;
        }
        mData.put(key, value);
    }
}
