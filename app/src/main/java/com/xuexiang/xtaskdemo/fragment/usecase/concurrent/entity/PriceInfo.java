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
 * 价格信息
 *
 * @author xuexiang
 * @since 3/22/22 1:10 AM
 */
public class PriceInfo {

    private String id;

    /**
     * 出厂价
     */
    private float factoryPrice;

    /**
     * 批发价
     */
    private float wholesalePrice;

    /**
     * 零售价
     */
    private float retailPrice;

    public PriceInfo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public PriceInfo setId(String id) {
        this.id = id;
        return this;
    }

    public float getFactoryPrice() {
        return factoryPrice;
    }

    public PriceInfo setFactoryPrice(float factoryPrice) {
        this.factoryPrice = factoryPrice;
        return this;
    }

    public float getWholesalePrice() {
        return wholesalePrice;
    }

    public PriceInfo setWholesalePrice(float wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
        return this;
    }

    public float getRetailPrice() {
        return retailPrice;
    }

    public PriceInfo setRetailPrice(float retailPrice) {
        this.retailPrice = retailPrice;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "价格编号:" + id
                + ", 出厂价:" + factoryPrice
                + ", 批发价:" + wholesalePrice
                + ", 零售价:" + retailPrice;
    }
}
