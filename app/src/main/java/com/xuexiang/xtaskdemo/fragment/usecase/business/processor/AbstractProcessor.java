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

package com.xuexiang.xtaskdemo.fragment.usecase.business.processor;

import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 抽象处理器
 *
 * @author xuexiang
 * @since 2/25/22 12:28 AM
 */
public abstract class AbstractProcessor<T> {

    private LoggerTextView mLogger;

    private IProcessorCallback<T> mCallback;


    public AbstractProcessor(LoggerTextView logger) {
        mLogger = logger;
    }

    public AbstractProcessor setProcessorCallback(IProcessorCallback<T> callback) {
        mCallback = callback;
        return this;
    }

    /**
     * 处理任务
     *
     * @return 返回的结果
     */
    public abstract T process();

    public void log(String logContent) {
        if (mLogger != null) {
            mLogger.logNormal(logContent);
        }
    }

    public void onProcessSuccess(T t) {
        if (mCallback != null) {
            mCallback.onSuccess(t);
        }
    }

    public void onProcessFailed(String error) {
        if (mCallback != null) {
            mCallback.onFailed(error);
        }
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

    public abstract static class ProcessorCallbackAdapter<T> implements IProcessorCallback<T> {
        @Override
        public void onFailed(String error) {

        }
    }

    public interface IProcessorCallback<T> {
        /**
         * 处理成功
         *
         * @param result 结果
         */
        void onSuccess(T result);

        /**
         * 处理失败
         *
         * @param error 错误信息
         */
        void onFailed(String error);
    }

}
