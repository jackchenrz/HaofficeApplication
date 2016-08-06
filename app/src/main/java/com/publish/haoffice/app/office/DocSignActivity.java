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
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ThreadUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.FlowStep;
import com.publish.haoffice.api.bean.office.OfficeUserBean;
import com.publish.haoffice.api.bean.office.SelUserBean;
import com.publish.haoffice.api.bean.office.TestBean;
import com.publish.haoffice.api.utils.DialogUtils;
import com.publish.haoffice.api.utils.StrConUtils;

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
    @InjectView(R.id.tv_selusers)
    TextView tv_selusers;



    public static DocSignActivity instance = null;
    private String stepNo;
    private String recID;
    private SPUtils spUtils;
    private String officeUrl;
    private List<OfficeUserBean.OfficeUser> temp;
    private String users = "";
    private String ids = "";
    private List<OfficeUserBean.OfficeUser> temp1;
    private String users1 = "";
    private String ids1 = "";
    private List<OfficeUserBean.OfficeUser> temp2;
    private String users2 = "";
    private String ids2 = "";

    private List<OfficeUserBean.OfficeUser> temp3;
    private String users3 = "";
    private String ids3 = "";

    private List<OfficeUserBean.OfficeUser> temp4;
    private String users4 = "";
    private String ids4 = "";

    private String trIsSelectUsers_Style_display = "block";
    private String tr_zb_Style_display = "block";
    private boolean trblfsVisible = false;
    private boolean tr_blVisible = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Const.CODE:

                    if(!"".equals(ids)){
                        String cha = ids.charAt(ids.length() -1 ) + "";
                        if(",".equals(cha)){
                            ids = ids.substring(0, ids.length() -1);
                        }
                    }
                    HashMap<String, Object> map4 = new HashMap<String, Object>();
                    map4.put("DocID", recID);
                    map4.put("PostilUserType", "0");
                    map4.put("AllUserID", ids);
                    HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETALLUSERSBYPOSTILTYPE, map4 , new IWebServiceCallBack() {

                        @Override
                        public void onSucced(SoapObject result) {

                            if(result != null){
                                tv_count.setVisibility(View.GONE);
                                ll_show.setVisibility(View.VISIBLE);
                                String string = result.getProperty(0).toString();
                                if(!"404".equals(string)){
                                    SelUserBean jsonBean = JsonToBean.getJsonBean(string, SelUserBean.class);
                                    ids = jsonBean.ds.get(0).AllUserID;
                                    et_SelectUsers.setHint(ids);
                                    users = jsonBean.ds.get(0).AllRealName;
                                    if(!"".equals(txtSelectUsers) && !"".equals(users)){
                                        et_SelectUsers.setText(txtSelectUsers + "," + users);
                                    }else if("".equals(txtSelectUsers)){
                                        et_SelectUsers.setText(users);
                                    }else if(!"".equals(txtSelectUsers) && "".equals(users)){
                                        et_SelectUsers.setText(txtSelectUsers);
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
                    break;
                case Const.CODE1:

                    if(!"".equals(ids1)){
                        String cha = ids1.charAt(ids1.length() -1 ) + "";
                        if(",".equals(cha)){
                            ids1 = ids1.substring(0, ids1.length() -1);
                        }
                    }
                    HashMap<String, Object> map3 = new HashMap<String, Object>();
                    map3.put("DocID", recID);
                    map3.put("PostilUserType", "1");
                    map3.put("AllUserID", ids1);
                    HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETALLUSERSBYPOSTILTYPE, map3 , new IWebServiceCallBack() {

                        @Override
                        public void onSucced(SoapObject result) {

                            if(result != null){
                                tv_count.setVisibility(View.GONE);
                                ll_show.setVisibility(View.VISIBLE);
                                String string = result.getProperty(0).toString();
                                if(!"404".equals(string)){
                                    SelUserBean jsonBean = JsonToBean.getJsonBean(string, SelUserBean.class);
                                    ids1 = jsonBean.ds.get(0).AllUserID;
                                    et_ZBSelect.setHint(ids1);
                                    users1 = jsonBean.ds.get(0).AllRealName;
                                    if(!"".equals(txtZBUsers) && !"".equals(users1)){
                                        et_ZBSelect.setText(txtZBUsers + "," + users1);
                                    }else if("".equals(txtZBUsers)){
                                        et_ZBSelect.setText(users1);
                                    }else if(!"".equals(txtZBUsers) && "".equals(users1)){
                                        et_ZBSelect.setText(txtZBUsers);
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
                    break;
                case Const.CODE2:

                    if(!"".equals(ids2)){
                        String cha = ids2.charAt(ids2.length() -1 ) + "";
                        if(",".equals(cha)){
                            ids2 = ids2.substring(0, ids2.length() -1);
                        }
                    }
                    HashMap<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("DocID", recID);
                    map1.put("PostilUserType", "2");
                    map1.put("AllUserID", ids2);
                    HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETALLUSERSBYPOSTILTYPE, map1 , new IWebServiceCallBack() {

                        @Override
                        public void onSucced(SoapObject result) {

                            if(result != null){
                                tv_count.setVisibility(View.GONE);
                                ll_show.setVisibility(View.VISIBLE);
                                String string = result.getProperty(0).toString();
                                if(!"404".equals(string)){
                                    SelUserBean jsonBean = JsonToBean.getJsonBean(string, SelUserBean.class);
                                    ids2 = jsonBean.ds.get(0).AllUserID;
                                    et_XBSelect.setHint(ids2);
                                    users2 = jsonBean.ds.get(0).AllRealName;
                                    if(!"".equals(txtXBUsers) && !"".equals(users2)){
                                        et_XBSelect.setText(txtXBUsers + "," + users2);
                                    }else if("".equals(txtXBUsers)){
                                        et_XBSelect.setText(users2);
                                    }else if(!"".equals(txtXBUsers) && "".equals(users2)){
                                        et_XBSelect.setText(txtXBUsers);
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
                    break;
                case Const.CODE3:


                    if(!"".equals(ids3)){
                        String cha = ids3.charAt(ids3.length() -1 ) + "";
                        if(",".equals(cha)){
                            ids3 = ids3.substring(0, ids3.length() -1);
                        }
                    }

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("DocID", recID);
                    map.put("PostilUserType", "3");
                    map.put("AllUserID", ids3);
                    HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETALLUSERSBYPOSTILTYPE, map , new IWebServiceCallBack() {

                        @Override
                        public void onSucced(SoapObject result) {

                            if(result != null){
                                tv_count.setVisibility(View.GONE);
                                ll_show.setVisibility(View.VISIBLE);
                                String string = result.getProperty(0).toString();
                                if(!"404".equals(string)){
                                    SelUserBean jsonBean = JsonToBean.getJsonBean(string, SelUserBean.class);
                                    ids3 = jsonBean.ds.get(0).AllUserID;
                                    et_BLSelect.setHint(ids3);
                                    users3 = jsonBean.ds.get(0).AllRealName;
                                    if(!"".equals(txtBLUsers) && !"".equals(users3)){
                                        et_BLSelect.setText(txtBLUsers + "," + users3);
                                    }else if("".equals(txtBLUsers)){
                                        et_BLSelect.setText(users3);
                                    }else if(!"".equals(txtBLUsers) && "".equals(users3)){
                                        et_BLSelect.setText(txtBLUsers);
                                    }

                                    if(bt_sel5.getVisibility() == View.GONE){
                                        HashMap<String, Object> map = new HashMap<String, Object>();
                                        map.put("DocID", recID);
                                        map.put("PostilUserType", "4");
                                        map.put("AllUserID", ids3);
                                        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETALLREADUSERSBYPOSTILTYPE, map , new IWebServiceCallBack() {

                                            @Override
                                            public void onSucced(SoapObject result) {

                                                if(result != null){
                                                    tv_count.setVisibility(View.GONE);
                                                    ll_show.setVisibility(View.VISIBLE);
                                                    String string = result.getProperty(0).toString();
                                                    if(!"404".equals(string)){
                                                        SelUserBean jsonBean = JsonToBean.getJsonBean(string, SelUserBean.class);
                                                        ids4 = jsonBean.ds.get(0).AllUserID;
                                                        et_PYSelect.setHint(ids4);
                                                        users4 = jsonBean.ds.get(0).AllRealName;
                                                        et_PYSelect.setText( users4);
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
//                    if(!"".equals(ids3)){
//                        String cha3 =  ids3.charAt( ids3.length() -1 ) + "";
//                        if(",".equals(cha3)){
//                            et_BLSelect.setHint(ids3.substring(0, ids3.length() -1));
//                        }else{
//                            et_BLSelect.setHint(ids3);
//                        }
//
//                    }else{
//                        et_BLSelect.setHint(ids3);
//                    }
                    break;
                case Const.CODE4:

                    if(!"".equals(ids4)){
                        String cha = ids4.charAt(ids4.length() -1 ) + "";
                        if(",".equals(cha)){
                            ids4 = ids4.substring(0, ids4.length() -1);
                        }
                    }
                    HashMap<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("DocID", recID);
                    map2.put("PostilUserType", "4");
                    map2.put("AllUserID", ids4);
                    HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETALLUSERSBYPOSTILTYPE, map2 , new IWebServiceCallBack() {

                        @Override
                        public void onSucced(SoapObject result) {

                            if(result != null){
                                tv_count.setVisibility(View.GONE);
                                ll_show.setVisibility(View.VISIBLE);
                                String string = result.getProperty(0).toString();
                                if(!"404".equals(string)){
                                    SelUserBean jsonBean = JsonToBean.getJsonBean(string, SelUserBean.class);
                                    ids4 = jsonBean.ds.get(0).AllUserID;
                                    et_PYSelect.setHint(ids4);
                                    users4 = jsonBean.ds.get(0).AllRealName;
                                    if(!"".equals(txtPYUsers) && !"".equals(users4)){
                                        et_PYSelect.setText(txtPYUsers + "," + users4);
                                    }else if("".equals(txtPYUsers)){
                                        et_PYSelect.setText(users4);
                                    }else if(!"".equals(txtPYUsers) && "".equals(users4)){
                                        et_PYSelect.setText(txtPYUsers);
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
                    break;
            }


        }
    };

    private String txtSelectUsers = "";
    private String HFSelectUsers = "";
    private String txtXBUsers = "";
    private String HFXBSelectUsers = "";
    private String HFZBSelectUsers = "";
    private String txtZBUsers = "";
    private String HFBLSelectUsers = "";
    private String txtBLUsers = "";
    private String txtPYUsers = "";
    private String HFPYSelectUsers = "";

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
        initSeluser();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("DocID", recID);
        String  userid = spUtils.getString(DocSignActivity.this, Const.USERID, "", Const.SP_OFFICE);
        map.put("UserID", spUtils.getString(DocSignActivity.this, Const.USERID, "", Const.SP_OFFICE));
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_BINDRECDOCCTR, map , new IWebServiceCallBack() {

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


        bt_sel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                Intent intent1 = new Intent(DocSignActivity.this,SelectUserActivity.class);
                intent1.putExtra("recID",recID);
                intent1.putExtra("flagclick",0);
                intent1.putExtra("docorno",1);
                startActivityForResult(intent1,Const.CODE);

            }
        });

        bt_sel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                Intent intent1 = new Intent(DocSignActivity.this,SelectUserActivity.class);
                intent1.putExtra("recID",recID);
                intent1.putExtra("flagclick",0);
                intent1.putExtra("docorno",2);
                startActivityForResult(intent1,Const.CODE1);

            }
        });

        bt_sel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                Intent intent1 = new Intent(DocSignActivity.this,SelectUserActivity.class);
                intent1.putExtra("recID",recID);
                intent1.putExtra("flagclick",0);
                intent1.putExtra("docorno",3);
                startActivityForResult(intent1,Const.CODE2);

            }
        });

        bt_sel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                Intent intent1 = new Intent(DocSignActivity.this,SelectUserActivity.class);
                intent1.putExtra("recID",recID);
                intent1.putExtra("flagclick",0);
                intent1.putExtra("flagchejian",1);
                intent1.putExtra("docorno",4);
                startActivityForResult(intent1,Const.CODE3);

            }
        });

        bt_sel5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                Intent intent1 = new Intent(DocSignActivity.this,SelectUserActivity.class);
                intent1.putExtra("recID",recID);
                intent1.putExtra("flagclick",0);
                intent1.putExtra("flagchejian",2);
                intent1.putExtra("docorno",5);
                startActivityForResult(intent1,Const.CODE4);

            }
        });


        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                loadingDialog = DialogUtils.createLoadingDialog(DocSignActivity.this, "正在签阅，请稍后...");
                loadingDialog.show();

                String _StepNo =  stepNos[spinner_next.getSelectedItemPosition()] + "";
                String _PostilDesc = etText.getText().toString().trim();
                if(llText.getVisibility() == View.VISIBLE && "".equals(_PostilDesc)){
                    loadingDialog.dismiss();
                    ToastUtils.showToast(DocSignActivity.this,"请填写审批意见");
                    return;
                }
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("DocID", recID);
                map.put("UserID", spUtils.getString(DocSignActivity.this, Const.USERID, "", Const.SP_OFFICE));
                map.put("txtStepNoSelectedValue", _StepNo);
                map.put("txtPostilDescText", _PostilDesc);

                map.put("trIsSelectUsers_Style_display", trIsSelectUsers_Style_display);
                CharSequence hint = et_SelectUsers.getHint();
                String HFSelectUsersValue = "";
                if(hint != null){
                    HFSelectUsersValue = hint.toString().trim();
                }
                map.put("HFSelectUsersValue", HFSelectUsersValue);

                map.put("trblfsVisible", trblfsVisible);
                if(trblfsVisible){
                    map.put("HFblfsValue", types[spinner_sign_type.getSelectedItemPosition()]);
                }else{
                    map.put("HFblfsValue", "");
                }


                map.put("tr_zb_Style_display", tr_zb_Style_display);
                CharSequence hint1 = et_ZBSelect.getHint();
                String HFZBSelectUsersValue = "";
                if(hint1 != null){
                    HFZBSelectUsersValue = hint1.toString().trim();
                }
                map.put("HFZBSelectUsersValue",HFZBSelectUsersValue);

                map.put("tr_blVisible", tr_blVisible);
                CharSequence hint2 = et_BLSelect.getHint();
                String HFBLSelectUsersValue = "";
                if(hint2 != null){
                    HFBLSelectUsersValue = hint2.toString().trim();
                }
                map.put("HFBLSelectUsersValue", HFBLSelectUsersValue);

                CharSequence hint3 = et_XBSelect.getHint();
                String HFXBSelectUsersValue = "";
                if(hint3 != null){
                    HFXBSelectUsersValue = hint3.toString().trim();
                }
                map.put("HFXBSelectUsersValue",HFXBSelectUsersValue);

                CharSequence hint4 = et_PYSelect.getHint();
                String HFPYSelectUsersValue = "";
                if(hint4 != null){
                    HFPYSelectUsersValue = hint4.toString().trim();
                }
                map.put("HFPYSelectUsersValue", HFPYSelectUsersValue);
                map.put("lblBLUsersText", tv_sel4.getText().toString().trim());
                HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_SAVEBINDRECDOCCTR, map , new IWebServiceCallBack() {

                    @Override
                    public void onSucced(SoapObject result) {

                        if(result != null){
                            loadingDialog.dismiss();
                            String string = result.getProperty(0).toString();
                            if(!"".equals(string)){
                                ToastUtils.showToast(DocSignActivity.this,"签阅成功");
                                jump2Activity(DocSignActivity.this,OfficeMainActivity.class,null,true);
                                DocSignActivity.this.finish();
                                DocDetailActivity.instance.finish();
                                OfficeMainActivity.instance.finish();
                            }
                        }else{
                            loadingDialog.dismiss();
                            ToastUtils.showToast(DocSignActivity.this,"签阅失败");
                        }
                    }

                    @Override
                    public void onFailure(String result) {
                        loadingDialog.dismiss();
                        ToastUtils.showToast(DocSignActivity.this,"签阅失败");
                    }
                });
            }
        });


        btn_lesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent1 = new Intent(DocSignActivity.this,DocAddUserActivity.class);
                intent1.putExtra("recID",recID);
                intent1.putExtra("flagclick",1);
                intent1.putExtra("flagNorO",0);
                startActivity(intent1);
            }
        });
    }

    private void initSeluser () {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("DocID", recID);
        map.put("UserID", spUtils.getString(DocSignActivity.this, Const.USERID, "", Const.SP_OFFICE));
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_ADDUSERRECDOC, map , new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {

                if(result != null){
                    tv_count.setVisibility(View.GONE);
                    ll_show.setVisibility(View.VISIBLE);
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        List<TestBean> list  = new Gson().fromJson(string, new TypeToken<List<TestBean>>() {
                        }.getType());
                        String str = "";
                        for (TestBean testBean:list) {
                            switch (testBean.Name){
                                case "txtXBUsers":
                                    switch (testBean.Attributes){
                                        case "Text":
                                            str += testBean.Value + ",";
                                            break;
                                    }
                                    break;
                                case "txtZBUsers":

                                    switch (testBean.Attributes){
                                        case "Text":
                                            str += testBean.Value + ",";
                                            break;
                                    }
                                    break;
                                case "txtBLUsers":

                                    switch (testBean.Attributes){
                                        case "Text":
                                            str += testBean.Value + ",";
                                            break;
                                    }
                                    break;
                                case "txtPYUsers":

                                    switch (testBean.Attributes){
                                        case "Text":
                                            str += testBean.Value + ",";
                                            break;
                                    }
                                    break;
                                case "txtSelectUsers":
                                    switch (testBean.Attributes){
                                        case "Text":
                                            str += testBean.Value + ",";
                                            break;
                                    }
                                    break;
                            }

                            tv_selusers.setText("已选择同级签阅人员：" + str);
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
                            if(!StrConUtils.StrCon(txtSelectUsers,temp.get(i).real_name) && !StrConUtils.StrCon(HFSelectUsers,temp.get(i).user_id)){
                                if(i == temp.size() -1){
                                    users +=  temp.get(i).real_name;
                                    ids += "'" + temp.get(i).user_id + "'";

                                }else{
                                    users +=  temp.get(i).real_name + ",";
                                    ids +=  "'" + temp.get(i).user_id + "'" + ",";
                                }
                            }
                        }

                        handler.sendEmptyMessage(Const.CODE);
                    }
                });
            }else if (resultCode == Const.CODE1) {
                users1 = "";
                ids1 = "";
                temp1 = (List<OfficeUserBean.OfficeUser>) data.getSerializableExtra("temp");
                ThreadUtils.runInBackground(new Runnable() {
                    @Override
                    public void run () {


                        for (int i = 0; i < temp1.size(); i++) {
                            if(!StrConUtils.StrCon(txtZBUsers,temp1.get(i).real_name) && !StrConUtils.StrCon(HFZBSelectUsers,temp1.get(i).user_id)){
                                if(i == temp1.size() -1){
                                    users1 +=  temp1.get(i).real_name;
                                    ids1 += "'" + temp1.get(i).user_id + "'";

                                }else{
                                    users1 +=  temp1.get(i).real_name + ",";
                                    ids1 +=  "'" + temp1.get(i).user_id + "'" + ",";
                                }
                            }
                        }

                        handler.sendEmptyMessage(Const.CODE1);
                    }
                });
            }else if (resultCode == Const.CODE2) {
                users2 = "";
                ids2 = "";
                temp2 = (List<OfficeUserBean.OfficeUser>) data.getSerializableExtra("temp");
                ThreadUtils.runInBackground(new Runnable() {
                    @Override
                    public void run () {


                        for (int i = 0; i < temp2.size(); i++) {
                            if(!StrConUtils.StrCon(txtXBUsers,temp2.get(i).real_name) && !StrConUtils.StrCon(HFXBSelectUsers,temp2.get(i).user_id)){
                                if(i == temp2.size() -1){
                                    users2 +=  temp2.get(i).real_name;
                                    ids2 += "'" + temp2.get(i).user_id + "'";

                                }else{
                                    users2 +=  temp2.get(i).real_name + ",";
                                    ids2 +=  "'" + temp2.get(i).user_id + "'" + ",";
                                }
                            }
                        }

                        handler.sendEmptyMessage(Const.CODE2);
                    }
                });
            }else if (resultCode == Const.CODE3) {
                users3 = "";
                ids3 = "";
                temp3 = (List<OfficeUserBean.OfficeUser>) data.getSerializableExtra("temp");
                ThreadUtils.runInBackground(new Runnable() {
                    @Override
                    public void run () {


                        for (int i = 0; i < temp3.size(); i++) {
                            if(!StrConUtils.StrCon(txtBLUsers,temp3.get(i).real_name) && !StrConUtils.StrCon(HFBLSelectUsers,temp3.get(i).user_id)){
                                if(i == temp3.size() -1){
                                    users3 +=  temp3.get(i).real_name;
                                    ids3 += "'" + temp3.get(i).user_id + "'";

                                }else{
                                    users3 +=  temp3.get(i).real_name + ",";
                                    ids3 +=  "'" + temp3.get(i).user_id + "'" + ",";
                                }
                            }
                        }

                        handler.sendEmptyMessage(Const.CODE3);
                    }
                });
            }else if (resultCode == Const.CODE4) {
                users4 = "";
                ids4 = "";
                temp4 = (List<OfficeUserBean.OfficeUser>) data.getSerializableExtra("temp");
                ThreadUtils.runInBackground(new Runnable() {
                    @Override
                    public void run () {


                        for (int i = 0; i < temp4.size(); i++) {
                            if(!StrConUtils.StrCon(txtPYUsers,temp4.get(i).real_name) && !StrConUtils.StrCon(HFPYSelectUsers,temp4.get(i).user_id)){
                                if(i == temp4.size() -1){
                                    users4 +=  temp4.get(i).real_name;
                                    ids4 += "'" + temp4.get(i).user_id + "'";

                                }else{
                                    users4 +=  temp4.get(i).real_name + ",";
                                    ids4 +=  "'" + temp4.get(i).user_id + "'" + ",";
                                }
                            }
                        }

                        handler.sendEmptyMessage(Const.CODE4);
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
                            trblfsVisible = Boolean.parseBoolean(testBean.Value);
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
                            txtXBUsers = testBean.Value;
                            et_XBSelect.setText(testBean.Value);
                            break;
                    }
                    break;
                case "HFXBSelectUsers":

                    switch (testBean.Attributes){
                        case "Value":
                            HFXBSelectUsers = testBean.Value;
                            et_XBSelect.setHint(testBean.Value);
                            break;
                    }
                    break;


                case "tr_zb":
                    switch (testBean.Attributes){
                        case "Style[\"display\"]":
                            tr_zb_Style_display = testBean.Value;
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
                            txtZBUsers = testBean.Value;
                            et_ZBSelect.setText(testBean.Value);
                            break;
                    }
                    break;
                case "HFZBSelectUsers":

                    switch (testBean.Attributes){
                        case "Value":
                            HFZBSelectUsers = testBean.Value;
                            et_ZBSelect.setHint(testBean.Value);
                            break;
                    }
                    break;



                case "tr_bl":
                    switch (testBean.Attributes){
                        case "Visible":
                            tr_blVisible = Boolean.parseBoolean(testBean.Value);
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
                            txtBLUsers = testBean.Value;
                            et_BLSelect.setText(testBean.Value);
                            break;
                    }
                    break;
                case "HFBLSelectUsers":

                    switch (testBean.Attributes){
                        case "Value":
                            HFBLSelectUsers = testBean.Value;
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
                            txtPYUsers = testBean.Value;
                            et_PYSelect.setText(testBean.Value);
                            break;
                    }
                    break;
                case "HFPYSelectUsers":

                    switch (testBean.Attributes){
                        case "Value":
                            HFPYSelectUsers = testBean.Value;
                            et_PYSelect.setHint(testBean.Value);
                            break;
                    }
                    break;

                case "trIsSelectUsers":
                    switch (testBean.Attributes){
                        case "Style[\"display\"]":
                            trIsSelectUsers_Style_display =  testBean.Value;
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

                case "txtSelectUsers":
                    switch (testBean.Attributes){
                        case "Text":
                            txtSelectUsers = testBean.Value;
                            et_SelectUsers.setText(testBean.Value);
                            break;
                    }
                    break;
                case "HFSelectUsers":

                    switch (testBean.Attributes){
                        case "Value":
                            HFSelectUsers = testBean.Value;
                            et_SelectUsers.setHint(testBean.Value);
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
                                    tv_selusers.setVisibility(View.GONE);
                                    tv_selusers.setVisibility(View.GONE);
                                    break;
                                case "true":
                                    btn_lesign.setVisibility(View.VISIBLE);
                                    tv_selusers.setVisibility(View.VISIBLE);
                                    tv_selusers.setVisibility(View.VISIBLE);
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
                            iv_line_shenhe.setVisibility(View.VISIBLE);
                            break;
                        case "false":
                            llText.setVisibility(View.GONE);
                            iv_line_shenhe.setVisibility(View.GONE);
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
                    llSel1.setVisibility(View.VISIBLE);
                    iv_line_sel1.setVisibility(View.VISIBLE);
                    llSel2.setVisibility(View.GONE);
                    iv_line_sel2.setVisibility(View.GONE);
                    llSel3.setVisibility(View.GONE);
                    iv_line_sel3.setVisibility(View.GONE);
                }else if(position == 0){
                    llSel1.setVisibility(View.GONE);
                    iv_line_sel1.setVisibility(View.GONE);
                    llSel2.setVisibility(View.VISIBLE);
                    iv_line_sel2.setVisibility(View.VISIBLE);
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
