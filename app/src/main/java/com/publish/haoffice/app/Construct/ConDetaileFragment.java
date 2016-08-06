package com.publish.haoffice.app.Construct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.Construct.ConBean;
import com.publish.haoffice.api.bean.Construct.TodayConBean;
import com.publish.haoffice.app.Repair.BaseFragmentapp;
import com.publish.haoffice.app.office.DocDetailActivity;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/7/13.
 */
public class ConDetaileFragment extends BaseFragmentapp {
    private String constructionID;

    @InjectView(R.id.tv_code)
    TextView tv_code;
    @InjectView(R.id.tv_lev)
    TextView tv_lev;
    @InjectView(R.id.tv_bigCategory)
    TextView tv_bigCategory;

    @InjectView(R.id.tv_smallCategory)
    TextView tv_smallCategory;
    @InjectView(R.id.tv_workdept)
    TextView tv_workdept;

    @InjectView(R.id.tv_lookman)
    TextView tv_lookman;
    @InjectView(R.id.tv_realdate)
    TextView tv_realdate;
    @InjectView(R.id.tv_winstartdate)
    TextView tv_winstartdate;
    @InjectView(R.id.tv_winenddate)
    TextView tv_winenddate;
    @InjectView(R.id.tv_regstation)
    TextView tv_regstation;
    @InjectView(R.id.tv_prostation)
    TextView tv_prostation;
    @InjectView(R.id.tv_workby)
    TextView tv_workby;
    @InjectView(R.id.tv_yesorno)
    TextView tv_yesorno;
    @InjectView(R.id.tv_line)
    TextView tv_line;
    @InjectView(R.id.tv_cate)
    TextView tv_cate;

    @InjectView(R.id.tv_contitle)
    TextView tv_contitle;
    @InjectView(R.id.tv_workdate)
    TextView tv_workdate;
    @InjectView(R.id.tv_worktime)
    TextView tv_worktime;
    @InjectView(R.id.tv_workaddress)
    TextView tv_workaddress;
    @InjectView(R.id.tv_workcontent)
    TextView tv_workcontent;
    @InjectView(R.id.tv_car)
    TextView tv_car;
    @InjectView(R.id.tv_eqpt)
    TextView tv_eqpt;
    @InjectView(R.id.tv_tran)
    TextView tv_tran;
    @InjectView(R.id.tv_workuser)
    TextView tv_workuser;
    @InjectView(R.id.tv_remark)
    TextView tv_remark;

    @InjectView(R.id.ll_show)
    LinearLayout ll_show;
    @InjectView(R.id.tv_count)
    TextView tv_count;

    private String constUrl;
    private SPUtils spUtils;
    private List<ConBean.Con> ConList;

    @Override
    public View initView () {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_condetail,null);
        ButterKnife.inject(this,view);
        tv_count.setVisibility(View.VISIBLE);
        ll_show.setVisibility(View.GONE);
        tv_count.setText("正在加载中");
        return view;
    }

    @Override
    public void initData () {

        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
            constructionID = bundle.getString("ConstructionID");
        }
        spUtils = new SPUtils();
        constUrl = "http://" + spUtils.getString(getActivity(), Const.SERVICE_IP, "", Const.SP_CONSTRUCT) + ":" + spUtils.getString(getActivity(), Const.SERVICE_PORT, "", Const.SP_CONSTRUCT) + Const.SERVICE_PAGE2;

        getData ();

    }


    public void getData () {

        HashMap<String, String> map = new HashMap<>();
        map.put("ConstructionID", constructionID);
        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE, Const.CONSTRUCT_BINDSINGLECONST, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    tv_count.setVisibility(View.GONE);
                    ll_show.setVisibility(View.VISIBLE);
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        ConBean jsonBean = JsonToBean.getJsonBean(string, ConBean.class);
                        ConList = jsonBean.ds;

                        if(ConList.size() == 0){
                            ll_show.setVisibility(View.GONE);
                            tv_count.setVisibility(View.VISIBLE);
                            tv_count.setText("暂无数据");
                        }else{
                            tv_count.setVisibility(View.GONE);

                            ConBean.Con con = ConList.get(0);

                            tv_code.setText("编号：" + con.SerialNo);
                            tv_lev.setText("等级：" + con.WorkLevel);
                            tv_bigCategory.setText("项目大类：" + con.ProjectTypeName);
                            tv_smallCategory.setText("项目小类：" + con.ConstructionTypeName);
                            tv_workdept.setText("作业车间：" + con.WorkSpace);
                            tv_lookman.setText("监控干部：" + con.MonitorText);
                            tv_realdate.setText("实际施工日期：" + con.TrueConstructionDate);
                            tv_winstartdate.setText("天窗开始时间："+ con.SkylightStart);
                            tv_winenddate.setText("天窗结束时间：" + con.SkylightEnd);
                            tv_regstation.setText("登记站：" + con.RegStation);
                            tv_prostation.setText("探测站：" + con.StationName);
                            tv_workby.setText("施工依据：" + con.ConstBasis);
                            tv_yesorno.setText("是否邻近施工：" + con.IsBC);
                            tv_line.setText("线路：" + con.WorkLine);
                            tv_cate.setText("行别：" + con.LineType);
                            tv_contitle.setText("施工项目：" + con.ConstructionName);
                            tv_workdate.setText("施工日期：" + con.ConstructionDate);
                            tv_worktime.setText("施工时间："+ con.ConstructionTime);
                            tv_workaddress.setText("施工地点(含站)施工里程：" + con.ConstructionPlace);
                            tv_workcontent.setText("施工内容及影响范围：" + con.ConstructionContent);
                            tv_car.setText("限速及行车方式变化：" + con.SpeedLimit);
                            tv_eqpt.setText("设备变化：" + con.EquipmentChanges);
                            tv_tran.setText("运输组织：" + con.TransportOrg);
                            tv_workuser.setText("施工单位及负责人：" + con.ConstructionDept);
                            tv_remark.setText("备注：" + con.OtherNote);


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
