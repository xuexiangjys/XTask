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

package com.xuexiang.xtaskdemo.fragment.usecase.concurrent.processor;

import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.AbstractProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.PromotionInfo;
import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 2.3 获取商品的促销信息
 *
 * @author xuexiang
 * @since 3/22/22 1:23 AM
 */
public class GetPromotionInfoProcessor extends AbstractProcessor<PromotionInfo> {

    private String promotionId;

    public GetPromotionInfoProcessor(LoggerTextView logger, String promotionId) {
        super(logger);
        this.promotionId = promotionId;
    }

    @Override
    public PromotionInfo process() {
        log("[促销信息查询]开始执行...");
        PromotionInfo info = getPromotionInfoById(promotionId);
        log("[促销信息查询]执行完毕!");
        onProcessSuccess(info);
        return info;
    }

    private PromotionInfo getPromotionInfoById(String id) {
        // 模拟耗费的时间
        mockProcess(150);
        return new PromotionInfo(id)
                .setType(5)
                .setContent("买一送一")
                .setEffectiveDate("2022年3月15日")
                .setExpirationDate("2022年4月15日");
    }
}
