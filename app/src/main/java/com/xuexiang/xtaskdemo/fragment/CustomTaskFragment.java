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

package com.xuexiang.xtaskdemo.fragment;

import android.util.Log;

import androidx.annotation.NonNull;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xtask.XTask;
import com.xuexiang.xtask.api.TaskChainEngine;
import com.xuexiang.xtask.api.step.SimpleTaskStep;
import com.xuexiang.xtask.core.ITaskChainEngine;
import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtask.core.param.impl.TaskParam;
import com.xuexiang.xtask.core.param.impl.TaskResult;
import com.xuexiang.xtask.core.step.impl.TaskChainCallbackAdapter;
import com.xuexiang.xtask.thread.pool.ICanceller;
import com.xuexiang.xtaskdemo.core.BaseSimpleListFragment;
import com.xuexiang.xtaskdemo.fragment.task.Job;
import com.xuexiang.xtaskdemo.fragment.task.JobTask;
import com.xuexiang.xtaskdemo.fragment.task.StepATask;
import com.xuexiang.xtaskdemo.fragment.task.StepBTask;
import com.xuexiang.xutil.common.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xuexiang
 * @since 2/1/22 11:07 PM
 */
@Page(name = "自定义任务使用\n通过继承AbstractTaskStep实现")
public class CustomTaskFragment extends BaseSimpleListFragment {

    public static final String TAG = "CustomTaskFragment";

    private Set<String> mPool = new HashSet<>();

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("简单继承AbstractTaskStep使用");
        lists.add("简单继承SimpleTaskStep使用");
        lists.add("复杂任务使用");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                doSimpleAbstractTaskStep();
                break;
            case 1:
                doSimpleTaskStep();
                break;
            case 2:
                doComplexTaskStep();
                break;
            default:
                break;
        }
    }

    /**
     * 简单继承AbstractTaskStep使用
     */
    private void doSimpleAbstractTaskStep() {
        final TaskChainEngine engine = XTask.getTaskChain();
        ICanceller canceller = engine.addTask(new StepATask())
                .addTask(new StepBTask())
                .setTaskChainCallback(new TaskChainCallbackAdapter() {
                    @Override
                    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                        Log.e(TAG, "task chain completed, result:" + result.getDataStore().getInt(StepATask.KEY_TOTAL));
                    }
                })
                .start();
        addCanceller(canceller);
    }


    /**
     * 简单继承SimpleTaskStep使用
     */
    private void doSimpleTaskStep() {
        final TaskChainEngine engine = XTask.getTaskChain();
        ICanceller canceller = engine
                .addTask(new SimpleTaskStep("TaskStep1", ThreadType.ASYNC_IO) {
                    @Override
                    public void doTask() throws Exception {
                        Log.e(CustomTaskFragment.TAG, "doing TaskStep1...., thread:" + Thread.currentThread().getName());
                        // 模拟耗时
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getTaskParam().put(StepATask.KEY_TOTAL, 123);
                        notifyTaskSucceed(TaskResult.succeed());
                    }
                })
                .addTask(new SimpleTaskStep("TaskStep2", ThreadType.SYNC) {
                    @Override
                    public void doTask() throws Exception {
                        Log.e(CustomTaskFragment.TAG, "doing TaskStep2...., thread:" + Thread.currentThread().getName());
                        // 模拟耗时
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ITaskParam param = getTaskParam();
                        param.put(StepATask.KEY_TOTAL, param.getInt(StepATask.KEY_TOTAL) + 321);
                        notifyTaskSucceed(TaskResult.succeed());
                    }
                })
                .setTaskChainCallback(new TaskChainCallbackAdapter() {
                    @Override
                    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                        Log.e(TAG, "task chain completed, thread:" + Thread.currentThread().getName() + ", result:" + result.getDataStore().getInt(StepATask.KEY_TOTAL));
                    }
                })
                .start();
        addCanceller(canceller);
    }

    /**
     * 复杂任务使用
     */
    private void doComplexTaskStep() {
        final TaskChainEngine engine = XTask.getTaskChain();
        TaskParam taskParam = TaskParam.get(Job.KEY_JOB_TARGET, 100);
        engine.setTaskParam(taskParam);
        for (int i = 0; i < 5; i++) {
            engine.addTask(new JobTask());
        }
        ICanceller canceller = engine.setTaskChainCallback(new TaskChainCallbackAdapter() {
            @Override
            public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                Log.e(TAG, "task chain completed, result:" + result.getDataStore().getInt(Job.KEY_JOB_TARGET));
            }
        }).start();
        addCanceller(canceller);
    }

    private void addCanceller(ICanceller canceller) {
        if (canceller != null) {
            mPool.add(canceller.getName());
        }
    }

    @Override
    public void onDestroyView() {
        if (!ObjectUtils.isEmpty(mPool)) {
            XTask.cancelTaskChain(mPool);
        }
        super.onDestroyView();
    }
}
