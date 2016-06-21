package com.publish.haoffice.app.Repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.base.BaseFragment;
import com.msystemlib.common.adapter.ComFragmentAdapter;
import com.msystemlib.common.adapter.CommonAdapter;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.img.ImgLoad;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.StatuesUtils;
import com.msystemlib.utils.ThreadUtils;
import com.msystemlib.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.RepairHandlerBean;
import com.publish.haoffice.api.bean.repair.ToRepairedBean;
import com.publish.haoffice.api.dao.repair.Sys_userDao;
import com.publish.haoffice.application.SysApplication;
import com.publish.haoffice.view.LazyViewPager;

import net.tsz.afinal.annotation.view.ViewInject;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ToRepairListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.id_listview)
    ListView listView;
    @InjectView(R.id.id_swipe_ly)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.tv_count)
    TextView tv_count;
    private ImageLoader imageLoader;
    private SPUtils spUtils;
    private int flag5t;
    private int firstIn = 0;//第一次执行onResume
    private String url;
    private Sys_userDao userDao;
    private List<ToRepairedBean.ToRepair> toRepairs;
    private CommonAdapter<ToRepairedBean.ToRepair> adapter;

    @Override
    public int bindLayout () {
        return R.layout.activity_torepair;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this,view);
    }

    @Override
    public void doBusiness (Context mContext) {
        imageLoader = ImgLoad.initImageLoader(this);
        spUtils = new SPUtils();
        userDao = Sys_userDao.getInstance(this);
        String serverIP = spUtils.getString(this, Const.SERVICE_IP, "", Const.SP_REPAIR);
        String serverPort = spUtils.getString(this, Const.SERVICE_PORT, "",Const.SP_REPAIR);
        if (serverIP != null && !"".equals(serverIP) && serverPort != null&& !"".equals(serverPort)) {
            url = "http://" + serverIP + ":" + serverPort+ Const.SERVICE_PAGE;
        }
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.chocolate,
                R.color.yellow,
                R.color.green,
                R.color.gold);
    }

    @Override
    public void resume () {
        if(firstIn == 0){
            Intent intent = getIntent();
            flag5t = intent.getIntExtra("flag5t", 0);
        }
        firstIn++;
        refreshLayout.post(new Runnable() {
            @Override
            public void run () {
                refreshLayout.setRefreshing(true);
            }
        });
        onRefresh();
    }

    /**
     * 联网获取数据
     */
    public void getData (String methodName) {

        HashMap<String, String> map = new HashMap<>();
        map.put("DeptID",userDao.getUserInfo(SysApplication.gainData(Const.USERNAME).toString().trim()).dept_id);
        HttpConn.callService(url, Const.SERVICE_NAMESPACE, methodName, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    String string = result.getProperty(0).toString();
                    refreshLayout.post(new Runnable() {
                        @Override
                        public void run () {
                            refreshLayout.setRefreshing(false);
                        }
                    });
                    if(!"404".equals(string)){
                        ToRepairedBean jsonBean = JsonToBean.getJsonBean(string,ToRepairedBean.class);
                        toRepairs = jsonBean.ds;
                        tv_count.setVisibility(View.GONE);
                        setOrUpdateAdapter(toRepairs);

                    }else{
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }
                }
            }

            @Override
            public void onFailure (String result) {
                ToastUtils.showToast(ToRepairListActivity.this, "联网失败");
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run () {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    protected void setOrUpdateAdapter(
            final List<ToRepairedBean.ToRepair> toRepairList) {
        adapter = new CommonAdapter<ToRepairedBean.ToRepair>(toRepairList) {

                    @Override
                    public View getView(int position, View convertView,
                                        ViewGroup parent) {
                        View view;
                        ViewHolder vh;
                        if (convertView == null) {
                            view = getLayoutInflater()
                                    .inflate(R.layout.list_item_repairhandler,
                                            parent, false);
                            vh = new ViewHolder(view);
                            view.setTag(vh);
                        } else {
                            view = convertView;
                            vh = (ViewHolder) view.getTag();
                        }
                        ToRepairedBean.ToRepair toRepair = toRepairList.get(position);
                        vh.tv_name.setText("设备名称：" + toRepair.EqptName);
                        vh.tv_department.setText("故障发生时间："
                                + toRepair.FaultOccu_Time);
                        vh.tv_describe.setText("故障描述：" + toRepair.FaultAppearance);
                        if(toRepair.ImageUrlPath != null || "".equals(toRepair.ImageUrlPath)){

                            imageLoader.displayImage(toRepair.ImageUrlPath, vh.ivImg);
                        }
                        return view;
                    }
                };
            listView.setAdapter(adapter);
    }
    static class ViewHolder {
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_department)
        TextView tv_department;
        @InjectView(R.id.tv_describe)
        TextView tv_describe;
        @InjectView(R.id.iv_img)
        ImageView ivImg;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
    @Override
    public void destroy () {

    }

    @Override
    public void onRefresh () {
        LogUtils.d("ckj",flag5t+"");
        if(flag5t == 0){
             getData (Const.SERVICE_GETTECHREPAIRINGLIST);
        }else if(flag5t == 1){
            getData (Const.SERVICE_GET5TREPAIRINGLIST);
        }
    }
}
