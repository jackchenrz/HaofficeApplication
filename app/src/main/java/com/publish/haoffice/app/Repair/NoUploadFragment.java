package com.publish.haoffice.app.Repair;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.msystemlib.base.BaseFragment;
import com.msystemlib.common.adapter.CommonAdapter;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.img.ImgLoad;
import com.msystemlib.utils.AlertUtils;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ThreadUtils;
import com.msystemlib.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.repair.RepairInfo;
import com.publish.haoffice.api.dao.repair.Reapir_SubmitDao;
import com.publish.haoffice.api.dao.repair.Sys_userDao;
import com.publish.haoffice.api.utils.DialogUtils;
import com.publish.haoffice.api.utils.FormatJsonUtils;
import com.publish.haoffice.api.utils.Image2StrUtils;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class NoUploadFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.id_listview)
    ListView listView;
    @InjectView(R.id.id_swipe_ly)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.tv_count)
    TextView tv_count;

    private Reapir_SubmitDao repairDao;
    private Sys_userDao userDao;
    private List<RepairInfo> repairInfoList;
    private CommonAdapter<RepairInfo> adapter1;
    private ImageLoader imageLoader;
    private final int FLUSH = 22;
    private final int UPLOAD = 23;
    private final int UPLOADSTR = 24;
    private Dialog loadingDialog;
    private SPUtils spUtils;
    private String url;
    private Bitmap bmp;
    private String image;
    private int flag5t;
    private int count = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg) {
            switch (msg.what){
                case FLUSH:
                    refreshLayout.post(new Runnable() {
                        @Override
                        public void run () {
                            refreshLayout.setRefreshing(false);
                        }
                    });

                    if(repairInfoList.size() == 0){
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }else{
                        tv_count.setVisibility(View.GONE);
                        setOrUpdateAdapter(repairInfoList);
                    }
                    break;
                case UPLOAD:
                    String str = (String) msg.obj;
                    uploadImg(str.split("#")[0],str.split("#")[1]);
                    break;
                case UPLOADSTR:
                    uploadInfo();
                    break;
            }

        }
    };


    private void uploadImg (String s, String s1) {
        HashMap<String, String> map  = new HashMap<>();
        map.put("FileName",s);
        map.put("image", s1);
        HttpConn.callService(url, Const.SERVICE_NAMESPACE, Const.REPAIR_UPLOADIMAGE, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if (result != null) {
                    String string = result.getProperty(0).toString();

                    if(count == repairInfoList.size()){
                        loadingDialog.dismiss();
                        AlertUtils.dialog1(getActivity(), "提示", "上传成功", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick (DialogInterface dialog, int which) {

                                repairInfoList.clear();
                                repairDao.deleteAllRepairSubmit();
                                adapter1.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        },null);
                    }

                }
            }

            @Override
            public void onFailure (String result) {
                ToastUtils.showToast(getActivity(), "联网失败");
                loadingDialog.dismiss();
            }
        });
    }

    private void editData () {

        ThreadUtils.runInBackground(new Runnable() {
            @Override
            public void run () {
                for (RepairInfo repairInfo:repairInfoList) {
                    repairInfo.IsUpload = 2;
                    repairInfo.FaultReceiveTime = new SimpleDateFormat(
                            "yyyy/MM/dd HH:mm:ss").format(new Date());
                }
                handler.sendEmptyMessage(UPLOADSTR);
            }
        });
    }

    private void uploadInfo() {
        loadingDialog = DialogUtils.createLoadingDialog(getActivity(), "正在上传，请稍后...");
        loadingDialog.show();
        if(repairInfoList.size() != 0 && null != repairInfoList){
            String formatJson = FormatJsonUtils.formatJson(repairInfoList);

            LogUtils.d("ckj",formatJson);
            HashMap<String,String> map  = new HashMap<>();
            map.put("strJson",formatJson);
            HttpConn.callService(url, Const.SERVICE_NAMESPACE, Const.REPAIR_RETURNREPAIR_SUBMIT, map, new IWebServiceCallBack() {
                @Override
                public void onSucced (SoapObject result) {
                    if (result != null) {
                        String string = result.getProperty(0).toString();
                        if("上传成功!".equals(string)){
                            ThreadUtils.runInBackground(new Runnable() {
                                @Override
                                public void run () {
                                    for (RepairInfo repairInfo:repairInfoList) {
                                        repairDao.editRepairInfo(2, "", repairInfo.RepairID);
                                        Message msg = Message.obtain();
                                        String imageName = repairInfo.ImageUrl;
                                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                                        bitmapOptions.inSampleSize = 5;
                                        bmp = BitmapFactory.decodeFile(Const.FILEPATH + imageName, bitmapOptions);
                                        image = Image2StrUtils.bitmapToBase64(bmp);//图片base64编码成字符串
                                        msg.obj = imageName + "# " + image;
                                        msg.what = UPLOAD;
                                        count ++;
                                        handler.sendMessage(msg);
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure (String result) {
                    ToastUtils.showToast(getActivity(), "联网失败");
                    loadingDialog.dismiss();
                }
            });


        }else{
            loadingDialog.dismiss();
        }

    }
    protected void setOrUpdateAdapter(final List<RepairInfo> repairInfoList) {
        adapter1 = new CommonAdapter<RepairInfo>(repairInfoList) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_repairhandler,
                                    parent, false);
                    vh = new ViewHolder(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder) view.getTag();
                }
                RepairInfo toRepair = repairInfoList.get(position);
                vh.tv_name.setText("设备名称：" + toRepair.EqptName);
                vh.tv_department.setText("故障发生时间：" + toRepair.FaultOccu_Time);
                vh.tv_describe.setText("故障描述：" + toRepair.FaultAppearance);

                imageLoader.displayImage("file://" + Const.FILEPATH + toRepair.ImageUrl, vh.ivImg);
                return view;
            }
        };
        listView.setAdapter(adapter1);
    }

    static class ViewHolder {
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_department)
        TextView tv_department;
        @InjectView(R.id.tv_describe)
        TextView tv_describe;
        @InjectView(R.id.iv_img)
        ImageView ivImg;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
    @Override
    public View initView () {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_uploadno,null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void initData () {
        repairDao = Reapir_SubmitDao.getInstance(getActivity());
        userDao = Sys_userDao.getInstance(getActivity());
        imageLoader = ImgLoad.initImageLoader(getActivity());
        spUtils = new SPUtils();

        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null ){
            flag5t = bundle.getInt("flag5t");
        }
        String serverIP = spUtils.getString(getActivity(), Const.SERVICE_IP, "", Const.SP_REPAIR);
        String serverPort = spUtils.getString(getActivity(), Const.SERVICE_PORT, "",Const.SP_REPAIR);
        if (serverIP != null && !"".equals(serverIP) && serverPort != null&& !"".equals(serverPort)) {
            url = "http://" + serverIP + ":" + serverPort+ Const.SERVICE_PAGE;
        }
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.chocolate,
                R.color.yellow,
                R.color.green,
                R.color.gold);

        refreshLayout.post(new Runnable() {
            @Override
            public void run () {
                refreshLayout.setRefreshing(true);
            }
        });
        onRefresh();
        getActivity().findViewById(R.id.ll_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                editData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                RepairInfo repairInfo = repairInfoList.get(position);
                Intent intent = new Intent(getActivity(),
                        RepairDetailActivity.class);
                intent.putExtra("repairInfo", repairInfo);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in,
                        R.anim.base_slide_remain);


            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick (AdapterView<?> parent, View view, final int position, long id) {
                AlertUtils.dialog1(getActivity(), "提示", "是否删除？", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        dialog.dismiss();
                        repairDao.delRepairInfo(repairInfoList.get(position).RepairID);
                        repairInfoList.remove(position);
                        adapter1.notifyDataSetChanged();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                return true;
            }
        });


    }

    @Override
    public void onResume () {
        super.onResume();
//        refreshLayout.post(new Runnable() {
//            @Override
//            public void run () {
//                refreshLayout.setRefreshing(true);
//            }
//        });
//        onRefresh();
    }

    @Override
    public void onRefresh () {
        ThreadUtils.runInBackground(new Runnable() {
            @Override
            public void run () {
                repairInfoList = repairDao.getRepairInfo(userDao
                        .getUserInfo(spUtils.getString(getActivity(),Const.USERNAME, "",Const.SP_REPAIR)).user_id,1,flag5t == 1 ? "5T" : "Tech");
                handler.sendEmptyMessage(FLUSH);
            }
        });
    }
}
