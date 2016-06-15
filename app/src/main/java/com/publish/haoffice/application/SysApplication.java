package com.publish.haoffice.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.msystemlib.MApplication;


public class SysApplication extends MApplication {

	/**对外提供整个应用生命周期的Context**/
	private static Context instance;
	private static final String LOG_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/monitorsystem/log/";
	private static final String LOG_NAME = getCurrentDateString() + ".txt";
	public static final String TAG = "jack";
	/**
	 * 对外提供Application Context
	 * @return
	 */
	public static Context gainContext() {
		return instance;
	}

	public void onCreate() {
		super.onCreate();
		instance = this;
//		Thread.setDefaultUncaughtExceptionHandler(handler);
	}
	UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
		 
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
        	writeErrorLog(ex);
			Intent intent = new Intent();
//			intent.setClass(instance, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			instance.startActivity(intent);
			android.os.Process.killProcess(android.os.Process.myPid());
        }
	};

	/**
     * 打印错误日志
     * 
     * @param ex
     */
    protected void writeErrorLog(Throwable ex) {
         FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
        	File dir = new File(LOG_DIR);
        	if (!dir.exists()) {
        		dir.mkdirs();
        	}
        	File file = new File(dir, LOG_NAME);
        	if (!file.exists()) {
        		file.createNewFile();
        	}
        	fileOutputStream = new FileOutputStream(file, true);
            printStream = new PrintStream(fileOutputStream);

			// 先写入手机的信息
			Class<?> clazz = Class.forName("android.os.Build");
			Field[] fields = clazz.getFields();// 获得所有的字员变量
			for (Field field : fields) {
				String name = field.getName(); // 获得成员变量的名称
				Object value = field.get(null); // 获得成员变量的值
				printStream.println(name+" : "+value); // 将成员变量的信息，写出日志文件
			}
			printStream.println("=============我是一条分隔线=====================");
            ex.printStackTrace(printStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printStream != null) {
                	printStream.flush();
                    printStream.close();
                }
                if(fileOutputStream != null){
                	 fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
    /**
     * 获取当前日期
     * 
     * @return    
     */
    private static String getCurrentDateString() {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Date nowDate = new Date();
        result = sdf.format(nowDate);
        return result;
    }
	/*******************************************Application中存放的Activity操作（压栈/出栈）API（结束）*****************************************/
}
