package com.publish.haoffice.app.Repair;

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
import com.publish.haoffice.api.bean.repair.RepairInfo;
import com.publish.haoffice.api.bean.repair.Search5tBean;
import com.publish.haoffice.api.bean.repair.SearchTechBean;
import com.publish.haoffice.api.bean.repair.ToRepairedBean;
import com.publish.haoffice.api.dao.repair.TechEqptDao;
import com.publish.haoffice.view.AutoListView;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/6/28.
 */
public class SearchResultActivity extends BaseActivity {

    @InjectView(R.id.lv_autolist)
    ListView lv_autolist;
    @InjectView(R.id.tv_count)
    TextView tv_count;
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.tv_size)
    TextView tv_size;

    @InjectView(R.id.btn_first)
    Button btn_first;
    @InjectView(R.id.btn_pre)
    Button btn_pre;
    @InjectView(R.id.btn_next)
    Button btn_next;
    @InjectView(R.id.btn_last)
    Button btn_last;


    private HashMap<String, Object> map;
    private SPUtils spUtils;
    private String url;
    private CommonAdapter<Search5tBean.Search5t> adapter1;
    private CommonAdapter<SearchTechBean.SearchTech> adapter;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private TechEqptDao techEqptDao;
    private int falgSearch;
    private int pageSize = 20;
    private int pageindex = 1;
    private int pageMax = 1;
    private List<SearchTechBean.SearchTech> searchTechList;
    private List<Search5tBean.Search5t> search5tList;
    private int i;

    @Override
    public int bindLayout () {
        return R.layout.activity_search_result;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
        tv_title.setText("查询列表");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(SearchResultActivity.this);
            }
        });
        tv_size.setVisibility(View.GONE);


        lv_autolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

                if(falgSearch == 0){
                    SearchTechBean.SearchTech searchTech = searchTechList.get(position);
                    Intent intent = new Intent(SearchResultActivity.this,
                            SearchDetailActivity.class);
                    intent.putExtra("searchTech", searchTech);
                    intent.putExtra("flag5t", falgSearch);
                    startActivity(intent);
                    overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);
                }else if(falgSearch == 1){
                    Search5tBean.Search5t search5t = search5tList.get(position);
                    Intent intent = new Intent(SearchResultActivity.this,
                            SearchDetailActivity.class);
                    intent.putExtra("search5t", search5t);
                    intent.putExtra("flag5t", falgSearch);
                    startActivity(intent);
                    overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);
                }


            }
        });
    }

    @Override
    public void doBusiness (Context mContext) {
        spUtils = new SPUtils();
        imageLoader = ImgLoad.initImageLoader(this);
        techEqptDao = TechEqptDao.getInstance(this);
        String serverIP = spUtils.getString(SearchResultActivity.this, Const.SERVICE_IP, "", Const.SP_REPAIR);
        String serverPort = spUtils.getString(SearchResultActivity.this, Const.SERVICE_PORT, "",Const.SP_REPAIR);
        if (serverIP != null && !"".equals(serverIP) && serverPort != null&& !"".equals(serverPort)) {
            url = "http://" + serverIP + ":" + serverPort+ Const.SERVICE_PAGE;
        }
        Intent intent = getIntent();
        falgSearch = intent.getIntExtra("falgSearch", 0);
        map = new HashMap<String, Object>();

        if(falgSearch == 0){
            map.put("dept_id",intent.getStringExtra("dept_id"));
            map.put("RepairDeptID",intent.getStringExtra("RepairDeptID"));
            map.put("FaultStatus",intent.getStringExtra("FaultStatus"));
            map.put("EqptName",intent.getStringExtra("EqptName"));
            map.put("StartDate",intent.getStringExtra("StartDate"));
            map.put("EndDate",intent.getStringExtra("EndDate"));

            HttpConn.callService(url, Const.SERVICE_NAMESPACE,Const.REPAIR_GETTECHSEARCHNUM, map,new IWebServiceCallBack() {

                @Override
                public void onSucced(SoapObject result) {
                    if (result != null) {
                        String string = result.getProperty(0)
                                .toString();
                        if(!"404".equals(string) && !"NULL".equals(string)){
                            tv_size.setVisibility(View.VISIBLE);

                            i = Integer.parseInt(string);
                            pageMax = i % pageSize == 0 ? i / pageSize : i / pageSize + 1;
                            tv_size.setText("共"+i+"条 第"+pageindex+"页/共" +pageMax+ "页");
                            getData(Const.REPAIR_GETTECHSEARCH,pageindex,pageSize);
                        }else {
                            tv_size.setVisibility(View.GONE);
                            tv_count.setVisibility(View.VISIBLE);
                            tv_count.setText("暂无数据");
                        }
                    } else {
                        tv_size.setVisibility(View.GONE);
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("联网失败");
                    }
                }

                @Override
                public void onFailure(String result) {
                    tv_count.setVisibility(View.VISIBLE);
                    tv_count.setText("联网失败");
                }
            });
        }else if(falgSearch == 1){
            map.put("dept_id",intent.getStringExtra("dept_id"));
            map.put("RepairDeptID",intent.getStringExtra("RepairDeptID"));
            map.put("FaultStatus",intent.getStringExtra("FaultStatus"));
            map.put("ProbeStation",intent.getStringExtra("ProbeStation"));
            map.put("StartDate",intent.getStringExtra("StartDate"));
            map.put("EndDate",intent.getStringExtra("EndDate"));


            HttpConn.callService(url, Const.SERVICE_NAMESPACE,Const.REPAIR_GET5TSEARCHNUM, map,new IWebServiceCallBack() {

                @Override
                public void onSucced(SoapObject result) {
                    if (result != null) {
                        String string = result.getProperty(0)
                                .toString();
                        if(!"404".equals(string) && !"NULL".equals(string)){
                            tv_size.setVisibility(View.VISIBLE);

                            i = Integer.parseInt(string);
                            pageMax = i % pageSize == 0 ? i / pageSize : i / pageSize + 1;
                            tv_size.setText("共"+i+"条 第"+pageindex+"页/共" +pageMax+ "页");
                            getData(Const.REPAIR_GET5TSEARCH,pageindex,pageSize);
                        }else {
                            tv_size.setVisibility(View.GONE);
                            tv_count.setVisibility(View.VISIBLE);
                            tv_count.setText("暂无数据");
                        }
                    } else {
                        tv_size.setVisibility(View.GONE);
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("联网失败");
                    }
                }

                @Override
                public void onFailure(String result) {
                    tv_count.setVisibility(View.VISIBLE);
                    tv_count.setText("联网失败");
                }
            });
        }

        initClick();
    }

    private void initClick () {

        btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                pageindex = 1;
                tv_size.setText("共"+i+"条 第"+pageindex+"页/共" +pageMax+ "页");
                if(falgSearch == 0){
                    getData(Const.REPAIR_GETTECHSEARCH,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.REPAIR_GET5TSEARCH,pageindex,pageSize);
                }
            }
        });

        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(pageindex == 1){
                    pageindex = 1;
                }else{
                    pageindex --;
                }
                tv_size.setText("共"+i+"条 第"+pageindex+"页/共" +pageMax+ "页");
                if(falgSearch == 0){
                    getData(Const.REPAIR_GETTECHSEARCH,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.REPAIR_GET5TSEARCH,pageindex,pageSize);
                }
            }
        });



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(pageindex == pageMax){
                    pageindex = pageMax;
                }else{
                    pageindex ++;
                }
                tv_size.setText("共"+i+"条 第"+pageindex+"页/共" +pageMax+ "页");
                if(falgSearch == 0){
                    getData(Const.REPAIR_GETTECHSEARCH,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.REPAIR_GET5TSEARCH,pageindex,pageSize);
                }
            }
        });

        btn_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                    pageindex = pageMax;
                tv_size.setText("共"+i+"条 第"+pageindex+"页/共" +pageMax+ "页");
                if(falgSearch == 0){
                    getData(Const.REPAIR_GETTECHSEARCH,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.REPAIR_GET5TSEARCH,pageindex,pageSize);
                }
            }
        });

    }

    private void getData (final String methodName, int PageIndex, int PageSize) {

        map.put("PageSize",PageSize);
        map.put("PageIndex",PageIndex);

        HttpConn.callService(url, Const.SERVICE_NAMESPACE,methodName, map,new IWebServiceCallBack() {


            @Override
            public void onSucced(SoapObject result) {
                if (result != null) {
                    String string = result.getProperty(0)
                            .toString();
                    if(!"404".equals(string)){
                        tv_count.setVisibility(View.GONE);
                        if(methodName.equals(Const.REPAIR_GETTECHSEARCH)){
                            SearchTechBean jsonBean = JsonToBean.getJsonBean(string, SearchTechBean.class);
                            searchTechList = jsonBean.ds;
                            setOrUpdateAdapter(searchTechList);
                        }else if(methodName.equals(Const.REPAIR_GET5TSEARCH)){
                            Search5tBean jsonBean = JsonToBean.getJsonBean(string, Search5tBean.class);
                            search5tList = jsonBean.ds;
                            setOrUpdateAdapter1(search5tList);
                        }

                    }else {
                        tv_size.setVisibility(View.GONE);
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }
                } else {
                    tv_size.setVisibility(View.GONE);
                    tv_count.setVisibility(View.VISIBLE);
                    tv_count.setText("联网失败");
                }
            }

            @Override
            public void onFailure(String result) {
                tv_size.setVisibility(View.GONE);
                tv_count.setVisibility(View.VISIBLE);
                tv_count.setText("联网失败");
            }
        });
    }


    protected void setOrUpdateAdapter1(final List<Search5tBean.Search5t> repairInfoList) {
        adapter1 = new CommonAdapter<Search5tBean.Search5t>(repairInfoList) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getLayoutInflater()
                            .inflate(R.layout.list_item_search,
                                    parent, false);
                    vh = new ViewHolder(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder) view.getTag();
                }
                Search5tBean.Search5t toRepair = repairInfoList.get(position);
                vh.tv_name.setText("设备名称：" + toRepair.ProbeStation);
                vh.tv_address.setText("设备处所：" + toRepair.EqptAddress);
                vh.tv_state.setText("故障状态：" + toRepair.FaultStatus);
                if("未处理".equals(toRepair.FaultStatus)){
                    view.setBackgroundColor(getResources().getColor(R.color.powderblue));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                if(toRepair.ImageUrl != null || "".equals(toRepair.ImageUrl)) {
                    imageLoader.displayImage("file://" + Const.FILEPATH + toRepair.ImageUrl, vh.ivImg);
                }
                return view;
            }
        };
        lv_autolist.setAdapter(adapter1);
    }


    protected void setOrUpdateAdapter(final List<SearchTechBean.SearchTech> repairInfoList) {
        adapter = new CommonAdapter<SearchTechBean.SearchTech>(repairInfoList) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getLayoutInflater()
                            .inflate(R.layout.list_item_search,
                                    parent, false);
                    vh = new ViewHolder(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder) view.getTag();
                }
                SearchTechBean.SearchTech toRepair = repairInfoList.get(position);
                vh.tv_name.setText("设备名称：" + toRepair.EqptName);
                vh.tv_address.setText("设备处所：" + toRepair.SettingAddr);
                vh.tv_state.setText("故障状态：" + toRepair.FaultStatus);
                if("未处理".equals(toRepair.FaultStatus)){
                    view.setBackgroundColor(getResources().getColor(R.color.powderblue));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                if(toRepair.ImageUrl != null || "".equals(toRepair.ImageUrl)) {
                    imageLoader.displayImage("file://" + Const.FILEPATH + toRepair.ImageUrl, vh.ivImg);
                }
                return view;
            }
        };
        lv_autolist.setAdapter(adapter);
    }

    static class ViewHolder {
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_address)
        TextView tv_address;
        @InjectView(R.id.tv_state)
        TextView tv_state;
        @InjectView(R.id.iv_img)
        ImageView ivImg;

        public ViewHolder(View view) {
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
