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

package com.xuexiang.xtaskdemo.fragment.usecase.business.task;

import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtaskdemo.fragment.usecase.business.entity.Product;
import com.xuexiang.xtaskdemo.fragment.usecase.business.entity.ProductFactory;
import com.xuexiang.xtaskdemo.fragment.usecase.business.entity.ProductInfo;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.AbstractProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.SearchFactoryProcessor;
import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 2.查找相关的工厂并生产
 *
 * @author xuexiang
 * @since 2/25/22 2:17 AM
 */
public class SearchFactoryTask extends AbstractProductTask {

    public SearchFactoryTask(LoggerTextView logger) {
        super(logger);
    }

    @Override
    public void doTask() throws Exception {
        ProductInfo productInfo = getTaskParam().getObject(ProductTaskConstants.KEY_PRODUCT_INFO, ProductInfo.class);
        new SearchFactoryProcessor(mLogger, productInfo)
                .setProcessorCallback(new AbstractProcessor.IProcessorCallback<ProductFactory>() {
                    @Override
                    public void onSuccess(ProductFactory factory) {
                        log("开始生产产品...");
                        Product product = factory.produce(productInfo);
                        getTaskParam().put(ProductTaskConstants.KEY_PRODUCT, product);
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
        return "2.查找相关的工厂并生产";
    }
}
