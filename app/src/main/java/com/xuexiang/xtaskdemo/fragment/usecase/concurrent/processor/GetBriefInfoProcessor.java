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
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.BriefInfo;
import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 1 获取商品简要信息
 *
 * @author xuexiang
 * @since 3/22/22 1:18 AM
 */
public class GetBriefInfoProcessor extends AbstractProcessor<BriefInfo> {

    private String productId;

    public GetBriefInfoProcessor(LoggerTextView logger, String productId) {
        super(logger);
        this.productId = productId;
    }

    @Override
    public BriefInfo process() {
        log("[简要信息查询]开始执行...");
        BriefInfo info = getBriefInfoById(productId);
        log("[简要信息查询]执行完毕!");
        onProcessSuccess(info);
        return info;
    }

    private BriefInfo getBriefInfoById(String id) {
        // 模拟耗费的时间
        mockProcess(500);
        return new BriefInfo(id)
                .setName("统一老坛酸菜牛肉面")
                .setFactoryId("fa234632-1234-4567")
                .setPriceId("pr432359-3745-9426")
                .setPromotionId("pt235123-9654-2942")
                .setRichId("ri735294-2346-1048");
    }
}
