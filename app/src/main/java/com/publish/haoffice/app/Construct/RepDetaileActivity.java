package com.publish.haoffice.app.Construct;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.utils.LogUtils;
import com.publish.haoffice.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/7/13.
 */
public class RepDetaileActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.rg_main1)
    RadioGroup rg_main;
    @InjectView(R.id.rb_construct)
    RadioButton rb_construct;
    @InjectView(R.id.rb_repair)
    RadioButton rb_repair;
    @InjectView(R.id.rb_other)
    RadioButton rb_other;

    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    public static RepDetaileActivity instance = null;

    private RepDetaileFragment repDetaileFragment;
    private ComWorkStateFragment comWorkStateFragment;
    private int firstIn = 0;
    private String RepairID;

    @Override
    public int bindLayout () {
        return R.layout.activity_construct_detail;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
        instance = this;
        rb_repair.setVisibility(View.GONE);
        rb_construct.setText("维修项目详情");
        tv_title.setText("详情页");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(RepDetaileActivity.this);
            }
        });
        Intent intent = getIntent();
        RepairID = intent.getStringExtra("RepairID");
    }


    protected void select (int i) {
        FragmentManager fm1 = getFragmentManager();  //获得Fragment管理器
        FragmentTransaction ft1 = fm1.beginTransaction(); //开启一个事务

        hidtFragment(ft1);   //先隐藏 Fragment
        Bundle bundle = new Bundle();
        bundle.putString("RepairID",RepairID);
        bundle.putString("flag","2");
        switch (i) {
            case 0:
                rb_construct.setTextColor(this.getResources().getColor(R.color.titleText));
                rb_repair.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_other.setTextColor(this.getResources().getColor(R.color.darkblack));

                if (repDetaileFragment == null) {
                    repDetaileFragment = new RepDetaileFragment();
                    repDetaileFragment.setArguments(bundle);
                    ft1.add(R.id.fragment_container1, repDetaileFragment,"DETAIL");
                } else {
                    ft1.show(repDetaileFragment);
                }
                rg_main.check(R.id.rb_construct);
                break;
            case 1:
                rb_other.setTextColor(this.getResources().getColor(R.color.titleText));
                rb_construct.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_repair.setTextColor(this.getResources().getColor(R.color.darkblack));
                if (comWorkStateFragment == null) {
                    comWorkStateFragment = new ComWorkStateFragment();
                    comWorkStateFragment.setArguments(bundle);
                    ft1.add(R.id.fragment_container1, comWorkStateFragment);
                } else {
                    ft1.show(comWorkStateFragment);
                }
                rg_main.check(R.id.rb_other);
                break;

        }
        ft1.commit();   //提交事务
    }

    //隐藏所有Fragment
    private void hidtFragment (FragmentTransaction fragmentTransaction) {
        if (repDetaileFragment != null) {
            fragmentTransaction.hide(repDetaileFragment);
        }
        if (comWorkStateFragment != null) {
            fragmentTransaction.hide(comWorkStateFragment);
        }
    }

    @Override
    public void doBusiness (Context mContext) {
        rg_main.setOnCheckedChangeListener(this);
        rg_main.check(R.id.rb_construct);
    }

    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }

    @Override
    public void onCheckedChanged (RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_construct://更新
                select(0);
                break;
            case R.id.rb_other://业务
                select(1);
                break;
        }
    }

    private int downX;
    private int downY;
    private boolean b;
    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                downX = (int) ev.getX();
                b = super.dispatchTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
//                int moveX1 = (int) ev.getX();
//                int moveY1 = (int) ev.getY();
//                int disX1 = Math.abs(moveX1 - downX);
//                int disY1 = Math.abs(moveY1 - downY);
//                if(disX1 > disY1 && disX1 > 5){
//                    ConstructMainFragment main1 = (ConstructMainFragment) getFragmentManager().findFragmentByTag("MAIN");
//                    main1.fragment_container1.setEnabled(false);
//                }
                b = super.dispatchTouchEvent(ev);
                break;
            case MotionEvent.ACTION_UP:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int disX = Math.abs(moveX - downX);
                int disY = Math.abs(moveY - downY);
                LogUtils.d("ckj",moveX + "/" + downX);
                if(disX > disY && disX > 5){if(rb_construct.isChecked()){
                    if(downX - moveX > 30){
                            select(1);
                            break;
                        }else{
                            select(0);
                            break;
                        }
                    }else if(rb_other.isChecked()){
                        if(downX - moveX > 30){
                            select(1);
                            break;
                        }else{
                            select(0);
                            break;

                        }

                    }
                    b = false;
                }else {
                    b = super.dispatchTouchEvent(ev);
                }
                break;
        }


        return b;
    }

    @Override
    protected void onResume () {
        super.onResume();
        if(firstIn == 0){
            select(0);
        }
        firstIn++;
    }
}
