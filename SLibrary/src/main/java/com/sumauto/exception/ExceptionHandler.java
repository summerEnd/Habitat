package com.sumauto.exception;

import android.util.Log;

import com.sumauto.common.support.cache.CacheManager;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import static com.sumauto.common.support.cache.CacheManager.ERROR_LOGS;


public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static Thread.UncaughtExceptionHandler defaultHandler;

    public static class ErrorLog implements Serializable{
        public String msg;
        public String time;
    }

    public static void init() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LinkedList<ErrorLog> logs = (LinkedList<ErrorLog>) CacheManager.getInstance().read(ERROR_LOGS);
        if (logs == null){
            logs = new LinkedList<ErrorLog>();
        }

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E", Locale.CHINA);

        StringWriter sw=new StringWriter();
        PrintWriter pw=new PrintWriter(sw);

        ex.printStackTrace(pw);
        ErrorLog log=new ErrorLog();
        log.msg=sw.toString();
        log.time=format.format(new Date());
        logs.add(0,log);

        CacheManager.getInstance().write(ERROR_LOGS, logs);
        defaultHandler.uncaughtException(thread, ex);
        Log.e("error","uncaughtException",ex);
    }
}
