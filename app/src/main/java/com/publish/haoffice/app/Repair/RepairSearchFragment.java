package com.publish.haoffice.app.Repair;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ThreadUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.DeptInfoBean;
import com.publish.haoffice.api.dao.repair.Sys_deptDao;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RepairSearchFragment extends BaseFragmentapp implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final int FLUSH = 55;
    @InjectView(R.id.ll_tech_search)
    LinearLayout ll_tech_search;
    @InjectView(R.id.ll_5t_search)
    LinearLayout ll_5t_search;
    @InjectView(R.id.layout_search)
    View layout_search;
    @InjectView(R.id.tv_tech_search)
    TextView tv_tech_search;
    @InjectView(R.id.tv_5t_search)
    TextView tv_5t_search;

    @InjectView(R.id.spinner_use_dept)
    Spinner spinner_use_dept;
    @InjectView(R.id.spinner_repair_dept)
    Spinner spinner_repair_dept;
    @InjectView(R.id.et_eqpt_name)
    EditText et_eqpt_name;
    @InjectView(R.id.spinner_fault_state)
    Spinner spinner_fault_state;
    @InjectView(R.id.et_fault_time)
    EditText et_fault_time;
    @InjectView(R.id.et_fault_time1)
    EditText et_fault_time1;

    @InjectView(R.id.btn_search)
    Button btn_search;

    private Sys_deptDao sys_deptDao;
    private List<DeptInfoBean.DeptInfo> useDeptList;
    private List<DeptInfoBean.DeptInfo> repairdeptList;
    private String[] useDepts;
    private String[] repairdepts;
    private ArrayAdapter<String> adapter_useDepts;
    private ArrayAdapter<String> adapter_repairdepts;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg) {
            init();
        }
    };
    private String date;
    private String time;
    private int flag;
    private ArrayAdapter<String> adapter_repairstates;
    private String[] repairstates;
    private SPUtils spUtils;
    private String url;

    private String dept_id = "";
    private String RepairDeptID = "";
    private String FaultStatus = "";
    private String ProbeStation = "";
    private String StartDate = "";
    private String EndDate = "";
    private int falgSearch;
    private String roleName;


    @Override
    public View initView () {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_repair_search, null);
        ButterKnife.inject(this,view);
        layout_search.setVisibility(View.GONE);

        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
            roleName = bundle.getString("roleName");
            LogUtils.d("ckj",bundle.getString("roleName"));
        }

        if("技术科".equals(roleName)){
            ll_5t_search.setVisibility(View.GONE);
        }else if("动态车间".equals(roleName) || "动态维修".equals(roleName) || "动态管理".equals(roleName) ){
            ll_tech_search.setVisibility(View.GONE);
        }

        return view;

    }

    private void init () {

        adapter_useDepts = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner_item,useDepts);
        adapter_useDepts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_use_dept.setAdapter(adapter_useDepts);

        adapter_repairdepts = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner_item,repairdepts);
        adapter_repairdepts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_repair_dept.setAdapter(adapter_repairdepts);

        repairstates = new String[]{"全部","未处理","已处理"};
        adapter_repairstates = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner_item,repairstates);
        adapter_repairstates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fault_state.setAdapter(adapter_repairstates);
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


        ll_tech_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                falgSearch = 0;
                layout_search.setVisibility(View.VISIBLE);
                tv_tech_search.setTextColor(getResources().getColor(R.color.title));
                tv_5t_search.setTextColor(getResources().getColor(R.color.darkblack));
            }
        });

        ll_5t_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                falgSearch = 1;
                layout_search.setVisibility(View.VISIBLE);
                tv_tech_search.setTextColor(getResources().getColor(R.color.darkblack));
                tv_5t_search.setTextColor(getResources().getColor(R.color.title));
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                if(spinner_use_dept.getSelectedItemPosition() != 0){
                    dept_id = useDeptList.get(spinner_use_dept.getSelectedItemPosition() - 1).dept_id;
                }

                if(spinner_repair_dept.getSelectedItemPosition() != 0){
                    RepairDeptID = repairdeptList.get(spinner_repair_dept.getSelectedItemPosition() -1).dept_id;
                }

                if(spinner_fault_state.getSelectedItemPosition() != 0){
                    FaultStatus = repairstates[spinner_fault_state.getSelectedItemPosition()];
                }

                ProbeStation = et_eqpt_name.getText().toString().trim();
                StartDate = et_fault_time.getText().toString().trim();
                EndDate = et_fault_time1.getText().toString().trim();

                HashMap<String, Object> map = new HashMap<String, Object>();


                if(falgSearch == 0){
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("falgSearch", 0);
                    intent.putExtra("dept_id", dept_id);
                    intent.putExtra("RepairDeptID", RepairDeptID);
                    intent.putExtra("FaultStatus",FaultStatus);
                    intent.putExtra("EqptName",ProbeStation);
                    intent.putExtra("StartDate",StartDate);
                    intent.putExtra("EndDate",EndDate);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                }else if(falgSearch == 1){
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("falgSearch", 1);
                    intent.putExtra("dept_id", dept_id);
                    intent.putExtra("RepairDeptID", RepairDeptID);
                    intent.putExtra("FaultStatus",FaultStatus);
                    intent.putExtra("ProbeStation",ProbeStation);
                    intent.putExtra("StartDate",StartDate);
                    intent.putExtra("EndDate",EndDate);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                }


            }
        });

    }

    @Override
    public void initData () {

        spUtils = new SPUtils();
        String serverIP = spUtils.getString(getActivity(), Const.SERVICE_IP, "", Const.SP_OFFICE);
        String serverPort = spUtils.getString(getActivity(), Const.SERVICE_PORT, "",Const.SP_OFFICE);
        if (serverIP != null && !"".equals(serverIP) && serverPort != null&& !"".equals(serverPort)) {
            url = "http://" + serverIP + ":" + serverPort+ Const.SERVICE_PAGE;
        }
        sys_deptDao = Sys_deptDao.getInstance(getActivity());

        ThreadUtils.runInBackground(new Runnable() {
            @Override
            public void run () {
                useDeptList = sys_deptDao.getAllDeptList1(1);
                repairdeptList = sys_deptDao.getAllDeptList(1);

                useDepts = new String[useDeptList.size()+1];
                useDepts[0] = "全部";
                repairdepts = new String[repairdeptList.size()+1];
                repairdepts[0] = "全部";
                for (int i = 0; i < useDeptList.size(); i++) {
                    useDepts[i + 1] = useDeptList.get(i).dept_name;
                }
                for (int i = 0; i < repairdeptList.size(); i++) {
                    repairdepts[i + 1] = repairdeptList.get(i).dept_name;
                }

                handler.sendEmptyMessage(FLUSH);
            }
        });
    }


    /**
     * 显示时间选择
     */
    private void showTime () {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                RepairSearchFragment.this,
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
        final TimePickerDialog tpd = TimePickerDialog.newInstance(
                RepairSearchFragment.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.vibrate(false);
        tpd.dismissOnPause(false);
        tpd.enableSeconds(false);
        if (true) {
            tpd.setAccentColor(Color.parseColor("#1E90FF"));
        }
        if (true) {
            tpd.setTitle("选择时间");
        }
        if (false) {
            tpd.setTimeInterval(2, 5, 10);
        }

        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel (DialogInterface dialog) {
                tpd.show(getFragmentManager(), "Timepickerdialog");
                tpd.dismiss();
                et_fault_time.setText("");
            }
        });
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss (DialogInterface dialog) {
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
    }


    @Override
    public void onTimeSet (RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        time = hourString+":"+minuteString+":"+secondString;
        if(flag == 0) {
            et_fault_time.setText(date + time);
        }else if(flag == 1){
            et_fault_time1.setText(date + time);
        }
    }

    @Override
    public void onDateSet (DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date =year +"/" +(++monthOfYear)+"/"+dayOfMonth+" ";
    }
}
