package com.publish.haoffice.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.utils.ThreadUtils;
import com.publish.haoffice.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashActivity extends BaseActivity {

    @InjectView(R.id.tv_version)
    TextView tv_version;

    @Override
    public int bindLayout () {
        return R.layout.activity_splash;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
    }

    @Override
    public void doBusiness (Context mContext) {

        ThreadUtils.runInBackground(new Runnable() {
            @Override
            public void run () {
                SystemClock.sleep(2000);
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void resume () {
//        PackageManager packageManager = getPackageManager();
//        try {
//            // packageInfo 就是对应清单文件中的所有信息
//            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
//            String versionName = packageInfo.versionName;
//            tv_version.setText(versionName);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void destroy () {

    }
}
