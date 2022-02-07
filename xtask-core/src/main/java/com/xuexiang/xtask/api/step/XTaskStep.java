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

package com.xuexiang.xtask.api.step;

import androidx.annotation.NonNull;

import com.xuexiang.xtask.core.ThreadType;
import com.xuexiang.xtask.core.param.ITaskParam;
import com.xuexiang.xtask.core.param.impl.TaskParam;
import com.xuexiang.xtask.core.step.ITaskStepHandler;
import com.xuexiang.xtask.core.step.impl.AbstractTaskStep;
import com.xuexiang.xtask.core.step.impl.TaskCommand;
import com.xuexiang.xtask.utils.CommonUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简化任务步骤的使用
 *
 * @author xuexiang
 * @since 1/30/22 5:08 PM
 */
public class XTaskStep extends AbstractTaskStep {
    /**
     * 任务的编号【静态全局】
     */
    private static final AtomicInteger TASK_NUMBER = new AtomicInteger(1);

    /**
     * 获取简化任务的构建者
     *
     * @param command 任务执行内容
     * @return 简化任务的构建者
     */
    public static XTaskStep getTask(@NonNull TaskCommand command) {
        return new Builder(command).build();
    }

    /**
     * 获取简化任务的构建者
     *
     * @param command    任务执行内容
     * @param threadType 线程类型
     * @return 简化任务的构建者
     */
    public static XTaskStep getTask(@NonNull TaskCommand command, ThreadType threadType) {
        return new Builder(command)
                .setThreadType(threadType)
                .build();
    }

    /**
     * 获取简化任务的构建者
     *
     * @param command   任务执行内容
     * @param taskParam 任务参数
     * @return 简化任务的构建者
     */
    public static XTaskStep getTask(@NonNull TaskCommand command, @NonNull ITaskParam taskParam) {
        return new Builder(command)
                .setTaskParam(taskParam)
                .build();
    }

    /**
     * 获取简化任务的构建者
     *
     * @param command    任务执行内容
     * @param threadType 线程类型
     * @param taskParam  任务参数
     * @return 简化任务的构建者
     */
    public static XTaskStep getTask(@NonNull TaskCommand command, ThreadType threadType, @NonNull ITaskParam taskParam) {
        return new Builder(command)
                .setThreadType(threadType)
                .setTaskParam(taskParam)
                .build();
    }

    /**
     * 获取简化任务的构建者
     *
     * @param command 任务执行内容
     * @return 简化任务的构建者
     */
    public static Builder newBuilder(@NonNull TaskCommand command) {
        return new Builder(command);
    }

    /**
     * 任务名称
     */
    private String mName;

    /**
     * 任务执行内容
     */
    private TaskCommand mTaskCommand;

    /**
     * 构造方法
     *
     * @param name        任务步骤名称
     * @param command     任务执行内容
     * @param threadType  线程类型
     * @param taskParam   任务参数
     * @param taskHandler 任务处理者
     */
    private XTaskStep(@NonNull String name, @NonNull TaskCommand command, ThreadType threadType, @NonNull ITaskParam taskParam, ITaskStepHandler taskHandler) {
        super(threadType, taskParam, taskHandler);
        mName = name;
        mTaskCommand = command;
        mTaskCommand.setTaskStepResultController(this);
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void doTask() throws Exception {
        mTaskCommand.run();
    }

    /**
     * 简化任务构建者
     *
     * @author xuexiang
     * @since 1/30/22 5:22 PM
     */
    public static final class Builder {
        /**
         * 任务步骤名称
         */
        String name;
        /**
         * 任务执行内容
         */
        TaskCommand command;
        /**
         * 线程执行类型
         */
        ThreadType threadType = ThreadType.ASYNC;
        /**
         * 任务参数
         */
        ITaskParam taskParam;
        /**
         * 任务处理者
         */
        ITaskStepHandler taskHandler;

        /**
         * 构造方法
         *
         * @param command 执行内容
         */
        private Builder(@NonNull TaskCommand command) {
            this.command = command;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCommand(TaskCommand command) {
            this.command = command;
            return this;
        }

        public Builder setThreadType(ThreadType threadType) {
            this.threadType = threadType;
            return this;
        }

        public Builder setTaskParam(ITaskParam taskParam) {
            this.taskParam = taskParam;
            return this;
        }

        public Builder setTaskHandler(ITaskStepHandler taskHandler) {
            this.taskHandler = taskHandler;
            return this;
        }

        public XTaskStep build() {
            CommonUtils.requireNonNull(this.command, "XTaskStep.Builder command can not be null!");

            if (CommonUtils.isEmpty(name)) {
                name = "XTaskStep-" + TASK_NUMBER.getAndIncrement();
            }
            if (taskParam == null) {
                taskParam = new TaskParam();
            }
            return new XTaskStep(name, command, threadType, taskParam, taskHandler);
        }
    }


}
