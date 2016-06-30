package com.publish.haoffice.app.office;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msystemlib.base.BaseActivity;
import com.msystemlib.common.adapter.CommonAdapter;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.OfficeDeptBean;
import com.publish.haoffice.api.bean.office.OfficeUserBean;
import com.publish.haoffice.api.bean.office.TestBean;
import com.publish.haoffice.api.bean.office.WordBean;
import com.publish.haoffice.api.utils.DensityUtils;
import com.publish.haoffice.application.SysApplication;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/6/22.
 */
public class SelectUserActivity extends BaseActivity {

    @InjectView(R.id.lv_item1)
    ListView lv_item1;
    @InjectView(R.id.lv_item2)
    ListView lv_item2;
    @InjectView(R.id.lv_item3)
    ListView lv_item3;


    @InjectView(R.id.ll_show)
    LinearLayout ll_show;
    @InjectView(R.id.tv_count)
    TextView tv_count;
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;

    @InjectView(R.id.btn_add)
    Button btn_add;
    @InjectView(R.id.btn_addall)
    Button btn_addall;
    @InjectView(R.id.btn_remove)
    Button btn_remove;
    @InjectView(R.id.btn_removeall)
    Button btn_removeall;
    @InjectView(R.id.btn_save)
    Button btn_save;

    private List<OfficeDeptBean.OfficeDept> ds;


    private SPUtils spUtils;
    private String officeUrl;
    private CommonAdapter<OfficeDeptBean.OfficeDept> adapter;
    private List<OfficeUserBean.OfficeUser> ds1;
    private CommonAdapter<OfficeUserBean.OfficeUser> adapter1;
    private List<OfficeUserBean.OfficeUser> temp = new ArrayList<>();
    private CommonAdapter<OfficeUserBean.OfficeUser> adapter3;
    private OfficeUserBean.OfficeUser officeUser;
    private OfficeUserBean.OfficeUser officeUserremove;
    private int positionflag = -1;

    @Override
    public int bindLayout () {
        return R.layout.activity_seluser;
    }

