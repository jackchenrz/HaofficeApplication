package com.publish.haoffice.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.WordBean;
import com.publish.haoffice.api.utils.DensityUtils;
import com.publish.haoffice.app.office.OfficDetailActivity;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.Calendar;
import java.util.HashMap;

public class TestActivity  extends Activity{


    private String officeUrl;
    private SPUtils spUtils;

    private WebView mWebView;
    private int fontSize = 1;
    private WebSettings settings;
    private Button imgViewFD;
    private Button imgViewFD1;
    private ScrollView ss;


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mWebView = (WebView) findViewById(R.id.webview);
        imgViewFD = (Button) findViewById(R.id.imgViewFD);
        imgViewFD1 = (Button) findViewById(R.id.imgViewFD1);
        ss = (ScrollView) findViewById(R.id.ss);



        settings = mWebView.getSettings();//常用设置类
        settings.setSupportZoom(true);//支持放大缩小
        settings.setTextSize(WebSettings.TextSize.SMALLEST);
        if (settings.getTextSize() == WebSettings.TextSize.SMALLEST) {
            fontSize = 1;
        } else if (settings.getTextSize() == WebSettings.TextSize.SMALLER) {
            fontSize = 2;
        } else if (settings.getTextSize() == WebSettings.TextSize.NORMAL) {
            fontSize = 3;
        } else if (settings.getTextSize() == WebSettings.TextSize.LARGER) {
            fontSize = 4;
        } else if (settings.getTextSize() == WebSettings.TextSize.LARGEST) {
            fontSize = 5;
        }

        /**
         * 缩小按钮
         */
        imgViewFD1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                fontSize--;

                if (fontSize < 0) {
                    fontSize = 1;
                }
                switch (fontSize) {

                    case 1:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 5:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                }

            }
        });

        imgViewFD.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                fontSize++;

                if (fontSize > 5) {
                    fontSize = 5;
                }
                switch (fontSize) {

                    case 1:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 5:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                }
            }
        });

//        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setUseWideViewPort(true);//关键点
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setDisplayZoomControls(false);
//        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
//        webSettings.setAllowFileAccess(true); // 允许访问文件
//        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
//        webSettings.setSupportZoom(true); // 支持缩放
//        webSettings.setLoadWithOverviewMode(true);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int mDensity = metrics.densityDpi;
//        Log.d("maomao", "densityDpi = " + mDensity);
//        if (mDensity == 240) {
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        } else if (mDensity == 160) {
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//        } else if(mDensity == 120) {
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
//        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        }else if (mDensity == DisplayMetrics.DENSITY_TV){
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        }else{
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//        }
///**
// * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
// * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
// */
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        spUtils = new SPUtils();
        officeUrl = "http://" + spUtils.getString(this, Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(this, Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE1;
        getWordData();
    }

    protected void getWordData() {
        HashMap<String, String> map = new HashMap<>(DEFAULT_KEYS_DIALER);
        map.put("DocID","4E2FAA12-6A33-46CA-BE36-0129146F1947");
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETDOCDATAURLEX, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {

                if(result != null){
                    final String string = result.getProperty(0).toString();
                    mWebView.loadUrl(string);
                }
            }

            @Override
            public void onFailure (String result) {
            }
        });


    }

        @Override
        public void onResume() {
            super.onResume();
        }



}
