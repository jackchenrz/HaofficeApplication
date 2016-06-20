package com.publish.haoffice.app.Repair;

import android.os.Bundle;
import android.os.Environment;
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
import com.msystemlib.img.ImgLoad;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ThreadUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.RepairHandlerBean;
import com.publish.haoffice.api.bean.repair.RepairHandlerBean.RepairHandler;
import com.publish.haoffice.api.bean.repair.RepairShow;

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

    private ImageLoader imageLoader;
    private SPUtils spUtils;
    private String url;
    private int flag5t;
    private CommonAdapter<RepairHandler> adapter;
    private List<RepairHandlerBean.RepairHandler> repairList = new ArrayList<>();
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

    /**
     * 设置报修处理页面
     * @param repairHandlerList
     */

    protected void setRepairHandlerAdapter(
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
                RepairHandler repairShow = repairHandlerList.get(position);
                vh.tv_name.setText("设备名称：" + repairShow.RepairID);
                vh.tv_department
                        .setText("故障发生时间：" + repairShow.IsDocument);
                vh.tv_describe.setText("故障描述：" + repairShow.IsDocument);
                imageLoader.displayImage("file://" + Const.FILEPATH + repairShow.IsDocument, vh.ivImg);
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    public void getData () {

        HashMap<String, String> map = new HashMap<>();
//        map.put("")
        HttpConn.callService(url, Const.SERVICE_NAMESPACE, Const.SERVICE_GETREPAIR_HANDLE, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {

            }

            @Override
            public void onFailure (String result) {

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

        ThreadUtils.runInBackground(new Runnable() {
            @Override
            public void run () {
                getData();
            }
        });

    }
}
