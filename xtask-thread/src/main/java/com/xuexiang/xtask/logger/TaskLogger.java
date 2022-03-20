/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xtask.logger;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * TaskLogger日志记录
 *
 * @author xuexiang
 * @since 2021/10/9 4:31 PM
 */
public final class TaskLogger {

    private TaskLogger() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    //==============常量================//
    /**
     * 默认tag
     */
    public final static String TASK_LOG_TAG = "TaskLogger";
    /**
     * 最大日志优先级【日志优先级为最大等级，所有日志都不打印】
     */
    private final static int MAX_LOG_PRIORITY = 10;
    /**
     * 最小日志优先级【日志优先级为最小等级，所有日志都打印】
     */
    private final static int MIN_LOG_PRIORITY = 0;

    //==============属性================//
    /**
     * 默认的日志记录为Logcat
     */
    private static ILogger sILogger = new LogcatLogger();

    private static String sTag = TASK_LOG_TAG;
    /**
     * 是否是调试模式
     */
    private static boolean sIsDebug = false;
    /**
     * 日志打印优先级
     */
    private static int sLogPriority = MAX_LOG_PRIORITY;

    /**
     * 是否打印任务执行所在的线程名
     */
    private static boolean sIsLogThreadName = false;

    //==============属性设置================//

    /**
     * 获取类的打印日志TAG
     *
     * @param clazz 类
     * @return 打印日志TAG
     */
    public static String getLogTag(@NonNull Class<?> clazz) {
        return sTag + "_" + clazz.getSimpleName();
    }

    /**
     * 获取类的打印日志TAG
     *
     * @param className 类名
     * @return 打印日志TAG
     */
    public static String getLogTag(@NonNull String className) {
        return sTag + "_" + className;
    }

    /**
     * 设置日志记录者的接口
     *
     * @param logger 日志记录接口
     */
    public static void setLogger(@NonNull ILogger logger) {
        TaskLogger.sILogger = logger;
    }

    /**
     * 设置日志的tag
     *
     * @param tag 日志的tag
     */
    public static void setTag(String tag) {
        TaskLogger.sTag = tag;
    }

    /**
     * 设置是否是调试模式
     *
     * @param isDebug 是否是调试模式
     */
    public static void setDebug(boolean isDebug) {
        TaskLogger.sIsDebug = isDebug;
    }

    /**
     * 设置打印日志的等级（只打印改等级以上的日志）
     *
     * @param priority 日志的等级
     */
    public static void setPriority(int priority) {
        TaskLogger.sLogPriority = priority;
    }

    /**
     * 设置是否打印任务执行所在的线程名
     *
     * @param isLogThreadName 是否打印任务执行所在的线程名
     */
    public static void setIsLogThreadName(boolean isLogThreadName) {
        TaskLogger.sIsLogThreadName = isLogThreadName;
    }

    /**
     * 是否打印任务执行所在的线程名
     *
     * @return 是否打印任务执行所在的线程名
     */
    public static boolean isLogThreadName() {
        return sIsLogThreadName;
    }

    //===================对外接口=======================//

    /**
     * 设置是否打开调试
     *
     * @param isDebug 是否是调试模式
     */
    public static void debug(boolean isDebug) {
        debug(isDebug ? TASK_LOG_TAG : "");
    }

