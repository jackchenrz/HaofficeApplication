package com.publish.haoffice.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msystemlib.MApplication;
import com.msystemlib.base.BaseActivity;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.AlertUtils;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.MD5Utils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.Construct.UserLogBean;
import com.publish.haoffice.api.bean.office.UserBean;
import com.publish.haoffice.api.dao.repair.Sys_userDao;
import com.publish.haoffice.api.utils.DialogUtils;
import com.publish.haoffice.app.Construct.ConstructMainActivity;
import com.publish.haoffice.app.Repair.RepairMainActivity;
import com.publish.haoffice.app.office.OfficeMainActivity;
import com.publish.haoffice.application.SysApplication;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends BaseActivity{

    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.iv_test)
    ImageView iv_test;

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
    private String repairUrl;
    private String officeUrl;
    private Sys_userDao userDao;
    private String constructUrl;

    @Override
    public int bindLayout () {
        return R.layout.activity_login;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);

//        WindowManager wm = (WindowManager) getContext()
//                .getSystemService(Context.WINDOW_SERVICE);
//        int screenWidth = wm.getDefaultDisplay().getWidth();
//        ViewGroup.LayoutParams lp = iv_test.getLayoutParams();
//        lp.width = screenWidth;
//        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        iv_test.setLayoutParams(lp);
//
//        iv_test.setMaxWidth(screenWidth);
//        iv_test.setMaxHeight(screenWidth * 5);

    }

    @Override
    public void doBusiness (Context mContext) {
        SPUtils = new SPUtils();
        userDao = Sys_userDao.getInstance(this);
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
        }else if("2".equals(flag)){
            tv_title.setText("施工管理");
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v) {
            String userName = etLoginName.getText().toString().trim();
            String pwd = etPassword.getText().toString().trim();

//            if("0".equals(flag)){
//
//                jump2Activity(LoginActivity.this, OfficeMainActivity.class,null,false);
//            }else{
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put(Const.USERNAME,userName);
//                jump2Activity(LoginActivity.this, RepairMainActivity.class,map,false);
//            }


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
                }else if("2".equals(flag)){
                    SPUtils.saveString(LoginActivity.this, Const.USERNAME, userName,Const.SP_CONSTRUCT);
                    SPUtils.saveString(LoginActivity.this, Const.PWD, pwd,Const.SP_CONSTRUCT);
                }
            }else{
                if("0".equals(flag)){
                    SPUtils.saveString(LoginActivity.this, Const.USERNAME, "",Const.SP_OFFICE);
                    SPUtils.saveString(LoginActivity.this, Const.PWD, "",Const.SP_OFFICE);
                }else if("1".equals(flag)){
                    SPUtils.saveString(LoginActivity.this, Const.USERNAME, "",Const.SP_REPAIR);
                    SPUtils.saveString(LoginActivity.this, Const.PWD, "",Const.SP_REPAIR);
                }else if("2".equals(flag)){
                    SPUtils.saveString(LoginActivity.this, Const.USERNAME, "",Const.SP_CONSTRUCT);
                    SPUtils.saveString(LoginActivity.this, Const.PWD, "",Const.SP_CONSTRUCT);
                }
            }
            loadingDialog = DialogUtils.createLoadingDialog(LoginActivity.this, "正在登陆，请稍后...");
            loadingDialog.show();
            if(Const.LOCAL.equalsIgnoreCase(userName) && Const.LOCALPWD.equals(pwd)){
                loadingDialog.dismiss();
                jump2Activity(LoginActivity.this,SettingActivity.class,null,false);
            }else{
                if("0".equals(flag)){
                    officeUrl = "http://" + SPUtils.getString(LoginActivity.this, Const.SERVICE_IP, "",Const.SP_OFFICE) + ":" + SPUtils.getString(LoginActivity.this, Const.SERVICE_PORT, "",Const.SP_OFFICE) + Const.SERVICE_PAGE1;

                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("userName",userName);
                    map.put("passWord",pwd);
                    HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_LOGIN, map, new IWebServiceCallBack() {
                        @Override
                        public void onSucced (SoapObject result) {

                            loadingDialog.dismiss();
                            if(result != null){
                                String string = result.getProperty(0).toString();
                                if(!"404".equals(string)){

                                    UserBean userBean = JsonToBean.getJsonBean(string, UserBean.class);
                                    if(userBean.ds != null || userBean.ds.size() != 0){
                                        SysApplication.assignData(Const.TOKEN, userBean.ds.get(0).token);

                                        SPUtils.saveString(LoginActivity.this, Const.USERID, userBean.ds.get(0).user_id,Const.SP_OFFICE);
                                        SPUtils.saveString(LoginActivity.this, Const.ROLENAME, userBean.ds.get(0).token,Const.SP_OFFICE);
//                                        SysApplication.assignData(Const.USERID, userBean.ds.get(0).user_id);
                                        SysApplication.assignData(Const.STEP, userBean.ds.get(0).Step);
                                    }
                                    jump2Activity(LoginActivity.this, OfficeMainActivity.class,null,false);
                                }else{
                                    ToastUtils.showToast(LoginActivity.this, "用户名或者密码错误");
                                }
                            }else{
                                ToastUtils.showToast(LoginActivity.this, "请检查网络连接");
                            }
                        }

                        @Override
                        public void onFailure (String result) {
                            ToastUtils.showToast(LoginActivity.this, "请检查网络连接");
                            loadingDialog.dismiss();
                        }
                    });

                }else if("1".equals(flag)){
                    repairUrl = "http://" + SPUtils.getString(LoginActivity.this, Const.SERVICE_IP, "",Const.SP_REPAIR) + ":" + SPUtils.getString(LoginActivity.this, Const.SERVICE_PORT, "",Const.SP_REPAIR) + Const.SERVICE_PAGE;
                    if(userDao.avaiLogin(userName, MD5Utils.md5Encode(pwd))){

//                        SysApplication.assignData(Const.USERNAME,userName);
                        SPUtils.saveString(LoginActivity.this, Const.USERNAME, userName,Const.SP_REPAIR);
                        SPUtils.saveString(LoginActivity.this, Const.ROLENAME, userDao.getRoleName(userName),Const.SP_REPAIR);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(Const.USERNAME,userName);
                        jump2Activity(LoginActivity.this, RepairMainActivity.class,map,false);
                    } else {
                        loadingDialog.dismiss();
                        ToastUtils.showToast(LoginActivity.this, "账号或者密码错误");
                    }
                }else if("2".equals(flag)){
                    constructUrl = "http://" + SPUtils.getString(LoginActivity.this, Const.SERVICE_IP, "",Const.SP_CONSTRUCT) + ":" + SPUtils.getString(LoginActivity.this, Const.SERVICE_PORT, "",Const.SP_CONSTRUCT) + Const.SERVICE_PAGE2;

                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("user_name",userName);
                    map.put("password",pwd);
                    HttpConn.callService(constructUrl, Const.SERVICE_NAMESPACE, Const.CONSTRUCT_LOGIN, map, new IWebServiceCallBack() {
                        @Override
                        public void onSucced (SoapObject result) {

                            loadingDialog.dismiss();
                            if(result != null){
                                String string = result.getProperty(0).toString();
                                if(!"404".equals(string)){

                                    UserLogBean userBean = JsonToBean.getJsonBean(string, UserLogBean.class);
                                    if(userBean.ds != null || userBean.ds.size() != 0){

                                        SPUtils.saveString(LoginActivity.this, Const.USERID, userBean.ds.get(0).user_id,Const.SP_CONSTRUCT);
                                        LogUtils.d("ckj",userBean.ds.get(0).user_id);
                                        SPUtils.saveString(LoginActivity.this, Const.ROLENAME, userBean.ds.get(0).role_name,Const.SP_CONSTRUCT);
//                                        SysApplication.assignData(Const.USERID, userBean.ds.get(0).user_id);
                                    }
                                    jump2Activity(LoginActivity.this, ConstructMainActivity.class,null,false);
                                }else{
                                    ToastUtils.showToast(LoginActivity.this, "用户名或者密码错误");
                                }
                            }else{
                                ToastUtils.showToast(LoginActivity.this, "请检查网络连接");
                            }
                        }

                        @Override
                        public void onFailure (String result) {
                            ToastUtils.showToast(LoginActivity.this, "请检查网络连接");
                            loadingDialog.dismiss();
                        }
                    });

                }

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
        }else if("2".equals(flag)){
            etLoginName.setText(SPUtils.getString(LoginActivity.this, Const.USERNAME, "",Const.SP_CONSTRUCT));
            etPassword.setText(SPUtils.getString(LoginActivity.this, Const.PWD, "",Const.SP_CONSTRUCT));
        }


        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }

    @Override
    public void destroy () {

    }
}
