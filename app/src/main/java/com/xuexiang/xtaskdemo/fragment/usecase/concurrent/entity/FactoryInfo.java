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

package com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity;

import androidx.annotation.NonNull;

/**
 * 工厂生产信息
 *
 * @author xuexiang
 * @since 3/22/22 1:11 AM
 */
public class FactoryInfo {

    private String id;

    /**
     * 生产地址
     */
    private String address;

    /**
     * 生产日期
     */
    private String productDate;

    /**
     * 过期日期
     */
    private String expirationDate;

    public FactoryInfo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public FactoryInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public FactoryInfo setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getProductDate() {
        return productDate;
    }

    public FactoryInfo setProductDate(String productDate) {
        this.productDate = productDate;
        return this;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public FactoryInfo setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "生产编号:" + id
                + ", 生产地址:" + address
                + ", 生产日期:" + productDate
                + ", 过期日期:" + expirationDate;
    }
}
