package com.publish.haoffice.app.Construct;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ThreadUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.Construct.LineBean;
import com.publish.haoffice.api.bean.Construct.RegStationBean;
import com.publish.haoffice.api.bean.Construct.RepBean;
import com.publish.haoffice.api.bean.Construct.StationBean;
import com.publish.haoffice.api.bean.Construct.WorkLineBean;
import com.publish.haoffice.api.bean.Construct.WorkSpaceBean;
import com.publish.haoffice.app.Repair.BaseFragmentapp;
import com.publish.haoffice.app.Repair.SearchResultActivity;
import com.publish.haoffice.app.office.OffSearchResultActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ConstructSearchFragment extends BaseFragmentapp implements DatePickerDialog.OnDateSetListener {

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

//    @InjectView(R.id.et_file_code)
//    EditText et_file_code;
//    @InjectView(R.id.et_file_title)
//    EditText et_file_title;
    @InjectView(R.id.et_start_time)
    EditText et_start_time;
    @InjectView(R.id.et_end_time)
    EditText et_end_time;
    @InjectView(R.id.spinner_line)
    Spinner spinner_line;
    @InjectView(R.id.spinner_station)
    Spinner spinner_station;
    @InjectView(R.id.spinner_regstation)
    Spinner spinner_regstation;
    @InjectView(R.id.spinner_linntype)
    Spinner spinner_linntype;
    @InjectView(R.id.spinner_workspace)
    Spinner spinner_workspace;
    @InjectView(R.id.btn_search)
    Button btn_search;
    private int falgSearch;

    private int flag;
    private String date;
    private String constUrl;
    private SPUtils spUtils;
    private String[] lines;
    private ArrayAdapter<String> adapter_lines;
    private List<LineBean.Line> ds;
    private List<WorkLineBean.WorkLine> ds1;
    private String[] workLines;
    private List<StationBean.Station> ds2;
    private String[] stations;
    private List<RegStationBean.RegStation> ds3;
    private String[] regStations;
    private List<WorkSpaceBean.WorkSpace> ds4;
    private String[] workSpaces;

    private String txtStartDateValue = "";
    private String txtEndDateValue = "";



    private final int LINE = 80;
    private final int WORKLINE = 81;
    private final int STATION = 82;
    private final int REGSTATION = 83;
    private final int WORKSPACE = 84;
    private ArrayAdapter<String> adapter_worklines;
    private ArrayAdapter<String> adapter_stations;
    private ArrayAdapter<String> adapter_regStations;
    private ArrayAdapter<String> adapter_workspace;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg) {
            switch (msg.what){
                case LINE:
                    adapter_lines = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner_item,lines);
                    adapter_lines.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_linntype.setAdapter(adapter_lines);
                    if(falgSearch == 0){
                        getworkLinedata(Const.CONSTRUCT_BINDSGWORKLINE);
                    }else   if(falgSearch == 1){
                        getworkLinedata(Const.CONSTRUCT_BINDWXWORKLINE);
                    }
                    break;
                case WORKLINE:
                    adapter_worklines = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner_item,workLines);
                    adapter_worklines.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_line.setAdapter(adapter_worklines);
                    if(falgSearch == 0){
                        getstationdata(Const.CONSTRUCT_BINDSGSTATION);
                    }else   if(falgSearch == 1){
                        getstationdata(Const.CONSTRUCT_BINDWXSTATION);
                    }
                    break;
                case STATION:
                    adapter_stations = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner_item,stations);
                    adapter_stations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_station.setAdapter(adapter_stations);
                    if(falgSearch == 0){
                        getregstationdata(Const.CONSTRUCT_BINDSGREGSTATION);
                    }else   if(falgSearch == 1){
                        getregstationdata(Const.CONSTRUCT_BINDWXREGSTATION);
                    }
                    break;
                case REGSTATION:
                    adapter_regStations = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner_item,regStations);
                    adapter_regStations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_regstation.setAdapter(adapter_regStations);
                    if(falgSearch == 0){
                        getworkspacedata(Const.CONSTRUCT_BINDSGWORKSPACE);
                    }else   if(falgSearch == 1){
                        getworkspacedata(Const.CONSTRUCT_BINDWXWORKSPACE);
                    }
                    break;
                case WORKSPACE:
                    adapter_workspace = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner_item,workSpaces);
                    adapter_workspace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_workspace.setAdapter(adapter_workspace);
                    Date date = new Date();// 创建一个时间对象，获取到当前的时间
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd");// 设置时间显示格式
                    String str = sdf.format(date);// 将当前时间格式化为需要的类型
                    et_end_time.setText(str);
                    SimpleDateFormat sdf1 = new SimpleDateFormat(
                            "yyyy-MM");// 设置时间显示格式
                    String str1 = sdf1.format(date) + "-01";// 将当前时间格式化为需要的类型
                    et_start_time.setText(str1);
                    if(falgSearch == 0){
                        layout_search.setVisibility(View.VISIBLE);
                        tv_off_search.setTextColor(getResources().getColor(R.color.title));
                        tv_doc_search.setTextColor(getResources().getColor(R.color.darkblack));
                    }else   if(falgSearch == 1 ){
                        layout_search.setVisibility(View.VISIBLE);
                        tv_off_search.setTextColor(getResources().getColor(R.color.darkblack));
                        tv_doc_search.setTextColor(getResources().getColor(R.color.title));
                    }
                    break;
            }
        }
    };
    private String txtWorkLineSelectedValue = "";
    private String txtStationNameSelectedValue = "";
    private String txtRegStationSelectedValue = "";
    private String txtLineTypeSelectedValue = "";
    private String txtWorkSpaceSelectedItemText = "";
    private String txtBzSelectedItemText = "";
    private String txtRealNameSelectedValue = "";

    @Override
    public View initView () {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_con_search,null);
        ButterKnife.inject(this,view);
        layout_search.setVisibility(View.GONE);
        spUtils = new SPUtils();
        constUrl = "http://" + spUtils.getString(getActivity(), Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(getActivity(), Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE2;

        init();
        return view;
    }

    private void init () {

        ll_off_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                falgSearch = 0;
                layout_search.setVisibility(View.GONE);
                getLinedata(Const.CONSTRUCT_BINDSGLINETYPE);

            }
        });

        ll_doc_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                falgSearch = 1;
                layout_search.setVisibility(View.GONE);
                getLinedata(Const.CONSTRUCT_BINDWXLINETYPE);
            }
        });


        et_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag = 0;
                showTime();
            }
        });

        et_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag = 1;
                showTime();
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                if(spinner_linntype.getSelectedItemPosition() != 0){
                    txtLineTypeSelectedValue = ds.get(spinner_linntype.getSelectedItemPosition() - 1).LineType;
                }else{
                    txtLineTypeSelectedValue = "";
                }

                if(spinner_station.getSelectedItemPosition() != 0){
                    txtStationNameSelectedValue = ds2.get(spinner_station.getSelectedItemPosition() -1).StationName;
                }else{
                    txtStationNameSelectedValue = "";
                }

                if(spinner_regstation.getSelectedItemPosition() != 0){
                    txtRegStationSelectedValue =  ds3.get(spinner_regstation.getSelectedItemPosition() -1).RegStation;
                }else{
                    txtRegStationSelectedValue = "";
                }

                if(spinner_line.getSelectedItemPosition() != 0){
                    txtWorkLineSelectedValue =  ds1.get(spinner_line.getSelectedItemPosition() -1).WorkLine;
                }else{
                    txtWorkLineSelectedValue = "";
                }
                if(spinner_workspace.getSelectedItemPosition() != 0){
                    txtWorkSpaceSelectedItemText =  ds4.get(spinner_workspace.getSelectedItemPosition() -1).dept_name;
                }else{
                    txtWorkSpaceSelectedItemText = "";
                }
                txtBzSelectedItemText = "";
                txtRealNameSelectedValue = "";
                txtStartDateValue = et_start_time.getText().toString().trim();
                txtEndDateValue = et_end_time.getText().toString().trim();
                Intent intent = new Intent(getActivity(), ConSearchResultActivity.class);
                intent.putExtra("txtStartDateValue", txtStartDateValue);
                intent.putExtra("txtEndDateValue", txtEndDateValue);
                intent.putExtra("txtWorkLineSelectedValue", txtWorkLineSelectedValue);
                intent.putExtra("txtStationNameSelectedValue",txtStationNameSelectedValue);
                intent.putExtra("txtRegStationSelectedValue",txtRegStationSelectedValue);
                intent.putExtra("txtLineTypeSelectedValue",txtLineTypeSelectedValue);
                intent.putExtra("txtWorkSpaceSelectedItemText",txtWorkSpaceSelectedItemText);
                intent.putExtra("txtBzSelectedItemText",txtBzSelectedItemText);
                intent.putExtra("txtRealNameSelectedValue",txtRealNameSelectedValue);
                intent.putExtra("falgSearch",falgSearch);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);

            }
        });

    }

    private void getLinedata (String methodName) {
        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE, methodName, null, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        LineBean jsonBean = JsonToBean.getJsonBean(string, LineBean.class);
                        ds = jsonBean.ds;
                        lines = new String[ds.size()+1];
                        lines[0] = "请选择";
                        ThreadUtils.runInBackground(new Runnable() {
                            @Override
                            public void run () {

                                for (int i = 0; i < ds.size(); i++) {
                                    lines[i + 1] = ds.get(i).LineType;
                                }
                                handler.sendEmptyMessage(LINE);
                            }
                        });

                    }else{
                    }
                }
            }

            @Override
            public void onFailure (String result) {
            }
        });

    }


    private void getworkLinedata (String methodName) {
        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE,methodName, null, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        WorkLineBean jsonBean = JsonToBean.getJsonBean(string, WorkLineBean.class);
                        ds1 = jsonBean.ds;
                        workLines = new String[ds1.size()+1];
                        workLines[0] = "请选择";
                        ThreadUtils.runInBackground(new Runnable() {
                            @Override
                            public void run () {

                                for (int i = 0; i < ds1.size(); i++) {
                                    workLines[i + 1] = ds1.get(i).WorkLine;
                                }
                                handler.sendEmptyMessage(WORKLINE);
                            }
                        });
                    }else{
                    }
                }
            }

            @Override
            public void onFailure (String result) {
            }
        });

    }


    private void getstationdata (final String methodName) {
        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE, methodName, null, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){

                        StationBean jsonBean = null;
                        jsonBean = JsonToBean.getJsonBean(string, StationBean.class);
                        ds2 = jsonBean.ds;
                        stations = new String[ds2.size()+1];
                        stations[0] = "请选择";
                        ThreadUtils.runInBackground(new Runnable() {
                            @Override
                            public void run () {

                                for (int i = 0; i < ds2.size(); i++) {
                                    stations[i + 1] = ds2.get(i).StationName;
                                }
                                handler.sendEmptyMessage(STATION);
                            }
                        });
                    }else{
                    }
                }
            }

            @Override
            public void onFailure (String result) {
            }
        });

    }

    private void getregstationdata (String methodName) {
        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE, methodName, null, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        RegStationBean jsonBean = JsonToBean.getJsonBean(string, RegStationBean.class);
                        ds3 = jsonBean.ds;
                        regStations = new String[ds3.size()+1];
                        regStations[0] = "请选择";
                        ThreadUtils.runInBackground(new Runnable() {
                            @Override
                            public void run () {

                                for (int i = 0; i < ds3.size(); i++) {
                                    regStations[i + 1] = ds3.get(i).RegStation;
                                }
                                handler.sendEmptyMessage(REGSTATION);
                            }
                        });
                    }else{
                    }
                }
            }

            @Override
            public void onFailure (String result) {
            }
        });

    }

    private void getworkspacedata (String methodName) {
        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE, methodName, null, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        WorkSpaceBean jsonBean = JsonToBean.getJsonBean(string, WorkSpaceBean.class);
                        ds4 = jsonBean.ds;
                        workSpaces = new String[ds4.size()+1];
                        workSpaces[0] = "请选择";
                        ThreadUtils.runInBackground(new Runnable() {
                            @Override
                            public void run () {

                                for (int i = 0; i < ds4.size(); i++) {
                                    workSpaces[i + 1] = ds4.get(i).dept_name;
                                }
                                handler.sendEmptyMessage(WORKSPACE);
                            }
                        });
                    }else{
                    }
                }
            }

            @Override
            public void onFailure (String result) {
            }
        });

    }


    /**
     * 显示时间选择
     */
    private void showTime () {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = null;
        if(flag == 0){
            String trim = et_start_time.getText().toString().trim();

            if("".equals(trim) ){
                dpd = DatePickerDialog.newInstance(
                        ConstructSearchFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
            }else{
                String[] split = trim.split("-");
                dpd = DatePickerDialog.newInstance(
                        ConstructSearchFragment.this,
                        Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]) - 1,
                        Integer.parseInt(split[2])
                );
            }

        }else if(flag == 1){
            String trim = et_end_time.getText().toString().trim();

            if("".equals(trim) ){
                dpd = DatePickerDialog.newInstance(
                        ConstructSearchFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
            }else{
                String[] split = trim.split("-");
                dpd = DatePickerDialog.newInstance(
                        ConstructSearchFragment.this,
                        Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]) - 1,
                        Integer.parseInt(split[2])
                );
            }
        }
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
                et_start_time.setText("");
                et_end_time.setText("");
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
        date =year +"-" +(month)+"-"+day+" ";
        if(flag == 0) {
            et_start_time.setText(date);
        }else if(flag == 1){
            et_end_time.setText(date);
        }
    }

}
