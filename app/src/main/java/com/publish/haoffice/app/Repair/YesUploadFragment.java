package com.publish.haoffice.app.Repair;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.msystemlib.base.BaseFragment;
import com.msystemlib.common.adapter.CommonAdapter;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.img.ImgLoad;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ThreadUtils;
import com.msystemlib.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.DBDocListBean;
import com.publish.haoffice.api.bean.repair.RepairHandlerBean;
import com.publish.haoffice.api.bean.repair.RepairHandlerBean.RepairHandler;
import com.publish.haoffice.api.bean.repair.RepairInfo;
import com.publish.haoffice.api.bean.repair.RepairShow;
import com.publish.haoffice.api.dao.repair.Reapir_SubmitDao;
import com.publish.haoffice.api.dao.repair.Sys_userDao;
import com.publish.haoffice.application.SysApplication;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class YesUploadFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.id_listview)
    ListView listView;
    @InjectView(R.id.id_swipe_ly)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.tv_count)
    TextView tv_count;

    private ImageLoader imageLoader;
    private SPUtils spUtils;
    private String url;
    private int flag5t;
    private CommonAdapter<RepairHandler> adapter;
    private List<RepairHandler> handlerList;
    private List<RepairHandler> templerList = new ArrayList<>();
    private Sys_userDao userDao;
    private final int FLUSH = 22;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg) {
            switch (msg.what){
                case FLUSH:
                    if(templerList.size() == 0){
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }
                    setOrUpdateAdapter(templerList);

                    break;
            }

        }
    };

    @Override
    public View initView () {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_uploadno,null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void initData () {

        imageLoader = ImgLoad.initImageLoader(getActivity());
        spUtils = new SPUtils();
        userDao = Sys_userDao.getInstance(getActivity());
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null ){
            flag5t = bundle.getInt("flag5t");
        }
        String serverIP = spUtils.getString(getActivity(), Const.SERVICE_IP, "", Const.SP_REPAIR);
        String serverPort = spUtils.getString(getActivity(), Const.SERVICE_PORT, "",Const.SP_REPAIR);
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
    public void onResume () {
        super.onResume();
        refreshLayout.post(new Runnable() {
            @Override
            public void run () {
                refreshLayout.setRefreshing(true);
            }
        });
        onRefresh();
    }

    /**
     * 设置报修处理页面
     * @param repairHandlerList
     */

    protected void setOrUpdateAdapter(
            final List<RepairHandler> repairHandlerList) {
        adapter = new CommonAdapter<RepairHandler>(repairHandlerList) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_repairhandler,
                                    parent, false);
                    vh = new ViewHolder(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder) view.getTag();
                }
                RepairHandler repairHandler= repairHandlerList.get(position);

                vh.tv_name.setText("设备名称：" + repairHandler.EqptName);
                vh.tv_department
                        .setText("故障发生时间：" + repairHandler.FaultOccu_Time);
                vh.tv_describe.setText("故障描述：" + repairHandler.FaultAppearance);
                if(repairHandler.ImageUrlPath != null || "".equals(repairHandler.ImageUrlPath)){

                    imageLoader.displayImage(repairHandler.ImageUrlPath, vh.ivImg);
                }


                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    /**
     * 联网获取数据
     */
    public void getData () {

        HashMap<String, String> map = new HashMap<>();
        map.put("DeptID",userDao.getUserInfo(SysApplication.gainData(Const.USERNAME).toString().trim()).dept_id);
        HttpConn.callService(url, Const.SERVICE_NAMESPACE, Const.SERVICE_GETREPAIR_HANDLE, map, new IWebServiceCallBack() {
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
                        RepairHandlerBean jsonBean = JsonToBean.getJsonBean(string,RepairHandlerBean.class);
                        handlerList = jsonBean.ds;
                        tv_count.setVisibility(View.GONE);

                        ThreadUtils.runInBackground(new Runnable() {
                            @Override
                            public void run () {
                                for (RepairHandler repairHandler:handlerList) {

                                    if(flag5t == 0){
                                        if(repairHandler.RepairType.equals("Tech")){
                                            templerList.add(repairHandler);
                                        }
                                    }else if(flag5t == 1){
                                        if(repairHandler.RepairType.equals("5T")){
                                            templerList.add(repairHandler);
                                        }
                                    }
                                }

                                handler.sendEmptyMessage(FLUSH);
                            }
                        });

                    }else{
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }
                }
            }

            @Override
            public void onFailure (String result) {
                ToastUtils.showToast(getActivity(), "联网失败");
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run () {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
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
    public void onRefresh () {
        templerList.clear();
         getData();
    }
}
