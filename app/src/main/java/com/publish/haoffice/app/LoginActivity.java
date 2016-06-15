package com.publish.haoffice.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msystemlib.MApplication;
import com.msystemlib.base.BaseActivity;
import com.msystemlib.utils.AlertUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.utils.DialogUtils;
import com.publish.haoffice.app.office.OfficeMainActivity;
import com.publish.haoffice.application.SysApplication;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends BaseActivity{

    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;

    @InjectView(R.id.etLoginName)
    EditText etLoginName;
    @InjectView(R.id.etPassword)
    EditText etPassword;
    @InjectView(R.id.cb_rember)
    CheckBox cbRember;
    @InjectView(R.id.btnLogin)
    Button btnLogin;

    private String flag;
    private Dialog loadingDialog;
    private SPUtils SPUtils;

    @Override
    public int bindLayout () {
        return R.layout.activity_login;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
    }

    @Override
    public void doBusiness (Context mContext) {
        SPUtils = new SPUtils();
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(LoginActivity.this);
            }
        });

        flag = SysApplication.gainData(Const.SYSTEM_FLAG).toString().trim();
        if("0".equals(flag)){
            tv_title.setText("电子公文");
        }else if("1".equals(flag)){
            tv_title.setText("报修管理");
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v) {
            String userName = etLoginName.getText().toString().trim();
            String pwd = etPassword.getText().toString().trim();
            if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)){
                ToastUtils.showToast(LoginActivity.this, "请输入用户名或密码");
                return;
            }
            if(cbRember.isChecked()){
                if("0".equals(flag)){
                    SPUtils.saveString(LoginActivity.this, Const.USERNAME, userName,Const.SP_OFFICE);
                    SPUtils.saveString(LoginActivity.this, Const.PWD, pwd,Const.SP_OFFICE);
                }else if("1".equals(flag)){
                    SPUtils.saveString(LoginActivity.this, Const.USERNAME, userName,Const.SP_REPAIR);
                    SPUtils.saveString(LoginActivity.this, Const.PWD, pwd,Const.SP_REPAIR);
                }
            }else{
                if("0".equals(flag)){
                    SPUtils.clear(LoginActivity.this,Const.SP_OFFICE);
                }else if("1".equals(flag)){
                    SPUtils.clear(LoginActivity.this,Const.SP_REPAIR);
                }
            }
            loadingDialog = DialogUtils.createLoadingDialog(LoginActivity.this, "正在登陆，请稍后...");
            loadingDialog.show();
            if(Const.LOCAL.equalsIgnoreCase(userName) && Const.LOCALPWD.equals(pwd)){
                loadingDialog.dismiss();
                jump2Activity(LoginActivity.this,SettingActivity.class,null,false);
            }else{
//                MApplication.assignData(Const.USERNAME, userName);
//                url = "http://" + ToolSP.getString(this, Const.SERVICE_IP, "") + ":" + ToolSP.getString(this, Const.SERVICE_PORT, "") + "/RecSerApp.asmx";
//                if("".equals(ToolSP.getString(this, Const.SERVICE_IP, ""))){
//                    loadingDialog.dismiss();
//                    ToolToast.showToast(this, "请先联系管理员设置IP地址");
//                    return;
//                }
//                MApplication.assignData(Const.SERVICE_URL, url);
//                matchUser(userName,pwd);
                jump2Activity(LoginActivity.this, OfficeMainActivity.class,null,false);
            }
        }
    });


    }

    @Override
    public void resume () {
        if("0".equals(flag)){
            etLoginName.setText(SPUtils.getString(LoginActivity.this, Const.USERNAME, "",Const.SP_OFFICE));
            etPassword.setText(SPUtils.getString(LoginActivity.this, Const.PWD, "",Const.SP_OFFICE));
        }else if("1".equals(flag)){
            etLoginName.setText(SPUtils.getString(LoginActivity.this, Const.USERNAME, "",Const.SP_REPAIR));
            etPassword.setText(SPUtils.getString(LoginActivity.this, Const.PWD, "",Const.SP_REPAIR));
        }


        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }

    @Override
    public void destroy () {

    }
}
