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
import com.msystemlib.utils.SPUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.DeptInfoBean;
import com.publish.haoffice.api.bean.repair.ToRepairSave;
import com.publish.haoffice.api.bean.repair.ToRepairedBean;
import com.publish.haoffice.api.dao.repair.Eqpt_InfoDao;
import com.publish.haoffice.api.dao.repair.FiveT_InfoDao;
import com.publish.haoffice.api.dao.repair.Sys_deptDao;
import com.publish.haoffice.api.dao.repair.Sys_userDao;
import com.publish.haoffice.api.dao.repair.TechEqptDao;
import com.publish.haoffice.api.dao.repair.ToRepairDao;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/6/25.
 */
public class ToRepairHanderDetailActivity extends BaseActivity {
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
    private ToRepairSave toRepairSave;
    private Sys_deptDao sys_deptDao;
    private Sys_userDao sys_userDao;
    private ToRepairDao toRepairDao;
    private com.publish.haoffice.api.dao.repair.Eqpt_InfoDao eqpt_InfoDao;
    private SPUtils spUtils;

    @Override
    public int bindLayout () {
        return R.layout.activity_torepair_detail;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
        btnTorepair.setVisibility(View.GONE);
        tv_title.setText("详情");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(ToRepairHanderDetailActivity.this);
            }
        });
    }

    @Override
    public void doBusiness (Context mContext) {
        spUtils = new SPUtils();
        Intent intent = getIntent();
        int flag = intent.getIntExtra("flag", 0);

        techEqptDao = TechEqptDao.getInstance(this);
        FiveTInfoDao = FiveT_InfoDao.getInstance(this);
        sys_deptDao = Sys_deptDao.getInstance(this);
        sys_userDao = Sys_userDao.getInstance(this);
        eqpt_InfoDao = Eqpt_InfoDao.getInstance(this);
        toRepairDao = ToRepairDao.getInstance(this);


        if(flag == 1){
            toRepair = (ToRepairedBean.ToRepair) intent.getSerializableExtra("repairHandler");
            tvDevicename.setText("设备名称：" + toRepair.EqptName);

            ImgLoad.initImageLoader(this).displayImage(toRepair.ImageUrlPath,ivPro);
            if("Tech".equals(toRepair.RepairType)){
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
        }else if(flag == 0){


            String RepairID = intent.getStringExtra("ToRepairSave");
            toRepairSave = toRepairDao.getToRepairSave(RepairID);
            tvDevicename.setText("设备名称：" + toRepairSave.EqptName);

            ImgLoad.initImageLoader(this).displayImage(toRepairSave.ImageUrl,ivPro);
            if("Tech".equals(toRepairSave.RepairType)){
                String eqptInfoID = eqpt_InfoDao.getEqptInfo(toRepairSave.EqptName).EqptInfoID;
                String eqptAddress = techEqptDao.getTechEqpt1(eqptInfoID).EqptAddress;
                tvDeviceaddress.setText("设备地点：" + eqptAddress);
            }else if("5T".equals(toRepairSave.RepairType)){
                tvDeviceaddress.setText("设备地点：" + FiveTInfoDao.getFiveTEqpt(toRepairSave.EqptName).EqptAddress);
            }
            DeptInfoBean.DeptInfo deptInfo1 = sys_deptDao.getDeptInfo1(sys_userDao.getUserInfo(
                    spUtils.getString(ToRepairHanderDetailActivity.this, Const.USERNAME, "", Const.SP_REPAIR)).dept_id);
            tvUseteam.setText("使用部门：" + deptInfo1.dept_name);
            tvTime.setText("故障发生时间：" + toRepairSave.FaultOccu_Time);
            tvProblem.setText("故障现象描述：" + toRepairSave.FaultAppearance);
            tvStatus.setText("故障状态 ："+toRepairSave.FaultStatus);
            tvFinishtime.setText("处理完成时间 ："+toRepairSave.RepairFinishTime);
            tvDeptname.setText("维修部门："+deptInfo1.dept_name);
        }



    }

    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }
}
