package com.publish.haoffice.app.Construct;

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

import com.msystemlib.common.adapter.CommonAdapter;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.Construct.TodayConBean;
import com.publish.haoffice.api.bean.office.DBDocListBean;
import com.publish.haoffice.app.Repair.BaseFragmentapp;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TodayConFragment extends BaseFragmentapp implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.id_listview)
    ListView listView;
    @InjectView(R.id.id_swipe_ly)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.tv_count)
    TextView tv_count;
    private SPUtils spUtils;
    private String constUrl;
    private CommonAdapter<TodayConBean.TodayCon> adapter;
    private int firstIn = 0;
    private List<TodayConBean.TodayCon> todayConList;
    private String date;

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

                TodayConBean.TodayCon todayCon = todayConList.get(position);

                Intent intent = new Intent(getActivity(),
                            ConDetaileActivity.class);

                    intent.putExtra("ConstructionID",todayCon.ConstructionID);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);



            }
        });
    }


//    @Override
//    public void onResume () {
//        super.onResume();
//        refreshLayout.post(new Runnable() {
//            @Override
//            public void run () {
//                refreshLayout.setRefreshing(true);
//            }
//        });
//        onRefresh();
//    }
    public void getData () {

        HashMap<String, String> map = new HashMap<>();


        map.put("DateStr", Const.DATE);
        LogUtils.d("ckj",Const.DATE);
        map.put("UserID", spUtils.getString(getActivity(), Const.USERID, "", Const.SP_CONSTRUCT));
        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE, Const.CONSTRUCT_GETDAYCONST, map, new IWebServiceCallBack() {
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
                        TodayConBean jsonBean = JsonToBean.getJsonBean(string, TodayConBean.class);
                        todayConList = jsonBean.ds;

                        if(todayConList.size() == 0){
                            tv_count.setVisibility(View.VISIBLE);
                            tv_count.setText("暂无数据");
                        }else{
                            tv_count.setVisibility(View.GONE);

                        }
                        setOrUpdateAdapter(todayConList);

                    }else{
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }
                }
            }

            @Override
            public void onFailure (String result) {
                if(adapter != null){
                    todayConList.clear();
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

    private void setOrUpdateAdapter (final List<TodayConBean.TodayCon> TodayConList) {
        adapter = new CommonAdapter<TodayConBean.TodayCon>(TodayConList) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getActivity().getLayoutInflater()
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

    @Override
    public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){

            if(firstIn == 0){
                Bundle bundle = getArguments();//从activity传过来的Bundle
                if(bundle!=null){
                    date = bundle.getString("date");
                }
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
    }


    @Override
    public void onRefresh () {
        getData();
    }

}
