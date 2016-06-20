package com.publish.haoffice.app.Repair;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.msystemlib.utils.AlertUtils;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SDUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ThreadUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.EqptInfoBean;
import com.publish.haoffice.api.bean.repair.FiveTEqptInfoBean;
import com.publish.haoffice.api.bean.repair.RepairInfo;
import com.publish.haoffice.api.dao.repair.Eqpt_InfoDao;
import com.publish.haoffice.api.dao.repair.FiveT_InfoDao;
import com.publish.haoffice.api.dao.repair.Reapir_SubmitDao;
import com.publish.haoffice.api.dao.repair.Sys_deptDao;
import com.publish.haoffice.api.dao.repair.Sys_userDao;
import com.publish.haoffice.api.dao.repair.TechEqptDao;
import com.publish.haoffice.api.utils.DialogUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RepairFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @InjectView(R.id.rl_repair)
    RelativeLayout rl_repair;
    @InjectView(R.id.rl_5Trepair)
    RelativeLayout rl_5Trepair;
    @InjectView(R.id.rl_5Trepair_look)
    RelativeLayout rl_5Trepair_look;
    @InjectView(R.id.rl_repair_look)
    RelativeLayout rl_repair_look;
    @InjectView(R.id.layout_5Tinput)
    View layout_5Tinput;
    @InjectView(R.id.layout_input)
    View layout_input;
    @InjectView(R.id.iv_click)
    ImageView iv_click;
    @InjectView(R.id.iv_click1)
    ImageView iv_click1;
    @InjectView(R.id.iv_photo)
    ImageView iv_photo;
    @InjectView(R.id.iv_selimg)
    ImageView iv_selimg;

    @InjectView(R.id.iv_photo1)
    ImageView iv_photo1;
    @InjectView(R.id.iv_selimg1)
    ImageView iv_selimg1;

    @InjectView(R.id.et_repairtime)
    EditText et_repairtime;
    @InjectView(R.id.et_repairtime1)
    EditText et_repairtime1;

    @InjectView(R.id.et_repairdepartment)
    EditText et_repairdepartment;
    @InjectView(R.id.et_repairdepartment1)
    EditText et_repairdepartment1;

    @InjectView(R.id.et_devicename)
    EditText et_devicename;
    @InjectView(R.id.et_devicename1)
    EditText et_devicename1;

    @InjectView(R.id.et_breakdown)
    EditText et_breakdown;
    @InjectView(R.id.et_breakdown1)
    EditText et_breakdown1;

    @InjectView(R.id.btn_save)
    Button btn_save;
    @InjectView(R.id.btn_save1)
    Button btn_save1;

    private boolean isClick = true;
    private boolean isClick1 = false;
    private String roleName;
    private String date;
    private String time;
    private int flag5t  = -1;
    private int flag  = -1;

    //图片名
    private String name;
    private String imgName;
    private String imgName5t;
    private static final int CAMERA = 55;
    private final int FLUSH = 100;
    private final int IMG = 101;
    private Bitmap bmp;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case FLUSH:
                    loadingDialog.dismiss();
                    AlertUtils.dialog1(getActivity(), "提示", "保存成功", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if(flag5t == 0){
                                et_repairtime.setText("");
                                et_repairdepartment.setText("");
                                et_devicename.setText("");
                                et_breakdown.setText("");
                                iv_photo.setVisibility(View.VISIBLE);
                                iv_selimg.setVisibility(View.GONE);
                                layout_input.setVisibility(View.GONE);
                                iv_click.setBackgroundResource(R.drawable.minus_index);

                            }else if(flag5t == 1){
                                et_repairtime1.setText("");
                                et_repairdepartment1.setText("");
                                et_devicename1.setText("");
                                et_breakdown1.setText("");
                                iv_photo1.setVisibility(View.VISIBLE);
                                iv_selimg1.setVisibility(View.GONE);
                                layout_5Tinput.setVisibility(View.GONE);
                                iv_click1.setBackgroundResource(R.drawable.minus_index);
                            }
                        }
                    },null);
                    break;
                case IMG:
                    if(flag5t == 0){
                        iv_photo.setVisibility(View.GONE);
                        iv_selimg.setVisibility(View.VISIBLE);
                        iv_selimg.setImageBitmap(bmp);
                        imgName = name;
                    }else if(flag5t == 1){
                        iv_photo1.setVisibility(View.GONE);
                        iv_selimg1.setVisibility(View.VISIBLE);
                        iv_selimg1.setImageBitmap(bmp);
                        imgName5t = name;
                    }
                    break;
            }
        };
    };
    private Dialog loadingDialog;
    private FiveT_InfoDao fivetDao;
    private Eqpt_InfoDao eqptDao;
    private TechEqptDao techDao;
    private Sys_userDao userDao;
    private SPUtils sp;
    private Sys_deptDao deptDao;
    private Reapir_SubmitDao repairDao;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_repair,container,false);
        ButterKnife.inject(this,view);
        init();
        return view;
    }

    /**
     * 初始化按钮点击事件
     */
    private void init(){
        fivetDao = FiveT_InfoDao.getInstance(getActivity());
        eqptDao = Eqpt_InfoDao.getInstance(getActivity());
        techDao = TechEqptDao.getInstance(getActivity());
        userDao = Sys_userDao.getInstance(getActivity());
        deptDao = Sys_deptDao.getInstance(getActivity());
        repairDao = Reapir_SubmitDao.getInstance(getActivity());
        sp = new SPUtils();
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null && null!=bundle.getString("datetime")){
            roleName = bundle.getString("roleName");
            LogUtils.d("ckj",bundle.getString("datetime"));
        }

        if("动态车间".equals(roleName)){
            rl_repair.setVisibility(View.GONE);
        }
        layout_input.setVisibility(View.GONE);
        layout_5Tinput.setVisibility(View.GONE);
        rl_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(isClick){
                    layout_input.setVisibility(View.GONE);
                    iv_click.setBackgroundResource(R.drawable.minus_index);
                }else{
                    layout_input.setVisibility(View.VISIBLE);
                    iv_click.setBackgroundResource(R.drawable.plus_index);
                }

                isClick = !isClick;
            }
        });
        rl_5Trepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(isClick1){
                    layout_5Tinput.setVisibility(View.GONE);

                    iv_click1.setBackgroundResource(R.drawable.minus_index);
                }else{
                    layout_5Tinput.setVisibility(View.VISIBLE);
                    iv_click1.setBackgroundResource(R.drawable.plus_index);
                }

                isClick1 = !isClick1;
            }
        });


        et_repairtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag5t = 0;
                showTime();
            }
        });
        et_repairtime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag5t = 1;
                showTime();
            }
        });
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag5t = 0;
                photoGraph();
            }
        });

        iv_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag5t = 1;
                photoGraph();
            }
        });

        et_devicename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag5t = 0;
                Intent intent2 = new Intent(getActivity(), SearchActivity.class);
                flag = 0;
                intent2.putExtra("flag", flag);
                intent2.putExtra("flag5t", flag5t);
                startActivityForResult(intent2, Const.DEVICENAME);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });

        et_devicename1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag5t = 1;
                Intent intent2 = new Intent(getActivity(), SearchActivity.class);
                flag = 0;
                intent2.putExtra("flag", flag);
                intent2.putExtra("flag5t", flag5t);
                startActivityForResult(intent2, Const.DEVICENAME);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });

        et_repairdepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag5t = 0;
                Intent intent2 = new Intent(getActivity(), SearchActivity.class);
                flag = 1;
                intent2.putExtra("flag", flag);
                intent2.putExtra("flag5t", flag5t);
                startActivityForResult(intent2, Const.DEVICENAME);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
        et_repairdepartment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag5t = 1;
                Intent intent2 = new Intent(getActivity(), SearchActivity.class);
                flag = 1;
                intent2.putExtra("flag", flag);
                intent2.putExtra("flag5t", flag5t);
                startActivityForResult(intent2, Const.DEVICENAME);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag5t = 0;
                final String deviceName = et_devicename.getText().toString().trim();
                final String repairdepartment = et_repairdepartment.getText()
                        .toString().trim();
                final String breakdown = et_breakdown.getText().toString().trim();
                final String repairtime = et_repairtime.getText().toString().trim();
                final String imgUri = imgName;
                if (TextUtils.isEmpty(imgName) || TextUtils.isEmpty(deviceName)
                        || TextUtils.isEmpty(repairdepartment)
                        || TextUtils.isEmpty(repairtime)
                        || TextUtils.isEmpty(breakdown)) {
                    Toast.makeText(getActivity(), "保存失败，请完善信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveRepairInfo(deviceName, repairdepartment, breakdown, repairtime,
                        imgUri);
            }
        });
        btn_save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                flag5t = 1;
                final String deviceName = et_devicename1.getText().toString().trim();
                final String repairdepartment = et_repairdepartment1.getText()
                        .toString().trim();
                final String breakdown = et_breakdown1.getText().toString().trim();
                final String repairtime = et_repairtime1.getText().toString().trim();
                final String imgUri = imgName5t;

                if (TextUtils.isEmpty(imgName5t) || TextUtils.isEmpty(deviceName)
                        || TextUtils.isEmpty(repairdepartment)
                        || TextUtils.isEmpty(repairtime)
                        || TextUtils.isEmpty(breakdown)) {
                    Toast.makeText(getActivity(), "保存失败，请完善信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveRepairInfo(deviceName, repairdepartment, breakdown, repairtime,
                        imgUri);
            }
        });

        rl_repair_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent2 = new Intent(getActivity(), RepairListActivity.class);
                flag5t = 0;
                intent2.putExtra("flag5t", flag5t);
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
        rl_5Trepair_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent2 = new Intent(getActivity(), RepairListActivity.class);
                flag5t = 1;
                intent2.putExtra("flag5t", flag5t);
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
    }

    /**
     * 保存输入信息
     * @param deviceName
     * @param repairdepartment
     * @param breakdown
     * @param repairtime
     * @param imgUri
     */
    private void saveRepairInfo(final String deviceName,
                                final String repairdepartment, final String breakdown,
                                final String repairtime, final String imgUri) {
        loadingDialog = DialogUtils.createLoadingDialog(getActivity(), "正在保存，请稍后...");
        loadingDialog.show();
        final RepairInfo repairInfo = new RepairInfo();
        ThreadUtils.runInBackground(new Runnable() {

            @Override
            public void run() {

                repairInfo.RepairID = UUID.randomUUID().toString();

                if (flag5t == 1) {
                    repairInfo.RepairType = "5T";
                    FiveTEqptInfoBean.FiveTEqpt fiveTEqpt = fivetDao.getFiveTEqpt(deviceName);
                    repairInfo.EqptID = fiveTEqpt.EqptID;
                    repairInfo.EqptName = fiveTEqpt.EqptAddress;
                    repairInfo.EqptType = fiveTEqpt.EqptType;
                    repairInfo.ProbeStation = fiveTEqpt.ProbeStation;
                    repairInfo.Specification = "";
                    repairInfo.Manufacturer = "";
                } else if (flag5t == 0) {
                    repairInfo.RepairType = "Tech";
                    EqptInfoBean.EqptInfo eqptInfo = eqptDao.getEqptInfo(deviceName);
                    repairInfo.EqptID = techDao.getTechEqptID(eqptInfo.EqptInfoID);
                    repairInfo.EqptName = eqptInfo.EqptName;
                    repairInfo.EqptType = "";
                    repairInfo.ProbeStation = "";
                    repairInfo.Specification = eqptInfo.EqptSpecif;
                    repairInfo.Manufacturer = eqptInfo.Manufacturer;
                }
                repairInfo.FaultStatus = "未处理";
                repairInfo.UserID = userDao
                        .getUserInfo(sp.getString(getActivity(),Const.USERNAME, "",Const.SP_REPAIR)).user_id;
                repairInfo.UserDeptID = userDao
                        .getUserInfo(sp.getString(getActivity(),Const.USERNAME, "",Const.SP_REPAIR)).dept_id;
                repairInfo.FaultOccu_Time = repairtime;
                repairInfo.FaultAppearance = breakdown;
                repairInfo.IsUpload = 1;
                repairInfo.ImageUrl = imgUri;
                repairInfo.CreateDate = new SimpleDateFormat("HH:mm")
                        .format(new Date());
                repairInfo.LastUpdateDate = new SimpleDateFormat("HH:mm")
                        .format(new Date());
                repairInfo.IsStop = false;
                repairInfo.StopTime = "";
                repairInfo.StopHours = 0;
                repairInfo.StopMinutes = 0;
                repairInfo.RepairDeptID = deptDao
                        .getDeptInfo(repairdepartment).dept_id;
                repairDao.addRepairSubmit(repairInfo);
                handler.sendEmptyMessage(FLUSH);
            }
        });
    }


    /**
     * 手机拍照
     */
    private void photoGraph() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {

            if(SDUtils.getSDFreeSize()>10){
                try {
                    File dir = new File(Const.FILEPATH);
                    new DateFormat();
                    name = DateFormat.format("yyyyMMdd_hhmmss",
                            Calendar.getInstance(Locale.CHINA))
                            + ".jpg";
                    if (!dir.exists())
                        dir.mkdirs();

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(dir, name);
                    Uri u = Uri.fromFile(f);
                    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                    startActivityForResult(intent, CAMERA);
                } catch (ActivityNotFoundException e) {
                }
            }else{
                Toast.makeText(getActivity(), "内存卡空间不足",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "没有储存卡",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 显示时间选择
     */
    private void showTime () {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                RepairFragment.this,
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
                RepairFragment.this,
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

    /**
     * 接收返回数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if (resultCode == Const.DEVICENAME) {
                String devicename = data.getStringExtra("devicename");

                if(flag5t == 0){
                    et_devicename.setText(devicename);
                }else if(flag5t == 1){
                    et_devicename1.setText(devicename);
                }
            }
            if (resultCode == Const.REPAIRDEPARTMENTLIST) {
                String repairdepartment = data
                        .getStringExtra("repairdepartment");
                if(flag5t == 0){
                    et_repairdepartment.setText(repairdepartment);
                }else if(flag5t == 1){
                    et_repairdepartment1.setText(repairdepartment);
                }
            }
        }
        if (resultCode == -1) {
            switch (requestCode) {
                case CAMERA:
                    File f = new File(Const.FILEPATH + name);
                    if(f.exists()){
                        ThreadUtils.runInBackground(new Runnable() {

                            @Override
                            public void run() {
                                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                                bitmapOptions.inSampleSize = 5;
                                bmp = BitmapFactory.decodeFile(Const.FILEPATH + name, bitmapOptions);
                                handler.sendEmptyMessage(IMG);
                            }
                        });
                    }
                    break;
            }
        }

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
            et_repairtime.setText(date + time);
        }else if(flag5t == 1){
            et_repairtime1.setText(date + time);
        }
    }
}
