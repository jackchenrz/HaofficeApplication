package com.publish.haoffice.app.office;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msystemlib.base.BaseFragment;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.DBDocListBean;
import com.publish.haoffice.app.Repair.BaseFragmentapp;
import com.publish.haoffice.app.Repair.RepairSearchFragment;
import com.publish.haoffice.app.Repair.SearchResultActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class OfficeSearchFragment extends BaseFragmentapp implements DatePickerDialog.OnDateSetListener {

    @InjectView(R.id.ll_off_search)
    LinearLayout ll_off_search;
    @InjectView(R.id.tv_off_search)
    TextView tv_off_search;
    @InjectView(R.id.ll_doc_search)
    LinearLayout ll_doc_search;
    @InjectView(R.id.tv_doc_search)
    TextView tv_doc_search;
    @InjectView(R.id.layout_search)
    View layout_search;

    @InjectView(R.id.et_file_code)
    EditText et_file_code;
    @InjectView(R.id.et_file_title)
    EditText et_file_title;
    @InjectView(R.id.et_fault_time)
    EditText et_fault_time;
    @InjectView(R.id.et_fault_time1)
    EditText et_fault_time1;
    @InjectView(R.id.btn_search)
    Button btn_search;
    private int falgSearch;
    private String fileCode;
    private String fileTitle;
    private String StartDate;
    private String EndDate;
    private int flag;
    private String date;
    private String time;

    @Override
    public View initView () {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_off_search,null);
        ButterKnife.inject(this,view);
        layout_search.setVisibility(View.GONE);

        init();
        return view;
    }

    private void init () {

        ll_off_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                falgSearch = 0;
                layout_search.setVisibility(View.VISIBLE);
                tv_off_search.setTextColor(getResources().getColor(R.color.title));
                tv_doc_search.setTextColor(getResources().getColor(R.color.darkblack));
            }
        });

        ll_doc_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                falgSearch = 1;
                layout_search.setVisibility(View.VISIBLE);
                tv_off_search.setTextColor(getResources().getColor(R.color.darkblack));
                tv_doc_search.setTextColor(getResources().getColor(R.color.title));
            }
        });


        et_fault_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag = 0;
                showTime();
            }
        });

        et_fault_time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag = 1;
                showTime();
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {


                fileCode = et_file_code.getText().toString().trim();
                fileTitle = et_file_title.getText().toString().trim();
                StartDate = et_fault_time.getText().toString().trim();
                EndDate = et_fault_time1.getText().toString().trim();

                Intent intent = new Intent(getActivity(), OffSearchResultActivity.class);
                intent.putExtra("falgSearch", falgSearch);
                intent.putExtra("fileCode", fileCode);
                intent.putExtra("fileTitle", fileTitle);
                intent.putExtra("StartDate",StartDate);
                intent.putExtra("EndDate",EndDate);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);



            }
        });

    }


    /**
     * 显示时间选择
     */
    private void showTime () {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                OfficeSearchFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
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
                et_fault_time.setText("");
                et_fault_time1.setText("");
            }
        });
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss (DialogInterface dialog) {
            }
        });
    }

    @Override
    public void initData () {

    }

    @Override
    public void onDateSet (DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date =year +"-" +(++monthOfYear)+"-"+dayOfMonth+" ";
        if(flag == 0) {
            et_fault_time.setText(date);
        }else if(flag == 1){
            et_fault_time1.setText(date);
        }
    }

}
