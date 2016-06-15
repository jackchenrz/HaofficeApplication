package com.publish.haoffice.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.common.adapter.GirdViewAdapter;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.AlertUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ThreadUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.DeptInfoBean;
import com.publish.haoffice.api.bean.repair.EqptInfoBean;
import com.publish.haoffice.api.bean.repair.FiveTEqptInfoBean;
import com.publish.haoffice.api.bean.repair.TechEqptBean;
import com.publish.haoffice.api.bean.repair.UserInfoBean;
import com.publish.haoffice.api.dao.repair.Eqpt_InfoDao;
import com.publish.haoffice.api.dao.repair.FiveT_InfoDao;
import com.publish.haoffice.api.dao.repair.Sys_deptDao;
import com.publish.haoffice.api.dao.repair.Sys_userDao;
import com.publish.haoffice.api.dao.repair.TechEqptDao;
import com.publish.haoffice.view.MyProgressBar;
import com.publish.haoffice.api.bean.repair.FiveTEqptInfoBean.FiveTEqpt;
import com.publish.haoffice.api.bean.repair.DeptInfoBean.DeptInfo;
import com.publish.haoffice.api.bean.repair.UserInfoBean.UserInfo;
import com.publish.haoffice.api.bean.repair.EqptInfoBean.EqptInfo;
import com.publish.haoffice.api.bean.repair.TechEqptBean.TechEqpt;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AdminActivity  extends BaseActivity implements AdapterView.OnItemClickListener {
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.gv_items)
    GridView gvItems;
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;

    private static final int PRO_USER = 101;//同步维修部门
    private static final int PRO_DEVICES = 102;//同步维修部门
    private static final int PRO_DEPARTMENT= 103;//同步维修部门
    private com.msystemlib.utils.SPUtils SPUtils;

    private String url;
    private GirdViewAdapter adapter;
    private Sys_userDao userDao;
    private Sys_deptDao deptDao;
    private Eqpt_InfoDao eqptDao;
    private FiveT_InfoDao fiveTDao;
    private TechEqptDao TechDao;
    private MyProgressBar pro;
    private TextView tvPro;
    private Dialog downloadDialog;
    private int i;
    private int addCount;
    private List<UserInfoBean.UserInfo> userInfoList;
    private TextView tipTextView;
    private List<DeptInfoBean.DeptInfo> deptInfoList;
    private List<FiveTEqptInfoBean.FiveTEqpt> fiveTEqptList;
    private List<EqptInfoBean.EqptInfo> eqptInfoList;
    private List<TechEqptBean.TechEqpt> techEqptList;
    private String[] names =  new String[] { "电子公文", "报修管理","施工管理"};
    private int[] imageIds = new int[] { R.drawable.main_office, R.drawable.main_repair,
            R.drawable.main_construct};

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PRO_USER:
                    tipTextView.setText("正在更新");
                    pro.setProgress(addCount*100/userInfoList.size());
                    tvPro.setText(addCount + "/" + userInfoList.size());
                    break;
                case PRO_DEPARTMENT:
                    tipTextView.setText("正在更新");
                    pro.setProgress(addCount*100/deptInfoList.size());
                    tvPro.setText(addCount + "/" + deptInfoList.size());
                    break;
                case PRO_DEVICES:
                    tipTextView.setText("正在更新");
                    pro.setProgress(addCount*100/(fiveTEqptList.size() + eqptInfoList.size()+ techEqptList.size()));
                    tvPro.setText(addCount + "/" + (fiveTEqptList.size() + eqptInfoList.size()+ techEqptList.size()));
                    break;
            }
        };
    };

    public AdminActivity () {
    }

    @Override
    public int bindLayout () {
        return R.layout.activity_admin;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
        tvTitle.setText("数据更新");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(AdminActivity.this);
            }
        });

        adapter = new GirdViewAdapter(names) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = null;
                if (convertView == null) {
                    view = View.inflate(AdminActivity.this, R.layout.gird_item_home,
                            null);
                } else {
                    view = convertView;
                }
                TextView name = (TextView) view
                        .findViewById(R.id.tv_name_grid_item);
                name.setText(names[position]);
                name.setTextColor(AdminActivity.this.getResources().getColor(
                        R.color.title));

                ImageView image = (ImageView) view
                        .findViewById(R.id.iv_icon_gird_item);
                image.setBackgroundResource(imageIds[position]);
                return view;
            }
        };
        gvItems.setAdapter(adapter);
        gvItems.setOnItemClickListener(this);
    }

    @Override
    public void doBusiness (Context mContext) {
        SPUtils = new SPUtils();
        String serverIP = SPUtils.getString(this, Const.SERVICE_IP, "", Const.SP_REPAIR);
        String serverPort = SPUtils.getString(this, Const.SERVICE_PORT, "",Const.SP_REPAIR);
        if (serverIP != null && !"".equals(serverIP) && serverPort != null&& !"".equals(serverPort)) {
            url = "http://" + serverIP + ":" + serverPort+ Const.SERVICE_PAGE;
        }
        userDao = Sys_userDao.getInstance(this);
        deptDao = Sys_deptDao.getInstance(this);
        eqptDao = Eqpt_InfoDao.getInstance(this);
        fiveTDao = FiveT_InfoDao.getInstance(this);
        TechDao = TechEqptDao.getInstance(this);
    }

    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: // 电子公文
                ToastUtils.showToast(AdminActivity.this,"该功能尚未开发，敬请期待");
                break;
            case 1: // 报修管理
                ToastUtils.showToast(AdminActivity.this,"该功能尚未开发，敬请期待");
                break;
            case 2: // 施工管理
                ToastUtils.showToast(AdminActivity.this,"该功能尚未开发，敬请期待");
                break;
        }
    }

    /**
     * 更新维修部门
     */
    private void updateDepartment() {
        downloadDialog = showDownloadDialog(this, "正在准备更新...", "0/0", i);
        downloadDialog.show();

        HttpConn.callService(url, Const.SERVICE_NAMESPACE,Const.SERVICE_GETSYS_DEPT, null, new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {
                if (result != null) {
                    final String string = result.getProperty(0).toString();
                    ThreadUtils.runInBackground(new Runnable() {

                        @Override
                        public void run() {
                            saveDeptInfo(string);
                        }
                    });
                } else {
                    ToastUtils.showToast(AdminActivity.this, "联网失败");
                    downloadDialog.dismiss();
                }
            }
            @Override
            public void onFailure(String result) {
                ToastUtils.showToast(AdminActivity.this, "联网失败");
                downloadDialog.dismiss();
            }
        });
    }

    /**
     * 更新设备信息
     */
    private void updateDevices() {
        downloadDialog = showDownloadDialog(this, "正在准备更新...", "0/0", i);
        downloadDialog.show();

        HttpConn.callService(url, Const.SERVICE_NAMESPACE, Const.SERVICE_GETFIVET_EQPT, null, new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {
                if (result != null) {
                    String string = result.getProperty(0).toString();
                    getEqpt(string + "&");
                } else {
                    ToastUtils.showToast(AdminActivity.this, "联网失败");
                    downloadDialog.dismiss();
                }
            }
            @Override
            public void onFailure(String result) {
                ToastUtils.showToast(AdminActivity.this, "联网失败");
                downloadDialog.dismiss();
            }
        });
    }
    private void getEqpt(final String str) {
        HttpConn.callService(url, Const.SERVICE_NAMESPACE,Const.SERVICE_GETEQPT_INFO, null, new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {
                if (result != null) {
                    String string = result.getProperty(0).toString();
                    getTech(str + string + "&");
                } else {
                    ToastUtils.showToast(AdminActivity.this, "联网失败");
                    downloadDialog.dismiss();
                }
            }
            @Override
            public void onFailure(String result) {
                ToastUtils.showToast(AdminActivity.this, "联网失败");
                downloadDialog.dismiss();
            }
        });
    }

    protected void getTech(final String str) {
        HttpConn.callService(url, Const.SERVICE_NAMESPACE,Const.SERVICE_GETTECH_EQPT, null, new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {
                if (result != null) {
                    final String string = result.getProperty(0).toString();
                    ThreadUtils.runInBackground(new Runnable() {

                        @Override
                        public void run() {
                            saveEqptInfo(str + string);
                        }
                    });
                } else {
                    ToastUtils.showToast(AdminActivity.this, "联网失败");
                    downloadDialog.dismiss();
                }
            }
            @Override
            public void onFailure(String result) {
                ToastUtils.showToast(AdminActivity.this, "联网失败");
                downloadDialog.dismiss();
            }
        });
    }

    /**
     * 更新用户信息
     */
    private void updateUser() {
        downloadDialog = showDownloadDialog(this, "正在准备更新...", "0/0", i);
        downloadDialog.show();
        HttpConn.callService(url, Const.SERVICE_NAMESPACE,Const.SERVICE_GETSYSUSER, null, new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {
                if (result != null) {
                    final String string = result.getProperty(0).toString();
                    ThreadUtils.runInBackground(new Runnable() {

                        @Override
                        public void run() {
                            saveUserInfo(string);
                        }
                    });
                } else {
                    ToastUtils.showToast(AdminActivity.this, "联网失败");
                    downloadDialog.dismiss();
                }
            }
            @Override
            public void onFailure(String result) {
                ToastUtils.showToast(AdminActivity.this, "联网失败");
                downloadDialog.dismiss();
            }
        });
    }

    /**
     * 保存设备信息
     *
     * @param msgObj
     */
    protected void saveEqptInfo(String msgObj) {
        String[] jsonArr = msgObj.split("&");
        addCount = 0;
        fiveTDao.deleteAllFiveTEqpt();
        FiveTEqptInfoBean fiveTEqptInfoBean = JsonToBean.getJsonBean(jsonArr[0],FiveTEqptInfoBean.class);
        fiveTEqptList = fiveTEqptInfoBean.ds;
        TechDao.deleteAllTechEqpt();
        TechEqptBean techEqptBean = JsonToBean.getJsonBean(jsonArr[2],TechEqptBean.class);
        techEqptList = techEqptBean.ds;
        eqptDao.deleteAllEqptInfo();
        EqptInfoBean eqptInfoBean = JsonToBean.getJsonBean(jsonArr[1],EqptInfoBean.class);
        eqptInfoList = eqptInfoBean.ds;
        for (FiveTEqpt fiveTEqpt : fiveTEqptList) {
            fiveTDao.addFiveTEqpt(fiveTEqpt);
            addCount++;
            handler.sendEmptyMessage(PRO_DEVICES);
        }

        for (EqptInfo eqptInfo : eqptInfoList) {
            eqptDao.addEqptInfo(eqptInfo);
            addCount++;
            handler.sendEmptyMessage(PRO_DEVICES);
        }

        for (TechEqpt techEqpt : techEqptList) {
            TechDao.addTechEqpt(techEqpt);
            addCount++;
            handler.sendEmptyMessage(PRO_DEVICES);
        }
        if (addCount == fiveTEqptList.size() + eqptInfoList.size()+ techEqptList.size()) {
            downloadDialog.dismiss();
            ThreadUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    AlertUtils.dialog(AdminActivity.this, "提示", "设备同步成功！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }
            });

        } else {
            ThreadUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    AlertUtils.dialog(AdminActivity.this, "提示", "同步全部设备失败，请重新同步...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }
            });
        }
    }

    /**
     * 保存维修部门信息
     *
     * @param msgObj
     */
    protected void saveDeptInfo(String msgObj) {
        deptDao.deleteAllDept();
        addCount = 0;
        DeptInfoBean deptInfoBean = JsonToBean.getJsonBean(msgObj,DeptInfoBean.class);
        deptInfoList = deptInfoBean.ds;
        for (DeptInfo deptInfo : deptInfoList) {
            deptDao.addDept(deptInfo);
            addCount++;
            handler.sendEmptyMessage(PRO_DEPARTMENT);
        }
        if (addCount == deptInfoList.size()) {
            downloadDialog.dismiss();
            ThreadUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    AlertUtils.dialog(AdminActivity.this, "提示", "维修部门同步成功！", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }
            });

        } else {
            ThreadUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    AlertUtils.dialog(AdminActivity.this, "提示", "同步全部维修部门失败，请重新同步...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }
            });
        }
    }

    /**
     * 保存用户信息
     *
     * @param msgObj
     */
    protected void saveUserInfo(String msgObj) {
        userDao.deleteAllUserLog();
        addCount = 0;
        UserInfoBean userInfoBean = JsonToBean.getJsonBean(msgObj,UserInfoBean.class);
        userInfoList = userInfoBean.ds;
        for (UserInfo userInfo : userInfoList) {
            userDao.addUserLog(userInfo);
            addCount++;
            handler.sendEmptyMessage(PRO_USER);
        }
        if (addCount == userInfoList.size()) {
            downloadDialog.dismiss();
            ThreadUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    AlertUtils.dialog(AdminActivity.this, "提示", "人员同步成功！", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }
            });
        } else {
            ThreadUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    AlertUtils.dialog(AdminActivity.this, "提示", "同步全部人员失败，请重新同步...", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }
            });
        }
    }


    /**
     * 下载提示对话框
     * @param context 上下文
     * @param msg 提示信息
     * @param text 下载进度文字
     * @param progress 下载进度
     * @return
     */
    private Dialog showDownloadDialog(Context context, String msg, String text, int progress) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.download_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        pro = (MyProgressBar) v.findViewById(R.id.pro);
        tvPro = (TextView) v.findViewById(R.id.tv_pro);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息
        tvPro.setText(text);//设置下载进度提示
        pro.setProgress(progress);
        pro.setMax(100);
        Dialog downloadDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        downloadDialog.setCancelable(false);// 不可以用“返回键”取消
        downloadDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return downloadDialog;

    }
}