    @Override
    public void initView (View view) {

        ButterKnife.inject(this);
        tv_title.setText("选择人员");
        tv_count.setVisibility(View.VISIBLE);
        ll_show.setVisibility(View.GONE);
        tv_count.setText("正在加载中");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(SelectUserActivity.this);
            }
        });

        lv_item1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                lv_item2.setBackgroundColor(getResources().getColor(R.color.white));
                if("BindSelectUsersEx".equals(ds.get(position).MethodName)){
                    getUser(Const.OFFIC_GETUSERBYDEPTIDEX,position);
                }else if("BindCheJianSelectUsersEx".equals(ds.get(position).MethodName)){
                    getUser(Const.OFFIC_GETUSERBYDEPTIDEX_CHEJIAN,position);
                }

                positionflag =  position;
                initClick1();
            }
        });

        lv_item2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    officeUser = ds1.get(position);
                    initClick();
            }
        });

        lv_item3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                officeUserremove = temp.get(position);
                initClick();
                initClick1();
            }
        });
    }


    /**
     * 得到二级菜单人员
     */
    private void getUser(String methodname,final int position){
        OfficeDeptBean.OfficeDept officeDept = ds.get(position);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("role_id", officeDept.RoleID);
        map.put("user_id", spUtils.getString(SelectUserActivity.this, Const.USERID, "", Const.SP_OFFICE));
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, methodname, map , new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {

                if(result != null){
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        OfficeUserBean jsonBean = JsonToBean.getJsonBean(string, OfficeUserBean.class);
                            ds1 = jsonBean.ds;
                            setOrUpdateAdapter1();

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
     * 填充二级菜单人员
     */
    private void setOrUpdateAdapter1 () {
        adapter1 = new CommonAdapter<OfficeUserBean.OfficeUser>(ds1) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder1 vh;
                if (convertView == null) {
                    view = getLayoutInflater()
                            .inflate(R.layout.list_item_2,
                                    parent, false);
                    vh = new ViewHolder1(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder1) view.getTag();
                }
                OfficeUserBean.OfficeUser OfficeUser = ds1.get(position);
                vh.tv_item2.setText(OfficeUser.real_name);
                vh.tv_item2.setHint(OfficeUser.user_id);
                return view;
            }
        };
        lv_item2.setAdapter(adapter1);
    }


    private void initClick () {

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(!temp.contains(officeUser)){
                    temp.add(officeUser);
                }
                ds1.remove(officeUser);
                setOrUpdateAdapter3();
                setOrUpdateAdapter1();
            }
        });


        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                temp.remove(officeUserremove);
                if(!ds1.contains(officeUserremove)){
                    ds1.add(officeUserremove);
                }
                setOrUpdateAdapter3();
                setOrUpdateAdapter1();
            }
        });
    }

    private void initClick1 () {

        btn_addall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                for (OfficeUserBean.OfficeUser officeUser:ds1) {
                     if(!temp.contains(officeUser)){
                        temp.add(officeUser);
                     }
                }
                setOrUpdateAdapter3();
                ds1.clear();
                setOrUpdateAdapter1();
            }
        });

        btn_removeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                for (OfficeUserBean.OfficeUser officeUser:temp) {
                    if(!ds1.contains(officeUser)){
                        ds1.add(officeUser);
                    }
                }
                temp.clear();
                setOrUpdateAdapter3();
                setOrUpdateAdapter1();
            }
        });

    }



    /**
     * 填充一级菜单部门
     */
    private void setOrUpdateAdapter3 () {
        adapter3 = new CommonAdapter<OfficeUserBean.OfficeUser>(temp) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder3 vh;
                if (convertView == null) {
                    view = getLayoutInflater()
                            .inflate(R.layout.list_item_3,
                                    parent, false);
                    vh = new ViewHolder3(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder3) view.getTag();
                }
                if(temp.size() != 0){
                    OfficeUserBean.OfficeUser OfficeUser = temp.get(position);
                    vh.tv_item3.setText(OfficeUser.real_name);

                }
                return view;
            }
        };
        lv_item3.setAdapter(adapter3);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                Intent intent = new Intent();
                intent.putExtra("temp",(Serializable)temp);
                setResult(Const.CODE,intent);
                finishActivity(SelectUserActivity.this);
            }
        });
    }

    @Override
    public void doBusiness (Context mContext) {

        spUtils = new SPUtils();

        officeUrl = "http://" + spUtils.getString(this, Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(this, Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE1;

        Intent intent = getIntent();
        String recID = intent.getStringExtra("recID");
        int flagclick = intent.getIntExtra("flagclick",0);


        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("DocID", recID);
        map.put("UserID", spUtils.getString(SelectUserActivity.this, Const.USERID, "", Const.SP_OFFICE));

        LogUtils.d("ckj",spUtils.getString(SelectUserActivity.this, Const.USERID, "", Const.SP_OFFICE));
        map.put("IsAddUser", flagclick);

        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_BINDSELECTUSERSEX, map , new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {

                if(result != null){
                    tv_count.setVisibility(View.GONE);
                    ll_show.setVisibility(View.VISIBLE);
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){

                        OfficeDeptBean jsonBean = JsonToBean.getJsonBean(string, OfficeDeptBean.class);
                        ds = jsonBean.ds;
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,ds.size()* DensityUtils.dip2px(SelectUserActivity.this,30),1);
                        lv_item1.setLayoutParams(params);
                        setOrUpdateAdapter();
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
     * 填充一级菜单部门
     */
    private void setOrUpdateAdapter () {
        adapter = new CommonAdapter<OfficeDeptBean.OfficeDept>(ds) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getLayoutInflater()
                            .inflate(R.layout.list_item_1,
                                    parent, false);
                    vh = new ViewHolder(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder) view.getTag();
                }
                OfficeDeptBean.OfficeDept OfficeDept = ds.get(position);
                vh.tv_item1.setText(OfficeDept.Text);
                return view;
            }
        };
        lv_item1.setAdapter(adapter);
    }

    static class ViewHolder {
        @InjectView(R.id.tv_item1)
        TextView tv_item1;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    static class ViewHolder1 {
        @InjectView(R.id.tv_item2)
        TextView tv_item2;

        public ViewHolder1(View view) {
            ButterKnife.inject(this, view);
        }
    }

    static class ViewHolder3 {
        @InjectView(R.id.tv_item3)
        TextView tv_item3;

        public ViewHolder3(View view) {
            ButterKnife.inject(this, view);
        }
    }


    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }
}
