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
 * 产品促销信息
 *
 * @author xuexiang
 * @since 3/22/22 1:12 AM
 */
public class PromotionInfo {

    private String id;

    /**
     * 促销类型
     */
    private int type;

    /**
     * 促销内容
     */
    private String content;

    /**
     * 生效日期
     */
    private String effectiveDate;

    /**
     * 失效日期
     */
    private String expirationDate;

    public PromotionInfo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public PromotionInfo setId(String id) {
        this.id = id;
        return this;
    }

    public int getType() {
        return type;
    }

    public PromotionInfo setType(int type) {
        this.type = type;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PromotionInfo setContent(String content) {
        this.content = content;
        return this;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public PromotionInfo setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public PromotionInfo setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "促销编号:" + id
                + ", 促销类型:" + type
                + ", 促销内容:" + content
                + ", 生效日期:" + effectiveDate
                + ", 失效日期:" + expirationDate;
    }
}
