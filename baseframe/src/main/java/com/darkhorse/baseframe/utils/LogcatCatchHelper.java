package com.darkhorse.baseframe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LogcatCatchHelper {

    //日志保存路径
    private static String mLogcatPath;
    private LogDumper mLogDumper = null;

    private LogcatCatchHelper() {
    }

    public void start(int deleteDay, String path, String cmds) {
        mLogcatPath = path;
        File file = new File(mLogcatPath);
        FileUtils.createOrExistsDir(file);
        autoClear(deleteDay);

        if (mLogDumper == null) {
            mLogDumper = new LogDumper(cmds);
        }
        mLogDumper.start();
    }

    public void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }

    private class LogDumper extends Thread {

        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        private String cmds;
        private String mPID;

        LogDumper( String cmds) {
            this.cmds = cmds+"";
        }

        void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                ArrayList<String> clearLog = new ArrayList<>();
                clearLog.add("logcat");
                clearLog.add("-c");

                logcatProc = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
                Runtime.getRuntime().exec(clearLog.toArray(new String[clearLog.size()]));

                String line;
                while (mRunning && (line = mReader.readLine()) != null) {
                    if (!mRunning) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }

                    String time = TimeUtils.timeStamp2DateString(TimeUtils.timeInMillis(), "yyyy-MM-dd");
                    FileUtils.writeFileBottom(line + "\n", mLogcatPath + File.separator + "logcat-" + time + ".log");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (logcatProc != null) {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 文件删除
     */
    private void autoClear(final int autoClearDay) {
        FileUtils.delete(mLogcatPath, pathname -> {
            String s = FileUtils.getFileNameNoExtension(pathname);
            int day = autoClearDay < 0 ? autoClearDay : -1 * autoClearDay;
            long deleteTime = TimeUtils.timeInMillis() + day * 24 * 60 * 60 * 1000;
            String date = "logcat-" + TimeUtils.timeStamp2DateString(deleteTime, "yyyy-MM-dd HH");
            return date.compareTo(s) >= 0;
        });

    }

    public static LogcatCatchHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static LogcatCatchHelper INSTANCE = new LogcatCatchHelper();
    }
}
