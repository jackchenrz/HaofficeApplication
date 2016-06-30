package com.publish.haoffice.app.office;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
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

import javax.sql.DataSource;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DocSignActivity extends BaseActivity {
    @InjectView(R.id.ll_show)
    LinearLayout ll_show;
    @InjectView(R.id.tv_count)
    TextView tv_count;
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;

    @InjectView(R.id.ll_sign_type)
    LinearLayout ll_sign_type;
    @InjectView(R.id.spinner_sign_type)
    Spinner spinner_sign_type;
    @InjectView(R.id.iv_line_seltype)
    ImageView iv_line_seltype;

    @InjectView(R.id.tv_txtZhuYi)
    TextView tv_txtZhuYi;
    @InjectView(R.id.iv_line_zhuyi)
    ImageView iv_line_zhuyi;

    @InjectView(R.id.tv_txtErrors)
    TextView tv_txtErrors;
    @InjectView(R.id.iv_line_txtErrors)
    ImageView iv_line_txtErrors;

    @InjectView(R.id.tv_txtErrorMsg1)
    TextView tv_txtErrorMsg1;
    @InjectView(R.id.iv_line_txtErrorMsg1)
    ImageView iv_line_txtErrorMsg1;

    @InjectView(R.id.tv_txtErrorMsg)
    TextView tv_txtErrorMsg;
    @InjectView(R.id.iv_line_txtErrorMsg)
    ImageView iv_line_txtErrorMsg;

    @InjectView(R.id.ll_sel1)
    LinearLayout llSel1;
    @InjectView(R.id.tv_sel1)
    TextView tv_sel1;
    @InjectView(R.id.et_SelectUsers)
    EditText et_SelectUsers;
    @InjectView(R.id.bt_sel1)
    Button bt_sel1;
    @InjectView(R.id.iv_line_sel1)
    ImageView iv_line_sel1;

    @InjectView(R.id.ll_sel2)
    LinearLayout llSel2;
    @InjectView(R.id.tv_sel2)
    TextView tv_sel2;
    @InjectView(R.id.et_ZBSelect)
    EditText et_ZBSelect;
    @InjectView(R.id.bt_sel2)
    Button bt_sel2;
    @InjectView(R.id.iv_line_sel2)
    ImageView iv_line_sel2;

    @InjectView(R.id.ll_sel3)
    LinearLayout llSel3;
    @InjectView(R.id.tv_sel3)
    TextView tv_sel3;
    @InjectView(R.id.et_XBSelect)
    EditText et_XBSelect;
    @InjectView(R.id.bt_sel3)
    Button bt_sel3;
    @InjectView(R.id.iv_line_sel3)
    ImageView iv_line_sel3;

    @InjectView(R.id.ll_sel4)
    LinearLayout llSel4;
    @InjectView(R.id.tv_sel4)
    TextView tv_sel4;
    @InjectView(R.id.et_BLSelect)
    EditText et_BLSelect;
    @InjectView(R.id.bt_sel4)
    Button bt_sel4;
    @InjectView(R.id.iv_line_sel4)
    ImageView iv_line_sel4;

    @InjectView(R.id.ll_sel5)
    LinearLayout llSel5;
    @InjectView(R.id.tv_sel5)
    TextView tv_sel5;
    @InjectView(R.id.et_PYSelect)
    EditText et_PYSelect;
    @InjectView(R.id.bt_sel5)
    Button bt_sel5;
    @InjectView(R.id.iv_line_sel5)
    ImageView iv_line_sel5;

    @InjectView(R.id.spinner_next)
    Spinner spinner_next;
    @InjectView(R.id.etText)
    EditText etText;
    @InjectView(R.id.ll_text)
    LinearLayout llText;

    @InjectView(R.id.tv_notify)
    TextView tvNotify;
    @InjectView(R.id.btn_sign)
    Button btn_sign;
    @InjectView(R.id.btn_lesign)
    Button btn_lesign;
    @InjectView(R.id.iv_line_shenhe)
    ImageView iv_line_shenhe;



    public static DocSignActivity instance = null;
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

//            etClass.setText(users);
        }
    };
    private int[] stepNos;
    private Dialog loadingDialog;
    private String[] types;
    private ArrayAdapter<String> adapter_types;

    @Override
    public int bindLayout () {
        return R.layout.activity_docsign;
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
                finishActivity(DocSignActivity.this);
            }
        });
        final Intent intent = getIntent();
        stepNo = intent.getStringExtra("stepNo");
        recID = intent.getStringExtra("recID");
        spUtils = new SPUtils();

        officeUrl = "http://" + spUtils.getString(this, Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(this, Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE1;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("DocID", recID);
        String  userid = spUtils.getString(DocSignActivity.this, Const.USERID, "", Const.SP_OFFICE);
        map.put("UserID", spUtils.getString(DocSignActivity.this, Const.USERID, "", Const.SP_OFFICE));
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_BINDRECDOCCTR, map , new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {
                if(result != null){
                String string = result.getProperty(0).toString();
                    LogUtils.d("ckj",string);
                }
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


//        bt_sel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View v) {
//
//                Intent intent1 = new Intent(DocSignActivity.this,SelectUserActivity.class);
//                intent1.putExtra("recID",recID);
//                intent1.putExtra("flagclick",0);
//                startActivityForResult(intent1,Const.CODE);
//
//            }
//        });

//        btn_sign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View v) {
//
//                loadingDialog = DialogUtils.createLoadingDialog(DocSignActivity.this, "正在签阅，请稍后...");
//                loadingDialog.show();
//
//                String _StepNo =  stepNos[spinner_next.getSelectedItemPosition()] + "";
//                String _PostilDesc = etText.getText().toString().trim();
//                String _HFSelectUsersValue = ids;
//                String _txtIsHuiQianSelectedValue = "";
////                if(rb_sign.isChecked()){
////                    _txtIsHuiQianSelectedValue = "签发";
////                }else if(rb_sign1.isChecked()){
////                    _txtIsHuiQianSelectedValue = "会签";
////                }
//                String _btnSaveText = btn_sign.getText().toString().trim();
//                if(llText.getVisibility() == View.VISIBLE && "".equals(_PostilDesc)){
//                    loadingDialog.dismiss();
//                    ToastUtils.showToast(DocSignActivity.this,"请填写审批意见");
//                    return;
//                }
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("DocID", recID);
//                map.put("UserID", spUtils.getString(DocSignActivity.this, Const.USERID, "", Const.SP_OFFICE));
//                map.put("_StepNo", _StepNo);
//                map.put("_PostilDesc", _PostilDesc);
//                map.put("_HFSelectUsersValue", _HFSelectUsersValue);
//                map.put("_txtIsHuiQianSelectedValue", _txtIsHuiQianSelectedValue);
//                map.put("_btnSaveText", _btnSaveText);
//                HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_SAVESENDDOCCTR, map , new IWebServiceCallBack() {
//
//                    @Override
//                    public void onSucced(SoapObject result) {
//
//                        if(result != null){
//                            loadingDialog.dismiss();
//                            String string = result.getProperty(0).toString();
//                            if(!"".equals(string)){
//                                ToastUtils.showToast(DocSignActivity.this,"签阅成功");
//                                jump2Activity(DocSignActivity.this,OfficeMainActivity.class,null,true);
//                                DocSignActivity.this.finish();
//                                OfficDetailActivity.instance.finish();
//                                OfficeMainActivity.instance.finish();
//                            }
//                        }else{
//                            loadingDialog.dismiss();
//                            ToastUtils.showToast(DocSignActivity.this,"签阅失败");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(String result) {
//                        loadingDialog.dismiss();
//                        ToastUtils.showToast(DocSignActivity.this,"签阅失败");
//                    }
//                });
//            }
//        });
//
//
//        btn_lesign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View v) {
//                Intent intent1 = new Intent(DocSignActivity.this,AddUserActivity.class);
//                intent1.putExtra("recID",recID);
//                intent1.putExtra("flagclick",1);
//                intent1.putExtra("flagNorO",0);
//                startActivity(intent1);
//            }
//        });
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

                case "trblfs":
                    switch (testBean.Attributes){
                        case "Visible":
                            switch (testBean.Value){
                                case "false":
                            ll_sign_type.setVisibility(View.GONE);
                            iv_line_seltype.setVisibility(View.GONE);
                                    break;
                                case "true":
                                    ll_sign_type.setVisibility(View.VISIBLE);
                                    types = new String[]{"逐级签批","直接承办"};
                                    adapter_types = new ArrayAdapter<String>(DocSignActivity.this,R.layout.simple_spinner_item,types);
                                    adapter_types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_sign_type.setAdapter(adapter_types);
                                    iv_line_seltype.setVisibility(View.VISIBLE);
                                    break;
                            }

                            break;
                    }
                    break;

                case "txtZhuYi":
                    switch (testBean.Attributes){
                        case "Visible":
                            switch (testBean.Value){
                                case "false":
                                    tv_txtZhuYi.setVisibility(View.GONE);
                                    iv_line_zhuyi.setVisibility(View.GONE);
                                    break;
                                case "true":
                                    tv_txtZhuYi.setVisibility(View.VISIBLE);
                                    iv_line_zhuyi.setVisibility(View.VISIBLE);
                                    break;
                            }
                            break;
                        case "Text":
                            tv_txtZhuYi.setText(testBean.Value);
                            break;
                    }

                    break;


                case "tr_xb":
                    switch (testBean.Attributes){
                        case "Style[\"display\"]":
                            switch (testBean.Value){
                                case "block":
                                    llSel3.setVisibility(View.VISIBLE);
                                    iv_line_sel3.setVisibility(View.VISIBLE);
                                    break;
                                case "none":
                                    llSel3.setVisibility(View.GONE);
                                    iv_line_sel3.setVisibility(View.GONE);
                                    break;
                            }
                            break;
                    }
                    break;


                case "lblXB":
                    switch (testBean.Attributes){
                        case "Text":
                            tv_sel3.setText(testBean.Value);
                            break;
                    }
                    break;

                case "btnXBSelect":
                    switch (testBean.Attributes){
                        case "Visible":
                            switch (testBean.Value){
                                case "false":
                                    bt_sel3.setVisibility(View.GONE);
                                    break;
                                case "true":
                                    bt_sel3.setVisibility(View.VISIBLE);
                                    break;
                            }
                            break;
                    }
                    break;
                case "txtXBUsers":

                    switch (testBean.Attributes){
                        case "Text":
                            et_XBSelect.setText(testBean.Value);
                            break;
                    }
                    break;
                case "HFXBSelectUsers":

                    switch (testBean.Attributes){
                        case "Value":
                            et_XBSelect.setHint(testBean.Value);
                            break;
                    }
                    break;


                case "tr_zb":
                    switch (testBean.Attributes){
                        case "Style[\"display\"]":
                            switch (testBean.Value){
                                case "block":
                                    llSel2.setVisibility(View.VISIBLE);
                                    iv_line_sel2.setVisibility(View.VISIBLE);
                                    break;
                                case "none":
                                    llSel2.setVisibility(View.GONE);
                                    iv_line_sel2.setVisibility(View.GONE);
                                    break;
                            }
                            break;
                    }
                    break;


                case "lblZB":
                    switch (testBean.Attributes){
                        case "Text":
                            tv_sel2.setText(testBean.Value);
                            break;
                    }
                    break;

                case "btnZBSelect":
                    switch (testBean.Attributes){
                        case "Visible":
                            switch (testBean.Value){
                                case "false":
                                    bt_sel2.setVisibility(View.GONE);
                                    break;
                                case "true":
                                    bt_sel2.setVisibility(View.VISIBLE);
                                    break;
                            }
                            break;
                    }
                    break;
                case "txtZBUsers":

                    switch (testBean.Attributes){
                        case "Text":
                            et_ZBSelect.setText(testBean.Value);
                            break;
                    }
                    break;
                case "HFZBSelectUsers":

                    switch (testBean.Attributes){
                        case "Value":
                            et_ZBSelect.setHint(testBean.Value);
                            break;
                    }
                    break;



                case "tr_bl":
                    switch (testBean.Attributes){
                        case "Visible":
                            switch (testBean.Value){
                                case "true":
                                    llSel4.setVisibility(View.VISIBLE);
                                    iv_line_sel4.setVisibility(View.VISIBLE);
                                    break;
                                case "false":
                                    llSel4.setVisibility(View.GONE);
                                    iv_line_sel4.setVisibility(View.GONE);
                                    break;
                            }
                            break;
                    }
                    break;


                case "lblBLUsers":
                    switch (testBean.Attributes){
                        case "Text":
                            tv_sel4.setText(testBean.Value);
                            break;
                    }
                    break;

                case "btnBLSelect":
                    switch (testBean.Attributes){
                        case "Visible":
                            switch (testBean.Value){
                                case "false":
                                    bt_sel4.setVisibility(View.GONE);
                                    break;
                                case "true":
                                    bt_sel4.setVisibility(View.VISIBLE);
                                    break;
                            }
                            break;
                    }
                    break;

                case "txtBLUsers":

                    switch (testBean.Attributes){
                        case "Text":
                            et_BLSelect.setText(testBean.Value);
                            break;
                    }
                    break;
                case "HFBLSelectUsers":

                    switch (testBean.Attributes){
                        case "Value":
                            et_BLSelect.setHint(testBean.Value);
                            break;
                    }
                    break;

                case "tr_py":
                    switch (testBean.Attributes){
                        case "Visible":
                            switch (testBean.Value){
                                case "true":
                                    llSel5.setVisibility(View.VISIBLE);
                                    iv_line_sel5.setVisibility(View.VISIBLE);
                                    break;
                                case "false":
                                    llSel5.setVisibility(View.GONE);
                                    iv_line_sel5.setVisibility(View.GONE);
                                    break;
                            }
                            break;
                    }
                    break;


                case "lblPYUsers":
                    switch (testBean.Attributes){
                        case "Text":
                            tv_sel5.setText(testBean.Value);
                            break;
                    }
                    break;

                case "btnPYSelect":
                    switch (testBean.Attributes){
                        case "Visible":
                            switch (testBean.Value){
                                case "false":
                                    bt_sel5.setVisibility(View.GONE);
                                    break;
                                case "true":
                                    bt_sel5.setVisibility(View.VISIBLE);
                                    break;
                            }
                            break;
                    }
                    break;

                case "txtPYUsers":

                    switch (testBean.Attributes){
                        case "Text":
                            et_PYSelect.setText(testBean.Value);
                            break;
                    }
                    break;
                case "HFPYSelectUsers":

                    switch (testBean.Attributes){
                        case "Value":
                            et_PYSelect.setHint(testBean.Value);
                            break;
                    }
                    break;

                case "trIsSelectUsers":
                    switch (testBean.Attributes){
                        case "Style[\"display\"]":
                            switch (testBean.Value){
                                case "block":
                                    llSel1.setVisibility(View.VISIBLE);
                                    iv_line_sel1.setVisibility(View.VISIBLE);
                                    break;
                                case "none":
                                    llSel1.setVisibility(View.GONE);
                                    iv_line_sel1.setVisibility(View.GONE);
                                    break;
                            }
                            break;
                    }
                    break;

                case "txtStepNo":
                    switch (testBean.Attributes){
                        case "DataSource":
                            testBean.Value.replaceAll("\\\\","");

                            LogUtils.d("ckj",testBean.Value);
                            List<FlowStep> list1  = new Gson().fromJson(testBean.Value, new TypeToken<List<FlowStep>>() {
                            }.getType());
                            steps = new String[list1.size()];
                            stepNos = new int[list1.size()];
                            for (int i = 0; i < list1.size(); i++) {
                                steps[i] = list1.get(i).StepName;
                                stepNos[i] = list1.get(i).StepNo;
                            }

                            adapter_steps = new ArrayAdapter<String>(DocSignActivity.this,R.layout.simple_spinner_item,steps);
                            adapter_steps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_next.setAdapter(adapter_steps);
                            break;
                        case "Enabled":
                            switch (testBean.Value){
                                case "false":
                                    spinner_next.setEnabled(false);
                                    break;
                                case "true":
                                    spinner_next.setEnabled(true);
                                    break;
                            }
                            break;
                    }
                    break;
                case "btnAddUser":
                    switch (testBean.Attributes){
                        case "Visible":
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
                        case "Enabled":
                            switch (testBean.Value){
                                case "false":
                                    btn_lesign.setEnabled(false);
                                    break;
                                case "true":
                                    btn_lesign.setEnabled(true);
                                    break;
                            }
                            break;
                    }

                    break;

                case "btnSave":
                    switch (testBean.Attributes){
                        case "Visible":
                            switch (testBean.Value){
                                case "false":
                                    btn_sign.setVisibility(View.GONE);
                                    iv_line_shenhe.setVisibility(View.GONE);
                                    break;
                                case "true":
                                    btn_sign.setVisibility(View.VISIBLE);
                                    iv_line_shenhe.setVisibility(View.VISIBLE);
                                    break;
                            }
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
                case "trshenhe":
                    switch (testBean.Value){
                        case "true":
                            llText.setVisibility(View.VISIBLE);
                            break;
                        case "false":
                            llText.setVisibility(View.GONE);
                            break;
                    }
                    break;

                case "txtPostilDesc":
                    switch (testBean.Attributes){
                        case "Enabled":
                            switch (testBean.Value){
                                case "true":
                                    etText.setEnabled(true);
                                    break;
                                case "false":
                                    etText.setEnabled(false);
                                    break;
                            }

                            break;
                    }
                    break;
                case "txtErrors":

                    switch (testBean.Attributes){
                        case "Text":
                            tv_txtErrors.setVisibility(View.VISIBLE);
                            iv_line_txtErrors.setVisibility(View.VISIBLE);
                            tv_txtErrors.setText(testBean.Value);
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
                case "txtErrorMsg":

                    switch (testBean.Attributes){
                        case "Text":
                            tv_txtErrorMsg.setVisibility(View.VISIBLE);
                            iv_line_txtErrorMsg.setVisibility(View.VISIBLE);
                            tv_txtErrorMsg.setText(testBean.Value);
                            break;
                    }
                    break;

                case "txtErrorMsg2":

                    switch (testBean.Attributes){
                        case "Text":
                            tv_txtErrorMsg1.setVisibility(View.VISIBLE);
                            iv_line_txtErrorMsg1.setVisibility(View.VISIBLE);
                            tv_txtErrorMsg1.setText(testBean.Value);
                            break;
                    }
                    break;
            }
        }

        spinner_sign_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    llSel3.setVisibility(View.GONE);
                    iv_line_sel3.setVisibility(View.GONE);
                }else if(position == 0){
                    llSel3.setVisibility(View.VISIBLE);
                    iv_line_sel3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });
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
