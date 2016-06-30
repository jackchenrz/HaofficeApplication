package com.publish.haoffice.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.common.adapter.GirdViewAdapter;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.DBDocListBean;
import com.publish.haoffice.api.bean.repair.ToRepairedBean;
import com.publish.haoffice.api.bean.repair.UserInfoBean;
import com.publish.haoffice.api.dao.repair.Sys_userDao;
import com.publish.haoffice.app.Repair.ToRepairListActivity;
import com.publish.haoffice.application.SysApplication;
import com.publish.haoffice.view.widget.BadgeView;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.gv_items)
    GridView gvItems;
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.ll_show)
    LinearLayout ll_show;
    @InjectView(R.id.tv_count)
    TextView tv_count;

    private GirdViewAdapter adapter;
    private String[] names =  new String[] { "电子公文", "报修管理","施工管理","生产交班"};
    private int[] imageIds = new int[] { R.drawable.main_office, R.drawable.main_repair,
            R.drawable.main_construct, R.drawable.main_produce};
    private SPUtils spUtils;
    private String officeUrl;
    private String repairUrl;
    private BadgeView mBadgeViewForChat;
    private Sys_userDao userDao;
    private int count_repair;
    private HashMap<String, String> map;
    private String userName;
    private String userId;

    @Override
    public int bindLayout () {
        return R.layout.activity_main;
    }

    @Override
    public void initView (View view) {

        ButterKnife.inject(this);
        ll_back.setVisibility(View.GONE);
        adapter = new GirdViewAdapter(names) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = null;
                if (convertView == null) {
                    view = View.inflate(MainActivity.this, R.layout.gird_item_home,
                            null);
                } else {
                    view = convertView;
                }
                TextView name = (TextView) view
                        .findViewById(R.id.tv_name_grid_item);
                name.setText(names[position]);
                name.setTextColor(MainActivity.this.getResources().getColor(
                        R.color.title));

                ImageView image = (ImageView) view
                        .findViewById(R.id.iv_icon_gird_item);
                image.setBackgroundResource(imageIds[position]);
                return view;
            }
        };
        gvItems.setAdapter(adapter);
        gvItems.setOnItemClickListener(this);
    }

    @Override
    public void doBusiness (Context mContext) {

    }

    @Override
    public void resume () {
        userDao = Sys_userDao.getInstance(this);
        spUtils = new SPUtils();
        userName = spUtils.getString(MainActivity.this, Const.USERNAME, "",Const.SP_REPAIR);
        userId = spUtils.getString(MainActivity.this, Const.USERID, "", Const.SP_OFFICE);
        officeUrl = "http://" + spUtils.getString(MainActivity.this, Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(MainActivity.this, Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE1;
        repairUrl = "http://" + spUtils.getString(MainActivity.this, Const.SERVICE_IP, "",Const.SP_REPAIR) + ":" + spUtils.getString(MainActivity.this, Const.SERVICE_PORT, "",Const.SP_REPAIR) + Const.SERVICE_PAGE;

        if(userName != null &&  !"".equals(userName) ){
            getData1 ();
        }

        if(userId != null  &&  !"".equals(userId)){
            getData ();
        }



    }
    private void getData1 () {
        UserInfoBean.UserInfo userInfo = userDao.getUserInfo(userName);
        if(userInfo != null){
            map = new HashMap<>();
            map.put("DeptID",userInfo.dept_id);
            HttpConn.callService(repairUrl, Const.SERVICE_NAMESPACE, Const.REPAIR_GETTECHREPAIRINGLIST, map, new IWebServiceCallBack() {
                @Override
                public void onSucced (SoapObject result) {
                    if(result != null){
                        String string = result.getProperty(0).toString();
                        if(!"404".equals(string)){
                            ToRepairedBean jsonBean = JsonToBean.getJsonBean(string,ToRepairedBean.class);
                            count_repair = jsonBean.ds.size();

                            HttpConn.callService(repairUrl, Const.SERVICE_NAMESPACE, Const.REPAIR_GETTECHREPAIRINGLIST, map, new IWebServiceCallBack() {
                                @Override
                                public void onSucced (SoapObject result) {
                                    if(result != null){
                                        String string = result.getProperty(0).toString();
                                        if(!"404".equals(string)){
                                            ToRepairedBean jsonBean = JsonToBean.getJsonBean(string,ToRepairedBean.class);
                                            count_repair += jsonBean.ds.size();
                                            //添加红色数字提示
                                            mBadgeViewForChat = new BadgeView(MainActivity.this);
                                            mBadgeViewForChat.setVisibility(View.VISIBLE);
                                            if(jsonBean != null){
                                                mBadgeViewForChat.setBadgeCount(count_repair);
                                            }
                                            LinearLayout itemAtPosition = (LinearLayout) gvItems.getChildAt(1);
                                            mBadgeViewForChat.setTargetView(itemAtPosition.findViewById(R.id.iv_icon_gird_item));

                                        }else{
                                            tv_count.setVisibility(View.VISIBLE);
                                            tv_count.setText("联网失败");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure (String result) {
                                }
                            });


                        }else{
                        }
                    }
                }
                @Override
                public void onFailure (String result) {
                }
            });
        }

    }


    public void getData () {

        HashMap<String, String> map = new HashMap<>();
        map.put("UserID",userId);
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETDBDOCLIST, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        DBDocListBean jsonBean = JsonToBean.getJsonBean(string, DBDocListBean.class);

                        //添加红色数字提示
                        mBadgeViewForChat = new BadgeView(MainActivity.this);
                        mBadgeViewForChat.setVisibility(View.VISIBLE);
                        if(jsonBean != null){
                            mBadgeViewForChat.setBadgeCount(jsonBean.ds.size());
                        }
                        LinearLayout itemAtPosition = (LinearLayout) gvItems.getChildAt(0);
                        mBadgeViewForChat.setTargetView(itemAtPosition.findViewById(R.id.iv_icon_gird_item));
                    }else{
                    }
                }
            }

            @Override
            public void onFailure (String result) {

            }
        });
    }


    @Override
    public void destroy () {

    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: // 电子公文
                SysApplication.assignData(Const.SYSTEM_FLAG,"0");
                jump2Activity(MainActivity.this,LoginActivity.class,null,false);
                break;
            case 1: // 报修管理
                SysApplication.assignData(Const.SYSTEM_FLAG,"1");
                jump2Activity(MainActivity.this,LoginActivity.class,null,false);
                break;
            case 2: // 施工管理
                ToastUtils.showToast(MainActivity.this,"该功能尚未开发，敬请期待");
                jump2Activity(MainActivity.this,TestActivity.class,null,false);
                break;
		    case 3: // 生产交班
                ToastUtils.showToast(MainActivity.this,"该功能尚未开发，敬请期待");
			break;

        }
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出应用",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                mApplication.removeAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
