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

package com.xuexiang.xtaskdemo.fragment.usecase.business.processor;

import com.xuexiang.xtaskdemo.fragment.usecase.business.entity.Product;
import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 3.评估产品，给出价格
 * @author xuexiang
 * @since 2/25/22 1:36 AM
 */
public class GivePriceProcessor extends AbstractProcessor<Product> {

    private Product product;

    public GivePriceProcessor(LoggerTextView logger, Product product) {
        super(logger);
        this.product = product;
    }

    @Override
    public Product process() {
        log("[评估产品价格]开始执行...");
        String price = givePrice(product);
        product.setPrice(price);
        log("[评估产品价格]执行完毕!");
        onProcessSuccess(product);
        return product;
    }

    private String givePrice(Product product) {
        // 模拟耗费的时间
        mockProcess(100);
        return "45¥";
    }
}
