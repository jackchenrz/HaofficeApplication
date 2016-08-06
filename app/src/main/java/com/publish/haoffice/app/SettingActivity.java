package com.publish.haoffice.app;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.utils.MobileUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.application.SysApplication;

import net.tsz.afinal.annotation.view.ViewInject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingActivity extends BaseActivity {

    @InjectView(R.id.etServerIP)
    EditText etServerIP;
    @InjectView(R.id.etServerPort)
    EditText etServerPort;
    @InjectView(R.id.btnSave)
    Button btnSave;
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;

    @InjectView(R.id.tv_mac)
    TextView tvMac;
    @InjectView(R.id.tv_imei)
    TextView tvImei;
    @InjectView(R.id.tv_version)
    TextView tvVersion;

    private String flag;
    private SPUtils SPUtils;
    @Override
    public int bindLayout () {
        return R.layout.activity_setting;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
        tv_title.setText("设置");
        tvImei.setText("IMEI:" + MobileUtils.getIMEI(this));
        tvMac.setText("MAC:" + MobileUtils.getMacAddress(this));
        tvVersion.setText("版本：" + MobileUtils.getVersionName(this));
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(SettingActivity.this);
            }
        });
    }

    @Override
    public void doBusiness (Context mContext) {
        SPUtils = new SPUtils();
        flag  = SysApplication.gainData(Const.SYSTEM_FLAG).toString().trim();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String serverIP = etServerIP.getText().toString();
                String serverPort = etServerPort.getText().toString();

                if(TextUtils.isEmpty(serverIP) || TextUtils.isEmpty(serverPort)){
                    ToastUtils.showToast(SettingActivity.this, "请输入IP地址或端口");
                    return;
                }
                if("0".equals(flag)){
                    SPUtils.saveString(SettingActivity.this, Const.SERVICE_IP, serverIP,Const.SP_OFFICE);
                    SPUtils.saveString(SettingActivity.this, Const.SERVICE_PORT, serverPort,Const.SP_OFFICE);
                    finish();
                }else if("1".equals(flag)){
                    SPUtils.saveString(SettingActivity.this, Const.SERVICE_IP, serverIP,Const.SP_REPAIR);
                    SPUtils.saveString(SettingActivity.this, Const.SERVICE_PORT, serverPort,Const.SP_REPAIR);
                    jump2Activity(SettingActivity.this,AdminActivity.class,null,true);
                }else if("2".equals(flag)){
                    SPUtils.saveString(SettingActivity.this, Const.SERVICE_IP, serverIP,Const.SP_CONSTRUCT);
                    SPUtils.saveString(SettingActivity.this, Const.SERVICE_PORT, serverPort,Const.SP_CONSTRUCT);
                    finish();
                }
            }
        });
    }

    @Override
    public void resume () {
        if("0".equals(flag)){
            etServerIP.setText(SPUtils.getString(this, Const.SERVICE_IP, "",Const.SP_OFFICE));
            etServerPort.setText(SPUtils.getString(this, Const.SERVICE_PORT, "",Const.SP_OFFICE));
        }else if("1".equals(flag)){
            etServerIP.setText(SPUtils.getString(this, Const.SERVICE_IP, "",Const.SP_REPAIR));
            etServerPort.setText(SPUtils.getString(this, Const.SERVICE_PORT, "",Const.SP_REPAIR));
        }else if("2".equals(flag)){
            etServerIP.setText(SPUtils.getString(this, Const.SERVICE_IP, "",Const.SP_CONSTRUCT));
            etServerPort.setText(SPUtils.getString(this, Const.SERVICE_PORT, "",Const.SP_CONSTRUCT));
        }
    }

    @Override
    public void destroy () {

    }
}
