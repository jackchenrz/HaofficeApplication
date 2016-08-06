package com.publish.haoffice.app.Construct;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.utils.LogUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.app.MineFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ConstructMainActivity extends BaseActivity implements  RadioGroup.OnCheckedChangeListener {

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

    private ConstructMainFragment constructMainFragment;
    private ConstructtabFragment constructtabFragment;
    private ConstructSearchFragment constructSearchFragment;
    private MineFragment mineFragment;
    public static ConstructMainActivity instance = null;

    @Override
    public int bindLayout () {
        return R.layout.activity_constructmain;
    }

    @Override
    public void initView (View view) {

        ButterKnife.inject(this);
        instance = this;
        tv_title.setText("施工管理");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(ConstructMainActivity.this);
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
                if (constructMainFragment == null) {
                    constructMainFragment = new ConstructMainFragment();
                    ft.add(R.id.fragment_container, constructMainFragment,"MAIN");
                } else {
                    ft.show(constructMainFragment);
                }
                break;
            case 1:
                rb_mine.setTextColor(this.getResources().getColor(R.color.bottomcolor));
                rb_business.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_update.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_min.setTextColor(this.getResources().getColor(R.color.darkblack));
                if (constructSearchFragment == null) {
                    constructSearchFragment = new ConstructSearchFragment();
                    ft.add(R.id.fragment_container, constructSearchFragment);
                } else {
                    ft.show(constructSearchFragment);
                }
                break;
            case 2:
                rb_business.setTextColor(this.getResources().getColor(R.color.bottomcolor));
                rb_update.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_mine.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_min.setTextColor(this.getResources().getColor(R.color.darkblack));
                if (constructtabFragment == null) {
                    constructtabFragment = new ConstructtabFragment();
                    ft.add(R.id.fragment_container, constructtabFragment,"TAB");
                } else {
                    ft.show(constructtabFragment);
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
        if (constructMainFragment != null) {
            fragmentTransaction.hide(constructMainFragment);
        }
        if (constructtabFragment != null) {
            fragmentTransaction.hide(constructtabFragment);
        }
        if (constructSearchFragment != null) {
            fragmentTransaction.hide(constructSearchFragment);
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
                if(disX > disY && disX > 5){

                    ConstructMainFragment main = (ConstructMainFragment) getFragmentManager().findFragmentByTag("MAIN");

                    if(main.rb_repair.isChecked()){
                        if(downX - moveX > 30){
                            main.select(2);
                            break;
                        }else{
                            main.select(0);
                            break;
                        }

                    }else if(main.rb_construct.isChecked()){
                        if(downX - moveX > 30){
                            main.select(1);
                            break;
                        }else{
                            main.select(0);
                            break;
                        }
                    }else if(main.rb_other.isChecked()){
                        if(downX - moveX > 30){
                            main.select(2);
                            break;
                        }else{
                            main.select(1);
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
    public void onCheckedChanged (RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_update:
                select(0);
                break;
            case R.id.rb_business:
                select(2);
                break;
            case R.id.rb_mine:
                select(1);
                break;
            case R.id.rb_min:
                select(3);
                break;
        }
    }
}
