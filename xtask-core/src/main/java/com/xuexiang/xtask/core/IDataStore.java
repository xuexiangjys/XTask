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

import java.util.Map;

/**
 * 数据存储仓库接口
 *
 * @author xuexiang
 * @since 2021/11/13 4:08 PM
 */
public interface IDataStore {

    /**
     * String的默认值
     */
    String DEFAULT_STRING = "";
    /**
     * Integer的默认值
     */
    int DEFAULT_INTEGER = -1;
    /**
     * Boolean的默认值
     */
    boolean DEFAULT_BOOLEAN = false;

    /**
     * 获取数据
     *
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * 存储数据
     *
     * @param key   键
     * @param value 值
     */
    void put(String key, Object value);

    /**
     * 获取目标数据
     *
     * @param key   键
     * @param clazz 目标数据类型
     * @return 值
     */
    <T> T getObject(String key, Class<T> clazz);

    /**
     * 获取目标数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    <T> T getObject(String key, T defaultValue);

    /**
     * 获取String类型数据
     *
     * @param key 键
     * @return 值
     */
    String getString(String key);

    /**
     * 获取String类型数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    String getString(String key, String defaultValue);

    /**
     * 获取Boolean类型数据
     *
     * @param key 键
     * @return 值
     */
    boolean getBoolean(String key);

    /**
     * 获取boolean类型数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * 获取Integer类型数据
     *
     * @param key 键
     * @return 值
     */
    int getInt(String key);

    /**
     * 获取int类型数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    int getInt(String key, int defaultValue);

    /**
     * 获取存储的所以信息
     *
     * @return 存储信息
     */
    Map<String, Object> getData();

}
