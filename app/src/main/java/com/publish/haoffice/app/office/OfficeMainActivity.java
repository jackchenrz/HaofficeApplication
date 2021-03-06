package com.publish.haoffice.app.office;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.utils.LogUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.app.MineFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OfficeMainActivity extends BaseActivity implements  RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.rb_update)
    RadioButton rb_update;
    @InjectView(R.id.rb_business)
    RadioButton rb_business;
    @InjectView(R.id.rb_mine)
    RadioButton rb_mine;
    @InjectView(R.id.rb_min)
    RadioButton rb_min;
    @InjectView(R.id.rg_main)
    RadioGroup rg_main;

    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;

    private OfficeNoFragment officeNoFragment;
    private OfficeYesFragment officeYesFragment;
    private OfficeSearchFragment officeSearchFragment;
    private MineFragment mineFragment;
    public static OfficeMainActivity instance = null;

    @Override
    public int bindLayout () {
        return R.layout.activity_officemain;
    }

    @Override
    public void initView (View view) {

        ButterKnife.inject(this);
        instance = this;
        tv_title.setText("电子公文");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(OfficeMainActivity.this);
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

        rg_main.setOnCheckedChangeListener(this);
        rg_main.check(R.id.rb_update);
    }

    private void select (int i) {
        FragmentManager fm = getFragmentManager();  //获得Fragment管理器
        FragmentTransaction ft = fm.beginTransaction(); //开启一个事务

        hidtFragment(ft);   //先隐藏 Fragment

        switch (i) {
            case 0:
                rb_update.setTextColor(this.getResources().getColor(R.color.bottomcolor));
                rb_business.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_mine.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_min.setTextColor(this.getResources().getColor(R.color.darkblack));
                if (officeNoFragment == null) {
                    officeNoFragment = new OfficeNoFragment();
                    ft.add(R.id.fragment_container, officeNoFragment);
                } else {
                    ft.show(officeNoFragment);
                }
                break;
            case 1:
                rb_business.setTextColor(this.getResources().getColor(R.color.bottomcolor));
                rb_update.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_mine.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_min.setTextColor(this.getResources().getColor(R.color.darkblack));
                if (officeYesFragment == null) {
                    officeYesFragment = new OfficeYesFragment();
                    ft.add(R.id.fragment_container, officeYesFragment);
                } else {
                    ft.show(officeYesFragment);
                }
                break;
            case 2:
                rb_mine.setTextColor(this.getResources().getColor(R.color.bottomcolor));
                rb_business.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_update.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_min.setTextColor(this.getResources().getColor(R.color.darkblack));
                if (officeSearchFragment == null) {
                    officeSearchFragment = new OfficeSearchFragment();
                    ft.add(R.id.fragment_container, officeSearchFragment);
                } else {
                    ft.show(officeSearchFragment);
                }
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
                break;
        }
        ft.commit();   //提交事务
    }

    //隐藏所有Fragment
    private void hidtFragment (FragmentTransaction fragmentTransaction) {
        if (officeNoFragment != null) {
            fragmentTransaction.hide(officeNoFragment);
        }
        if (officeYesFragment != null) {
            fragmentTransaction.hide(officeYesFragment);
        }
        if (officeSearchFragment != null) {
            fragmentTransaction.hide(officeSearchFragment);
        }
        if (mineFragment != null) {
            fragmentTransaction.hide(mineFragment);
        }
    }

    @Override
    public void doBusiness (Context mContext) {
        rg_main.check(R.id.rb_update);
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
