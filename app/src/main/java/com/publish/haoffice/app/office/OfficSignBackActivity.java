package com.publish.haoffice.app.office;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msystemlib.base.BaseActivity;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.FlowStep;
import com.publish.haoffice.api.bean.office.SignBackBean;
import com.publish.haoffice.api.bean.office.TestBean;
import com.publish.haoffice.api.utils.DialogUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/6/24.
 */
public class OfficSignBackActivity extends BaseActivity {

    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.ll_show)
    LinearLayout ll_show;
    @InjectView(R.id.tv_count)
    TextView tv_count;


    @InjectView(R.id.ll_text)
    LinearLayout llText;
    @InjectView(R.id.spinner_next)
    Spinner spinner_next;
    @InjectView(R.id.etText)
    EditText etText;
    @InjectView(R.id.btn_save)
    Button btn_save;
    @InjectView(R.id.btn_back)
    Button btn_back;
    private String recID;
    private SPUtils spUtils;
    private String officeUrl;
    private String[] steps;
    private String[] stepNos;
    private ArrayAdapter<String> adapter_steps;
    private Dialog loadingDialog;
    private String flagNorO;
    private String txtBackStepNoSelectedValue;
    private String txtPostilDescText;

    @Override
    public int bindLayout () {
        return R.layout.activity_sign_back;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
        tv_title.setText("回退页面");
        tv_count.setVisibility(View.VISIBLE);
        ll_show.setVisibility(View.GONE);
        tv_count.setText("正在加载中");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(OfficSignBackActivity.this);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(OfficSignBackActivity.this);
            }
        });
        final Intent intent = getIntent();
        recID = intent.getStringExtra("recID");
        flagNorO = intent.getStringExtra("flagNorO");
        spUtils = new SPUtils();

        officeUrl = "http://" + spUtils.getString(this, Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(this, Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE1;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("DocID", recID);
        map.put("UserID", spUtils.getString(OfficSignBackActivity.this, Const.USERID, "", Const.SP_OFFICE));

        if("1".equals(flagNorO)){
            getData(Const.OFFIC_BACKNOTICEDOC);

        }else if("0".equals(flagNorO)){
            getData(Const.OFFIC_BACKSENDDOC);
        }


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                loadingDialog = DialogUtils.createLoadingDialog(OfficSignBackActivity.this, "正在签阅，请稍后...");
                loadingDialog.show();

                txtBackStepNoSelectedValue =  stepNos[spinner_next.getSelectedItemPosition()] + "";
                txtPostilDescText = etText.getText().toString().trim();
//                String _HFSelectUsersValue = ids;
//                String _txtIsHuiQianSelectedValue = "";
//                if(rb_sign.isChecked()){
//                    _txtIsHuiQianSelectedValue = "签发";
//                }else if(rb_sign1.isChecked()){
//                    _txtIsHuiQianSelectedValue = "会签";
//                }
//                String _btnSaveText = btn_sign.getText().toString().trim();
                if("".equals(txtPostilDescText)){
                    loadingDialog.dismiss();
                    ToastUtils.showToast(OfficSignBackActivity.this,"请填写审批意见");
                    return;
                }



                if("1".equals(flagNorO)){
                    uploadData(Const.OFFIC_SAVEBACKNOTICEDOC);

                }else if("0".equals(flagNorO)){
                    uploadData(Const.OFFIC_SAVEBACKSENDDOC);
                }

            }
        });
    }

    private void uploadData (String methodName) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("DocID", recID);
        map.put("UserID", spUtils.getString(OfficSignBackActivity.this, Const.USERID, "", Const.SP_OFFICE));
        map.put("txtBackStepNoSelectedValue", txtBackStepNoSelectedValue);
        map.put("txtPostilDescText", txtPostilDescText);
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, methodName, map , new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {

                if(result != null){
                    loadingDialog.dismiss();
                    String string = result.getProperty(0).toString();
                    if(!"".equals(string)){
                        ToastUtils.showToast(OfficSignBackActivity.this,"回退成功");
                        jump2Activity(OfficSignBackActivity.this,OfficeMainActivity.class,null,true);
                        OfficSignBackActivity.this.finish();
                        OfficDetailActivity.instance.finish();
                        OfficeMainActivity.instance.finish();
                    }
                }else{
                    loadingDialog.dismiss();
                    ToastUtils.showToast(OfficSignBackActivity.this,"回退失败");
                }
            }

            @Override
            public void onFailure(String result) {
                loadingDialog.dismiss();
                ToastUtils.showToast(OfficSignBackActivity.this,"回退失败");
            }
        });
    }


    private void getData (String methodName) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("DocID", recID);
        map.put("UserID", spUtils.getString(OfficSignBackActivity.this, Const.USERID, "", Const.SP_OFFICE));
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, methodName, map , new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {

                if(result != null){
                    tv_count.setVisibility(View.GONE);
                    ll_show.setVisibility(View.VISIBLE);
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        SignBackBean jsonBean = JsonToBean.getJsonBean(string, SignBackBean.class);
                        List<SignBackBean.SignBack> ds = jsonBean.ds;
                        init(ds);
                    }
                }else{
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


    /**
     * 初始化控件显示
     * @param list
     */
    private void init (List<SignBackBean.SignBack> list) {
        steps = new String[list.size()];
        stepNos = new String[list.size()];

        for (int i =0;i<list.size();i++) {
            steps[i] = list.get(i).StepName;
            stepNos[i] = list.get(i).StepNo;
        }

        adapter_steps = new ArrayAdapter<String>(OfficSignBackActivity.this,R.layout.simple_spinner_item,steps);
        adapter_steps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_next.setAdapter(adapter_steps);
    }

    @Override
    public void doBusiness (Context mContext) {

    }

    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }
}
