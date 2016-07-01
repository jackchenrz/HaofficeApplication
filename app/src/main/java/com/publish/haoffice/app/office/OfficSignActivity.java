package com.publish.haoffice.app.office;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msystemlib.base.BaseActivity;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ThreadUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.FlowStep;
import com.publish.haoffice.api.bean.office.OfficeUserBean;
import com.publish.haoffice.api.bean.office.TestBean;
import com.publish.haoffice.api.utils.DialogUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OfficSignActivity extends BaseActivity {

    @InjectView(R.id.etClass)
    EditText etClass;
    @InjectView(R.id.ll_sel1)
    LinearLayout llSel1;
    @InjectView(R.id.ll_sel2)
    LinearLayout llSel2;
    @InjectView(R.id.rb_sign1)
    RadioButton rb_sign1;
    @InjectView(R.id.rb_sign)
    RadioButton rb_sign;
    @InjectView(R.id.spinner_next)
    Spinner spinner_next;
    @InjectView(R.id.etText)
    EditText etText;
    @InjectView(R.id.ll_text)
    LinearLayout llText;

    @InjectView(R.id.ll_show)
    LinearLayout ll_show;
    @InjectView(R.id.tv_count)
    TextView tv_count;

    @InjectView(R.id.tv_notify)
    TextView tvNotify;
    @InjectView(R.id.iv_line_txtErrors)
    ImageView iv_line_txtErrors;
    @InjectView(R.id.btn_sign)
    Button btn_sign;
    @InjectView(R.id.bt_sel)
    Button bt_sel;
    @InjectView(R.id.btn_lesign)
    Button btn_lesign;
    @InjectView(R.id.iv_line_seluser)
    ImageView ivSeluser;
    @InjectView(R.id.iv_line_radio)
    ImageView ivRadio;
    @InjectView(R.id.iv_line_shenhe)
    ImageView iv_line_shenhe;
    @InjectView(R.id.iv_line_shenhe1)
    ImageView iv_line_shenhe1;

    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;

    public static OfficSignActivity instance = null;
    private String stepNo;
    private String recID;
    private SPUtils spUtils;
    private String officeUrl;
    private List<OfficeUserBean.OfficeUser> temp;
    private String users = "";
    private String ids = "";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);

            etClass.setText(users);
        }
    };
    private int[] stepNos;
    private Dialog loadingDialog;

    @Override
    public int bindLayout () {
        return R.layout.activity_officsign;
    }

    @Override
    public void initView (View view) {


        ButterKnife.inject(this);
        instance = this;
        tv_title.setText("签阅页面");
        tv_count.setVisibility(View.VISIBLE);
        ll_show.setVisibility(View.GONE);
        tv_count.setText("正在加载中");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(OfficSignActivity.this);
            }
        });
        final Intent intent = getIntent();
        stepNo = intent.getStringExtra("stepNo");
        recID = intent.getStringExtra("recID");
        spUtils = new SPUtils();

        officeUrl = "http://" + spUtils.getString(this, Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(this, Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE1;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("DocID", recID);
        map.put("UserID", spUtils.getString(OfficSignActivity.this, Const.USERID, "", Const.SP_OFFICE));
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_BINDSENDDOCCTR, map , new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {

                if(result != null){
                    tv_count.setVisibility(View.GONE);
                    ll_show.setVisibility(View.VISIBLE);
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        List<TestBean> list  = new Gson().fromJson(string, new TypeToken<List<TestBean>>() {
                        }.getType());
                        init(list);
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


        bt_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                
                Intent intent1 = new Intent(OfficSignActivity.this,SelectUserActivity.class);
                intent1.putExtra("recID",recID);
                intent1.putExtra("flagclick",0);
                intent1.putExtra("docorno",0);
                startActivityForResult(intent1,Const.CODE);

            }
        });

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                loadingDialog = DialogUtils.createLoadingDialog(OfficSignActivity.this, "正在签阅，请稍后...");
                loadingDialog.show();

                String _StepNo =  stepNos[spinner_next.getSelectedItemPosition()] + "";
                String _PostilDesc = etText.getText().toString().trim();
                String _HFSelectUsersValue = ids;
                String _txtIsHuiQianSelectedValue = "";
                if(rb_sign.isChecked()){
                    _txtIsHuiQianSelectedValue = "签发";
                }else if(rb_sign1.isChecked()){
                    _txtIsHuiQianSelectedValue = "会签";
                }
                String _btnSaveText = btn_sign.getText().toString().trim();
                if(llText.getVisibility() == View.VISIBLE && "".equals(_PostilDesc)){
                    loadingDialog.dismiss();
                    ToastUtils.showToast(OfficSignActivity.this,"请填写审批意见");
                    return;
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("DocID", recID);
                map.put("UserID", spUtils.getString(OfficSignActivity.this, Const.USERID, "", Const.SP_OFFICE));
                map.put("_StepNo", _StepNo);
                map.put("_PostilDesc", _PostilDesc);
                map.put("_HFSelectUsersValue", _HFSelectUsersValue);
                map.put("_txtIsHuiQianSelectedValue", _txtIsHuiQianSelectedValue);
                map.put("_btnSaveText", _btnSaveText);
                HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_SAVESENDDOCCTR, map , new IWebServiceCallBack() {

                    @Override
                    public void onSucced(SoapObject result) {

                        if(result != null){
                            loadingDialog.dismiss();
                            String string = result.getProperty(0).toString();
                            if(!"".equals(string)){
                                ToastUtils.showToast(OfficSignActivity.this,"签阅成功");
                                jump2Activity(OfficSignActivity.this,OfficeMainActivity.class,null,true);
                                OfficSignActivity.this.finish();
                                OfficDetailActivity.instance.finish();
                                OfficeMainActivity.instance.finish();
                            }
                        }else{
                            loadingDialog.dismiss();
                            ToastUtils.showToast(OfficSignActivity.this,"签阅失败");
                        }
                    }

                    @Override
                    public void onFailure(String result) {
                        loadingDialog.dismiss();
                        ToastUtils.showToast(OfficSignActivity.this,"签阅失败");
                    }
                });
            }
        });


        btn_lesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent1 = new Intent(OfficSignActivity.this,AddUserActivity.class);
                intent1.putExtra("recID",recID);
                intent1.putExtra("flagclick",1);
                intent1.putExtra("flagNorO",0);
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            if (resultCode == Const.CODE) {
                users = "";
                ids = "";
                temp = (List<OfficeUserBean.OfficeUser>) data.getSerializableExtra("temp");
                ThreadUtils.runInBackground(new Runnable() {
                    @Override
                    public void run () {


                        for (int i = 0; i < temp.size(); i++) {

                            if(i == temp.size() -1){
                                users +=  temp.get(i).real_name;
                                ids += "'" + temp.get(i).user_id + "'";

                            }else{
                                users +=  temp.get(i).real_name + ",";
                                ids +=  "'" + temp.get(i).user_id + "'" + ",";
                            }
                        }

                        handler.sendEmptyMessage(100);
                    }
                });
            }

    }
    }

    private String[] steps;

    private int stepPosition;

    private ArrayAdapter<String> adapter_steps;

    /**
     * 初始化控件显示
     * @param list
     */
    private void init (List<TestBean> list) {

        for (TestBean testBean:list) {

            switch (testBean.Name){

                case "trIsSelectUsers":
                    switch (testBean.Value){
                        case "false":
                            llSel1.setVisibility(View.GONE);
                            ivSeluser.setVisibility(View.GONE);
                            break;
                        case "true":
                            llSel1.setVisibility(View.VISIBLE);
                            ivSeluser.setVisibility(View.VISIBLE);
                            break;
                    }
                break;
                case "txtStepNo":
                    testBean.Value.replaceAll("\\\\","");
                    List<FlowStep> list1  = new Gson().fromJson(testBean.Value, new TypeToken<List<FlowStep>>() {
                    }.getType());
                    steps = new String[list1.size()];
                    stepNos = new int[list1.size()];
                    for (int i = 0; i < list1.size(); i++) {
                        steps[i] = list1.get(i).StepName;
                        stepNos[i] = list1.get(i).StepNo;
                    }

                    adapter_steps = new ArrayAdapter<String>(OfficSignActivity.this,R.layout.simple_spinner_item,steps);
                    adapter_steps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_next.setAdapter(adapter_steps);
                    break;
                case "trHuiQian":
                    switch (testBean.Value){
                        case "false":
                            llSel2.setVisibility(View.GONE);
                            ivRadio.setVisibility(View.GONE);
                            break;
                        case "true":
                            llSel2.setVisibility(View.VISIBLE);
                            ivRadio.setVisibility(View.VISIBLE);
                            break;
                    }

                    break;
                case "btnAddUser":
                    switch (testBean.Value){
                        case "false":
                            btn_lesign.setVisibility(View.GONE);
                            iv_line_shenhe.setVisibility(View.GONE);
                            break;
                        case "true":
                            btn_lesign.setVisibility(View.VISIBLE);
                            iv_line_shenhe.setVisibility(View.VISIBLE);
                            break;
                    }
                    break;
                case "txtIsHuiQian":
                    switch (testBean.Value){
                        case "签发":
                            llSel2.setVisibility(View.VISIBLE);
                            rb_sign1.setVisibility(View.GONE);
                            break;
                        default:
                            rb_sign1.setVisibility(View.VISIBLE);
                            break;
                    }
                    break;
                case "trshenhe":
                    switch (testBean.Value){
                        case "true":
                            llText.setVisibility(View.VISIBLE);
                            iv_line_shenhe.setVisibility(View.VISIBLE);
                            iv_line_shenhe1.setVisibility(View.VISIBLE);
                            break;
                        case "false":
                            llText.setVisibility(View.GONE);
                            iv_line_shenhe.setVisibility(View.GONE);
                            iv_line_shenhe1.setVisibility(View.GONE);
                            break;
                    }
                    break;
                case "btnSave":

                    switch (testBean.Attributes){
                        case "Visible":
                            btn_sign.setText(testBean.Value);
                            break;
                        case "Enabled":

                            switch (testBean.Value){
                                case "false":
                                    btn_sign.setEnabled(false);
                                    break;
                                case "true":
                                    btn_sign.setEnabled(true);
                                    break;
                            }
                            break;
                    }
                    break;
                case "txtError":
                    switch (testBean.Attributes){
                        case "Text":
                            tvNotify.setVisibility(View.VISIBLE);
                            iv_line_txtErrors.setVisibility(View.VISIBLE);
                            tvNotify.setText(testBean.Value);
                            break;
                    }
                    break;
            }
        }
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
