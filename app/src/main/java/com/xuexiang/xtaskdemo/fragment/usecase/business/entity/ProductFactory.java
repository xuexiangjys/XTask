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
 * 产品工厂
 *
 * @author xuexiang
 * @since 2/25/22 1:15 AM
 */
public class ProductFactory {

    private String id;

    private String address;

    public ProductFactory(String id, String address) {
        this.id = id;
        this.address = address;
    }

    public Product produce(ProductInfo info) {
        return new Product(info, address);
    }

    public String getId() {
        return id;
    }

    public ProductFactory setId(String id) {
        this.id = id;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ProductFactory setAddress(String address) {
        this.address = address;
        return this;
    }
}
