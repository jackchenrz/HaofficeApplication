package com.publish.haoffice.app.Construct;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.SPUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.Construct.ComworkBean;
import com.publish.haoffice.api.bean.Construct.ConBean;
import com.publish.haoffice.app.Repair.BaseFragmentapp;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/7/13.
 */
public class ComWorkStateFragment extends BaseFragmentapp {

    private String constructionID;

    @InjectView(R.id.tab)
    TableLayout tabLayout;

    @InjectView(R.id.ll_show)
    LinearLayout ll_show;
    @InjectView(R.id.tv_count)
    TextView tv_count;

    private String constUrl;
    private SPUtils spUtils;
    private List<ComworkBean.Comwork> ComworkList;
    private String flag;
    private HashMap<String, String> map;
    private String OtherJobID;
    private String RepairID;

    @Override
    public View initView () {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_comwork,null);
        ButterKnife.inject(this,view);
        tv_count.setVisibility(View.VISIBLE);
        ll_show.setVisibility(View.GONE);
        tv_count.setText("正在加载中");
        return view;
    }

    /**
     * 添加审批记录
     */
    protected void addTab() {
        WindowManager systemService = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        for (int i = 0; i < ComworkList.size(); i++) {
            TableRow tr = new TableRow( getActivity());
            tr.setLayoutParams(new TableLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tv = new TextView( getActivity());
            android.widget.TableRow.LayoutParams params = new TableRow.LayoutParams(systemService.getDefaultDisplay().getWidth()/14, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
            tv.setLayoutParams(params);
            tv.setText((i + 1) + "");
            tr.addView(tv);

            TextView tv1 = new TextView( getActivity());
            android.widget.TableRow.LayoutParams params1 = new TableRow.LayoutParams( systemService.getDefaultDisplay().getWidth()/7, ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
            tv1.setLayoutParams(params1);
            tv1.setText(ComworkList.get(i).Fkr);
            tr.addView(tv1);

            TextView tv2 = new TextView( getActivity());
            android.widget.TableRow.LayoutParams params3 = new TableRow.LayoutParams(systemService.getDefaultDisplay().getWidth()/6, ViewGroup.LayoutParams.WRAP_CONTENT);
            params3.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
            tv2.setLayoutParams(params3);
            tv2.setText(ComworkList.get(i).FkDate);
            tr.addView(tv2);

            TextView tv3 = new TextView( getActivity());
            android.widget.TableRow.LayoutParams params4 = new TableRow.LayoutParams(systemService.getDefaultDisplay().getWidth()/6, ViewGroup.LayoutParams.WRAP_CONTENT);
            params4.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
            tv3.setLayoutParams(params4);
            tv3.setText(ComworkList.get(i).InfoType);
            tr.addView(tv3);

            TextView tv4 = new TextView( getActivity());
            android.widget.TableRow.LayoutParams params5 = new TableRow.LayoutParams(systemService.getDefaultDisplay().getWidth()/6, ViewGroup.LayoutParams.WRAP_CONTENT);
            params5.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
            tv4.setLayoutParams(params5);
            tv4.setText(ComworkList.get(i).FkInfo);
            tr.addView(tv4);

            TextView tv5 = new TextView( getActivity());
            android.widget.TableRow.LayoutParams params7 = new TableRow.LayoutParams(systemService.getDefaultDisplay().getWidth()/6, ViewGroup.LayoutParams.WRAP_CONTENT);
            params7.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
            tv5.setLayoutParams(params7);
            tv5.setText(ComworkList.get(i).CreateDate);
            tr.addView(tv5);

            tabLayout.addView(tr);
            ImageView iv = new ImageView( getActivity());
            TableLayout.LayoutParams params6 = new TableLayout.LayoutParams(systemService.getDefaultDisplay().getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            params6.gravity = Gravity.CENTER;
            iv.setLayoutParams(params6);
            iv.setBackgroundResource(R.drawable.line);
            tabLayout.addView(iv);
        }
    }

    @Override
    public void initData () {
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){

            flag = bundle.getString("flag");
            if("1".equals(flag)){
                constructionID = bundle.getString("ConstructionID");
            }else if("2".equals(flag)){
                RepairID = bundle.getString("RepairID");
            }else if("3".equals(flag)){
                OtherJobID = bundle.getString("OtherJobID");
            }

        }
        spUtils = new SPUtils();
        constUrl = "http://" + spUtils.getString(getActivity(), Const.SERVICE_IP, "", Const.SP_CONSTRUCT) + ":" + spUtils.getString(getActivity(), Const.SERVICE_PORT, "", Const.SP_CONSTRUCT) + Const.SERVICE_PAGE2;
        map = new HashMap<>();

        if("1".equals(flag)){
            map.put("ConstructionID", constructionID);
            getData (Const.CONSTRUCT_BINDSINGLECONSTLOG);
        }else if("2".equals(flag)){
            map.put("RepairID", RepairID);
            getData (Const.CONSTRUCT_BINDSINGLEREPAIRLOG);
        }else if("3".equals(flag)){
            map.put("OtherJobID", OtherJobID);
            getData (Const.CONSTRUCT_BINDSINGLEOTHERJOBLOG);
        }

    }

    public void getData (String methodName) {

        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE,methodName , map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    tv_count.setVisibility(View.GONE);
                    ll_show.setVisibility(View.VISIBLE);
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        ComworkBean jsonBean = JsonToBean.getJsonBean(string, ComworkBean.class);
                        ComworkList = jsonBean.ds;

                        if(ComworkList.size() == 0){
                            ll_show.setVisibility(View.GONE);
                            tv_count.setVisibility(View.VISIBLE);
                            tv_count.setText("暂无数据");
                        }else{
                            tv_count.setVisibility(View.GONE);
                            addTab();
                        }

                    }else{
                        ll_show.setVisibility(View.GONE);
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }
                }
            }

            @Override
            public void onFailure (String result) {
                tv_count.setVisibility(View.VISIBLE);
                ll_show.setVisibility(View.GONE);
                tv_count.setText("联网失败");
            }
        });
    }
}
