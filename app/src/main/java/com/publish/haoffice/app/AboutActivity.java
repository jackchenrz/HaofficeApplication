package com.publish.haoffice.app;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.utils.MobileUtils;
import com.publish.haoffice.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AboutActivity extends BaseActivity {

    @InjectView(R.id.tv_version)
    TextView tv_version;

    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;

    @Override
    public int bindLayout () {
        return R.layout.activity_about;
    }

    @Override
    public void initView (View view) {

        ButterKnife.inject(this);
        tv_title.setText("关于");
        tv_version.setText(MobileUtils.getVersionName(this));
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(AboutActivity.this);
            }
        });
    }

    @Override
    public void doBusiness (Context mContext) {

    }

    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }
}
