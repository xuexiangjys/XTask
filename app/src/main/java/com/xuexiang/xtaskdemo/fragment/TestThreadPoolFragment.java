/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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
import com.xuexiang.xtask.thread.priority.IPriority;
import com.xuexiang.xtask.thread.pool.PriorityThreadPoolExecutor;
import com.xuexiang.xtaskdemo.core.BaseSimpleListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池测试页面
 *
 * @author xuexiang
 * @since 2019-07-08 00:52
 */
@Page(name = "线程池测试页面")
public class TestThreadPoolFragment extends BaseSimpleListFragment {

    private static final String TAG = "TestFragment";

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("优先级线程池测试Runnable的优先级执行");
        lists.add("普通线程池测试Runnable的执行");
        lists.add("测试线程池利用率");
        lists.add("线程优先级");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        final Runnable r1 = getRunnable("r1");
        final Runnable r2 = getRunnable("r2");
        final Runnable r3 = getRunnable("r3");
        final Runnable r4 = getRunnable("r4");
        final Runnable r5 = getRunnable("r5");

        switch (position) {
            case 0:
                PriorityThreadPoolExecutor executor = PriorityThreadPoolExecutor.newBuilder(1).build();
                // 优先级大的优先执行
                executor.execute(r5, 1);
                Log.d(TAG, "==== add r5 ====");
                executor.execute(r4, 2);
                Log.d(TAG, "==== add r4 ====");
                executor.execute(r3, 3);
                Log.d(TAG, "==== add r3 ====");
                executor.execute(r2, 4);
                Log.d(TAG, "==== add r2 ====");
                executor.execute(r1, 5);
                Log.d(TAG, "==== add r1 ====");
                break;
            case 1:
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(r5);
                Log.d(TAG, "==== add r5 ====");
                executorService.execute(r4);
                Log.d(TAG, "==== add r4 ====");
                executorService.execute(r3);
                Log.d(TAG, "==== add r3 ====");
                executorService.execute(r2);
                Log.d(TAG, "==== add r2 ====");
                executorService.execute(r1);
                Log.d(TAG, "==== add r1 ====");
                break;
            case 2:
                Log.d(TAG, "cpu count:" + Runtime.getRuntime().availableProcessors());
                int length = 30;
                ExecutorService fixedThreadPool = new ThreadPoolExecutor(5, 20,
                        30L, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(10));
                List<Runnable> list = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    list.add(getRunnable("r" + i));
                }
                for (int i = 0; i < list.size(); i++) {
                    fixedThreadPool.execute(list.get(i));
                }
                break;
            case 3:
                Thread thread1 = new Thread(() -> {
                    Log.e(TAG, "MIN_PRIORITY start...");
                });
                thread1.setPriority(Thread.MIN_PRIORITY);

                Thread thread2 = new Thread(() -> {
                    Log.e(TAG, "NORM_PRIORITY start...");
                });
                thread2.setPriority(Thread.NORM_PRIORITY);

                Thread thread3 = new Thread(() -> {
                    Log.e(TAG, "MAX_PRIORITY start...");
                });
                thread3.setPriority(Thread.MAX_PRIORITY);

                thread1.start();
                thread2.start();
                thread3.start();

                break;
            default:
                break;
        }
    }

    private static Runnable getRunnable(final String name) {

        return new Runnable() {
            @Override
            public void run() {
                Integer priority = getPriority(this);
                if (priority != null) {
                    Log.d(TAG, "==[execute start]== " + name + ", priority=" + priority);
                } else {
                    Log.d(TAG, "==[execute start]== " + name);
                }
                // 模拟复杂处理任务
                // Simulate processing tasks
                int a = 0;
                for (int i = 0; i < 100000000; i++) {
                    a += 1;
                }
                if (priority != null) {
                    Log.d(TAG, "execute end " + name + ", priority=" + priority);
                } else {
                    Log.d(TAG, "execute end " + name);
                }

            }
        };
    }

    private static Integer getPriority(Object o) {
        if (o instanceof IPriority) {
            return ((IPriority) o).priority();
        }
        return null;
    }
}
