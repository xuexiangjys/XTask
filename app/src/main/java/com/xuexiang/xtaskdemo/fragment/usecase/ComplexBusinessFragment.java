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

import android.util.Pair;
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
import com.xuexiang.xtaskdemo.fragment.usecase.business.entity.ProductInfo;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.AbstractProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.GetProductInfoProcessor;
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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * ????????????????????????
 *
 * ??????[??????????????????]??????????????????
 * 1.?????????????????? -> 2.???????????????????????? -> 3.???????????????????????? -> 4.?????????????????????????????? -> 5.????????????
 *
 * @author xuexiang
 * @since 3/18/22 11:28 PM
 */
@Page(name = "????????????????????????")
public class ComplexBusinessFragment extends BaseFragment {

    @BindView(R.id.logger)
    LoggerTextView logger;

    private String productId = "123456";

    private Disposable disposable;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_usecase_compare;
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick({R.id.btn_normal, R.id.btn_rxjava, R.id.btn_xtask})
    public void onViewClicked(View view) {
        clearLog();
        log("??????????????????????????????...");
        final long startTime = System.currentTimeMillis();
        switch (view.getId()) {
            case R.id.btn_normal:
                doBusinessNormal(startTime);
                break;
            case R.id.btn_rxjava:
                doBusinessRxJava(startTime);
                break;
            case R.id.btn_xtask:
                doBusinessXTask(startTime);
                break;
            default:
                break;
        }
    }

    /**
     * ???????????????????????????, ???????????????????????????????????????????????????
     * ???????????????
     * 1.?????????????????? -> 2.???????????????????????? -> 3.???????????????????????? -> 4.?????????????????????????????? -> 5.????????????
     */
    private void doBusinessNormal(final long startTime) {
        AppExecutors.get().singleIO().execute(() -> {
            // 1.??????????????????
            new GetProductInfoProcessor(logger, productId).setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<ProductInfo>() {
                @Override
                public void onSuccess(final ProductInfo productInfo) {
                    // 2.????????????????????????
                    new SearchFactoryProcessor(logger, productInfo).setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<ProductFactory>() {
                        @Override
                        public void onSuccess(final ProductFactory factory) {
                            // 3.????????????????????????
                            log("??????????????????...");
                            Product product = factory.produce(productInfo);
                            // 4.??????????????????????????????
                            new GivePriceProcessor(logger, product).setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<Product>() {
                                @Override
                                public void onSuccess(Product product) {
                                    // 5.????????????
                                    PublicProductProcessor publicProductProcessor = new PublicProductProcessor(logger, product);
                                    publicProductProcessor.setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<Product>() {
                                        @Override
                                        public void onSuccess(Product product) {
                                            log("????????????:" + (System.currentTimeMillis() - startTime) + "ms");
                                            log("??????????????????????????????, " + product);
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
     * RxJava??????, ???????????????????????????????????????????????????
     * ???????????????
     * 1.?????????????????? -> 2.???????????????????????? -> 3.???????????????????????? -> 4.?????????????????????????????? -> 5.????????????
     */
    private void doBusinessRxJava(final long startTime) {
        disposable = Observable.just(productId)
                // 1.??????????????????
                .map(id -> new GetProductInfoProcessor(logger, id).process())
                // 2.????????????????????????
                .map(productInfo -> new Pair<>(new SearchFactoryProcessor(logger, productInfo).process(), productInfo))
                .map(productPair -> {
                    // 3.????????????????????????
                    log("??????????????????...");
                    Product product = productPair.first.produce(productPair.second);
                    // 4.??????????????????????????????
                    return new GivePriceProcessor(logger, product).process();
                })
                // 5.????????????
                .map(product -> new PublicProductProcessor(logger, product).process())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(product -> {
                    log("????????????:" + (System.currentTimeMillis() - startTime) + "ms");
                    log("??????????????????????????????, " + product);
                });
    }


    /**
     * XTask??????, ???????????????????????????????????????????????????
     * ???????????????
     * 1.?????????????????? -> 2.???????????????????????? -> 3.???????????????????????? -> 4.?????????????????????????????? -> 5.????????????
     */
    private void doBusinessXTask(final long startTime) {
        XTask.getTaskChain()
                .setTaskParam(TaskParam.get(ProductTaskConstants.KEY_PRODUCT_ID, productId))
                // 1.??????????????????
                .addTask(new GetProductInfoTask(logger))
                // 2.????????????????????????, 3.????????????????????????
                .addTask(new SearchFactoryTask(logger))
                // 4.??????????????????????????????
                .addTask(new GivePriceTask(logger))
                // 5.????????????
                .addTask(new PublicProductTask(logger))
                .setTaskChainCallback(new TaskChainCallbackAdapter() {
                    @Override
                    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                        log("????????????:" + (System.currentTimeMillis() - startTime) + "ms");
                        Product product = result.getDataStore().getObject(ProductTaskConstants.KEY_PRODUCT, Product.class);
                        log("??????????????????????????????, " + product);
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
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        XTask.cancelAllTaskChain();
    }
}
