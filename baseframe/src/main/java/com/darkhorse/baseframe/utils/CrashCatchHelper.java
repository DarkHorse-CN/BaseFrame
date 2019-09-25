package com.darkhorse.baseframe.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.darkhorse.baseframe.extension.LogExtensionKt.e;

/**
 * 全局捕获异常
 * 当程序发生Uncaught异常的时候,有该类来接管程序,并记录错误日志
 */
@SuppressLint("SimpleDateFormat")
public class CrashCatchHelper implements Thread.UncaughtExceptionHandler {

    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private Context mContext;

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();

    public CrashCatchHelper(Context context, int deleteDay) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        autoClear(deleteDay);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            SystemClock.sleep(3000);
            // 退出程序
            AppManager.restartApp();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null)
            return false;

        try {
            // 使用Toast来显示异常信息
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "很抱歉,程序出现异常,即将重启.",
                            Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
            // 收集设备参数信息
//            collectDeviceInfo(mContext);
            // 保存日志文件
            saveCrashInfoFile(ex);
            SystemClock.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName + "";
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e("an error occured when collect package info" + e.getMessage());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                e("an error occured when collect crash info" + e.getMessage());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     */
    private void saveCrashInfoFile(Throwable ex) throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            String date = TimeUtils.timeInMillisString("yyyy-MM-dd HH:mm:ss");
            sb.append("\r\n").append(date).append("\n");
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key).append("=").append(value).append("\n");
            }

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
        } catch (Exception e) {
            e("an error occured while writing file..." + e.getMessage());
            sb.append("an error occured while writing file...\r\n");
        } finally {
            String time = TimeUtils.timeStamp2DateString(TimeUtils.timeInMillis(), "yyyy-MM-dd");
            String fileName = getGlobalpath() + "crash-" + time + ".log";
            FileUtils.writeFileBottom(sb.toString(), fileName);
        }
    }

    private static String getGlobalpath() {
        return AppManager.getCacheDirPath() + File.separator + "crash" + File.separator;
    }

    /**
     * 文件删除
     */
    private void autoClear(final int autoClearDay) {
        FileUtils.delete(getGlobalpath(), pathname -> {
            String s = FileUtils.getFileNameNoExtension(pathname);
            int day = autoClearDay < 0 ? autoClearDay : -1 * autoClearDay;
            long deleteTime = TimeUtils.timeInMillis() + day * 24 * 60 * 60 * 1000;
            String date = "crash-" + TimeUtils.timeStamp2DateString(deleteTime, "yyyy-MM-dd");
            return date.compareTo(s) >= 0;
        });
    }

}