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

package com.xuexiang.xtaskdemo.fragment.usecase.business.entity;

/**
 * 产品信息
 *
 * @author xuexiang
 * @since 2/25/22 12:40 AM
 */
public class ProductInfo {
    /**
     * 编号
     */
    private String id;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 质量
     */
    private String quality;

    public ProductInfo(String id) {
        this.id = id;
    }

    public ProductInfo(String id, String brand, String quality) {
        this.id = id;
        this.brand = brand;
        this.quality = quality;
    }

    public String getId() {
        return id;
    }

    public ProductInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public ProductInfo setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getQuality() {
        return quality;
    }

    public ProductInfo setQuality(String quality) {
        this.quality = quality;
        return this;
    }

    @Override
    public String toString() {
        return "id:" + id + ", 品牌:" + brand + ", 品质:" + quality;
    }
}
