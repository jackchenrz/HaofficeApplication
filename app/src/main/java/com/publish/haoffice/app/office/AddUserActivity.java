package com.publish.haoffice.app.office;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.publish.haoffice.api.bean.office.OfficeUserBean;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddUserActivity extends BaseActivity {

    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.ll_show)
    LinearLayout ll_show;
    @InjectView(R.id.tv_count)
    TextView tv_count;
    @InjectView(R.id.tv_rectitle)
    TextView tv_rectitle;
    @InjectView(R.id.tv_step)
    TextView tv_step;
    @InjectView(R.id.etText)
    EditText etText;
    @InjectView(R.id.btn_sel)
    Button btn_sel;
    @InjectView(R.id.btn_save)
    Button btn_save;
    @InjectView(R.id.btn_back)
    Button btn_back;

    private String recID;
    private SPUtils spUtils;
    private String officeUrl;
    private List<AddUserBean.AddUser> ds;
    private int flagclick;
    private String users;
    private String ids;
    private List<OfficeUserBean.OfficeUser> temp;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);

            etText.setText(users);
        }
    };
    private int flagNorO;

    @Override
    public int bindLayout () {
        return R.layout.activity_docadd_user;
    }

    @Override
    public void initView (View view) {

        ButterKnife.inject(this);
        tv_title.setText("增加同级人员");
        tv_count.setVisibility(View.VISIBLE);
        ll_show.setVisibility(View.GONE);
        tv_count.setText("正在加载中");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(AddUserActivity.this);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(AddUserActivity.this);
            }
        });

    }

    @Override
    public void doBusiness (Context mContext) {


        final Intent intent = getIntent();
        recID = intent.getStringExtra("recID");
        flagclick = intent.getIntExtra("flagclick",1);
        flagNorO = intent.getIntExtra("flagNorO",0);
        spUtils = new SPUtils();

        officeUrl = "http://" + spUtils.getString(this, Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(this, Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE1;

        if(flagNorO == 0){
            getAddUser(Const.OFFIC_ADDUSERDOC);
        }else if(flagNorO == 1){
            getAddUser(Const.OFFIC_ADDUSERNOTICE);
        }


    }

    private void getAddUser (String methodName) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("DocID", recID);
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, methodName, map , new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {

                if(result != null){
                    tv_count.setVisibility(View.GONE);
                    ll_show.setVisibility(View.VISIBLE);
                    String string = result.getProperty(0).toString();
                    if(!"".equals(string)){

                        AddUserBean jsonBean = JsonToBean.getJsonBean(string, AddUserBean.class);
                        ds = jsonBean.ds;

                        init();
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

    private void init () {
        tv_rectitle.setText("标        题："+ ds.get(0).txtTitle);
        tv_step.setText("当前环节："+ ds.get(0).txtCurrentStepNo);
        etText.setText(ds.get(0).txtSelectUsers);

        btn_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent1 = new Intent(AddUserActivity.this,SelectUserActivity.class);
                intent1.putExtra("recID",recID);
                intent1.putExtra("flagclick",flagclick);
                startActivityForResult(intent1,Const.CODE);
            }
        });

        ids = ds.get(0).HFSelectUsers;

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                LogUtils.d("ckj",ids);
                if(flagNorO == 0){
                    SaveAddUser(Const.OFFIC_SAVEADDUSERDOC);
                }else if(flagNorO == 1){
                    SaveAddUser(Const.OFFIC_ADDUSERNOTICE);
                }

            }
        });
    }

    private void SaveAddUser (String methodName) {
        HashMap<String,  Object> map = new HashMap<String, Object>();
        map.put("DocID", recID);
        map.put("HFSelectUsersValue", ids);
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, methodName, map , new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {

                if(result != null){
                    AddUserActivity.this.finish();
                    OfficSignActivity.instance.finish();
                    OfficDetailActivity.instance.finish();
                    ToastUtils.showToast(AddUserActivity.this,"添加成功");
                    HashMap<String,  String> map = new HashMap<String, String>();
                    map.put("RecID",recID);
                    jump2Activity(AddUserActivity.this,OfficDetailActivity.class,map,true);

                }else{
                    ToastUtils.showToast(AddUserActivity.this,"添加失败");
                }
            }

            @Override
            public void onFailure(String result) {
                ToastUtils.showToast(AddUserActivity.this,"添加失败");
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            if (resultCode == Const.CODE) {
                users = "";
                ids = ds.get(0).HFSelectUsers;

                temp = (List<OfficeUserBean.OfficeUser>) data.getSerializableExtra("temp");
                ThreadUtils.runInBackground(new Runnable() {
                    @Override
                    public void run () {
                        String[] split = ids.split(",");

                        for (int i = 0; i < temp.size(); i++) {

                            if(i == temp.size() -1){
                                users +=  temp.get(i).real_name;
                                ids +=  ",'" + temp.get(i).user_id + "'";
                            }else{
                                users +=  temp.get(i).real_name + ",";
                                ids += "," +  "'" + temp.get(i).user_id + "'";
                            }
                        }

                        handler.sendEmptyMessage(100);
                    }
                });
            }

        }
    }

    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }
}
