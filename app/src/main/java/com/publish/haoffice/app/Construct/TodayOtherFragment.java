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
import com.publish.haoffice.api.bean.Construct.TodayotherBean;
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

public class TodayOtherFragment extends BaseFragmentapp implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.id_listview)
    ListView listView;
    @InjectView(R.id.id_swipe_ly)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.tv_count)
    TextView tv_count;
    private SPUtils spUtils;
    private String constUrl;
    private int firstIn = 0;
    private List<TodayotherBean.Todayother> todayotherList;
    private CommonAdapter<TodayotherBean.Todayother> adapter;

    @Override
    public View initView () {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_uploadno,null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void initData () {

        spUtils = new SPUtils();
        constUrl = "http://" + spUtils.getString(getActivity(), Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(getActivity(), Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE2;
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.chocolate,
                R.color.yellow,
                R.color.green,
                R.color.gold);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                TodayotherBean.Todayother Todayother = todayotherList.get(position);
                    Intent intent = new Intent(getActivity(),
                            OtherDetaileActivity.class);
                intent.putExtra("OtherJobID",Todayother.OtherJobID);
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
        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE, Const.CONSTRUCT_GETDAYOTHERJOB, map, new IWebServiceCallBack() {
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
                        TodayotherBean jsonBean = JsonToBean.getJsonBean(string, TodayotherBean.class);
                        todayotherList  = jsonBean.ds;

                        if(todayotherList.size() == 0){
                            tv_count.setVisibility(View.VISIBLE);
                            tv_count.setText("暂无数据");
                        }else{
                            tv_count.setVisibility(View.GONE);

                        }
                        setOrUpdateAdapter(todayotherList);
                    }else{
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }
                }
            }

            @Override
            public void onFailure (String result) {
                if(adapter != null){
                    todayotherList.clear();
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

    private void setOrUpdateAdapter (final List<TodayotherBean.Todayother> TodayotherList) {
        adapter = new CommonAdapter<TodayotherBean.Todayother>(TodayotherList) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_todayother,
                                    parent, false);
                    vh = new ViewHolder(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder) view.getTag();
                }
                TodayotherBean.Todayother Todayother = TodayotherList.get(position);
                vh.tv_name.setText(Todayother.Project);
                vh.tv_time.setText("时间："+Todayother.ConstTime);
                vh.tv_workdept.setText("作业车间："+Todayother.WorkSpace);
                vh.tv_code.setText("计划号："+Todayother.PlanNum);

                vh.tv_station.setText("站/区段："+Todayother.Station);
                vh.tv_regstation.setText("登记站："+Todayother.RegStation);
                vh.tv_line.setText("线路："+Todayother.WorkLine);
                vh.tv_cate.setText("行别："+Todayother.LineType);
                vh.tv_date.setText("计划日期："+Todayother.PlanDate);
                vh.tv_flow.setText("流程跟踪："+Todayother.FlowTracking);
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

        @InjectView(R.id.tv_date)
        TextView tv_date;
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
        @InjectView(R.id.tv_flow)
        TextView tv_flow;

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
