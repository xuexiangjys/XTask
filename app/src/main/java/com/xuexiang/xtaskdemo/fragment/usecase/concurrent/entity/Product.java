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
 * 产品
 *
 * @author xuexiang
 * @since 3/22/22 1:07 AM
 */
public class Product extends BriefInfo {

    /**
     * 生产信息
     */
    private FactoryInfo factory;

    /**
     * 价格信息
     */
    private PriceInfo price;

    /**
     * 促销信息
     */
    private PromotionInfo promotion;

    /**
     * 富文本信息
     */
    private RichInfo rich;

    public Product(@NonNull BriefInfo briefInfo) {
        super(briefInfo);
    }

    public FactoryInfo getFactory() {
        return factory;
    }

    public Product setFactory(FactoryInfo factory) {
        this.factory = factory;
        return this;
    }

    public PriceInfo getPrice() {
        return price;
    }

    public Product setPrice(PriceInfo price) {
        this.price = price;
        return this;
    }

    public PromotionInfo getPromotion() {
        return promotion;
    }

    public Product setPromotion(PromotionInfo promotion) {
        this.promotion = promotion;
        return this;
    }

    public RichInfo getRich() {
        return rich;
    }

    public Product setRich(RichInfo rich) {
        this.rich = rich;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "产品信息: " + name
                + "\n产地信息: " + factory
                + "\n价格信息: " + price
                + "\n促销信息: " + promotion
                + "\n富文本信息: " + rich;
    }
}
