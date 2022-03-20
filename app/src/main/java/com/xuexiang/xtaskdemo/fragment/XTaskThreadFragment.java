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
import com.xuexiang.xtask.XTask;
import com.xuexiang.xtask.thread.pool.cancel.ICancelable;
import com.xuexiang.xtask.thread.utils.CancelUtils;
import com.xuexiang.xtaskdemo.core.BaseSimpleListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 使用XTask进行线程池操作
 *
 * @author xuexiang
 * @since 3/20/22 2:22 AM
 */
@Page(name = "XTask线程池使用\nXTask拥有丰富的线程调度方法")
public class XTaskThreadFragment extends BaseSimpleListFragment {

    private static final String TAG = "XTaskThreadFragment";

    private List<ICancelable> mCancelableList = new ArrayList<>();

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("普通异步执行");
        lists.add("紧急异步执行");
        lists.add("后台异步执行");
        lists.add("优先级异步执行");
        lists.add("主线程延迟执行");
        lists.add("子线程延迟执行");
        lists.add("周期执行(固定间期)");
        lists.add("周期执行(固定延期)");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                doNormalAsync();
                break;
            case 1:
                doEmergentAsync();
                break;
            case 2:
                doBackgroundAsync();
                break;
            case 3:
                doPriorityAsync();
                break;
            case 4:
                doDelayOnMainThread();
                break;
            case 5:
                doDelay();
                break;
            case 6:
                doPollingFixedRate();
                break;
            case 7:
                doPollingFixedDelay();
                break;
            default:
                break;
        }
    }

    private void doNormalAsync() {
        Log.e(TAG, "doNormalAsync start...");
        ICancelable cancelable = XTask.submit(() -> {
            Log.e(TAG, "Async task start, thread:" + Thread.currentThread().getName());
            mockProcess(2000);
        });
        mCancelableList.add(cancelable);
    }

    private void doEmergentAsync() {
        Log.e(TAG, "doEmergentAsync start...");
        ICancelable cancelable = XTask.emergentSubmit(() -> {
            Log.e(TAG, "Emergent task start, thread:" + Thread.currentThread().getName());
            mockProcess(2000);
        });
        mCancelableList.add(cancelable);
    }

    private void doBackgroundAsync() {
        Log.e(TAG, "doBackgroundAsync start...");
        ICancelable cancelable = XTask.backgroundSubmit(() -> {
            Log.e(TAG, "Background task start, thread:" + Thread.currentThread().getName());
            mockProcess(2000);
        });
        mCancelableList.add(cancelable);
    }

    private void doPriorityAsync() {
        Log.e(TAG, "doPriorityAsync start...");
        ICancelable cancelable = XTask.submit(() -> {
            Log.e(TAG, "Priority task start, thread:" + Thread.currentThread().getName());
            mockProcess(2000);
        }, 10);
        mCancelableList.add(cancelable);
    }

    private void doDelayOnMainThread() {
        Log.e(TAG, "doDelayOnMainThread start...");
        ICancelable cancelable = XTask.postToMainDelay(() -> {
            Log.e(TAG, "Delay task start, thread:" + Thread.currentThread().getName());
        }, 2000);
        mCancelableList.add(cancelable);
    }

    private void doDelay() {
        Log.e(TAG, "doDelay start...");
        ICancelable cancelable = XTask.schedule(() -> {
            Log.e(TAG, "Delay task start, thread:" + Thread.currentThread().getName());
            mockProcess(2000);
        }, 2, TimeUnit.SECONDS);
        mCancelableList.add(cancelable);
    }

    private void doPollingFixedRate() {
        Log.e(TAG, "doPollingFixedRate start...");
        ICancelable cancelable = XTask.scheduleAtFixedRate(() -> {
            Log.e(TAG, "FixedRate task start, thread:" + Thread.currentThread().getName());
            mockProcess(2000);
        }, 0, 2, TimeUnit.SECONDS);
        mCancelableList.add(cancelable);
    }

    private void doPollingFixedDelay() {
        Log.e(TAG, "doPollingFixedDelay start...");
        ICancelable cancelable = XTask.scheduleWithFixedDelay(() -> {
            Log.e(TAG, "FixedDelay task start, thread:" + Thread.currentThread().getName());
            mockProcess(2000);
        }, 0, 2, TimeUnit.SECONDS);
        mCancelableList.add(cancelable);
    }

    /**
     * 模拟执行
     *
     * @param time 模拟执行所需要的时间
     */
    public void mockProcess(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        CancelUtils.cancel(mCancelableList);
        super.onDestroyView();
    }
}
