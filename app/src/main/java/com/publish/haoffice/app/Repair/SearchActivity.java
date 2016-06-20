package com.publish.haoffice.app.Repair;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.common.adapter.CommonAdapter;
import com.msystemlib.utils.ThreadUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.DeptInfoBean;
import com.publish.haoffice.api.bean.repair.EqptInfoBean;
import com.publish.haoffice.api.bean.repair.FiveTEqptInfoBean;
import com.publish.haoffice.api.dao.repair.Eqpt_InfoDao;
import com.publish.haoffice.api.dao.repair.FiveT_InfoDao;
import com.publish.haoffice.api.dao.repair.Sys_deptDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchActivity extends BaseActivity {

        @InjectView(R.id.tv_search)
        TextView tvSearch;
        @InjectView(R.id.et_search)
        AutoCompleteTextView etSearch;
        @InjectView(R.id.lv_search)
        ListView lvSearch;
        @InjectView(R.id.tv_title)
        TextView tv_title;
        @InjectView(R.id.btn_ok)
        Button btnOk;
        @InjectView(R.id.ll_back)
        LinearLayout ll_back;

        private Eqpt_InfoDao eqptDao;
        private FiveT_InfoDao fiveTDao;
        private Sys_deptDao deptDao;

        private List<EqptInfoBean.EqptInfo> allEqptInfoList;
        private List<FiveTEqptInfoBean.FiveTEqpt> allFiveTEqptList;
        private List<DeptInfoBean.DeptInfo> allDeptList;

        private List<String> list = new ArrayList<String>();
        private final int FILL_DEPT = 100;
        private final int FILL_TECH = 101;
        private final int FILL_5T = 102;

        private Handler handler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case FILL_DEPT:
                        if (allDeptList.size() != 0) {

                            lvSearch.setAdapter(new CommonAdapter<DeptInfoBean.DeptInfo>(allDeptList) {

                                @Override
                                public View getView(int position, View convertView,
                                                    ViewGroup parent) {

                                    TextView view;
                                    if (convertView == null) {
                                        view = new TextView(SearchActivity.this);
                                    } else {
                                        view = (TextView) convertView;
                                    }
                                    view.setText(allDeptList.get(position).dept_name);
                                    view.setTextSize(18);
                                    return view;
                                }
                            });
                        }
                        break;
                    case FILL_TECH:
                        if (allEqptInfoList.size() != 0) {


                            lvSearch.setAdapter(new CommonAdapter<EqptInfoBean.EqptInfo>(allEqptInfoList) {

                                @Override
                                public View getView(int position, View convertView,
                                                    ViewGroup parent) {

                                    TextView view;
                                    if (convertView == null) {
                                        view = new TextView(SearchActivity.this);
                                    } else {
                                        view = (TextView) convertView;
                                    }
                                    view.setText(allEqptInfoList.get(position).EqptName);
                                    view.setTextSize(18);
                                    return view;
                                }
                            });
                        }
                        break;
                    case FILL_5T:
                        if (allFiveTEqptList.size() != 0) {


                            lvSearch.setAdapter(new CommonAdapter<FiveTEqptInfoBean.FiveTEqpt>(allFiveTEqptList) {

                                @Override
                                public View getView(int position, View convertView,
                                                    ViewGroup parent) {
                                    TextView view;
                                    if (convertView == null) {
                                        view = new TextView(SearchActivity.this);
                                    } else {
                                        view = (TextView) convertView;
                                    }
                                    view.setText(allFiveTEqptList.get(position).EqptAddress);
                                    view.setTextSize(18);
                                    return view;
                                }
                            });
                        }
                        break;
                }
            };
        };


        private void init(final int flag) {
            if (flag == 0) {
                if (flag5t == 0) {
                    tv_title.setText("机械设备");
                }else if(flag5t == 1){
                    tv_title.setText("行安设备");
                }
                tvSearch.setText("设备名称 ：");
                fillDevNameData();
            }

            if (flag == 1) {
                tv_title.setText("维修部门");
                tvSearch.setText("维修部门 ：");
                fillDeptData();
            }

            ll_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    finishActivity(SearchActivity.this);
                }
            });
            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String searchContent = etSearch.getText().toString().trim();
                    if (flag == 0 && flag5t == 0) {
                        Intent intent1 = new Intent();
                        intent1.putExtra("devicename", searchContent);
                        setResult(Const.DEVICENAME, intent1);
                        finish();
                        overridePendingTransition(0, R.anim.base_slide_right_out);

                    } else if (flag == 0 && flag5t == 1) {
                        Intent intent1 = new Intent();
                        intent1.putExtra("devicename", searchContent);
                        setResult(Const.DEVICENAME, intent1);
                        finish();
                        overridePendingTransition(0, R.anim.base_slide_right_out);
                    } else if (flag == 1) {
                        Intent intent1 = new Intent();
                        intent1.putExtra("repairdepartment", searchContent);
                        setResult(Const.REPAIRDEPARTMENTLIST, intent1);
                        finish();
                        overridePendingTransition(0, R.anim.base_slide_right_out);
                    }
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.search_item, list);
            etSearch.setThreshold(1);
            etSearch.setAdapter(adapter);

            lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (flag == 0 && flag5t == 0) {
                        etSearch.setText(allEqptInfoList.get(position).EqptName);
                    } else if (flag == 0 && flag5t == 1) {
                        etSearch.setText(allFiveTEqptList.get(position).EqptAddress);
                    } else if (flag == 1) {
                        etSearch.setText(allDeptList.get(position).dept_name);
                    }
                }
            });
        }

        private void fillDeptData() {
            ThreadUtils.runInBackground(new Runnable() {

                @Override
                public void run() {
                    allDeptList = deptDao.getAllDeptList(1);
                    list.clear();
                    for (DeptInfoBean.DeptInfo deptInfo : allDeptList) {
                        list.add(deptInfo.dept_name);
                    }

                    handler.sendEmptyMessage(FILL_DEPT);
                }
            });
        }

        private void fillDevNameData() {
            ThreadUtils.runInBackground(new Runnable() {

                @Override
                public void run() {
                    allEqptInfoList = eqptDao.getAllEqptInfoList();

                    allFiveTEqptList = fiveTDao.getAllFiveTEqptList();

                    if (flag5t == 0) {
                        list.clear();
                        for (EqptInfoBean.EqptInfo eqptInfo : allEqptInfoList) {
                            list.add(eqptInfo.EqptName);
                        }
                        handler.sendEmptyMessage(FILL_TECH);
                    } else if (flag5t == 1) {
                        list.clear();
                        for (FiveTEqptInfoBean.FiveTEqpt fiveTEqpt : allFiveTEqptList) {
                            list.add(fiveTEqpt.EqptAddress);
                        }
                        handler.sendEmptyMessage(FILL_5T);
                    }
                }
            });

        }

        @Override
        public int bindLayout() {
            return R.layout.activity_serach;
        }

        @Override
        public void initView(View view) {
            ButterKnife.inject(this);
        }


    private int flag5t;
    private int flag;
        @Override
        public void doBusiness(Context mContext) {
            eqptDao = Eqpt_InfoDao.getInstance(this);
            fiveTDao = FiveT_InfoDao.getInstance(this);
            deptDao = Sys_deptDao.getInstance(this);

            Intent intent = getIntent();
            flag = intent.getIntExtra("flag", 0);
            flag5t = intent.getIntExtra("flag5t", 0);
            init(flag);
        }

        @Override
        public void resume() {

        }

        @Override
        public void destroy() {

        }
}
