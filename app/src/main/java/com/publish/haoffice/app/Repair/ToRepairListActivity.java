package com.publish.haoffice.app.Repair;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
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
import com.publish.haoffice.api.dao.repair.ToRepairDao;

import org.ksoap2.serialization.SoapObject;

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
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    private ImageLoader imageLoader;
    private SPUtils spUtils;
    private int flag5t;
    private int firstIn = 0;//第一次执行onResume
    private String url;
    private Sys_userDao userDao;
    private List<ToRepairedBean.ToRepair> toRepairs;
    private CommonAdapter<ToRepairedBean.ToRepair> adapter;
    public static ToRepairListActivity instance = null;
    private ToRepairDao toRepairDao;
//    private List<ToRepairSave> repairInfoList;


//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage (Message msg) {
//            super.handleMessage(msg);
//
//            int position = (int) msg.obj;
//
//            listView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.powderblue));
//        }
//    };

    @Override
    public int bindLayout () {
        return R.layout.activity_torepair;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this,view);
        tv_title.setText("维修列表");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(ToRepairListActivity.this);
            }
        });
    }

    @Override
    public void doBusiness (Context mContext) {
        instance = this;
        imageLoader = ImgLoad.initImageLoader(this);
        spUtils = new SPUtils();
        userDao = Sys_userDao.getInstance(this);
        String serverIP = spUtils.getString(this, Const.SERVICE_IP, "", Const.SP_OFFICE);
        String serverPort = spUtils.getString(this, Const.SERVICE_PORT, "",Const.SP_OFFICE);
        if (serverIP != null && !"".equals(serverIP) && serverPort != null&& !"".equals(serverPort)) {
            url = "http://" + serverIP + ":" + serverPort+ Const.SERVICE_PAGE;
        }
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.chocolate,
                R.color.yellow,
                R.color.green,
                R.color.gold);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                ToRepairedBean.ToRepair toRepair = toRepairs.get(position);
                Intent intent = new Intent(ToRepairListActivity.this,
                        ToRepairDetailActivity.class);
                intent.putExtra("toRepair", toRepair);
                intent.putExtra("flag5t", flag5t);
                startActivity(intent);
                overridePendingTransition(R.anim.base_slide_right_in,
                        R.anim.base_slide_remain);


            }
        });
    }

    @Override
    public void resume () {
        toRepairDao = ToRepairDao.getInstance(this);
        if(firstIn == 0){
            Intent intent = getIntent();
            flag5t = intent.getIntExtra("flag5t", 0);
            refreshLayout.post(new Runnable() {
                @Override
                public void run () {
                    refreshLayout.setRefreshing(true);
                }
            });
            onRefresh();
        }
        firstIn++;
    }

    /**
     * 联网获取数据
     */
    public void getData (String methodName) {

        HashMap<String, String> map = new HashMap<>();
        map.put("DeptID",userDao.getUserInfo(spUtils.getString(ToRepairListActivity.this,Const.USERNAME,"",Const.SP_REPAIR)).dept_id);
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


    private ToRepairedBean.ToRepair toRepair;
    protected void setOrUpdateAdapter(
            final List<ToRepairedBean.ToRepair> toRepairList) {
        adapter = new CommonAdapter<ToRepairedBean.ToRepair>(toRepairList) {

                    @Override
                    public View getView(final int position, View convertView,
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
                        toRepair = toRepairList.get(position);

                        if(toRepairDao.getToRepairSave(toRepair.RepairID) != null){
                            view.setBackgroundColor(getResources().getColor(R.color.powderblue));
                        }else{
                            view.setBackgroundColor(getResources().getColor(R.color.darkbg));
                        }

//                        if(repairInfoList.size() != 0 && repairInfoList !=null){
//                            ThreadUtils.runInBackground(new Runnable() {
//                                @Override
//                                public void run () {
//                                    Message msg = Message.obtain();
//                                    for (int i = 0; i < repairInfoList.size(); i++) {
//                                        if(repairInfoList.get(i).RepairID.equals(toRepair.RepairID)){
//                                            msg.obj = position;
//                                            handler.sendMessage(msg);
//                                        }
//                                    }
//                                }
//                            });
//                        }
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
//        repairInfoList = toRepairDao.getAllToRepairSave(userDao
//                .getUserInfo(spUtils.getString(ToRepairListActivity.this,Const.USERNAME, "",Const.SP_REPAIR)).user_id,1,flag5t == 1 ? "5T" : "Tech");
        if(flag5t == 0){
             getData (Const.REPAIR_GETTECHREPAIRINGLIST);
        }else if(flag5t == 1){
            getData (Const.REPAIR_GET5TREPAIRINGLIST);
        }
    }
}
