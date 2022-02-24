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

package com.xuexiang.xtaskdemo.fragment.usecase;

import android.view.View;

import androidx.annotation.NonNull;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xtask.XTask;
import com.xuexiang.xtask.core.ITaskChainEngine;
import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtask.core.param.impl.TaskParam;
import com.xuexiang.xtask.core.step.impl.TaskChainCallbackAdapter;
import com.xuexiang.xtaskdemo.R;
import com.xuexiang.xtaskdemo.core.BaseFragment;
import com.xuexiang.xtaskdemo.fragment.usecase.business.entity.Product;
import com.xuexiang.xtaskdemo.fragment.usecase.business.entity.ProductFactory;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.AbstractProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.GetProductInfoProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.business.entity.ProductInfo;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.GivePriceProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.PublicProductProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.SearchFactoryProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.business.task.GetProductInfoTask;
import com.xuexiang.xtaskdemo.fragment.usecase.business.task.GivePriceTask;
import com.xuexiang.xtaskdemo.fragment.usecase.business.task.ProductTaskConstants;
import com.xuexiang.xtaskdemo.fragment.usecase.business.task.PublicProductTask;
import com.xuexiang.xtaskdemo.fragment.usecase.business.task.SearchFactoryTask;
import com.xuexiang.xui.widget.textview.LoggerTextView;
import com.xuexiang.xutil.system.AppExecutors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2/24/22 1:03 AM
 */
@Page(name = "复杂业务流程处理")
public class ComplexBusinessFragment extends BaseFragment {

    @BindView(R.id.logger)
    LoggerTextView logger;

    private String productId = "123456";


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_usecase_template;
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick({R.id.btn_before_improve, R.id.btn_after_improve})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_before_improve:
                doBusinessBeforeImprove();
                break;
            case R.id.btn_after_improve:
                doBusinessAfterImprove();
                break;
            default:
                break;
        }
    }

    /**
     * 优化前的写法, 这里仅是演示模拟，实际的可能更复杂
     * 流程如下：
     * 1.获取产品信息 -> 2.查询可生产的工厂 -> 3.联系工厂生产产品 -> 4.送去市场部门评估售价 -> 5.产品上市
     */
    private void doBusinessBeforeImprove() {
        clearLog();
        log("开始仿冒生产网红产品...");
        AppExecutors.get().singleIO().execute(() -> {
            // 1.获取产品信息
            new GetProductInfoProcessor(logger, productId).setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<ProductInfo>() {
                @Override
                public void onSuccess(final ProductInfo productInfo) {
                    // 2.查询可生产的工厂
                    new SearchFactoryProcessor(logger, productInfo).setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<ProductFactory>() {
                        @Override
                        public void onSuccess(final ProductFactory factory) {
                            // 3.联系工厂生产产品
                            log("开始生产产品...");
                            Product product = factory.produce(productInfo);
                            // 4.送去市场部门评估售价
                            new GivePriceProcessor(logger, product).setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<Product>() {
                                @Override
                                public void onSuccess(Product product) {
                                    // 5.产品上市
                                    PublicProductProcessor publicProductProcessor = new PublicProductProcessor(logger, product);
                                    publicProductProcessor.setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<Product>() {
                                        @Override
                                        public void onSuccess(Product product) {
                                            log("仿冒生产网红产品完成, " + product);
                                        }
                                    }).process();
                                }
                            }).process();
                        }
                    }).process();
                }
            }).process();
        });
    }

    /**
     * 优化后的写法, 这里仅是演示模拟，实际的可能更复杂
     */
    private void doBusinessAfterImprove() {
        clearLog();
        log("开始仿冒生产网红产品...");
        XTask.getTaskChain()
                .setTaskParam(TaskParam.get(ProductTaskConstants.KEY_PRODUCT_ID, productId))
                .addTask(new GetProductInfoTask(logger))
                .addTask(new SearchFactoryTask(logger))
                .addTask(new GivePriceTask(logger))
                .addTask(new PublicProductTask(logger))
                .setTaskChainCallback(new TaskChainCallbackAdapter() {
                    @Override
                    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                        Product product = result.getDataStore().getObject(ProductTaskConstants.KEY_PRODUCT, Product.class);
                        log("仿冒生产网红产品完成, " + product);
                    }
                }).start();
    }


    public void log(String logContent) {
        if (logger != null) {
            logger.logSuccess(logContent);
        }
    }

    public void clearLog() {
        if (logger != null) {
            logger.clearLog();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        XTask.cancelAllTaskChain();
    }
}
