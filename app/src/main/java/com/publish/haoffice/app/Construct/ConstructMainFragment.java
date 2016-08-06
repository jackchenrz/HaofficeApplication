package com.publish.haoffice.app.Construct;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msystemlib.base.BaseFragment;
import com.msystemlib.common.adapter.ComFragmentAdapter;
import com.msystemlib.common.adapter.CommonAdapter;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.DBDocListBean;
import com.publish.haoffice.app.MineFragment;
import com.publish.haoffice.app.Repair.BaseFragmentapp;
import com.publish.haoffice.app.Repair.ToNoUploadFragment;
import com.publish.haoffice.app.Repair.ToYesUploadFragment;
import com.publish.haoffice.app.office.DocDetailActivity;
import com.publish.haoffice.app.office.NoticeDetailActivity;
import com.publish.haoffice.app.office.OfficDetailActivity;
import com.publish.haoffice.app.office.OfficeNoFragment;
import com.publish.haoffice.app.office.OfficeSearchFragment;
import com.publish.haoffice.app.office.OfficeYesFragment;
import com.publish.haoffice.view.LazyViewPager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ConstructMainFragment extends BaseFragmentapp implements RadioGroup.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener {

    @InjectView(R.id.rg_main1)
    RadioGroup rg_main;
    @InjectView(R.id.rb_construct)
    RadioButton rb_construct;
    @InjectView(R.id.rb_repair)
    RadioButton rb_repair;
    @InjectView(R.id.rb_other)
    RadioButton rb_other;
    @InjectView(R.id.tv_time)
    TextView tv_time;
    @InjectView(R.id.tv_pre)
    TextView tv_pre;
    @InjectView(R.id.tv_next)
    TextView tv_next;
    @InjectView(R.id.fragment_container1)
    FrameLayout fragment_container1;


    private TodayConFragment todayConFragment;
    private TodayRepairFragment todayRepairFragment;
    private TodayOtherFragment todayOtherFragment;
    private int firstIn = 0;

    @Override
    public View initView () {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_construct_main,null);
        ButterKnife.inject(this,view);
        Date date = new Date();// 创建一个时间对象，获取到当前的时间
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd");// 设置时间显示格式
        String str = sdf.format(date);// 将当前时间格式化为需要的类型
        tv_time.setText(str);
        Const.DATE = str;
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                showTime();
            }
        });
        tv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String trim = tv_time.getText().toString().trim();
                String[] split = trim.split("-");
                String s = split[1];
                Calendar now = Calendar.getInstance();
                now.set( Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]) - 1,
                        Integer.parseInt(split[2]));
                now.add(Calendar.DAY_OF_MONTH,-1);

                Date time = now.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd");// 设置时间显示格式
                String str = sdf.format(time);// 将当前时间格式化为需要的类型
                tv_time.setText(str);
                Const.DATE = str;
                if (todayConFragment != null) {
                    todayConFragment.onRefresh();
                }

                if (todayRepairFragment != null) {
                    todayRepairFragment.onRefresh();
                }

                if (todayOtherFragment != null) {
                    todayOtherFragment.onRefresh();
                }
            }
        });
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String trim = tv_time.getText().toString().trim();
                String[] split = trim.split("-");
                String s = split[1];
                Calendar now = Calendar.getInstance();
                now.set( Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]) - 1,
                        Integer.parseInt(split[2]));
                now.add(Calendar.DAY_OF_MONTH,+1);

                Date time = now.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd");// 设置时间显示格式
                String str = sdf.format(time);// 将当前时间格式化为需要的类型
                tv_time.setText(str);
                Const.DATE = str;
                if (todayConFragment != null) {
                    todayConFragment.onRefresh();
                }

                if (todayRepairFragment != null) {
                    todayRepairFragment.onRefresh();
                }

                if (todayOtherFragment != null) {
                    todayOtherFragment.onRefresh();
                }
            }
        });

        return view;
    }



    /**
     * 显示时间选择
     */
    private void showTime () {
        Calendar now = Calendar.getInstance();

        String trim = tv_time.getText().toString().trim();
        String[] split = trim.split("-");
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ConstructMainFragment.this,
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1]) - 1,
                Integer.parseInt(split[2])
        );

        LogUtils.d("ckj",now.get(Calendar.MONTH) + "");
        dpd.setThemeDark(false);
        dpd.vibrate(false);
        dpd.dismissOnPause(false);
        dpd.showYearPickerFirst(false);
        if (true) {
            dpd.setAccentColor(Color.parseColor("#1E90FF"));
        }
        if (true) {
            dpd.setTitle("选择日期");
        }
        if (false) {
            Calendar[] dates = new Calendar[13];
            for(int i = -6; i <= 6; i++) {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.MONTH, i);
                dates[i+6] = date;
            }
            dpd.setSelectableDays(dates);
        }
        if (false) {
            Calendar[] dates = new Calendar[13];
            for(int i = -6; i <= 6; i++) {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.WEEK_OF_YEAR, i);
                dates[i+6] = date;
            }
            dpd.setHighlightedDays(dates);
        }
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel (DialogInterface dialog) {
            }
        });
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss (DialogInterface dialog) {
            }
        });
    }

    @Override
    public void onDateSet (DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int monthOfYea = ++monthOfYear;
        String month = "";
        String day = "";
        if(monthOfYea > 9){
            month = monthOfYea + "";
        }else{
            month =  "0" + monthOfYea;
        }

        if(dayOfMonth > 9){
            day = dayOfMonth + "";
        }else{
            day =  "0" + dayOfMonth;
        }
        String date =year +"-" +(month)+"-"+day+" ";
        tv_time.setText(date);
        Const.DATE = date;
        if (todayConFragment != null) {
            todayConFragment.onRefresh();
        }

        if (todayRepairFragment != null) {
            todayRepairFragment.onRefresh();
        }

        if (todayOtherFragment != null) {
            todayOtherFragment.onRefresh();
        }
    }



    protected void select (int i) {
        FragmentManager fm1 = getFragmentManager();  //获得Fragment管理器
        FragmentTransaction ft1 = fm1.beginTransaction(); //开启一个事务

        hidtFragment(ft1);   //先隐藏 Fragment
        Bundle bundle = new Bundle();
        bundle.putString("date", tv_time.getText().toString().trim());
        switch (i) {
            case 0:
                rb_construct.setTextColor(this.getResources().getColor(R.color.titleText));
                rb_repair.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_other.setTextColor(this.getResources().getColor(R.color.darkblack));

                if (todayConFragment == null) {
                    todayConFragment = new TodayConFragment();
                    todayConFragment.setArguments(bundle);
                    ft1.add(R.id.fragment_container1, todayConFragment);
                } else {
                    ft1.show(todayConFragment);
                }

                rg_main.check(R.id.rb_construct);
                break;
            case 1:
                rb_repair.setTextColor(this.getResources().getColor(R.color.titleText));
                rb_construct.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_other.setTextColor(this.getResources().getColor(R.color.darkblack));
                if (todayRepairFragment == null) {
                    todayRepairFragment = new TodayRepairFragment();
                    todayRepairFragment.setArguments(bundle);
                    ft1.add(R.id.fragment_container1, todayRepairFragment);
                } else {
                    ft1.show(todayRepairFragment);
                }

                rg_main.check(R.id.rb_repair);
                break;
            case 2:
                rb_other.setTextColor(this.getResources().getColor(R.color.titleText));
                rb_construct.setTextColor(this.getResources().getColor(R.color.darkblack));
                rb_repair.setTextColor(this.getResources().getColor(R.color.darkblack));
                if (todayOtherFragment == null) {
                    todayOtherFragment = new TodayOtherFragment();
                    todayOtherFragment.setArguments(bundle);
                    ft1.add(R.id.fragment_container1, todayOtherFragment);
                } else {
                    ft1.show(todayOtherFragment);
                }
                rg_main.check(R.id.rb_other);
                break;

        }
        ft1.commit();   //提交事务
    }

    //隐藏所有Fragment
    private void hidtFragment (FragmentTransaction fragmentTransaction) {
        if (todayConFragment != null) {
            fragmentTransaction.hide(todayConFragment);
        }
        if (todayRepairFragment != null) {
            fragmentTransaction.hide(todayRepairFragment);
        }
        if (todayOtherFragment != null) {
            fragmentTransaction.hide(todayOtherFragment);
        }
    }



    @Override
    public void initData () {

        rg_main.setOnCheckedChangeListener(this);
        rg_main.check(R.id.rb_construct);
    }

    @Override
    public void onCheckedChanged (RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_construct://更新
                select(0);
                break;
            case R.id.rb_repair://业务
                select(1);
                break;
            case R.id.rb_other://业务
                select(2);
                break;
        }
    }

    @Override
    public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){

            if(firstIn == 0){
               select(0);
            }
            firstIn++;
        }
    }
}
