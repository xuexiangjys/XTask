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
 * 产品
 *
 * @author xuexiang
 * @since 2/25/22 1:17 AM
 */
public class Product {

    private ProductInfo info;

    private String address;

    private String price;

    private String publicTime;

    public Product(ProductInfo info, String address) {
        this.info = info;
        this.address = address;
    }

    public ProductInfo getInfo() {
        return info;
    }

    public Product setInfo(ProductInfo info) {
        this.info = info;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Product setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public Product setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getPublicTime() {
        return publicTime;
    }


    public Product setPublicTime(String publicTime) {
        this.publicTime = publicTime;
        return this;
    }

    @Override
    public String toString() {
        return "产品信息:" + info + ", 产地:'" + address + ", 价格:'" + price + ", 上市日前:'" + publicTime;
    }
}
