package com.publish.haoffice.app.Construct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.SPUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.Construct.ConBean;
import com.publish.haoffice.api.bean.Construct.RepBean;
import com.publish.haoffice.app.Repair.BaseFragmentapp;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/7/13.
 */
public class RepDetaileFragment extends BaseFragmentapp {

    @InjectView(R.id.tv_plandate)
    TextView tv_plandate;
    @InjectView(R.id.tv_plancode)
    TextView tv_plancode;
    @InjectView(R.id.tv_flow)
    TextView tv_flow;

    @InjectView(R.id.tv_line)
    TextView tv_line;
    @InjectView(R.id.tv_station)
    TextView tv_station;

    @InjectView(R.id.tv_cate)
    TextView tv_cate;
    @InjectView(R.id.tv_regstation)
    TextView tv_regstation;
    @InjectView(R.id.tv_contitle)
    TextView tv_contitle;
    @InjectView(R.id.tv_bigCategory)
    TextView tv_bigCategory;
    @InjectView(R.id.tv_workdept)
    TextView tv_workdept;
    @InjectView(R.id.tv_workuser)
    TextView tv_workuser;
    @InjectView(R.id.tv_realdate)
    TextView tv_realdate;
    @InjectView(R.id.tv_winstartdate)
    TextView tv_winstartdate;
    @InjectView(R.id.tv_winenddate)
    TextView tv_winenddate;

    @InjectView(R.id.tv_repaircate)
    TextView tv_repaircate;
    @InjectView(R.id.tv_wincate)
    TextView tv_wincate;
    @InjectView(R.id.tv_workmile)
    TextView tv_workmile;
    @InjectView(R.id.tv_blockmile)
    TextView tv_blockmile;
    @InjectView(R.id.tv_cardetail)
    TextView tv_cardetail;
    @InjectView(R.id.tv_repairnum)
    TextView tv_repairnum;
    @InjectView(R.id.tv_workcontent)
    TextView tv_workcontent;
    @InjectView(R.id.tv_elec)
    TextView tv_elec;
    @InjectView(R.id.tv_time)
    TextView tv_time;
    @InjectView(R.id.tv_workby)
    TextView tv_workby;
    @InjectView(R.id.tv_workdeptuser)
    TextView tv_workdeptuser;
    @InjectView(R.id.tv_remark)
    TextView tv_remark;

    @InjectView(R.id.ll_show)
    LinearLayout ll_show;
    @InjectView(R.id.tv_count)
    TextView tv_count;

    private String constUrl;
    private SPUtils spUtils;
    private String RepairID;
    private List<RepBean.Rep> RepList;

    @Override
    public View initView () {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_repdetail,null);
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
            RepairID = bundle.getString("RepairID");
        }
        spUtils = new SPUtils();
        constUrl = "http://" + spUtils.getString(getActivity(), Const.SERVICE_IP, "", Const.SP_CONSTRUCT) + ":" + spUtils.getString(getActivity(), Const.SERVICE_PORT, "", Const.SP_CONSTRUCT) + Const.SERVICE_PAGE2;

        getData ();
    }

    public void getData () {

        HashMap<String, String> map = new HashMap<>();
        map.put("RepairID", RepairID);
        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE, Const.CONSTRUCT_BINDSINGLEREPAIR, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    tv_count.setVisibility(View.GONE);
                    ll_show.setVisibility(View.VISIBLE);
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        RepBean jsonBean = JsonToBean.getJsonBean(string, RepBean.class);
                        RepList = jsonBean.ds;

                        if(RepList.size() == 0){
                            ll_show.setVisibility(View.GONE);
                            tv_count.setVisibility(View.VISIBLE);
                            tv_count.setText("暂无数据");
                        }else{
                            tv_count.setVisibility(View.GONE);

                            RepBean.Rep Rep = RepList.get(0);

                            tv_plandate.setText("计划日期：" + Rep.PlanDate);
                            tv_plancode.setText("计划号：" + Rep.PlanNum);
                            tv_flow.setText("流程跟踪：" + Rep.FlowTracking);
                            tv_line.setText("线别：" + Rep.WorkLine);

                            tv_station.setText("站/区段：" + Rep.Station);
                            tv_cate.setText("行别：" + Rep.LineType);
                            tv_regstation.setText("登记站：" + Rep.RegStation);
                            tv_contitle.setText("施工项目：" + Rep.Project);
                            tv_bigCategory.setText("项目大类：" + Rep.ProjectTypeName);
                            tv_workdept.setText("作业车间：" + Rep.WorkSpace);
                            tv_workuser.setText("作业人员：" + Rep.MonitorText);
                            tv_realdate.setText("实际施工日期：" + Rep.TrueConstructionDate);
                            tv_winstartdate.setText("天窗开始时间："+ Rep.SkylightStart);
                            tv_winenddate.setText("天窗结束时间：" + Rep.SkylightEnd);
                            tv_repaircate.setText("维修类型：" + Rep.RepairType);
                            tv_wincate.setText("天窗类型：" + Rep.RoofType);
                            tv_workmile.setText("施工里程：" + Rep.ConstMileage);
                            tv_blockmile.setText("封锁里程："+ Rep.LockMileage);
                            tv_cardetail.setText("路用车信息：" + Rep.RoadCarInfo);
                            tv_repairnum.setText("检修台数：" + Rep.RepairCount);
                            tv_workcontent.setText("施工内容：" + Rep.ConstContent);

                            tv_elec.setText("供电臂：" + Rep.PowerSupplyArm);
                            tv_time.setText("时间：" + Rep.ConstTime);
                            tv_workby.setText("配合单位：" + Rep.FitUnit);
                            tv_workdeptuser.setText("作业单位及负责人：" + Rep.DeptAndMan);

                            tv_remark.setText("备注：" + Rep.OtherNote);


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
