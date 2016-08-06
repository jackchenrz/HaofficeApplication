package com.publish.haoffice.app.Construct;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.publish.haoffice.api.bean.office.YBDocListBean;
import com.publish.haoffice.app.Repair.BaseFragmentapp;
import com.publish.haoffice.app.office.DocDetailActivity;
import com.publish.haoffice.app.office.NoticeDetailActivity;
import com.publish.haoffice.app.office.OfficDetailActivity;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TodayRepairFragment extends BaseFragmentapp implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.id_listview)
    ListView listView;
    @InjectView(R.id.id_swipe_ly)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.tv_count)
    TextView tv_count;
    private SPUtils spUtils;
    private String constUrl;
    private int firstIn = 0;
    private List<TodayRepBean.TodayRep> todayRepList;
    private CommonAdapter<TodayRepBean.TodayRep> adapter;

    @Override
    public View initView () {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_uploadno,null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void initData () {

        spUtils = new SPUtils();
        constUrl = "http://" + spUtils.getString(getActivity(), Const.SERVICE_IP, "", Const.SP_CONSTRUCT) + ":" + spUtils.getString(getActivity(), Const.SERVICE_PORT, "", Const.SP_CONSTRUCT) + Const.SERVICE_PAGE2;
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.chocolate,
                R.color.yellow,
                R.color.green,
                R.color.gold);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                TodayRepBean.TodayRep todayRep = todayRepList.get(position);
                    Intent intent = new Intent(getActivity(),
                            RepDetaileActivity.class);
                intent.putExtra("RepairID",todayRep.RepairID);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);
            }
        });

    }


    public void getData () {

        HashMap<String, String> map = new HashMap<>();
        map.put("DateStr", Const.DATE);
        LogUtils.d("ckj",Const.DATE);
        map.put("UserID", spUtils.getString(getActivity(), Const.USERID, "", Const.SP_CONSTRUCT));
        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE, Const.CONSTRUCT_GETDAYREPAIR, map, new IWebServiceCallBack() {
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
                       TodayRepBean jsonBean = JsonToBean.getJsonBean(string, TodayRepBean.class);
                        todayRepList = jsonBean.ds;

                        if(todayRepList.size() == 0){
                            tv_count.setVisibility(View.VISIBLE);
                            tv_count.setText("暂无数据");
                        }else{
                            tv_count.setVisibility(View.GONE);

                        }
                        setOrUpdateAdapter(todayRepList);

                    }else{
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }
                }
            }

            @Override
            public void onFailure (String result) {
                if(adapter != null){
                    todayRepList.clear();
                    adapter.notifyDataSetChanged();
                }
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

    private void setOrUpdateAdapter (final List<TodayRepBean.TodayRep> TodayRepList) {
        adapter = new CommonAdapter<TodayRepBean.TodayRep>(TodayRepList) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_todayrep,
                                    parent, false);
                    vh = new ViewHolder(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder) view.getTag();
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
        listView.setAdapter(adapter);

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

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

//
//    @Override
//    public void onHiddenChanged (boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if(!hidden){
//            if(firstIn == 0){
//                refreshLayout.post(new Runnable() {
//                    @Override
//                    public void run () {
//                        refreshLayout.setRefreshing(true);
//                    }
//                });
//                onRefresh();
//            }
//            firstIn++;
//        }
//    }

    @Override
    public void onResume () {
        super.onResume();
        if(firstIn == 0){
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

    @Override
    public void onRefresh () {

        getData ();

    }
}
