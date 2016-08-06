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
import com.publish.haoffice.api.bean.Construct.ConWorkCardBean;
import com.publish.haoffice.app.Repair.BaseFragmentapp;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/7/13.
 */
public class ConWorkCardFragment extends BaseFragmentapp {
    private String constructionID;

    @InjectView(R.id.tv_code)
    TextView tv_code;
    @InjectView(R.id.tv_contitle)
    TextView tv_contitle;
    @InjectView(R.id.tv_lev)
    TextView tv_lev;
    @InjectView(R.id.tv_date)
    TextView tv_date;

    @InjectView(R.id.tv_time)
    TextView tv_time;
    @InjectView(R.id.tv_weather)
    TextView tv_weather;
    @InjectView(R.id.tv_workby)
    TextView tv_workby;
    @InjectView(R.id.tv_workcontent)
    TextView tv_workcontent;
    @InjectView(R.id.tv_affect)
    TextView tv_affect;
    @InjectView(R.id.tv_workuser)
    TextView tv_workuser;
    @InjectView(R.id.tv_date1)
    TextView tv_date1;
    @InjectView(R.id.tv_time1)
    TextView tv_time1;
    @InjectView(R.id.tv_address)
    TextView tv_address;
    @InjectView(R.id.tv_users)
    TextView tv_users;


    @InjectView(R.id.tv_regdate)
    TextView tv_regdate;
    @InjectView(R.id.tv_regaddress)
    TextView tv_regaddress;
    @InjectView(R.id.tv_affect1)
    TextView tv_affect1;
    @InjectView(R.id.tv_workall)
    TextView tv_workall;
    @InjectView(R.id.tv_date2)
    TextView tv_date2;
    @InjectView(R.id.tv_time2)
    TextView tv_time2;
    @InjectView(R.id.tv_address2)
    TextView tv_address2;
    @InjectView(R.id.tv_users2)
    TextView tv_users2;
    @InjectView(R.id.tv_tran_tran)
    TextView tv_tran_tran;
    @InjectView(R.id.tv_tran_car)
    TextView tv_tran_car;
    @InjectView(R.id.tv_risk)
    TextView tv_risk;

    @InjectView(R.id.ll_show)
    LinearLayout ll_show;
    @InjectView(R.id.tv_count)
    TextView tv_count;

    private String constUrl;
    private SPUtils spUtils;
    private List<ConWorkCardBean.ConWorkCard> ConWorkCardList;


    @Override
    public View initView () {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_conworkcard,null);
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
        HttpConn.callService(constUrl, Const.SERVICE_NAMESPACE, Const.CONSTRUCT_BINDSINGLECONSTFORM, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    tv_count.setVisibility(View.GONE);
                    ll_show.setVisibility(View.VISIBLE);
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){

                        LogUtils.d("ckj",string);
                        ConWorkCardBean jsonBean = JsonToBean.getJsonBean(string, ConWorkCardBean.class);
                        ConWorkCardList = jsonBean.ds;

                        if(ConWorkCardList.size() == 0){
                            ll_show.setVisibility(View.GONE);
                            tv_count.setVisibility(View.VISIBLE);
                            tv_count.setText("暂无数据");
                        }else{
                            tv_count.setVisibility(View.GONE);

                            ConWorkCardBean.ConWorkCard con = ConWorkCardList.get(0);

                            tv_code.setText("编号：" + con.SerialNo);
                            tv_contitle.setText("施工项目：" + con.ConstructionName);
                            tv_lev.setText("等级：" + con.WorkLevel);
                            tv_date.setText("日期：" + con.WorkDate);
                            tv_time.setText("时间：" + con.WorkTime);
                            tv_weather.setText("天气：" + con.TianQi);
                            tv_workby.setText("施工依据：" + con.Sgyj);
                            tv_workcontent.setText("施工内容：" + con.Sgnr);
                            tv_affect.setText("影响范围："+ con.Yxfw);
                            tv_workuser.setText("施工(配合)单位：" + con.Phdw);
                            tv_date1.setText("日期：" + con.ADate);
                            tv_time1.setText("时间：" + con.ATime);
                            tv_address.setText("会议地点：" + con.AHydd);
                            tv_users.setText("参加人：" + con.ACjrText);
                            tv_regdate.setText("登记时间：" + con.Adjsj);
                            tv_regaddress.setText("登记地点：" + con.Adjdd);

                            tv_affect1.setText("影响范围：" + con.AYxfw);
                            tv_workall.setText("工作量："+ con.AGzl);
                            tv_date2.setText("日期：" + con.BDate);
                            tv_time2.setText("时间：" + con.BTime);
                            tv_address2.setText("地点：" + con.Bdd);
                            tv_users2.setText("参加人：" + con.BCjryText);
                            tv_tran_tran.setText("交通方式(火车)：" + con.BJtfsHc);
                            tv_tran_car.setText("交通方式(汽车)：" + con.BJtfsQc);
                            tv_risk.setText("安全风险点及卡控措施：" + con.BKkcs);


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
