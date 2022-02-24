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

import com.xuexiang.xtask.api.step.SimpleTaskStep;
import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtaskdemo.fragment.usecase.business.entity.ProductInfo;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.AbstractProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.GetProductInfoProcessor;
import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 1.获取产品信息
 *
 * @author xuexiang
 * @since 2/25/22 2:07 AM
 */
public class GetProductInfoTask extends AbstractProductTask {

    public GetProductInfoTask(LoggerTextView logger) {
        super(logger);
    }

    @Override
    public void doTask() throws Exception {
        String productId = getTaskParam().getString(ProductTaskConstants.KEY_PRODUCT_ID);
        new GetProductInfoProcessor(mLogger, productId)
                .setProcessorCallback(new AbstractProcessor.IProcessorCallback<ProductInfo>() {
                    @Override
                    public void onSuccess(ProductInfo info) {
                        getTaskParam().put(ProductTaskConstants.KEY_PRODUCT_INFO, info);
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
        return "1.获取产品信息";
    }


}
