package com.publish.haoffice.app.Repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msystemlib.base.BaseFragment;
import com.msystemlib.common.adapter.ComFragmentAdapter;
import com.msystemlib.utils.StatuesUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.view.LazyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ToRepairLookListActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.ll_upload)
    LinearLayout ll_upload;
    @InjectView(R.id.view_pager)
    LazyViewPager viewPager;

    @InjectView(R.id.rg_main1)
    RadioGroup rg_main;
    @InjectView(R.id.rb_upload_no)
    RadioButton rb_upload_no;
    @InjectView(R.id.rb_upload_yes)
    RadioButton rb_upload_yes;


    private List<BaseFragment> pagerList;
    private ComFragmentAdapter adapter;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        StatuesUtils.openImmerseStatasBarMode(this);
        setContentView(R.layout.activity_repair_list);
        ButterKnife.inject(this);
        tv_title.setText("维修查看列表");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                ToRepairLookListActivity.this.finish();
                overridePendingTransition(0, com.msystemlib.R.anim.base_slide_right_out);
            }
        });
        doBusiness(this);
    }



    public void doBusiness (Context mContext) {
        Intent intent = getIntent();
        int flag5t = intent.getIntExtra("flag5t", -1);
        Bundle bundle = new Bundle();
        bundle.putInt("flag5t", flag5t);
        ll_upload.setVisibility(View.VISIBLE);
        rg_main.check(R.id.rb_upload_no);
        rg_main.setOnCheckedChangeListener(this);
        pagerList = new ArrayList<>();
        //添加五个页面
        ToNoUploadFragment noUploadFragment = new ToNoUploadFragment();
        ToYesUploadFragment yesUploadFragment = new ToYesUploadFragment();
        pagerList.add(noUploadFragment);
        pagerList.add(yesUploadFragment);
        noUploadFragment.setArguments(bundle);
        yesUploadFragment.setArguments(bundle);

        adapter = new ComFragmentAdapter(getSupportFragmentManager(),pagerList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if(arg0 == 0){
                    rg_main.check(R.id.rb_upload_no);
                }else if(arg0 == 1){
                    rg_main.check(R.id.rb_upload_yes);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int index = 0;

        switch (checkedId) {
            case R.id.rb_upload_no://更新
                index = 0;
                ll_upload.setVisibility(View.VISIBLE);
                rb_upload_no.setTextColor(this.getResources().getColor(R.color.red));
                rb_upload_yes.setTextColor(this.getResources().getColor(R.color.darkblack));
                break;
            case R.id.rb_upload_yes://业务
                index = 1;
                ll_upload.setVisibility(View.GONE);
                rb_upload_no.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_upload_yes.setTextColor(this.getResources().getColor(R.color.red));
                break;
        }
        //将页面设置到index下标的页面
        viewPager.setCurrentItem(index);
    }


}