    /**
     * 设置调试模式
     *
     * @param tag tag,如果不为空是调试模式
     */
    public static void debug(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            setDebug(true);
            setPriority(MIN_LOG_PRIORITY);
            setTag(tag);
        } else {
            setDebug(false);
            setPriority(MAX_LOG_PRIORITY);
            setTag("");
        }
    }

    //=============打印方法===============//

    /**
     * 打印任何（所有）信息
     *
     * @param msg 日志信息
     */
    public static void v(String msg) {
        if (enableLog(Log.VERBOSE)) {
            sILogger.log(Log.VERBOSE, sTag, msg, null);
        }
    }

    /**
     * 打印任何（所有）信息
     *
     * @param tag tag信息
     * @param msg 日志信息
     */
    public static void vTag(String tag, String msg) {
        if (enableLog(Log.VERBOSE)) {
            sILogger.log(Log.VERBOSE, tag, msg, null);
        }
    }

    /**
     * 打印调试信息
     *
     * @param msg 调试信息
     */
    public static void d(String msg) {
        if (enableLog(Log.DEBUG)) {
            sILogger.log(Log.DEBUG, sTag, msg, null);
        }
    }

    /**
     * 打印调试信息
     *
     * @param tag tag信息
     * @param msg 调试信息
     */
    public static void dTag(String tag, String msg) {
        if (enableLog(Log.DEBUG)) {
            sILogger.log(Log.DEBUG, tag, msg, null);
        }
    }

    /**
     * 打印提示性的信息
     *
     * @param msg 提示性的信息
     */
    public static void i(String msg) {
        if (enableLog(Log.INFO)) {
            sILogger.log(Log.INFO, sTag, msg, null);
        }
    }

    /**
     * 打印提示性的信息
     *
     * @param tag tag信息
     * @param msg 提示性的信息
     */
    public static void iTag(String tag, String msg) {
        if (enableLog(Log.INFO)) {
            sILogger.log(Log.INFO, tag, msg, null);
        }
    }

    /**
     * 打印warning警告信息
     *
     * @param msg 警告信息
     */
    public static void w(String msg) {
        if (enableLog(Log.WARN)) {
            sILogger.log(Log.WARN, sTag, msg, null);
        }
    }

    /**
     * 打印warning警告信息
     *
     * @param tag tag信息
     * @param msg 警告信息
     */
    public static void wTag(String tag, String msg) {
        if (enableLog(Log.WARN)) {
            sILogger.log(Log.WARN, tag, msg, null);
        }
    }

    /**
     * 打印出错信息
     *
     * @param msg 出错信息
     */
    public static void e(String msg) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, sTag, msg, null);
        }
    }

    /**
     * 打印出错信息
     *
     * @param tag tag信息
     * @param msg 出错信息
     */
    public static void eTag(String tag, String msg) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, tag, msg, null);
        }
    }

    /**
     * 打印出错堆栈信息
     *
     * @param t 出错堆栈信息
     */
    public static void e(Throwable t) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, sTag, null, t);
        }
    }

    /**
     * 打印出错堆栈信息
     *
     * @param tag tag信息
     * @param t   出错堆栈信息
     */
    public static void eTag(String tag, Throwable t) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, tag, null, t);
        }
    }


    /**
     * 打印出错堆栈信息
     *
     * @param msg 出错信息
     * @param t   出错堆栈信息
     */
    public static void e(String msg, Throwable t) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, sTag, msg, t);
        }
    }

    /**
     * 打印出错堆栈信息
     *
     * @param tag tag信息
     * @param msg 出错堆栈信息
     * @param t   出错堆栈信息
     */
    public static void eTag(String tag, String msg, Throwable t) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, tag, msg, t);
        }
    }

    /**
     * 打印严重的错误信息
     *
     * @param msg 严重的错误信息
     */
    public static void wtf(String msg) {
        if (enableLog(Log.ASSERT)) {
            sILogger.log(Log.ASSERT, sTag, msg, null);
        }
    }

    /**
     * 打印严重的错误信息
     *
     * @param tag tag信息
     * @param msg 严重的错误信息
     */
    public static void wtfTag(String tag, String msg) {
        if (enableLog(Log.ASSERT)) {
            sILogger.log(Log.ASSERT, tag, msg, null);
        }
    }

    /**
     * 打印日志
     *
     * @param tag tag信息
     * @param msg 日志信息
     */
    public static void log(int priority, String tag, String msg) {
        if (enableLog(priority)) {
            sILogger.log(priority, tag, msg, null);
        }
    }

    /**
     * 能否打印
     *
     * @param logPriority 日志等级
     * @return 能否打印日志
     */
    private static boolean enableLog(int logPriority) {
        return isDebug() && logPriority >= sLogPriority;
    }

    /**
     * 当前是否是调试模式
     *
     * @return 是否是调试模式
     */
    public static boolean isDebug() {
        return sILogger != null && sIsDebug;
    }
}
