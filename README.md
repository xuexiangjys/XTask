# XTask
[![](https://jitpack.io/v/xuexiangjys/XTask.svg)](https://jitpack.io/#xuexiangjys/XTask)
[![api](https://img.shields.io/badge/API-14+-brightgreen.svg)](https://android-arsenal.com/api?level=14)
[![I](https://img.shields.io/github/issues/xuexiangjys/XTask.svg)](https://github.com/xuexiangjys/XTask/issues)
[![Star](https://img.shields.io/github/stars/xuexiangjys/XTask.svg)](https://github.com/xuexiangjys/XTask)

一个拓展性极强的任务执行框架。可自由定义和组合任务来实现你想要的功能，尤其适用于处理复杂的业务流程，可灵活添加前置任务或者调整执行顺序。例如：应用的启动初始化流程。

## 关于我

| 公众号   | 掘金     |  知乎    |  CSDN   |   简书   |   思否  |   哔哩哔哩  |   今日头条
|---------|---------|--------- |---------|---------|---------|---------|---------|
| [我的Android开源之旅](https://ss.im5i.com/2021/06/14/6tqAU.png)  |  [点我](https://juejin.im/user/598feef55188257d592e56ed/posts)    |   [点我](https://www.zhihu.com/people/xuexiangjys/posts)       |   [点我](https://xuexiangjys.blog.csdn.net/)  |   [点我](https://www.jianshu.com/u/6bf605575337)  |   [点我](https://segmentfault.com/u/xuexiangjys)  |   [点我](https://space.bilibili.com/483850585)  |   [点我](https://img.rruu.net/image/5ff34ff7b02dd)

## X系列库快速集成

为了方便大家快速集成X系列框架库，我提供了一个空壳模版供大家参考使用: https://github.com/xuexiangjys/TemplateAppProject

除此之外，我还特别制作了几期[X系列视频教程](https://space.bilibili.com/483850585/channel/detail?cid=104998)供大家学习参考.

---

## 特征

* 支持6种线程类型方式执行任务。
* 支持任务链中各任务的执行线程调度和控制。
* 支持快捷任务创建，同时支持自定义任务。
* 支持串行和并行等组任务。
* 支持任务间数据共享。
* 支持自由组合任务执行。
* 支持任务链执行取消。
* 支持取消所有任务链和指定名称的任务链。
* 支持任务链调用顺序记录和查询。
* 支持自定义任务执行的线程池。

## 设计思想

框架主体使用责任链的设计模式，辅以建造者模式、工厂模式、适配器模式、组合模式、外观模式以及代理模式来实现。

### 组成结构

* 任务链`ITaskChainEngine`：任务链执行引擎，负责统筹调度各任务步骤。

* 任务步骤`ITaskStep`：负责具体任务逻辑处理。

* 数据存储仓库`IDataStore`：存放数据的仓库，主要用于保存任务参数中的数据。

* 任务参数`ITaskParam`：负责任务路径记录以及任务产生的参数管理。

* 任务执行结果`ITaskResult`：存放任务最终执行的结果以及产生的数据。

* 任务组`IGroupTaskStep`：负责统筹调度各子任务步骤。

[点击查看框架UML设计图](https://github.com/xuexiangjys/XTask/blob/master/art/xtask_uml.png)

## 日志一览

![task_log.png](https://s4.ax1x.com/2022/02/16/HWdTHJ.png)

![task_log2.png](https://s4.ax1x.com/2022/02/16/HWwnbQ.png)

---

## 集成指南

### 添加Gradle依赖

1.先在项目根目录的 `build.gradle` 的 `repositories` 添加:

```
allprojects {
     repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.然后在dependencies添加:

```
dependencies {
  ...
  // XTask
  implementation 'com.github.xuexiangjys:XTask:xtask-core:0.0.1'
}
```

## 使用方法

> [XTask](https://github.com/xuexiangjys/XTask/blob/master/xtask-core/src/main/java/com/xuexiang/xtask/XTask.java)作为对外统一的API入口，所有常用的方法都能从中找到。

### 打开调试模式

当需要定位问题，需要进行调试时，可打开调试模式，这样便可开启框架的日志。

```
XTask.debug(true);
```

### XTask的API介绍

方法名	| 描述
|---|---
debug | 设置是否打开调试
setLogger | 自定义日志打印
setIsLogTaskRunThreadName | 设置是否打印任务执行所在的线程名，默认false
getTaskChain | 获取任务链执行引擎
getTask | 获取简化的任务
getTaskBuilder | 获取简化任务的构建者
getConcurrentGroupTask | 获取并行任务组
getSerialGroupTask | 获取串行任务组
cancelTaskChain | 取消指定任务链执行
postToMain | 执行任务到主线程
submit | 执行普通异步任务
emergentSubmit | 执行紧急异步任务
backgroundSubmit | 执行后台异步任务
ioSubmit | 执行io耗时的异步任务
groupSubmit | 执行分组异步任务

### 如何执行一条任务链

下面是一整个完整的例子：

```
// 1.创建一条任务链（必须）
final TaskChainEngine engine = XTask.getTaskChain();
// 2.设置任务链的初始化参数（可选）
engine.setTaskParam(TaskParam.get("chainName", engine.getName()));
TaskParam taskParam = TaskParam.get("param1", 100)
        .put("param2", true);
// 3.创建多个任务，并向任务链中添加（必须）
XTaskStep taskStep = XTask.getTask(new TaskCommand() {
    @Override
    public void run() {
        ITaskParam param = getTaskParam();
        Log.e(TAG, getName() + "  start, param1:" + param.get("param1") + ", chainName:" + param.get("chainName"));
        param.put("param1", 200);
        param.put("param3", "this is param3!");
    }
}, taskParam);
engine.addTask(taskStep)
        .addTask(XTask.getTask(new TaskCommand() {
            @Override
            public void run() {
                ITaskParam param = getTaskParam();
                Log.e(TAG, getName() + "  start, param1:" + param.get("param1") + ", param3:" + param.get("param3"));
                param.put("param2", false);
            }
        }));
// 4.设置任务链执行回调（可选）
ICanceller canceller = engine.setTaskChainCallback(new TaskChainCallbackAdapter() {
    @Override
    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
        Log.e(TAG, "task chain completed, thread:" + Thread.currentThread().getName());
        Map<String, Object> data = result.getDataStore().getData();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Log.e(TAG, "key:" + entry.getKey() + ", value:" + entry.getValue());
        }
    }
// 5.任务链执行（必须）
}).start();
```

1.创建一条任务链.（必须）

```
TaskChainEngine engine = XTask.getTaskChain();
```

2.设置任务链的初始化参数.（可选）

```
engine.setTaskParam(TaskParam.get("chainName", engine.getName()));
```

3.创建多个任务，并向任务链中添加.（必须）

```
// 设置任务初始化参数
TaskParam taskParam = TaskParam.get("param1", 100)
        .put("param2", true);
XTaskStep taskStep = XTask.getTask(new TaskCommand() {
    @Override
    public void run() {
        // ...执行任务
    }
}, taskParam);
engine.addTask(taskStep)
        .addTask(XTask.getTask(new TaskCommand() {
            @Override
            public void run() {
                // ...执行任务
            }
        }));
```
【注意】:如果任务执行成功，就调用`notifyTaskSucceed`，任务执行失败，就调用`notifyTaskFailed`。这里任务无论成功还是失败，只要执行完成都需要调用`notifyTaskXXX`通知任务链该任务完成，否则任务将无法正常执行。
【注意】:`TaskCommand`和`SimpleTaskStep`默认提供了自动通知执行结果的功能，但是AbstractTaskStep没有提供，需要手动通知。

4.设置任务链执行回调.（可选）

调用setTaskChainCallback设置任务链执行回调。

```
engine.setTaskChainCallback(new TaskChainCallbackAdapter() {

    @Override
    public boolean isCallBackOnMainThread() {
        // 回调是否返回主线程, 默认是true
        return false;
    }
    @Override
    public void onTaskChainStart(@NonNull ITaskChainEngine engine) {
        Log.e(TAG, "task chain start");
    }
    @Override
    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
        Log.e(TAG, "task chain completed, thread:" + Thread.currentThread().getName());
    }
    @Override
    public void onTaskChainError(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {
        Log.e(TAG, "task chain error");
    }
})
```

5.任务链执行.（必须）

调用start执行任务链。

```
ICanceller canceller = engine.start();
```

## 任务创建

创建任务有两种方式：

* 通过XTask.getTask构建
* 继承AbstractTaskStep实现任务的自定义

### 通过XTask创建

> 通过XTask.getTask, 传入对应的属性进行构建

属性名	| 描述
|---|---
name | 任务步骤名称
command | 任务执行内容
threadType | 线程执行类型
taskParam | 任务参数
taskHandler | 任务处理者

```
XTaskStep taskStep = XTask.getTask(new TaskCommand() {
    @Override
    public void run() {
        // todo
    }
}, ThreadType.ASYNC, taskParam);
```

### 通过继承创建

> 通过继承AbstractTaskStep或者SimpleTaskStep实现具体功能。

```
public class StepATask extends AbstractTaskStep {

    @Override
    public void doTask() throws Exception {
        // todo
        // 通知任务链任务完成
        notifyTaskSucceed(TaskResult.succeed());
    }

    @Override
    public String getName() {
        return "StepATask";
    }
}

```

## 任务执行原则

每一个任务都是依托于任务链进行流程控制。任何任务都需要遵循以下原则：

* 任何任务无论失败还是成功，都需要调用`notifyTaskSucceed`或者`notifyTaskFailed`去通知任务链任务的完成情况。`TaskCommand`和`SimpleTaskStep`默认提供了自动通知执行结果的功能。
* 一旦任务链中某个任务执行失败，整个链路都停止工作。

```
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
                notifyTaskFailed(404, "任务执行失败!");
            } else {
                notifyTaskSucceed(TaskResult.succeed());
            }
        }
    }, false));
}
engine.start();
```

## 参数传递

* 任何TaskStep我们都可以通过`getTaskParam`获取任务参数和任务执行结果`ITaskParam`。
* 上一个TaskStep保存处理过的任务参数会自动带入到下一个TaskStep中去，因此最后一个TaskStep拥有之前所有任务的参数数据。

```
XTask.getTask(new TaskCommand() {
    @Override
    public void run() {
        ITaskParam param = getTaskParam();
        Log.e(TAG, getName() + "  start, param1:" + param.get("param1") + ", param3:" + param.get("param3"));
        param.put("param2", false);
    }
})
```

## 线程控制

设置任务的threadType类型，即可完成对任务运行线程的控制。目前支持6种线程处理方式。

类型	| 描述
|---|---
MAIN | 主线程（UI线程）
ASYNC | 异步线程（开子线程，普通线程池）
ASYNC_IO | 异步线程（开子线程，io线程池）
ASYNC_EMERGENT | 异步线程（开子线程，紧急线程池）
ASYNC_BACKGROUND | 异步线程（开子线程，优先级较低线程池）
SYNC | 同步线程（直接执行）

```
// 1.构造时传入线程
XTaskStep taskStep = XTask.getTask(new SimpleTaskCommand(1000), ThreadType.ASYNC_EMERGENT);
// 2.设置线程的方法
taskStep.setThreadType(ThreadType.ASYNC_IO);
```

## 任务组

目前共有串行任务组（SerialGroupTaskStep）和并行任务组（ConcurrentGroupTaskStep）

### 串行任务组

串行任务组是按顺序依次执行，和任务链的处理方式类似。使用XTask.getSerialGroupTask获取。

```
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

```

### 并行任务组

并行任务组是组内所有任务同时执行，待所有任务都完成后才视为任务组完成。使用XTask.getConcurrentGroupTask获取。

```
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

```

---

## 如果觉得项目还不错，可以考虑打赏一波

> 你的打赏是我维护的动力，我将会列出所有打赏人员的清单在下方作为凭证，打赏前请留下打赏项目的备注！

![pay.png](https://ss.im5i.com/2021/06/14/6twG6.png)

## 联系方式

> 更多资讯内容，欢迎扫描关注我的个人微信公众号:【我的Android开源之旅】

![gzh_weixin.jpg](https://ss.im5i.com/2021/06/14/65yoL.jpg)
