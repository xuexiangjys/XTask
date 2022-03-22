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

package com.xuexiang.xtaskdemo.fragment.usecase.concurrent.task;

import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.AbstractProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.business.task.AbstractTask;
import com.xuexiang.xtaskdemo.fragment.usecase.business.task.ProductTaskConstants;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.PriceInfo;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.Product;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.processor.GetPriceInfoProcessor;
import com.xuexiang.xui.widget.textview.LoggerTextView;
import com.xuexiang.xutil.common.StringUtils;

/**
 * 2.2 获取商品的价格信息
 *
 * @author xuexiang
 * @since 3/23/22 12:18 AM
 */
public class GetPriceInfoTask extends AbstractTask {

    public GetPriceInfoTask(LoggerTextView logger) {
        super(logger);
    }

    @Override
    public void doTask() throws Exception {
        final Product product = getTaskParam().getObject(ProductTaskConstants.KEY_PRODUCT, Product.class);
        if (product == null || StringUtils.isEmpty(product.getPriceId())) {
            notifyTaskFailed(ITaskResult.ERROR, "product is null or priceId is empty!");
            return;
        }
        new GetPriceInfoProcessor(mLogger, product.getPriceId())
                .setProcessorCallback(new AbstractProcessor.IProcessorCallback<PriceInfo>() {
                    @Override
                    public void onSuccess(PriceInfo priceInfo) {
                        getTaskParam().put(ProductTaskConstants.KEY_PRODUCT, product.setPrice(priceInfo));
                        notifyTaskSucceed();
                    }

                    @Override
                    public void onFailed(String error) {
                        notifyTaskFailed(ITaskResult.ERROR, error);
                    }
                })
                .process();
    }


    @Override
    public String getName() {
        return "2.2 获取商品的价格信息";
    }
}
