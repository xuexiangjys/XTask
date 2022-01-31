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

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xtask.api.TaskChainEngine;
import com.xuexiang.xtask.api.step.XTaskStep;
import com.xuexiang.xtask.core.ITaskChainEngine;
import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtask.core.param.impl.TaskParam;
import com.xuexiang.xtask.core.param.impl.TaskResult;
import com.xuexiang.xtask.core.step.impl.TaskChainCallbackAdapter;
import com.xuexiang.xtask.core.step.impl.TaskCommand;
import com.xuexiang.xtaskdemo.core.BaseSimpleListFragment;

import java.util.List;
import java.util.Map;

/**
 * @author xuexiang
 * @since 1/20/22 12:24 AM
 */
@Page(name = "Task任务使用")
public class TaskFragment extends BaseSimpleListFragment {

    private static final String TAG = "TaskFragment";

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("简单任务链使用");
        lists.add("任务链参数传递");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                doSimpleTaskChain();
                break;
            case 1:
                doParamTaskChain();
                break;
            default:
                break;
        }

    }

    private void doSimpleTaskChain() {
        final TaskChainEngine engine = TaskChainEngine.get();
        for (int i = 0; i < 5; i++) {
            engine.addTask(XTaskStep.getTask(new SimpleTaskCommand(1000 * i)));
        }
        engine.setTaskChainCallback(new TaskChainCallbackAdapter() {

            @Override
            public boolean isCallBackOnMainThread() {
                return false;
            }

            @Override
            public void onTaskChainStart(ITaskChainEngine engine) {
                Log.e(TAG, "task chain start");
            }

            @Override
            public void onTaskChainCompleted(ITaskChainEngine engine, ITaskResult result) {
                Log.e(TAG, "task chain completed, thread:" + Thread.currentThread().getName());
                engine.destroy();
            }

            @Override
            public void onTaskChainError(ITaskChainEngine engine, ITaskResult result) {
                Log.e(TAG, "task chain error");
            }
        });
        engine.start();
    }

    private void doParamTaskChain() {
        final TaskChainEngine engine = TaskChainEngine.get();
        TaskParam taskParam = TaskParam.get("param1", 100)
                .put("param2", true);
        XTaskStep taskStep = XTaskStep.newBuilder(new TaskCommand() {
            @Override
            public void run() {
                ITaskParam param = getTaskParam();
                Log.e(TAG, getName() + "  start, param1:" + param.get("param1"));
                param.put("param1", 200);
                param.put("param3", "this is param3!");
                onTaskSucceed(TaskResult.succeed());
            }
        }).setTaskParam(taskParam).build();
        engine.addTask(taskStep)
                .addTask(XTaskStep.getTask(new TaskCommand() {
                    @Override
                    public void run() {
                        ITaskParam param = getTaskParam();
                        Log.e(TAG, getName() + "  start, param1:" + param.get("param1") + ", param3:" + param.get("param3"));
                        param.put("param2", false);
                        onTaskSucceed(TaskResult.succeed());
                    }
                }));
        engine.setTaskChainCallback(new TaskChainCallbackAdapter() {
            @Override
            public void onTaskChainCompleted(ITaskChainEngine engine, ITaskResult result) {
                Log.e(TAG, "task chain completed, thread:" + Thread.currentThread().getName());
                Map<String, Object> data = result.getDataStore().getData();
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    Log.e(TAG, "key:" + entry.getKey() + ", value:" + entry.getValue());
                }
            }
        });
        engine.start();

    }


    private static class SimpleTaskCommand extends TaskCommand {

        private long mDuring;

        private SimpleTaskCommand(long during) {
            mDuring = during;
        }

        @Override
        public void run() {
            Log.e(TAG, getName() + "  start...");
            try {
                Thread.sleep(mDuring);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e(TAG, getName() + "  end...");
            onTaskSucceed(TaskResult.succeed());
        }
    }
}
