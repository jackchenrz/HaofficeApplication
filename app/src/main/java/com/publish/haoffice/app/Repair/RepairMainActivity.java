package com.publish.haoffice.app.Repair;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.utils.LogUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.dao.repair.Sys_userDao;
import com.publish.haoffice.app.MineFragment;
import com.publish.haoffice.app.office.OfficeSearchFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RepairMainActivity extends BaseActivity implements  RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.rb_update)
    RadioButton rb_update;
    @InjectView(R.id.rb_business)
    RadioButton rb_business;
    @InjectView(R.id.rb_mine)
    RadioButton rb_mine;
    @InjectView(R.id.rg_main)
    RadioGroup rg_main;

    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.rb_min)
    RadioButton rb_min;

    private RepairSearchFragment repairSearchFragment;
    private RepairFragment repairFragment;
    private ToRepairFragment toRepairFragment;
    private MineFragment mineFragment;
    private Sys_userDao userDao;
    private String roleName;

    @Override
    public int bindLayout () {
        return R.layout.activity_repairmain;
    }

    @Override
    public void initView (View view) {

        ButterKnife.inject(this);
        tv_title.setText("故障报修");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(RepairMainActivity.this);
            }
        });
        Drawable[] drawables = rb_update.getCompoundDrawables();
        drawables[1].setBounds(0, 0, drawables[1].getMinimumWidth() - 65, drawables[1].getMinimumHeight() - 65);
        rb_update.setCompoundDrawables(null, drawables[1], null, null);

        Drawable[] drawables1 = rb_business.getCompoundDrawables();
        drawables1[1].setBounds(0, 0, drawables1[1].getMinimumWidth() - 65, drawables1[1].getMinimumHeight() - 65);
        rb_business.setCompoundDrawables(null, drawables1[1], null, null);

        Drawable[] drawables2 = rb_mine.getCompoundDrawables();
        drawables2[1].setBounds(0, 0, drawables2[1].getMinimumWidth() - 65, drawables2[1].getMinimumHeight() - 65);
        rb_mine.setCompoundDrawables(null, drawables2[1], null, null);

        Drawable[] drawables3 = rb_min.getCompoundDrawables();
        drawables3[1].setBounds(0, 0, drawables3[1].getMinimumWidth() - 65, drawables3[1].getMinimumHeight() - 65);
        rb_min.setCompoundDrawables(null, drawables3[1], null, null);
        userDao = Sys_userDao.getInstance(this);

        Intent intent = getIntent();
        String userName = intent.getStringExtra(Const.USERNAME);
        roleName = userDao.getRoleName(userName);
        rg_main.setOnCheckedChangeListener(this);
        rg_main.check(R.id.rb_update);
    }

    private void select (int i) {
        FragmentManager fm = getFragmentManager();  //获得Fragment管理器
        FragmentTransaction ft = fm.beginTransaction(); //开启一个事务

        hidtFragment(ft);   //先隐藏 Fragment
        Bundle bundle = new Bundle();
        bundle.putString("roleName", roleName);
        switch (i) {
            case 0:
                rb_update.setTextColor(this.getResources().getColor(R.color.bottomcolor));
                rb_business.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_mine.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_min.setTextColor(this.getResources().getColor(R.color.darkblack));
                rg_main.check(R.id.rb_update);
                if (repairFragment == null) {
                    repairFragment = new RepairFragment();
                    repairFragment.setArguments(bundle);
                    ft.add(R.id.fragment_container, repairFragment);
                } else {
                    ft.show(repairFragment);
                }
                break;
            case 1:
                rb_business.setTextColor(this.getResources().getColor(R.color.bottomcolor));
                rb_update.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_mine.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_min.setTextColor(this.getResources().getColor(R.color.darkblack));
                if (toRepairFragment == null) {
                    toRepairFragment = new ToRepairFragment();
                    toRepairFragment.setArguments(bundle);
                    ft.add(R.id.fragment_container, toRepairFragment);
                } else {
                    ft.show(toRepairFragment);
                }
                rg_main.check(R.id.rb_business);
                break;
            case 2:
                rb_mine.setTextColor(this.getResources().getColor(R.color.bottomcolor));
                rb_business.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_update.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_min.setTextColor(this.getResources().getColor(R.color.darkblack));
                if (repairSearchFragment == null) {
                    repairSearchFragment = new RepairSearchFragment();
                    ft.add(R.id.fragment_container, repairSearchFragment);
                } else {
                    ft.show(repairSearchFragment);
                }
                rg_main.check(R.id.rb_mine);
                break;
            case 3:
                rb_min.setTextColor(this.getResources().getColor(R.color.bottomcolor));
                rb_mine.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_business.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_update.setTextColor(this.getResources().getColor(R.color.darkblack));
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    ft.add(R.id.fragment_container, mineFragment);
                } else {
                    ft.show(mineFragment);
                }
                rg_main.check(R.id.rb_min);
                break;
        }

        ft.commit();   //提交事务
    }

    //隐藏所有Fragment
    private void hidtFragment (FragmentTransaction fragmentTransaction) {
        if (repairSearchFragment != null) {
            fragmentTransaction.hide(repairSearchFragment);
        }
        if (repairFragment != null) {
            fragmentTransaction.hide(repairFragment);
        }
        if (toRepairFragment != null) {
            fragmentTransaction.hide(toRepairFragment);
        }
        if (mineFragment != null) {
            fragmentTransaction.hide(mineFragment);
        }
    }

    @Override
    public void doBusiness (Context mContext) {

        if("动态管理".equals(roleName) || "技术科".equals(roleName) || "领导班子".equals(roleName)){
            rb_update.setVisibility(View.GONE);
            rb_business.setVisibility(View.GONE);
            rg_main.check(R.id.rb_mine);
        }else if("动态维修".equals(roleName) ){
            rb_update.setVisibility(View.GONE);
            rg_main.check(R.id.rb_business);
        }else if("设备使用".equals(roleName)){
            rb_business.setVisibility(View.GONE);
            rg_main.check(R.id.rb_update);
        }else{
            rg_main.check(R.id.rb_update);
        }
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
            case R.id.rb_update:
                select(0);
                break;
            case R.id.rb_business:
                select(1);
                break;
            case R.id.rb_mine:
                select(2);
                break;
            case R.id.rb_min:
                select(3);
                break;
        }
    }
}
