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
import com.xuexiang.xtask.api.step.ConcurrentGroupTaskStep;
import com.xuexiang.xtask.core.ITaskChainEngine;
import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtask.core.step.impl.TaskChainCallbackAdapter;
import com.xuexiang.xtask.core.step.impl.TaskCommand;
import com.xuexiang.xtaskdemo.R;
import com.xuexiang.xtaskdemo.core.BaseFragment;
import com.xuexiang.xtaskdemo.fragment.usecase.appstart.job.LongTimeJob;
import com.xuexiang.xtaskdemo.fragment.usecase.appstart.job.SingleJob;
import com.xuexiang.xtaskdemo.fragment.usecase.appstart.job.TopPriorityJob;
import com.xuexiang.xtaskdemo.fragment.usecase.appstart.task.AsyncInitTask;
import com.xuexiang.xtaskdemo.fragment.usecase.appstart.task.MainInitTask;
import com.xuexiang.xui.widget.textview.LoggerTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 应用冷启动优化
 * <p>
 * 所谓的应用冷启动优化，最主要的就是减少应用在初始化应用在主线程中所占用的时间。
 * 针对一些耗时的操作，我们常用的优化策略是：
 * 1.同步主线程执行 -> 异步子线程执行
 * 2.串行操作 -> 并行操作
 * 3.预加载 -> 懒加载
 * 4.业务流程优化（按业务的优先级调整执行顺序）
 * 5.数据结构优化（减少加载时间）
 *
 * @author xuexiang
 * @since 2/23/22 12:26 AM
 */
@Page(name = "应用冷启动优化")
public class AppStartFragment extends BaseFragment {

    @BindView(R.id.logger)
    LoggerTextView logger;

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
        clearLog();
        log("开始执行任务...");
        final long startTime = System.currentTimeMillis();
        switch (view.getId()) {
            case R.id.btn_before_improve:
                doJobBeforeImprove(startTime);
                break;
            case R.id.btn_after_improve:
                doJobAfterImprove(startTime);
                break;
            default:
                break;
        }
    }


    /**
     * 优化前的写法, 这里仅是演示模拟，实际的可能更复杂
     */
    private void doJobBeforeImprove(long startTime) {
        new TopPriorityJob(logger).doJob();
        for (int i = 0; i < 4; i++) {
            new SingleJob((i + 1), logger).doJob();
        }
        new LongTimeJob(logger).doJob();
        log("任务执行完毕，总共耗时:" + (System.currentTimeMillis() - startTime) + "ms");
    }

    /**
     * 优化后的写法, 这里仅是演示模拟，实际的可能更复杂
     */
    private void doJobAfterImprove(final long startTime) {
        ConcurrentGroupTaskStep groupTaskStep = XTask.getConcurrentGroupTask();
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            groupTaskStep.addTask(XTask.getTask(new TaskCommand() {
                @Override
                public void run() throws Exception {
                    new SingleJob((finalI + 1), logger).doJob();
                }
            }));
        }
        XTask.getTaskChain()
                .addTask(new MainInitTask(logger))
                .addTask(groupTaskStep)
                .addTask(new AsyncInitTask(logger))
                .setTaskChainCallback(new TaskChainCallbackAdapter() {
                    @Override
                    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                        log("任务完全执行完毕，总共耗时:" + (System.currentTimeMillis() - startTime) + "ms");
                    }
                }).start();
        log("主线程任务执行完毕，总共耗时:" + (System.currentTimeMillis() - startTime) + "ms");
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
