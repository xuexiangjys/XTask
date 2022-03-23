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
import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtask.core.param.impl.TaskParam;
import com.xuexiang.xtask.core.step.impl.TaskChainCallbackAdapter;
import com.xuexiang.xtaskdemo.R;
import com.xuexiang.xtaskdemo.core.BaseFragment;
import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.AbstractProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.business.task.ProductTaskConstants;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.BriefInfo;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.FactoryInfo;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.PriceInfo;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.Product;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.PromotionInfo;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.RichInfo;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.processor.GetBriefInfoProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.processor.GetFactoryInfoProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.processor.GetPriceInfoProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.processor.GetPromotionInfoProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.processor.GetRichInfoProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.task.GetBriefInfoTask;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.task.GetFactoryInfoTask;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.task.GetPriceInfoTask;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.task.GetPromotionInfoTask;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.task.GetRichInfoTask;
import com.xuexiang.xui.widget.textview.LoggerTextView;
import com.xuexiang.xutil.system.AppExecutors;

import java.util.concurrent.CountDownLatch;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 复杂并发任务处理
 * <p>
 * 案例[展示商品详细信息]的流程如下：
 * <p>
 * 1.根据商品的唯一号ID获取商品简要信息
 * 2.获取商品的详细信息：
 * 2.1 获取商品的生产信息
 * 2.2 获取商品的价格信息
 * 2.3 获取商品的促销信息
 * 2.4 获取商品的富文本信息
 * 3.进行商品信息的展示
 *
 * @author xuexiang
 * @since 3/21/22 10:57 PM
 */
@Page(name = "复杂并发任务处理")
public class ConcurrentProcessFragment extends BaseFragment {

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
        log("开始查询商品信息...");
        final long startTime = System.currentTimeMillis();
        switch (view.getId()) {
            case R.id.btn_normal:
                queryInfoNormal(startTime, productId);
                break;
            case R.id.btn_rxjava:
                queryInfoRxJava(startTime, productId);
                break;
            case R.id.btn_xtask:
                queryInfoXTask(startTime, productId);
                break;
            default:
                break;
        }
    }


    /**
     * 普通的接口回调写法, 这里仅是演示模拟，实际的可能更复杂
     */
    private void queryInfoNormal(final long startTime, String productId) {
        AppExecutors.get().singleIO().execute(() -> {
            new GetBriefInfoProcessor(logger, productId).setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<BriefInfo>() {
                @Override
                public void onSuccess(BriefInfo briefInfo) {
                    final Product product = new Product(briefInfo);
                    CountDownLatch latch = new CountDownLatch(4);

                    // 2.1 获取商品的生产信息
                    AppExecutors.get().networkIO().execute(() -> {
                        new GetFactoryInfoProcessor(logger, product.getFactoryId()).setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<FactoryInfo>() {
                            @Override
                            public void onSuccess(FactoryInfo result) {
                                product.setFactory(result);
                                latch.countDown();
                            }
                        }).process();
                    });
                    // 2.2 获取商品的价格信息
                    AppExecutors.get().networkIO().execute(() -> {
                        new GetPriceInfoProcessor(logger, product.getPriceId()).setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<PriceInfo>() {
                            @Override
                            public void onSuccess(PriceInfo result) {
                                product.setPrice(result);
                                latch.countDown();
                            }
                        }).process();
                    });
                    // 2.3 获取商品的促销信息
                    AppExecutors.get().networkIO().execute(() -> {
                        new GetPromotionInfoProcessor(logger, product.getPromotionId()).setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<PromotionInfo>() {
                            @Override
                            public void onSuccess(PromotionInfo result) {
                                product.setPromotion(result);
                                latch.countDown();
                            }
                        }).process();
                    });
                    // 2.4 获取商品的富文本信息
                    AppExecutors.get().networkIO().execute(() -> {
                        new GetRichInfoProcessor(logger, product.getRichId()).setProcessorCallback(new AbstractProcessor.ProcessorCallbackAdapter<RichInfo>() {
                            @Override
                            public void onSuccess(RichInfo result) {
                                product.setRich(result);
                                latch.countDown();
                            }
                        }).process();
                    });
                    try {
                        latch.await();
                        log("总共耗时:" + (System.currentTimeMillis() - startTime) + "ms");
                        log("查询商品信息完成, " + product);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).process();
        });
    }

    /**
     * RxJava写法, 这里仅是演示模拟，实际的可能更复杂
     */
    private void queryInfoRxJava(final long startTime, String productId) {
        disposable = Observable.just(productId)
                // 1.获取商品简要信息
                .map(id -> new GetBriefInfoProcessor(logger, id).process())
                .map(Product::new)
                .flatMap(product ->
                        Observable.zip(
                                // 2.1 获取商品的生产信息
                                Observable.fromCallable(() -> new GetFactoryInfoProcessor(logger, product.getFactoryId()).process()).subscribeOn(Schedulers.io()),
                                // 2.2 获取商品的价格信息
                                Observable.fromCallable(() -> new GetPriceInfoProcessor(logger, product.getPriceId()).process()).subscribeOn(Schedulers.io()),
                                // 2.3 获取商品的促销信息
                                Observable.fromCallable(() -> new GetPromotionInfoProcessor(logger, product.getPromotionId()).process()).subscribeOn(Schedulers.io()),
                                // 2.4 获取商品的富文本信息
                                Observable.fromCallable(() -> new GetRichInfoProcessor(logger, product.getRichId()).process()).subscribeOn(Schedulers.io()), (factoryInfo, priceInfo, promotionInfo, richInfo) -> product.setFactory(factoryInfo)
                                        .setPrice(priceInfo)
                                        .setPromotion(promotionInfo)
                                        .setRich(richInfo)
                        )
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(product -> {
                    log("总共耗时:" + (System.currentTimeMillis() - startTime) + "ms");
                    log("查询商品信息完成, " + product);
                });
    }

    /**
     * XTask写法, 这里仅是演示模拟，实际的可能更复杂
     */
    private void queryInfoXTask(final long startTime, String productId) {
        XTask.getTaskChain()
                .setTaskParam(TaskParam.get(ProductTaskConstants.KEY_PRODUCT_ID, productId))
                // 1.获取商品简要信息
                .addTask(new GetBriefInfoTask(logger))
                .addTask(XTask.getConcurrentGroupTask(ThreadType.SYNC)
                        // 2.1 获取商品的生产信息
                        .addTask(new GetFactoryInfoTask(logger))
                        // 2.2 获取商品的价格信息
                        .addTask(new GetPriceInfoTask(logger))
                        // 2.3 获取商品的促销信息
                        .addTask(new GetPromotionInfoTask(logger))
                        // 2.4 获取商品的富文本信息
                        .addTask(new GetRichInfoTask(logger)))
                .setTaskChainCallback(new TaskChainCallbackAdapter() {
                    @Override
                    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                        log("总共耗时:" + (System.currentTimeMillis() - startTime) + "ms");
                        Product product = result.getDataStore().getObject(ProductTaskConstants.KEY_PRODUCT, Product.class);
                        log("查询商品信息完成, " + product);
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
