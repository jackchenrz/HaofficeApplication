package com.publish.haoffice.app.Repair;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.img.ImgLoad;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.Search5tBean;
import com.publish.haoffice.api.bean.repair.SearchTechBean;
import com.publish.haoffice.api.bean.repair.ToRepairedBean;
import com.publish.haoffice.api.dao.repair.FiveT_InfoDao;
import com.publish.haoffice.api.dao.repair.TechEqptDao;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/6/21.
 */
public class SearchDetailActivity extends BaseActivity {
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.ivImg)
    ImageView ivImg;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id.tv_address)
    TextView tv_address;
    @InjectView(R.id.tv_state)
    TextView tv_state;
    @InjectView(R.id.tv_faultdesc)
    TextView tv_faultdesc;
    @InjectView(R.id.tv_fault)
    TextView tv_fault;
    @InjectView(R.id.tv_usedept)
    TextView tv_usedept;
    @InjectView(R.id.tv_repairdept)
    TextView tv_repairdept;
    @InjectView(R.id.tv_occurtime)
    TextView tv_occurtime;
    @InjectView(R.id.tv_repairtime)
    TextView tv_repairtime;
    @InjectView(R.id.tv_type)
    TextView tv_type;
    @InjectView(R.id.tv_catory)
    TextView tv_catory;


    private TechEqptDao techEqptDao;
    private FiveT_InfoDao FiveTInfoDao;
    private int flag;
    private int falgSearch;
    private SearchTechBean.SearchTech searchTech;
    private Search5tBean.Search5t search5t;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;

    @Override
    public int bindLayout () {
        return R.layout.activity_search_detail;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
        tv_title.setText("详情");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(SearchDetailActivity.this);
            }
        });
    }

    @Override
    public void doBusiness (Context mContext) {
        imageLoader = ImgLoad.initImageLoader(this);
        Intent intent = getIntent();
        falgSearch = intent.getIntExtra("flag5t",0);

        if(falgSearch == 0){
            searchTech = (SearchTechBean.SearchTech)intent.getSerializableExtra("searchTech");
            tv_name.setText("设备名称：" + searchTech.EqptName);
            tv_address.setText("设备处所：" + searchTech.SettingAddr);
            tv_state.setText("故障状态：" + searchTech.FaultStatus);
            tv_faultdesc.setText("故障描述：" + searchTech.FaultAppearance);
            tv_fault.setText("故障处理：" + searchTech.FaultHandle);
            tv_usedept.setText("使用部门：" + searchTech.dept_name);
            tv_repairdept.setText("维修部门：" + searchTech.repair_dept_name);
            tv_occurtime.setText("发生时间：" + searchTech.FaultOccu_Time);
            tv_repairtime.setText("修复时间：" + searchTech.RepairFinishTime);
            tv_type.setText("故障类型：" + searchTech.FaultType);
            tv_catory.setText("责任分类：" + searchTech.FaultReason);

            if(searchTech.ImageUrl != null || "".equals(searchTech.ImageUrl)) {
                imageLoader.displayImage("file://" + Const.FILEPATH + searchTech.ImageUrl, ivImg);
            }


        }else if(falgSearch == 1){
            search5t = (Search5tBean.Search5t)intent.getSerializableExtra("search5t");
            tv_name.setText("设备名称：" + search5t.ProbeStation);
            tv_address.setText("设备处所：" + search5t.EqptAddress);
            tv_state.setText("故障状态：" + search5t.FaultStatus);
            tv_faultdesc.setText("故障描述：" + search5t.FaultAppearance);
            tv_fault.setText("故障处理：" + search5t.FaultHandle);
            tv_usedept.setText("使用部门：" + search5t.dept_name);
            tv_repairdept.setText("维修部门：" + search5t.repair_dept_name);
            tv_occurtime.setText("发生时间：" + search5t.FaultOccu_Time);
            tv_repairtime.setText("修复时间：" + search5t.RepairFinishTime);
            tv_type.setText("故障类型：" + search5t.FaultType);
            tv_catory.setText("责任分类：" + search5t.FaultReason);

            if(search5t.ImageUrl != null || "".equals(search5t.ImageUrl)) {
                imageLoader.displayImage("file://" + Const.FILEPATH + search5t.ImageUrl, ivImg);
            }
        }


    }

    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }
}
