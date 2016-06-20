package com.publish.haoffice.app.Repair;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.img.ImgLoad;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.RepairInfo;
import com.publish.haoffice.api.bean.repair.RepairShow;
import com.publish.haoffice.api.dao.repair.Sys_deptDao;


import butterknife.ButterKnife;
import butterknife.InjectView;

public class RepairDetailActivity extends BaseActivity {
        @InjectView(R.id.ll_back)
        LinearLayout ll_back;
        @InjectView(R.id.tv_title)
        TextView tv_title;
        @InjectView(R.id.iv_pro)
        ImageView ivPro;
        @InjectView(R.id.tv_devicename)
        TextView tvDeviceName;
        @InjectView(R.id.tv_repair)
        TextView tvRepair;
        @InjectView(R.id.tvDetail)
        TextView tvDetail;

        @InjectView(R.id.tvRepairdept)
        TextView tvRepairdept;


        private RepairInfo repairShow;
        private Sys_deptDao deptDao;

        @Override
        public int bindLayout() {
            return R.layout.activity_retair_detail;
        }

        @Override
        public void initView(View view) {
            ButterKnife.inject(this);
            tv_title.setText("详情");
            ll_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    finishActivity(RepairDetailActivity.this);
                }
            });
        }

        @Override
        public void doBusiness(Context mContext) {
            deptDao = Sys_deptDao.getInstance(this);
            Intent intent = getIntent();
            repairShow = (RepairInfo) intent.getSerializableExtra("repairInfo");
            ImgLoad.initImageLoader(this).displayImage("file://"+Const.FILEPATH + repairShow.ImageUrl,ivPro);
            tvDeviceName.setText("设备名称：" + repairShow.EqptName);
            tvRepair.setText("故障发生时间：" + repairShow.FaultOccu_Time);
            tvDetail.setText("故障详情：" + repairShow.FaultAppearance);
            tvRepairdept.setText("维修部门："+ deptDao.getDeptInfo1(repairShow.RepairDeptID).dept_name);

        }

        @Override
        public void resume() {

        }

        @Override
        public void destroy() {
        }

}
