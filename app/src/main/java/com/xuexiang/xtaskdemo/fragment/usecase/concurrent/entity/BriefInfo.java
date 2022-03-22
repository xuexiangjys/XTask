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
 * 产品简要信息
 *
 * @author xuexiang
 * @since 3/22/22 1:08 AM
 */
public class BriefInfo {

    private String id;

    protected String name;

    private String factoryId;

    private String priceId;

    private String promotionId;

    private String richId;

    public BriefInfo(String id) {
        this.id = id;
    }

    public BriefInfo(@NonNull BriefInfo briefInfo) {
        this(briefInfo.id, briefInfo.name, briefInfo.factoryId, briefInfo.priceId, briefInfo.promotionId, briefInfo.richId);
    }

    public BriefInfo(String id, String name, String factoryId, String priceId, String promotionId, String richId) {
        this.id = id;
        this.name = name;
        this.factoryId = factoryId;
        this.priceId = priceId;
        this.promotionId = promotionId;
        this.richId = richId;
    }

    public String getId() {
        return id;
    }

    public BriefInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public BriefInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public BriefInfo setFactoryId(String factoryId) {
        this.factoryId = factoryId;
        return this;
    }

    public String getPriceId() {
        return priceId;
    }

    public BriefInfo setPriceId(String priceId) {
        this.priceId = priceId;
        return this;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public BriefInfo setPromotionId(String promotionId) {
        this.promotionId = promotionId;
        return this;
    }

    public String getRichId() {
        return richId;
    }

    public BriefInfo setRichId(String richId) {
        this.richId = richId;
        return this;
    }

}
