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
import com.msystemlib.utils.ThreadUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.DBDocListBean;
import com.publish.haoffice.api.bean.repair.RepairInfo;
import com.publish.haoffice.app.LoginActivity;
import com.publish.haoffice.app.Repair.BaseFragmentapp;
import com.publish.haoffice.app.Repair.RepairDetailActivity;
import com.publish.haoffice.application.SysApplication;
import com.publish.haoffice.view.FocusTextView;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OfficeNoFragment extends BaseFragmentapp implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.id_listview)
    ListView listView;
    @InjectView(R.id.id_swipe_ly)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.tv_count)
    TextView tv_count;
    private SPUtils spUtils;
    private String officeUrl;
    private List<DBDocListBean.Doc> DBDocList;
    private CommonAdapter<DBDocListBean.Doc> adapter;

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
                DBDocListBean.Doc doc = DBDocList.get(position);
                Intent intent = new Intent(getActivity(),
                        DocDetailActivity.class);
                intent.putExtra("doc", doc);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in,
                        R.anim.base_slide_remain);


            }
        });
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
    public void getData () {

        HashMap<String, String> map = new HashMap<>();
        map.put("UserID", SysApplication.gainData(Const.USERID).toString().trim());
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETDBDOCLIST, map, new IWebServiceCallBack() {
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
                        DBDocListBean jsonBean = JsonToBean.getJsonBean(string, DBDocListBean.class);
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

    private void setOrUpdateAdapter (final List<DBDocListBean.Doc> dbDocList) {
        adapter = new CommonAdapter<DBDocListBean.Doc>(dbDocList) {

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
                DBDocListBean.Doc Doc = dbDocList.get(position);
                vh.tv_name.setText(Doc.FileTitle);
                vh.tv_time.setText(Doc.CreateDate);
                vh.tv_describe.setText(Doc.FileCode);
                vh.tv_fileDZ.setText(Doc.FileDZ);
                return view;
            }
        };
        listView.setAdapter(adapter);

    }

    static class ViewHolder {
        @InjectView(R.id.tv_name)
        FocusTextView tv_name;
        @InjectView(R.id.tv_time)
        TextView tv_time;
        @InjectView(R.id.tv_describe)
        TextView tv_describe;
        @InjectView(R.id.tv_fileDZ)
        TextView tv_fileDZ;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }


    @Override
    public void onRefresh () {
        getData();
    }

}
