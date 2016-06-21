package com.publish.haoffice.app.office;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.msystemlib.utils.FileUtils;
import com.msystemlib.utils.LogUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;

import java.io.File;
public class MsgService extends Service {

    FinalHttp fh	= new FinalHttp();
    /**
     * 进度条的最大值
     */

    HttpHandler<File> handler;
    /**
     * 进度条的进度值
     */
    private long count1 = 0;
    private long current1 = 0;

    /**
     * 增加get()方法，供Activity调用
     * @return 下载进度
     */
    public long getCount() {
        return count1;
    }

    /**
     * 增加get()方法，供Activity调用
     * @return 下载进度
     */
    public long getCurrent() {
        return current1;
    }

    /**
     * 模拟下载任务，每秒钟更新一次
     */
    public void startDownLoad(String url,String name){
        File folder = new File(FileUtils.gainSDCardPath() + "/word");

        if(!folder.exists()){
            folder.mkdirs();
        }
        // 调用download方法开始下载
        handler = fh.download(url, new AjaxParams(), FileUtils.gainSDCardPath()+"/word/" + name, true,
                new AjaxCallBack() {

                    @Override
                    public void onLoading(long count, long current) {
                        count1 = count;
                        current1 = current;

                        LogUtils.d("ckj",count+ "/" + current );
                    }

                    @Override
                    public void onSuccess(Object t) {

                        LogUtils.d("ckj","dsdsd");
//                        if (t instanceof File) {
//                            File f = (File) t;
//
//                            textView.setText(f == null ? "null" : f.getAbsoluteFile().toString());
//                        }


                    }

                    @Override
                    public void onFailure (Throwable t, int errorNo, String strMsg) {
                        // 调用stop()方法停止下载
                        handler.stop();
                    }
                });


    }


    /**
     * 返回一个Binder对象
     */
    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         * @return
         */
        public MsgService getService(){
            return MsgService.this;
        }
    }
}
