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
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.BriefInfo;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.Product;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.processor.GetBriefInfoProcessor;
import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 获取商品简要信息
 *
 * @author xuexiang
 * @since 3/22/22 11:52 PM
 */
public class GetBriefInfoTask extends AbstractTask {

    public GetBriefInfoTask(LoggerTextView logger) {
        super(logger);
    }

    @Override
    public void doTask() throws Exception {
        String productId = getTaskParam().getString(ProductTaskConstants.KEY_PRODUCT_ID);
        new GetBriefInfoProcessor(mLogger, productId)
                .setProcessorCallback(new AbstractProcessor.IProcessorCallback<BriefInfo>() {
                    @Override
                    public void onSuccess(BriefInfo briefInfo) {
                        Product product = new Product(briefInfo);
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
        return "1.获取商品简要信息";
    }
}
