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
import com.publish.haoffice.api.bean.repair.ToRepairedBean;
import com.publish.haoffice.api.dao.repair.FiveT_InfoDao;
import com.publish.haoffice.api.dao.repair.TechEqptDao;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/6/21.
 */
public class ToRepairDetailActivity extends BaseActivity {
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.iv_pro)
    ImageView ivPro;
    @InjectView(R.id.tv_devicename)
    TextView tvDevicename;
    @InjectView(R.id.tv_deviceaddress)
    TextView tvDeviceaddress;
    @InjectView(R.id.tv_useteam)
    TextView tvUseteam;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.tv_problem)
    TextView tvProblem;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.tv_finishtime)
    TextView tvFinishtime;
    @InjectView(R.id.tv_deptname)
    TextView tvDeptname;
    @InjectView(R.id.btn_torepair)
    Button btnTorepair;
    private ToRepairedBean.ToRepair toRepair;
    private TechEqptDao techEqptDao;
    private FiveT_InfoDao FiveTInfoDao;
    private int flag;
    public static ToRepairDetailActivity instance = null;

    @Override
    public int bindLayout () {
        return R.layout.activity_torepair_detail;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
        tv_title.setText("详情");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(ToRepairDetailActivity.this);
            }
        });
    }

    @Override
    public void doBusiness (Context mContext) {
        instance = this;
        Intent intent = getIntent();
        toRepair = (ToRepairedBean.ToRepair) intent.getSerializableExtra("toRepair");
        flag = intent.getIntExtra("flag5t", 0);
        techEqptDao = TechEqptDao.getInstance(this);
        FiveTInfoDao = FiveT_InfoDao.getInstance(this);

        tvDevicename.setText("设备名称：" + toRepair.EqptName);

        ImgLoad.initImageLoader(this).displayImage(toRepair.ImageUrlPath,ivPro);
        if(flag == 0){
            tvDeviceaddress.setText("设备地点：" + techEqptDao.getTechEqpt(toRepair.EqptID).EqptAddress);
        }else{
            tvDeviceaddress.setText("设备地点：" + FiveTInfoDao.getFiveTEqpt1(toRepair.EqptID).EqptAddress);
        }

        tvUseteam.setText("使用部门：" + toRepair.dept_name);
        tvTime.setText("故障发生时间：" + toRepair.FaultOccu_Time);
        tvProblem.setText("故障现象描述：" + toRepair.FaultAppearance);
        tvStatus.setText("故障状态 ："+toRepair.FaultStatus);
        tvFinishtime.setText("处理完成时间 ："+toRepair.RepairFinishTime);
        tvDeptname.setText("维修部门："+toRepair.repair_dept_name);

        btnTorepair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToRepairDetailActivity.this,ToRepairInputActivity.class);
                intent.putExtra("toRepair", toRepair);
                intent.putExtra("flag5t", flag);
                startActivity(intent);
                finishActivity(ToRepairDetailActivity.this);
                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });


    }

    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }
}
