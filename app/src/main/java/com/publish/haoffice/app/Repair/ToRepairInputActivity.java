package com.publish.haoffice.app.Repair;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.common.adapter.CommonAdapter;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ThreadUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.DeptInfoBean;
import com.publish.haoffice.api.bean.repair.FiveTProbTypeBean;
import com.publish.haoffice.api.bean.repair.ToRepairSave;
import com.publish.haoffice.api.bean.repair.ToRepairedBean;
import com.publish.haoffice.api.dao.repair.Sys_deptDao;
import com.publish.haoffice.api.dao.repair.Sys_userDao;
import com.publish.haoffice.api.dao.repair.ToRepairDao;
import com.publish.haoffice.api.utils.DensityUtils;
import com.publish.haoffice.api.utils.DialogUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;


import org.ksoap2.serialization.SoapObject;

import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ToRepairInputActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    @InjectView(R.id.et_repair_workshop)
    EditText et_repair_workshop;
    @InjectView(R.id.et_repair_people)
    EditText et_repair_people;
    @InjectView(R.id.et_torepairtime)
    EditText et_torepairtime;
    @InjectView(R.id.spinner_repair_type)
    Spinner spinner_repair_type;
    @InjectView(R.id.spinner_repair_category)
    Spinner spinner_repair_category;
    @InjectView(R.id.et_repair_type)
    EditText et_repair_type;
    @InjectView(R.id.et_repair_category)
    EditText et_repair_category;

    @InjectView(R.id.ll_repair_workshop)
    LinearLayout ll_repair_workshop;
    @InjectView(R.id.ll_repair_people)
    LinearLayout ll_repair_people;
    @InjectView(R.id.ll_torepairtime)
    LinearLayout ll_torepairtime;
    @InjectView(R.id.ll_repair_type)
    LinearLayout ll_repair_type;
    @InjectView(R.id.ll_repair_category)
    LinearLayout ll_repair_category;
    @InjectView(R.id.et_breakdown)
    EditText et_breakdown;
    @InjectView(R.id.btn_save)
    Button btn_save;

    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.ll_show)
    LinearLayout ll_show;
    @InjectView(R.id.tv_count)
    TextView tv_count;


    private Sys_userDao sys_userDao;
    private Sys_deptDao sys_deptDao;
    private ToRepairDao toRepairDao;
    private SPUtils spUtils;
    private String url;
    private CommonAdapter<Object> adapter;
    private ToRepairedBean.ToRepair toRepair;
    private Dialog loadingDialog;
    private List<FiveTProbTypeBean.FiveTProbType> FiveTProbTypeList;
    private String FiveTSysName;
    private int flag5t;
    private ArrayAdapter<String> adapter_category;
    private String[] repairTypes;
    private ArrayAdapter<String> adapter_repair_type;
    private String time;
    private String date;
    private String FiveTProbType;
    private String faultType;
    private String faultReason;

    @Override
    public int bindLayout () {

       return R.layout.activity_torepair_input;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);

        tv_title.setText("维修录入");
        tv_count.setVisibility(View.VISIBLE);
        tv_count.setText("正在加载中");
        ll_show.setVisibility(View.GONE);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(ToRepairInputActivity.this);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                loadingDialog = DialogUtils.createLoadingDialog(ToRepairInputActivity.this, "正在保存，请稍后...");
                loadingDialog.show();

                ToRepairDetailActivity.instance.finish();
                ToRepairListActivity.instance.finish();
                final String repairFinishTime = et_torepairtime.getText()
                        .toString().trim();
                final String repairUserName = et_repair_people.getText().toString()
                        .trim();
                final String faultHandle = et_breakdown.getText().toString().trim();

                /**
                 * 保存维修故障信息
                 */
                if (flag5t == 0) {
                    faultType = repairTypes[spinner_repair_type.getSelectedItemPosition()];
                    faultReason = categorys[spinner_repair_category.getSelectedItemPosition()];

                    if (TextUtils.isEmpty(faultType)
                            || TextUtils.isEmpty(repairFinishTime)
                            || TextUtils.isEmpty(faultReason)
                            || TextUtils.isEmpty(repairUserName)
                            || TextUtils.isEmpty(faultHandle)) {
                        loadingDialog.dismiss();
                        ToastUtils.showToast(ToRepairInputActivity.this, "请完善相关信息");
                        return;
                    }
                    ThreadUtils.runInBackground(new Runnable() {

                        @Override
                        public void run() {
                            toRepairDao.delToRepair(toRepair.RepairID);
                            ToRepairSave toRepairSave = new ToRepairSave();
                            toRepairSave.RepairID = toRepair.RepairID;
                            toRepairSave.RepairDeptID = toRepair.RepairDeptID;
                            toRepairSave.FaultType = faultType;
                            toRepairSave.FaultReason = faultReason;
                            toRepairSave.RepairUserName = repairUserName;
                            toRepairSave.FaultHandle = faultHandle;
                            toRepairSave.RepairFinishTime = repairFinishTime;
                            toRepairSave.ProbType = "";
                            toRepairSave.ProbSysName = "";
                            toRepairSave.RepairType = "Tech";
                            toRepairSave.UserID = sys_userDao
                                    .getUserInfo(spUtils.getString(ToRepairInputActivity.this,Const.USERNAME, "",Const.SP_REPAIR)).user_id;
                            toRepairSave.IsUpload = "1";
                            toRepairSave.EqptName = toRepair.EqptName;
                            toRepairSave.FaultAppearance = toRepair.FaultAppearance;
                            toRepairSave.ImageUrl = toRepair.ImageUrl;
                            toRepairSave.FaultOccu_Time = toRepair.FaultOccu_Time;
                            toRepairSave.FaultStatus = toRepair.FaultStatus;
                            toRepairDao.addTorepair(toRepairSave);
                            loadingDialog.dismiss();
                            Intent intent = new Intent(ToRepairInputActivity.this,
                                    ToRepairListActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.base_slide_right_in,
                                    R.anim.base_slide_remain);
                            ToRepairInputActivity.this.finish();
                        }
                    });
                }else if(flag5t == 1){

                    faultType = et_repair_type.getText().toString().trim();
                    faultReason = et_repair_category.getText().toString()
                            .trim();

                    ThreadUtils.runInBackground(new Runnable() {

                        @Override
                        public void run() {
                            toRepairDao.delToRepair(toRepair.RepairID);
                            ToRepairSave toRepairSave = new ToRepairSave();
                            toRepairSave.RepairID = toRepair.RepairID;
                            toRepairSave.RepairDeptID = toRepair.RepairDeptID;
                            toRepairSave.RepairUserName = repairUserName;
                            toRepairSave.FaultHandle = faultHandle;
                            toRepairSave.RepairFinishTime = repairFinishTime;
                            toRepairSave.FaultType = faultType;
                            toRepairSave.FaultReason = faultReason;
                            toRepairSave.ProbType = FiveTProbType;
                            toRepairSave.ProbSysName = FiveTSysName;
                            toRepairSave.RepairType = "5T";
                            toRepairSave.UserID = sys_userDao
                                    .getUserInfo(spUtils.getString(ToRepairInputActivity.this,Const.USERNAME, "",Const.SP_REPAIR)).user_id;
                            toRepairSave.IsUpload = "1";
                            toRepairSave.EqptName = toRepair.EqptName;
                            toRepairSave.FaultAppearance = toRepair.FaultAppearance;
                            toRepairSave.ImageUrl = toRepair.ImageUrl;
                            toRepairSave.FaultOccu_Time = toRepair.FaultOccu_Time;
                            toRepairSave.FaultStatus = toRepair.FaultStatus;
                            toRepairDao.addTorepair(toRepairSave);
                            loadingDialog.dismiss();
                            Intent intent = new Intent(ToRepairInputActivity.this,
                                    ToRepairListActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.base_slide_right_in,
                                    R.anim.base_slide_remain);
                            ToRepairInputActivity.this.finish();
                        }
                    });
                }
            }
        });
    }
    private String[] categorys;
    @Override
    public void doBusiness (Context mContext) {
        sys_userDao = Sys_userDao.getInstance(mContext);
        sys_deptDao = Sys_deptDao.getInstance(mContext);
        toRepairDao = ToRepairDao.getInstance(mContext);
        spUtils = new SPUtils();
        String serverIP = spUtils.getString(ToRepairInputActivity.this, Const.SERVICE_IP, "", Const.SP_OFFICE);
        String serverPort = spUtils.getString(ToRepairInputActivity.this, Const.SERVICE_PORT, "",Const.SP_OFFICE);
        if (serverIP != null && !"".equals(serverIP) && serverPort != null&& !"".equals(serverPort)) {
            url = "http://" + serverIP + ":" + serverPort+ Const.SERVICE_PAGE;
        }
        Intent intent = getIntent();
        toRepair = (ToRepairedBean.ToRepair) intent.getSerializableExtra("toRepair");
        flag5t = intent.getIntExtra("flag5t",0);
        DeptInfoBean.DeptInfo deptInfo = sys_deptDao.getDeptInfo1(sys_userDao.getUserInfo(
                spUtils.getString(ToRepairInputActivity.this,Const.USERNAME, "",Const.SP_REPAIR)).dept_id);
        et_repair_workshop.setText(deptInfo.dept_name);

        et_torepairtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                showTime();
            }
        });
        if (flag5t == 0) {
            tv_count.setVisibility(View.GONE);
            ll_show.setVisibility(View.VISIBLE);
            spinner_repair_type.setVisibility(View.VISIBLE);
            spinner_repair_category.setVisibility(View.VISIBLE);
            et_repair_type.setVisibility(View.GONE);
            et_repair_category.setVisibility(View.GONE);
            categorys = new String[3];
            categorys[0] = "使用";
            categorys[1] = "维修";
            categorys[2] = "厂家";
            adapter_category = new ArrayAdapter<String>(ToRepairInputActivity.this,R.layout.simple_spinner_item,categorys);
            adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_repair_category.setAdapter(adapter_category);


            repairTypes = new String[3];
            repairTypes[0] = "机械原因";
            repairTypes[1] = "电器原因";
            repairTypes[2] = "机电综合";
            adapter_repair_type = new ArrayAdapter<String>(ToRepairInputActivity.this,R.layout.simple_spinner_item,repairTypes);
            adapter_repair_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_repair_type.setAdapter(adapter_repair_type);
        } else if (flag5t == 1) {

            spinner_repair_type.setVisibility(View.GONE);
            spinner_repair_category.setVisibility(View.GONE);
            et_repair_type.setVisibility(View.VISIBLE);
            et_repair_category.setVisibility(View.VISIBLE);

            HttpConn.callService(url, Const.SERVICE_NAMESPACE,Const.REPAIR_GETFIVET_EQPT_PROB_TYPE, null,new IWebServiceCallBack() {

                @Override
                public void onSucced(SoapObject result) {
                    if (result != null) {
                        tv_count.setVisibility(View.GONE);
                        ll_show.setVisibility(View.VISIBLE);
                        String string = result.getProperty(0)
                                .toString();
                        FiveTProbTypeBean jsonBean = JsonToBean
                                .getJsonBean(string,
                                        FiveTProbTypeBean.class);
                        FiveTProbTypeList = jsonBean.ds;

                        init(FiveTProbTypeList);
                    } else {
                        tv_count.setVisibility(View.VISIBLE);
                        ll_show.setVisibility(View.GONE);
                        tv_count.setText("联网失败");
                    }
                }

                @Override
                public void onFailure(String result) {
                    tv_count.setVisibility(View.VISIBLE);
                    ll_show.setVisibility(View.GONE);
                    tv_count.setText("联网失败");
                }
            });
        }
    }

    /**
     * 初始化控件显示
     * @param list
     */
    private void init (List<FiveTProbTypeBean.FiveTProbType> list) {
//        steps = new String[list.size()];
//
//
//        adapter_steps = new ArrayAdapter<String>(ToRepairInputActivity.this,R.layout.simple_spinner_item,steps);
//        adapter_steps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_repair_type.setAdapter(adapter_steps);
//        spinner_repair_type.setOnItemClickListene

        et_repair_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                popWindow(FiveTProbTypeList, et_repair_type, et_repair_category);
            }
        });
        et_repair_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                popWindow(FiveTProbTypeList, et_repair_type, et_repair_category);
            }
        });
    }
        /**
         * 自定义布局的popupWindow
         *
         * @param anchor
         */
        private void popWindow(final List list, final EditText anchor,final EditText tv) {
            LayoutInflater inflater = LayoutInflater.from(this);// 获取一个填充器
            View view = inflater.inflate(R.layout.popup_sel, null);// 填充我们自定义的布局

            Display display = getWindowManager().getDefaultDisplay();// 得到当前屏幕的显示器对象
            Point size = new Point();// 创建一个Point点对象用来接收屏幕尺寸信息
            display.getSize(size);// Point点对象接收当前设备屏幕尺寸信息
            int width = size.x;// 从Point点对象中获取屏幕的宽度(单位像素)
            int height = size.y;// 从Point点对象中获取屏幕的高度(单位像素)
            // 创建一个PopupWindow对象，第二个参数是设置宽度的，用刚刚获取到的屏幕宽度乘以2/3，2*width/3取该屏幕的2/3宽度，从而在任何设备中都可以适配，高度则包裹内容即可，最后一个参数是设置得到焦点
            final PopupWindow popWindow = new PopupWindow(view,  width
                    , 1 * height / 2, true);
            popWindow.setBackgroundDrawable(new BitmapDrawable());// 设置PopupWindow的背景为一个空的Drawable对象，如果不设置这个，那么PopupWindow弹出后就无法退出了
            popWindow.setOutsideTouchable(true);// 设置是否点击PopupWindow外退出PopupWindow
            //防止虚拟软键盘被弹出菜单遮住
            popWindow.setFocusable(true);
            popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popWindow.setAnimationStyle(R.style.AnimationPreview);
            WindowManager.LayoutParams params = getWindow().getAttributes();// 创建当前界面的一个参数对象
            params.alpha = 1f;// 设置参数的透明度为0.8，透明度取值为0~1，1为完全不透明，0为完全透明，因为android中默认的屏幕颜色都是纯黑色的，所以如果设置为1，那么背景将都是黑色，设置为0，背景显示我们的当前界面
            getWindow().setAttributes(params);// 把该参数对象设置进当前界面中
            popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {// 设置PopupWindow退出监听器
                @Override
                public void onDismiss() {// 如果PopupWindow消失了，即退出了，那么触发该事件，然后把当前界面的透明度设置为不透明
                    WindowManager.LayoutParams params = getWindow()
                            .getAttributes();
                    params.alpha = 1f;// 设置为不透明，即恢复原来的界面
                    getWindow().setAttributes(params);
                }
            });
            // 第一个参数为父View对象，即PopupWindow所在的父控件对象，第二个参数为它的重心，后面两个分别为x轴和y轴的偏移量
            popWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);

            ListView lv_content = (ListView) view.findViewById(R.id.lv_content);

            adapter = new CommonAdapter<Object>(list) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    TextView tv = new TextView(ToRepairInputActivity.this);
                    tv.setText(list.get(position).toString());
                    tv.setTextSize(DensityUtils.dip2px(ToRepairInputActivity.this,
                            10));
                    tv.setTextColor(Color.BLACK);
                    return tv;
                }
            };

            lv_content.setAdapter(adapter);

            lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String string = list.get(position).toString();
                    String[] strings = string.split(" ");
                    if (strings.length == 1) {
                        anchor.setText(string);
                    } else {
                        anchor.setText(strings[strings.length - 2]);
                        if (tv != null) {
                            tv.setText(strings[strings.length - 1]);
                        }
                        FiveTProbType = strings[0];
                        FiveTSysName = strings[strings.length - 3];
                    }
                    popWindow.dismiss();
                }
            });
        }



        /**
         * 显示时间选择
         */
    private void showTime () {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ToRepairInputActivity.this,
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
                ToRepairInputActivity.this,
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
    public void resume () {

    }

    @Override
    public void destroy () {

    }

    @Override
    public void onDateSet (DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date =year +"/" +(++monthOfYear)+"/"+dayOfMonth+" ";
    }

    @Override
    public void onTimeSet (RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        time = hourString+":"+minuteString+":"+secondString;
        if(flag5t == 0) {
            et_torepairtime.setText(date + time);
        }else if(flag5t == 1){
            et_torepairtime.setText(date + time);
        }
    }

}
