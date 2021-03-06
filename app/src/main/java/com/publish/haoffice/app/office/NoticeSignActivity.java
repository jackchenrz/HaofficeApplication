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
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ThreadUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.AddUserBean;
import com.publish.haoffice.api.bean.office.FlowStep;
import com.publish.haoffice.api.bean.office.OfficeUserBean;
import com.publish.haoffice.api.bean.office.SelUserBean;
import com.publish.haoffice.api.bean.office.TestBean;
import com.publish.haoffice.api.utils.DialogUtils;
import com.publish.haoffice.api.utils.StrConUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NoticeSignActivity extends BaseActivity {

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
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;

    @InjectView(R.id.tv_notify)
    TextView tvNotify;
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
    @InjectView(R.id.tv_selusers)
    TextView tv_selusers;




    public static NoticeSignActivity instance = null;
    private String stepNo;
    private String recID;
    private SPUtils spUtils;
    private String officeUrl;
    private List<OfficeUserBean.OfficeUser> temp;
    private String users = "";
    private String ids = "";
    private String _HFSelectUsersValuecoloum = "";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            if(!"".equals(ids)){
                String cha = ids.charAt(ids.length() -1 ) + "";
                if(",".equals(cha)){
                    ids = ids.substring(0, ids.length() -1);
                }
            }

            HashMap<String, Object> map2 = new HashMap<String, Object>();
            map2.put("DocID", recID);
            map2.put("AllUserID", ids);
            HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_NOTICEGETALLUSERSBYPOSTILTYPE, map2 , new IWebServiceCallBack() {

                @Override
                public void onSucced(SoapObject result) {

                    if(result != null){
                        tv_count.setVisibility(View.GONE);
                        ll_show.setVisibility(View.VISIBLE);
                        String string = result.getProperty(0).toString();
                        if(!"404".equals(string)){
                            SelUserBean jsonBean = JsonToBean.getJsonBean(string, SelUserBean.class);
                            _HFSelectUsersValuecoloum  = jsonBean.ds.get(0).AllUserID;
                            users = jsonBean.ds.get(0).AllRealName;
                            if(!"".equals(textSelectUser)){
                                etClass.setText(textSelectUser + "," +users);
                            }else {
                                etClass.setText(users);
                            }
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
    };
    private int[] stepNos;
    private Dialog loadingDialog;
    private String _HFSelectUsersValue = "";
    private String textSelectUser = "";

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
                finishActivity(NoticeSignActivity.this);
            }
        });
        llSel2.setVisibility(View.GONE);
        final Intent intent = getIntent();
        stepNo = intent.getStringExtra("stepNo");
        recID = intent.getStringExtra("recID");
        spUtils = new SPUtils();

        officeUrl = "http://" + spUtils.getString(this, Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(this, Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE1;

        initSelUsers();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("DocID", recID);
        map.put("UserID", spUtils.getString(NoticeSignActivity.this, Const.USERID, "", Const.SP_OFFICE));
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_BINDNOTICECTR, map , new IWebServiceCallBack() {

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
                
                Intent intent1 = new Intent(NoticeSignActivity.this,SelectUserActivity.class);
                intent1.putExtra("recID",recID);
                intent1.putExtra("flagclick",0);
                intent1.putExtra("docorno",0);
                startActivityForResult(intent1,Const.CODE);

            }
        });

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                loadingDialog = DialogUtils.createLoadingDialog(NoticeSignActivity.this, "正在签阅，请稍后...");
                loadingDialog.show();

                String _StepNo =  stepNos[spinner_next.getSelectedItemPosition()] + "";
                String _PostilDesc = etText.getText().toString().trim();
                String _txtIsHuiQianSelectedValue = "";
                if(rb_sign.isChecked()){
                    _txtIsHuiQianSelectedValue = "签发";
                }else if(rb_sign1.isChecked()){
                    _txtIsHuiQianSelectedValue = "会签";
                }else{
                    loadingDialog.dismiss();
                    ToastUtils.showToast(NoticeSignActivity.this,"请选择签阅状态");
                    return;
                }
                String _btnSaveText = btn_sign.getText().toString().trim();
                if(llText.getVisibility() == View.VISIBLE &&"".equals(_PostilDesc)){
                    loadingDialog.dismiss();
                    ToastUtils.showToast(NoticeSignActivity.this,"请填写审批意见");
                    return;
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("DocID", recID);
                map.put("UserID", spUtils.getString(NoticeSignActivity.this, Const.USERID, "", Const.SP_OFFICE));
                map.put("txtStepNoSelectedValue", _StepNo);
                map.put("txtPostilDescText", _PostilDesc);
                map.put("HFSelectUsersValue", _HFSelectUsersValuecoloum);
                map.put("btnSaveText", _btnSaveText);
                HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_SAVENOTICECTR, map , new IWebServiceCallBack() {

                    @Override
                    public void onSucced(SoapObject result) {

                        if(result != null){
                            loadingDialog.dismiss();
                            String string = result.getProperty(0).toString();
                            if(!"".equals(string)){
                                ToastUtils.showToast(NoticeSignActivity.this,"签阅成功");
                                jump2Activity(NoticeSignActivity.this,OfficeMainActivity.class,null,true);
                                NoticeSignActivity.this.finish();
                                NoticeDetailActivity.instance.finish();
                                OfficeMainActivity.instance.finish();
                            }
                        }else{
                            loadingDialog.dismiss();
                            ToastUtils.showToast(NoticeSignActivity.this,"签阅失败");
                        }
                    }

                    @Override
                    public void onFailure(String result) {
                        loadingDialog.dismiss();
                        ToastUtils.showToast(NoticeSignActivity.this,"签阅失败");
                    }
                });
            }
        });


        btn_lesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent1 = new Intent(NoticeSignActivity.this,AddUserActivity.class);
                intent1.putExtra("recID",recID);
                intent1.putExtra("flagclick",1);
                intent1.putExtra("flagNorO",1);
                startActivity(intent1);
            }
        });

    }

    private void initSelUsers () {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("DocID", recID);
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_ADDUSERNOTICE, map , new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {

                if(result != null){
                    tv_count.setVisibility(View.GONE);
                    ll_show.setVisibility(View.VISIBLE);
                    String string = result.getProperty(0).toString();
                    if(!"".equals(string)){

                        AddUserBean jsonBean = JsonToBean.getJsonBean(string, AddUserBean.class);
                        tv_selusers.setText("已选择同级签阅人员：" + jsonBean.ds.get(0).txtSelectUsers);
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
                            if(!StrConUtils.StrCon(textSelectUser,temp.get(i).real_name) && !StrConUtils.StrCon(_HFSelectUsersValue,temp.get(i).user_id)){
                                if(i == temp.size() -1){
                                    users +=  temp.get(i).real_name;
                                    ids += "'" + temp.get(i).user_id + "'";

                                }else{
                                    users +=  temp.get(i).real_name + ",";
                                    ids +=  "'" + temp.get(i).user_id + "'" + ",";
                                }
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
                case "txtSelectUsers":
                    switch (testBean.Attributes){
                        case "Text":
                            textSelectUser = testBean.Value;
                            etClass.setText(testBean.Value);
                            break;
                    }
                    break;
                case "HFSelectUsers":

                    switch (testBean.Attributes){
                        case "Value":
                            _HFSelectUsersValue = testBean.Value;
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

                    adapter_steps = new ArrayAdapter<String>(NoticeSignActivity.this,R.layout.simple_spinner_item,steps);
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
                            tv_selusers.setVisibility(View.GONE);
                            iv_line_shenhe.setVisibility(View.GONE);
                            break;
                        case "true":
                            btn_lesign.setVisibility(View.VISIBLE);
                            tv_selusers.setVisibility(View.VISIBLE);
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
                            btn_sign.setClickable(false);
                            break;
                        case "Text":
                            btn_sign.setText(testBean.Value);
                            break;
                    }
                    break;

                case "txtError":

                    switch (testBean.Attributes){
                        case "Text":
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
