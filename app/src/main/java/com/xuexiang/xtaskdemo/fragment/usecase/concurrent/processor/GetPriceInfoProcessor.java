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
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.PriceInfo;
import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 2.2 获取商品的价格信息
 *
 * @author xuexiang
 * @since 3/22/22 1:20 AM
 */
public class GetPriceInfoProcessor extends AbstractProcessor<PriceInfo> {

    private String priceId;

    public GetPriceInfoProcessor(LoggerTextView logger, String priceId) {
        super(logger);
        this.priceId = priceId;
    }

    @Override
    public PriceInfo process() {
        log("[价格信息查询]开始执行...");
        PriceInfo info = getPriceInfoById(priceId);
        log("[价格信息查询]执行完毕!");
        onProcessSuccess(info);
        return info;
    }

    private PriceInfo getPriceInfoById(String id) {
        // 模拟耗费的时间
        mockProcess(300);
        return new PriceInfo(id)
                .setFactoryPrice(1.5F)
                .setWholesalePrice(2.5F)
                .setRetailPrice(4.5F);
    }
}
