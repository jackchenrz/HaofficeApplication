package com.publish.haoffice.app.Repair;

import android.content.Intent;
import android.os.Bundle;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.ToRepairedBean;
import com.publish.haoffice.api.dao.repair.Sys_userDao;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ToYesUploadFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
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
    private CommonAdapter<ToRepairedBean.ToRepair> adapter;
    private Sys_userDao userDao;
    private final int FLUSH = 22;
    private int firstIn = 0;
    private List<ToRepairedBean.ToRepair> toRepairs;

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
        refreshLayout.post(new Runnable() {
            @Override
            public void run () {
                refreshLayout.setRefreshing(true);
            }
        });
        onRefresh();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                ToRepairedBean.ToRepair repairHandler = toRepairs.get(position);
                int flag = 1;
                Intent intent = new Intent(getActivity(),
                        ToRepairHanderDetailActivity.class);
                intent.putExtra("repairHandler", repairHandler);
                intent.putExtra("flag", flag);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in,
                        R.anim.base_slide_remain);


            }
        });

    }


    @Override
    public void onResume () {
        super.onResume();
//        refreshLayout.post(new Runnable() {
//            @Override
//            public void run () {
//                refreshLayout.setRefreshing(true);
//            }
//        });
//        onRefresh();
    }

    protected void setOrUpdateAdapter(
            final List<ToRepairedBean.ToRepair> toRepairList) {
        adapter = new CommonAdapter<ToRepairedBean.ToRepair>(toRepairList) {

            @Override
            public View getView(final int position, View convertView,
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

    /**
     * 联网获取数据
     */
    public void getData (String methodName) {

        HashMap<String, String> map = new HashMap<>();
        map.put("DeptID",userDao.getUserInfo(spUtils.getString(getActivity(),Const.USERNAME,"",Const.SP_REPAIR)).dept_id);
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
                        tv_count.setVisibility(View.GONE);
                        ToRepairedBean jsonBean = JsonToBean.getJsonBean(string,ToRepairedBean.class);
                        toRepairs = jsonBean.ds;
                        if(toRepairs.size() == 0){
                            tv_count.setVisibility(View.VISIBLE);
                            tv_count.setText("暂无数据");
                        }
                        setOrUpdateAdapter(toRepairs);

                    }else{
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("联网失败");
                    }
                }
            }

            @Override
            public void onFailure (String result) {
                tv_count.setVisibility(View.VISIBLE);
                tv_count.setText("联网失败");
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
        if(flag5t == 0){
            getData (Const.REPAIR_GETTECHREPAIREDLIST);
        }else if(flag5t == 1){
            getData (Const.REPAIR_GET5TREPAIREDLIST);
        }
    }
}
