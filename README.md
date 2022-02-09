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
        notifyTaskSucceed();
    }
}, taskParam);
engine.addTask(taskStep)
        .addTask(XTask.getTask(new TaskCommand() {
            @Override
            public void run() {
                // ...执行任务
                notifyTaskSucceed();
            }
        }));
```
【注意】:如果任务执行成功，就调用`notifyTaskSucceed`，任务执行失败，就调用`notifyTaskFailed`。这里任务无论成功还是失败，只要执行完成都需要调用`notifyTaskXXX`通知任务链该任务完成，否则任务将无法正常执行。


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

4.任务链执行.（必须）

调用start执行任务链。

```
ICanceller canceller = engine.start();
```


## 如果觉得项目还不错，可以考虑打赏一波

> 你的打赏是我维护的动力，我将会列出所有打赏人员的清单在下方作为凭证，打赏前请留下打赏项目的备注！

![pay.png](https://ss.im5i.com/2021/06/14/6twG6.png)

## 联系方式

> 更多资讯内容，欢迎扫描关注我的个人微信公众号:【我的Android开源之旅】

![gzh_weixin.jpg](https://ss.im5i.com/2021/06/14/65yoL.jpg)
