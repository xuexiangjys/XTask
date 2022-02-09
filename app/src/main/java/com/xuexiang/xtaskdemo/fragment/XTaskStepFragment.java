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
import com.xuexiang.xtask.api.step.ConcurrentGroupTaskStep;
import com.xuexiang.xtask.api.step.SerialGroupTaskStep;
import com.xuexiang.xtask.api.step.XTaskStep;
import com.xuexiang.xtask.core.ITaskChainEngine;
import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtask.core.param.impl.TaskParam;
import com.xuexiang.xtask.core.param.impl.TaskResult;
import com.xuexiang.xtask.core.step.impl.TaskChainCallbackAdapter;
import com.xuexiang.xtask.core.step.impl.TaskCommand;
import com.xuexiang.xtask.thread.pool.ICanceller;
import com.xuexiang.xtaskdemo.core.BaseSimpleListFragment;
import com.xuexiang.xutil.common.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xuexiang
 * @since 1/20/22 12:24 AM
 */
@Page(name = "XTaskStep任务使用\n通过XTaskStep进行简化使用")
public class XTaskStepFragment extends BaseSimpleListFragment {

    private static final String TAG = "TaskFragment";

    private Set<String> mPool = new HashSet<>();

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("简单任务链使用");
        lists.add("任务链参数传递");
        lists.add("任务线程控制");
        lists.add("简单的串行任务组使用");
        lists.add("简单的并行任务组使用");
        lists.add("任务执行失败");
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
            case 2:
                doThreadTypeTaskChain();
                break;
            case 3:
                doSerialGroupTaskChain();
                break;
            case 4:
                doConcurrentGroupTaskChain();
                break;
            case 5:
                doTaskError();
                break;
            default:
                break;
        }

    }

    /**
     * 简单任务链的使用
     */
    private void doSimpleTaskChain() {
        final TaskChainEngine engine = XTask.getTaskChain();
        for (int i = 0; i < 5; i++) {
            engine.addTask(XTask.getTask(new SimpleTaskCommand(1000 * i)));
        }
        ICanceller canceller = engine.setTaskChainCallback(new TaskChainCallbackAdapter() {

            @Override
            public boolean isCallBackOnMainThread() {
                return false;
            }

            @Override
            public void onTaskChainStart(@NonNull ITaskChainEngine engine) {
                Log.e(TAG, "task chain start");
            }

            @Override
            public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                Log.e(TAG, "task chain completed, thread:" + Thread.currentThread().getName());
                engine.destroy();
            }

            @Override
            public void onTaskChainError(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                Log.e(TAG, "task chain error");
            }
        }).start();
        addCanceller(canceller);
    }

    /**
     * 任务链参数传递，上一个任务的参数可传递至下一个任务
     */
    private void doParamTaskChain() {
        final TaskChainEngine engine = XTask.getTaskChain();
        engine.setTaskParam(TaskParam.get("chainName", engine.getName()));
        TaskParam taskParam = TaskParam.get("param1", 100)
                .put("param2", true);
        XTaskStep taskStep = XTask.getTask(new TaskCommand() {
            @Override
            public void run() {
                ITaskParam param = getTaskParam();
                Log.e(TAG, getName() + "  start, param1:" + param.get("param1") + ", chainName:" + param.get("chainName"));
                param.put("param1", 200);
                param.put("param3", "this is param3!");
                notifyTaskSucceed();
            }
        }, taskParam);
        engine.addTask(taskStep)
                .addTask(XTask.getTask(new TaskCommand() {
                    @Override
                    public void run() {
                        ITaskParam param = getTaskParam();
                        Log.e(TAG, getName() + "  start, param1:" + param.get("param1") + ", param3:" + param.get("param3"));
                        param.put("param2", false);
                        notifyTaskSucceed();
                    }
                }));
        ICanceller canceller = engine.setTaskChainCallback(new TaskChainCallbackAdapter() {
            @Override
            public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                Log.e(TAG, "task chain completed, thread:" + Thread.currentThread().getName());
                Map<String, Object> data = result.getDataStore().getData();
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    Log.e(TAG, "key:" + entry.getKey() + ", value:" + entry.getValue());
                }
            }
        }).start();
        addCanceller(canceller);
    }

    /**
     * 任务线程控制
     */
    private void doThreadTypeTaskChain() {
        final TaskChainEngine engine = XTask.getTaskChain();
        ICanceller canceller = engine.addTask(XTask.getTask(new SimpleTaskCommand(1000), ThreadType.ASYNC))
                .addTask(XTask.getTask(new SimpleTaskCommand(1000), ThreadType.ASYNC_EMERGENT))
                .addTask(XTask.getTask(new SimpleTaskCommand(200), ThreadType.MAIN))
                .addTask(XTask.getTask(new SimpleTaskCommand(1000), ThreadType.ASYNC_IO))
                .addTask(XTask.getTask(new SimpleTaskCommand(1000), ThreadType.SYNC))
                .addTask(XTask.getTask(new SimpleTaskCommand(1000), ThreadType.ASYNC_BACKGROUND))
                .setTaskChainCallback(new TaskChainCallbackAdapter() {
                    @Override
                    public void onTaskChainStart(@NonNull ITaskChainEngine engine) {
                        Log.e(TAG, "task chain start, thread:" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                        Log.e(TAG, "task chain completed, thread:" + Thread.currentThread().getName());
                    }
                }).start();
        addCanceller(canceller);
    }

    /**
     * 简单的串行任务组使用
     */
    private void doSerialGroupTaskChain() {
        final TaskChainEngine engine = XTask.getTaskChain();
        SerialGroupTaskStep group1 = XTask.getSerialGroupTask("group1");
        for (int i = 0; i < 5; i++) {
            group1.addTask(XTask.getTask(new SimpleTaskCommand(500)));
        }
        SerialGroupTaskStep group2 = XTask.getSerialGroupTask("group2");
        for (int i = 0; i < 5; i++) {
            group2.addTask(XTask.getTask(new SimpleTaskCommand(1000)));
        }
        ICanceller canceller = engine.addTask(group1)
                .addTask(group2)
                .setTaskChainCallback(new TaskChainCallbackAdapter() {
                    @Override
                    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                        Log.e(TAG, "task chain completed, path:" + result.getPath());
                    }
                })
                .start();
        addCanceller(canceller);
    }

    /**
     * 简单的并行任务组使用
     */
    private void doConcurrentGroupTaskChain() {
        final TaskChainEngine engine = XTask.getTaskChain();
        ConcurrentGroupTaskStep group1 = XTask.getConcurrentGroupTask("group1");
        for (int i = 0; i < 5; i++) {
            group1.addTask(XTask.getTask(new SimpleTaskCommand(100 * (i + 1))));
        }
        ConcurrentGroupTaskStep group2 = XTask.getConcurrentGroupTask("group2");
        for (int i = 0; i < 5; i++) {
            group2.addTask(XTask.getTask(new SimpleTaskCommand(200 * (i + 1))));
        }
        ICanceller canceller = engine.addTask(group1)
                .addTask(group2)
                .setTaskChainCallback(new TaskChainCallbackAdapter() {
                    @Override
                    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                        Log.e(TAG, "task chain completed, path:" + result.getPath());
                    }
                })
                .start();
        addCanceller(canceller);
    }


    /**
     * 任务执行失败，整个链路都停止工作
     */
    private void doTaskError() {
        final TaskChainEngine engine = XTask.getTaskChain();
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            engine.addTask(XTask.getTask(new TaskCommand() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (finalI == 2) {
                        notifyTaskFailed(404, "执行出现异常!");
                    } else {
                        notifyTaskSucceed(TaskResult.succeed());
                    }
                }
            }));
        }
        ICanceller canceller = engine.setTaskChainCallback(new TaskChainCallbackAdapter() {
            @Override
            public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                Log.e(TAG, "task chain completed, thread:" + Thread.currentThread().getName());
            }

            @Override
            public void onTaskChainError(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
                Log.e(TAG, "task chain error, " + result.getDetailMessage());
            }
        }).start();
        addCanceller(canceller);
    }

    private void addCanceller(ICanceller canceller) {
        if (canceller != null) {
            mPool.add(canceller.getName());
        }
    }

    private static class SimpleTaskCommand extends TaskCommand {

        private long mDuring;

        private SimpleTaskCommand(long during) {
            mDuring = during;
        }

        @Override
        public void run() {
            Log.e(TAG, getName() + "  start...thread:" + Thread.currentThread().getName());
            try {
                Thread.sleep(mDuring);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e(TAG, getName() + "  end...");
            // 任何任务无论失败还是成功，都需要调用notifyTaskSucceed或者notifyTaskFailed去通知任务链任务的完成情况
            notifyTaskSucceed(TaskResult.succeed());
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
