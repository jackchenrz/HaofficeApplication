package com.publish.haoffice.app.Construct;

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
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.Construct.TodayConBean;
import com.publish.haoffice.api.bean.Construct.TodayRepBean;
import com.publish.haoffice.api.bean.office.DBDocListBean;
import com.publish.haoffice.app.office.DocDetailActivity;
import com.publish.haoffice.app.office.NoticeDetailActivity;
import com.publish.haoffice.app.office.OfficDetailActivity;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/7/1.
 */
public class ConSearchResultActivity extends BaseActivity {

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
    private SPUtils spUtils;
    private String constUrl;
    private int falgSearch;
    private int pageSize = 20;
    private int pageindex = 1;
    private int pageMax = 1;
    private HashMap<String, Object> map;
    private int i;
    private CommonAdapter<TodayConBean.TodayCon> adapter;
    private List<TodayConBean.TodayCon> todayConList;
    private List<TodayRepBean.TodayRep> todayRepList;
    private CommonAdapter<TodayRepBean.TodayRep> adapter1;

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
                finishActivity(ConSearchResultActivity.this);
            }
        });
        tv_size.setVisibility(View.GONE);
        initClick();
    }

    @Override
    public void doBusiness (Context mContext) {
        spUtils = new SPUtils();
        constUrl = "http://" + spUtils.getString(ConSearchResultActivity.this, Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(ConSearchResultActivity.this, Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE2;


        Intent intent = getIntent();
        falgSearch = intent.getIntExtra("falgSearch", 0);
        map = new HashMap<>();
        map.put("UserID",spUtils.getString(ConSearchResultActivity.this, Const.USERID, "", Const.SP_CONSTRUCT));
        map.put("txtStartDateValue", intent.getStringExtra("txtStartDateValue"));
        map.put("txtEndDateValue", intent.getStringExtra("txtEndDateValue"));
        map.put("txtWorkLineSelectedValue", intent.getStringExtra("txtWorkLineSelectedValue"));

        map.put("txtRegStationSelectedValue", intent.getStringExtra("txtRegStationSelectedValue"));
        map.put("txtLineTypeSelectedValue", intent.getStringExtra("txtLineTypeSelectedValue"));
        map.put("txtWorkSpaceSelectedItemText", intent.getStringExtra("txtWorkSpaceSelectedItemText"));
        map.put("txtBzSelectedItemText", intent.getStringExtra("txtBzSelectedItemText"));
        map.put("txtRealNameSelectedValue", intent.getStringExtra("txtRealNameSelectedValue"));
        if(falgSearch == 0){
            map.put("txtStationNameSelectedValue", intent.getStringExtra("txtStationNameSelectedValue"));
            getData(Const.CONSTRUCT_GETSGSEARCH,pageindex,pageSize);
        }else if(falgSearch == 1){
            map.put("txtStationSelectedValue", intent.getStringExtra("txtStationNameSelectedValue"));
            getData(Const.CONSTRUCT_GETWXSEARCH,pageindex,pageSize);
        }

    }

    private void getData (final String methodName, int pageindex1, int pageSize1) {
        map.put("PageSize", pageSize1);
        map.put("PageIndex", pageindex1);

        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE, methodName, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    String string = result.getProperty(0).toString();
                    String string1= result.getProperty(1).toString();
                    if(!"404".equals(string) && !"404".equals(string1)){
                        tv_size.setVisibility(View.VISIBLE);
                        i = Integer.parseInt(string1);
                        pageMax = i % pageSize == 0 ? i / pageSize : i / pageSize + 1;
                        tv_size.setText("共"+i+"条 第"+pageindex+"页/共" +pageMax+ "页");

                        if(methodName.equals(Const.CONSTRUCT_GETSGSEARCH)){
                            TodayConBean jsonBean = JsonToBean.getJsonBean(string, TodayConBean.class);
                            todayConList = jsonBean.ds;

                            if(todayConList.size() == 0){
                                tv_count.setVisibility(View.VISIBLE);
                                tv_count.setText("暂无数据");
                            }else{
                                tv_count.setVisibility(View.GONE);

                            }
                            setOrUpdateAdapter(todayConList);
                        }else  if(methodName.equals(Const.CONSTRUCT_GETWXSEARCH)){
                            TodayRepBean jsonBean = JsonToBean.getJsonBean(string, TodayRepBean.class);
                            todayRepList = jsonBean.ds;

                            if(todayRepList.size() == 0){
                                tv_count.setVisibility(View.VISIBLE);
                                tv_count.setText("暂无数据");
                            }else{
                                tv_count.setVisibility(View.GONE);

                            }
                            setOrUpdateAdapter1(todayRepList);
                        }

                    }else{
                        tv_size.setVisibility(View.GONE);
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }
                }
            }

            @Override
            public void onFailure (String result) {
                tv_size.setVisibility(View.GONE);
                tv_count.setVisibility(View.VISIBLE);
                tv_count.setText("联网失败");
            }
        });
    }

    private void initClick () {

        btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                pageindex = 1;
                if(falgSearch == 0){
                    getData(Const.CONSTRUCT_GETSGSEARCH,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.CONSTRUCT_GETWXSEARCH,pageindex,pageSize);
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
                if(falgSearch == 0){
                    getData(Const.CONSTRUCT_GETSGSEARCH,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.CONSTRUCT_GETWXSEARCH,pageindex,pageSize);
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
                    getData(Const.CONSTRUCT_GETSGSEARCH,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.CONSTRUCT_GETWXSEARCH,pageindex,pageSize);
                }

            }
        });

        btn_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                pageindex = pageMax;
                tv_size.setText("共"+i+"条 第"+pageindex+"页/共" +pageMax+ "页");
                if(falgSearch == 0){
                    getData(Const.CONSTRUCT_GETSGSEARCH,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.CONSTRUCT_GETWXSEARCH,pageindex,pageSize);
                }

            }
        });

        lv_autolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

                if(falgSearch == 0){
                    TodayConBean.TodayCon todayCon = todayConList.get(position);

                    Intent intent = new Intent(ConSearchResultActivity.this,
                            ConDetaileActivity.class);

                    intent.putExtra("ConstructionID",todayCon.ConstructionID);
                    startActivity(intent);
                    overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);
                }else  if(falgSearch == 1){
                    TodayRepBean.TodayRep todayRep = todayRepList.get(position);
                    Intent intent = new Intent(ConSearchResultActivity.this,
                            RepDetaileActivity.class);
                    intent.putExtra("RepairID",todayRep.RepairID);
                    startActivity(intent);
                    overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);
                }




            }
        });

    }

    private void setOrUpdateAdapter (final List<TodayConBean.TodayCon> TodayConList) {
        adapter = new CommonAdapter<TodayConBean.TodayCon>(TodayConList) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getLayoutInflater()
                            .inflate(R.layout.list_item_todaycon,
                                    parent, false);
                    vh = new ViewHolder(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder) view.getTag();
                }
                TodayConBean.TodayCon TodayCon = TodayConList.get(position);
                vh.tv_name.setText(TodayCon.ConstructionName);
                vh.tv_time.setText("施工时间："+TodayCon.ConstructionTime);
                vh.tv_workdept.setText("作业车间："+TodayCon.WorkSpace);
                vh.tv_address.setText("施工地点："+TodayCon.ConstructionPlace);
                vh.tv_type.setText("类型："+TodayCon.constructionTypeName);
                vh.tv_lev.setText("等级："+TodayCon.WorkLevel);
                vh.tv_line.setText("线路："+TodayCon.WorkLine);
                vh.tv_cate.setText("行别："+TodayCon.LineType);
                vh.tv_date.setText("施工日期："+TodayCon.ConstructionDate);
                vh.tv_date1.setText("真实日期："+TodayCon.TrueConstructionDate);
                vh.tv_code.setText("编号："+TodayCon.SerialNo);

                return view;
            }
        };
        lv_autolist.setAdapter(adapter);

    }

    private void setOrUpdateAdapter1 (final List<TodayRepBean.TodayRep> TodayRepList) {
        adapter1 = new CommonAdapter<TodayRepBean.TodayRep>(TodayRepList) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder1 vh;
                if (convertView == null) {
                    view = getLayoutInflater()
                            .inflate(R.layout.list_item_todayrep,
                                    parent, false);
                    vh = new ViewHolder1(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder1) view.getTag();
                }
                TodayRepBean.TodayRep TodayRep = TodayRepList.get(position);
                vh.tv_name.setText(TodayRep.Project);
                vh.tv_time.setText("时间："+TodayRep.ConstTime);
                vh.tv_workdept.setText("作业车间："+TodayRep.WorkSpace);
                vh.tv_address.setText("施工里程："+TodayRep.ConstMileage);
                vh.tv_code.setText("计划号："+TodayRep.PlanNum);

                vh.tv_station.setText("站/区段："+TodayRep.Station);
                vh.tv_regstation.setText("登记站："+TodayRep.RegStation);
                vh.tv_line.setText("线路："+TodayRep.WorkLine);
                vh.tv_cate.setText("行别："+TodayRep.LineType);
                vh.tv_date.setText("计划日期："+TodayRep.PlanDate);
                vh.tv_flow.setText("流程跟踪："+TodayRep.FlowTracking);
                vh.tv_type.setText("维修类型："+TodayRep.RepairType);
                vh.tv_type1.setText("天窗类型："+TodayRep.RoofType);
                vh.tv_user.setText("工长："+TodayRep.sign_real_name);
                vh.tv_state.setText("签收状态："+(TodayRep.IsSign == "true" ? "已签" : "未签"));


                return view;
            }
        };
        lv_autolist.setAdapter(adapter1);

    }

    static class ViewHolder {
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_workdept)
        TextView tv_workdept;
        @InjectView(R.id.tv_time)
        TextView tv_time;
        @InjectView(R.id.tv_address)
        TextView tv_address;
        @InjectView(R.id.tv_type)
        TextView tv_type;
        @InjectView(R.id.tv_lev)
        TextView tv_lev;
        @InjectView(R.id.tv_line)
        TextView tv_line;
        @InjectView(R.id.tv_cate)
        TextView tv_cate;
        @InjectView(R.id.tv_date)
        TextView tv_date;
        @InjectView(R.id.tv_date1)
        TextView tv_date1;
        @InjectView(R.id.tv_code)
        TextView tv_code;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    static class ViewHolder1 {
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_workdept)
        TextView tv_workdept;
        @InjectView(R.id.tv_time)
        TextView tv_time;

        @InjectView(R.id.tv_address)
        TextView tv_address;
        @InjectView(R.id.tv_code)
        TextView tv_code;

        @InjectView(R.id.tv_station)
        TextView tv_station;
        @InjectView(R.id.tv_regstation)
        TextView tv_regstation;
        @InjectView(R.id.tv_line)
        TextView tv_line;
        @InjectView(R.id.tv_cate)
        TextView tv_cate;
        @InjectView(R.id.tv_date)
        TextView tv_date;
        @InjectView(R.id.tv_flow)
        TextView tv_flow;
        @InjectView(R.id.tv_type)
        TextView tv_type;
        @InjectView(R.id.tv_type1)
        TextView tv_type1;
        @InjectView(R.id.tv_user)
        TextView tv_user;
        @InjectView(R.id.tv_state)
        TextView tv_state;

        public ViewHolder1(View view) {
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
