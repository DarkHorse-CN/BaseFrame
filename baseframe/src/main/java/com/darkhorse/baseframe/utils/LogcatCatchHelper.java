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

    /**
     * 应用进程ID
     */
    private int mPId;

    private LogcatCatchHelper() {
    }

    public void start(int deleteDay, String path) {
        mPId = android.os.Process.myPid();
        mLogcatPath = path;
        File file = new File(mLogcatPath);
        FileUtils.createOrExistsDir(file);
        autoClear(deleteDay);

        if (mLogDumper == null) {
            mLogDumper = new LogDumper(String.valueOf(mPId));
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
        String cmds;
        private String mPID;

        LogDumper(String pid) {
            mPID = pid;
            /**
             *
             * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
             *
             * 显示当前mPID程序的 E和W等级的日志.
             *
             * */

            // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
//            cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";//打印所有日志信息
            cmds = "logcat *:e *:w ";//打印所有日志信息

            // cmds = "logcat -s way";//打印标签过滤信息
            //cmds = "logcat *:e *:i | grep \"(" + mPID + ")\"";
        }

        void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
//                ArrayList<String> getLog = new ArrayList<>();
//                getLog.add("logcat");
//                getLog.add("-w");
//                getLog.add("-e");
//                getLog.add("time");
                ArrayList<String> clearLog = new ArrayList<>();
                clearLog.add("logcat");
                clearLog.add("-c");

//                logcatProc = Runtime.getRuntime().exec(getLog.toArray(new String[getLog.size()]));
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

                    if (!line.contains(" MDS_Log") &&
                            !line.contains(" stunport.cc") &&
                            !line.contains(" port.cc") &&
                            !line.contains(" rtpsender.cc") &&
                            !line.contains(" physicalsocketserver.cc") &&
                            !line.contains(" dtlstransport.cc") &&
                            !line.contains(" common_header.cc") &&
                            !line.contains(" rtcp_receiver.cc") &&
                            !line.contains(" packet_buffer.cc") &&
                            !line.contains(" sender_report.cc") &&
                            !line.contains(" rtp_frame_reference_finder.cc") &&
                            !line.contains(" packet_buffer.cc")
                    ) {
                        String time = TimeUtils.timeStamp2DateString(TimeUtils.timeInMillis(), "yyyy-MM-dd HH");
                        if (line.length() > 30) {
                            String text = line.substring(19, 30);
                            line = line.replace(text, "");
                        }
                        FileUtils.writeFileBottom(line + "\n", mLogcatPath + File.separator + "logcat-" + time + ".log");
                    }
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
