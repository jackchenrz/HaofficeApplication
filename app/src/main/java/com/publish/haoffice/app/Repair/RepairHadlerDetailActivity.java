package com.publish.haoffice.app.Repair;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.img.ImgLoad;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.RepairHandlerBean;
import com.publish.haoffice.api.bean.repair.RepairInfo;
import com.publish.haoffice.api.dao.repair.Sys_deptDao;

import net.tsz.afinal.annotation.view.ViewInject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RepairHadlerDetailActivity extends BaseActivity {
    @InjectView(R.id.iv_pro)
    ImageView ivPro;
    @InjectView(R.id.tv_devicename)
    TextView tvDeviceName;
    @InjectView(R.id.tv_repair)
    TextView tvRepair;
    @InjectView(R.id.tvDetail)
    TextView tvDetail;
    @InjectView(R.id.tv_repairuser)
    TextView tv_repairuser;
    @InjectView(R.id.tv_finishtime)
    TextView tv_finishtime;
    @InjectView(R.id.tvRepairdept)
    TextView tvRepairdept;
    @InjectView(R.id.tv_handler)
    TextView tv_handler;
    private RepairHandlerBean.RepairHandler repairShow;
    private Sys_deptDao deptDao;


    @Override
    public int bindLayout () {
        return R.layout.activity_handlerdetail;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
    }

    @Override
    public void doBusiness (Context mContext) {

        Intent intent = getIntent();
        repairShow = (RepairHandlerBean.RepairHandler) intent.getSerializableExtra("repairHandler");
        deptDao = Sys_deptDao.getInstance(this);
        tvDeviceName.setText("设备名称：" + repairShow.EqptName);
        tvRepair.setText("故障发生时间：" + repairShow.FaultOccu_Time);
        tvDetail.setText("故障详情：" + repairShow.FaultAppearance);

        ImgLoad.initImageLoader(this).displayImage(repairShow.ImageUrlPath,ivPro);
        tvRepairdept.setText("维修部门："+ deptDao.getDeptInfo1(repairShow.RepairDeptID).dept_name);
        if(repairShow.RepairUserName == null || "".equals(repairShow.RepairUserName)){
            tv_repairuser.setText("维修人：" + "未维修");
        }else{
            tv_repairuser.setText("维修人：" + repairShow.RepairUserName);
        }
        if(repairShow.RepairFinishTime == null || "".equals(repairShow.RepairFinishTime)){
            tv_finishtime.setText("完成时间：" + "未完成");
        }else{
            tv_finishtime.setText("完成时间：" + repairShow.RepairFinishTime);
        }
        if(repairShow.FaultHandle == null || "".equals(repairShow.FaultHandle)){
            tv_handler.setText("处理信息：" + "正在处理");
        }else{
            tv_handler.setText("处理信息：" + repairShow.FaultHandle);
        }
    }

    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }
}
