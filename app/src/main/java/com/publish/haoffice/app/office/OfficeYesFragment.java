package com.publish.haoffice.app.office;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.DBDocListBean;
import com.publish.haoffice.api.bean.office.YBDocListBean;
import com.publish.haoffice.app.Repair.BaseFragmentapp;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OfficeYesFragment extends BaseFragmentapp implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.id_listview)
    ListView listView;
    @InjectView(R.id.id_swipe_ly)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.tv_count)
    TextView tv_count;
    private SPUtils spUtils;
    private String officeUrl;
    private int firstIn = 0;
    private List<YBDocListBean.YBDoc> DBDocList;
    private CommonAdapter<YBDocListBean.YBDoc> adapter;

    @Override
    public View initView () {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_uploadno,null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void initData () {

        spUtils = new SPUtils();
        officeUrl = "http://" + spUtils.getString(getActivity(), Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(getActivity(), Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE1;
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.chocolate,
                R.color.yellow,
                R.color.green,
                R.color.gold);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                YBDocListBean.YBDoc doc = DBDocList.get(position);
                if("上级文电".equals(doc.DocType)){
                    Intent intent = new Intent(getActivity(),
                            DocDetailActivity.class);
                    intent.putExtra("RecID", doc.RecID);
                    intent.putExtra("btnFlag", 1);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);
                }else if("段发公文".equals(doc.DocType)){
                    Intent intent = new Intent(getActivity(),
                            OfficDetailActivity.class);
                    intent.putExtra("RecID", doc.RecID);
                    intent.putExtra("btnFlag", 1);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);
                }else{
                    Intent intent = new Intent(getActivity(),
                            NoticeDetailActivity.class);
                    intent.putExtra("RecID", doc.RecID);
                    intent.putExtra("btnFlag", 1);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);
                }
            }
        });

    }


    public void getData () {

        HashMap<String, String> map = new HashMap<>();
        map.put("UserID", spUtils.getString(getActivity(), Const.USERID, "", Const.SP_OFFICE));
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETYBDOCLIST, map, new IWebServiceCallBack() {
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
                        YBDocListBean jsonBean = JsonToBean.getJsonBean(string, YBDocListBean.class);
                        DBDocList = jsonBean.ds;
                        tv_count.setVisibility(View.GONE);
                        setOrUpdateAdapter(DBDocList);
                    }else{
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }
                }
            }

            @Override
            public void onFailure (String result) {
                if(adapter != null){
                    DBDocList.clear();
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

    private void setOrUpdateAdapter (final List<YBDocListBean.YBDoc> dbDocList) {
        adapter = new CommonAdapter<YBDocListBean.YBDoc>(dbDocList) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_dbdoc,
                                    parent, false);
                    vh = new ViewHolder(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder) view.getTag();
                }
                YBDocListBean.YBDoc Doc = dbDocList.get(position);
                vh.tv_name.setText(Doc.RecTitle);
                vh.tv_time.setText(Doc.CreateDate);
                vh.tv_describe.setText(Doc.FileCode);
                vh.tv_step.setText("当前环节：" + Doc.CurrentStepName);

                if("上级文电".equals(Doc.DocType)){
                    vh.tv_fileDZ.setText("局");
                    vh.iv_img.setBackgroundResource(R.drawable.round_bg);
                }else if("段发公文".equals(Doc.DocType)){
                    vh.tv_fileDZ.setText("段");
                    vh.iv_img.setBackgroundResource(R.drawable.round_bg1);
                }else{
                    vh.tv_fileDZ.setText("段");
                    vh.iv_img.setBackgroundResource(R.drawable.round_bg1);
                }
                return view;
            }
        };
        listView.setAdapter(adapter);

    }

    static class ViewHolder {
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_time)
        TextView tv_time;
        @InjectView(R.id.tv_step)
        TextView tv_step;
        @InjectView(R.id.tv_describe)
        TextView tv_describe;
        @InjectView(R.id.tv_fileDZ)
        TextView tv_fileDZ;
        @InjectView(R.id.iv_img)
        ImageView iv_img;

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
