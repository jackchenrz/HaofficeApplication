package com.publish.haoffice.app.office;

import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.common.adapter.CommonAdapter;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.FileUtils;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.ApproveRecordBean;
import com.publish.haoffice.api.bean.office.DocDetailBean;
import com.publish.haoffice.api.bean.office.DocListBean;
import com.publish.haoffice.api.bean.office.OfficDocDetailBean;
import com.publish.haoffice.api.bean.office.WordBean;
import com.publish.haoffice.api.bean.repair.RepairHandlerBean;
import com.publish.haoffice.api.utils.DensityUtils;
import com.publish.haoffice.app.LoginActivity;
import com.publish.haoffice.application.SysApplication;
import com.publish.haoffice.view.FocusTextView;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OfficDetailActivity extends BaseActivity {
    @InjectView(R.id.tv_doctitle)
    TextView tvTitle;
    @InjectView(R.id.tv_doctype)
    TextView tvType;
    @InjectView(R.id.tv_docCode)
    TextView tv_docCode;
    @InjectView(R.id.tv_FileHJCD)
    TextView tv_FileHJCD;
    @InjectView(R.id.tv_FileJMDJ)
    TextView tv_FileJMDJ;
    @InjectView(R.id.tv_docuser)
    TextView tv_docuser;
    @InjectView(R.id.tv_docdept)
    TextView tv_docdept;
    @InjectView(R.id.tv_send_main)
    TextView tv_send_main;
    @InjectView(R.id.tv_send_other)
    TextView tv_send_other;
    @InjectView(R.id.tv_doc)
    TextView tv_doc;
    @InjectView(R.id.lv_doc)
    ListView lv_doc;
    @InjectView(R.id.tv_main_doc)
    TextView tv_main_doc;



    @InjectView(R.id.tab)
    TableLayout tabLayout;
    @InjectView(R.id.ll_sign_back)
    LinearLayout ll_sign_back;
    @InjectView(R.id.ll_sign)
    LinearLayout ll_sign;
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;

    private List<ApproveRecordBean.Record> record;
    private ApproveRecordBean approveRecordBean;
    private String recID;
    private SPUtils spUtils;
    private HashMap<String, String> map;
    private String officeUrl;
    private OfficDocDetailBean.OfficDocDetail officdocDetail;
    private List<WordBean.Word> wordList;
    private CommonAdapter<WordBean.Word> adapter;


    /**
     * 添加审批记录
     */
    protected void addTab() {
        WindowManager systemService = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        for (int i = 0; i < approveRecordBean.ds.size(); i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tv = new TextView(this);
            android.widget.TableRow.LayoutParams params = new TableRow.LayoutParams( systemService.getDefaultDisplay().getWidth()/5, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            tv.setLayoutParams(params);
            tv.setText(record.get(i).StepName);
            tr.addView(tv);

            TextView tv1 = new TextView(this);
            android.widget.TableRow.LayoutParams params1 = new TableRow.LayoutParams( systemService.getDefaultDisplay().getWidth()/5, ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.gravity = Gravity.CENTER;
            tv1.setLayoutParams(params1);
            tv1.setText(record.get(i).Real_Name +"|"+ record.get(i).PostilType);
            tr.addView(tv1);

            TextView tv2 = new TextView(this);
            android.widget.TableRow.LayoutParams params3 = new TableRow.LayoutParams(systemService.getDefaultDisplay().getWidth()/5, ViewGroup.LayoutParams.WRAP_CONTENT);
            params3.gravity = Gravity.CENTER;
            tv2.setLayoutParams(params3);
            tv2.setText(record.get(i).PostilDesc);
            tr.addView(tv2);

            TextView tv3 = new TextView(this);
            android.widget.TableRow.LayoutParams params4 = new TableRow.LayoutParams(systemService.getDefaultDisplay().getWidth()/5, ViewGroup.LayoutParams.WRAP_CONTENT);
            params4.gravity = Gravity.CENTER;
            tv3.setLayoutParams(params4);
            tv3.setText(record.get(i).StartDate + "|" + record.get(i).EndDate);
            tr.addView(tv3);
            tabLayout.addView(tr);
            ImageView iv = new ImageView(this);
            TableLayout.LayoutParams params5 = new TableLayout.LayoutParams(systemService.getDefaultDisplay().getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            params5.gravity = Gravity.CENTER;
            iv.setLayoutParams(params5);
            iv.setBackgroundResource(R.drawable.line);
            tabLayout.addView(iv);
        }
    }
    @Override
    public int bindLayout () {
        return R.layout.activity_officdetail;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
        tv_title.setText("详情页");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(OfficDetailActivity.this);
            }
        });
        lv_doc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                WordBean.Word word = wordList.get(position);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(word.DownUrl);
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }





    @Override
    public void doBusiness (Context mContext) {
        Intent intent = getIntent();
        recID = intent.getStringExtra("RecID");
        spUtils = new SPUtils();

        officeUrl = "http://" + spUtils.getString(this, Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(this, Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE1;
        map = new HashMap<>();
        map.put("DocID",recID);
        getWordData();
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETDOCDETAIL, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        OfficDocDetailBean jsonBean = JsonToBean.getJsonBean(string, OfficDocDetailBean.class);
                        officdocDetail = jsonBean.ds.get(0);
                        tvTitle.setText("标        题：" + officdocDetail.FileTitle);
                        tvType.setText("公文种类：" + officdocDetail.FileDZ);
                        tv_docCode.setText("文电号：" + officdocDetail.FileCode );
                        tv_FileHJCD.setText("紧急程度：" + officdocDetail.FileHJCD );
                        tv_FileJMDJ.setText("秘密等级：" + officdocDetail.FileJMDJ );
                        tv_docuser.setText("拟稿人：" + officdocDetail.LXR );
                        tv_docdept.setText("拟稿部门：" + officdocDetail.SendDept );
                        tv_send_main.setText("主送：" + officdocDetail.FileRecUsersText );
                        tv_send_other.setText("抄送：" + officdocDetail.FileRecCopyUsersText );
                    }
                    getAutoRecord();
                }else{
                    ToastUtils.showToast(OfficDetailActivity.this, "联网失败");
                }
            }

            @Override
            public void onFailure (String result) {
                ToastUtils.showToast(OfficDetailActivity.this, "联网失败");
            }
        });

    }


    protected void getWordData() {
        // TODO: 2016/6/21
        map.put("DocID","4E2FAA12-6A33-46CA-BE36-0129146F1947");
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETDOCDATAURL, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {

                if(result != null){
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        tv_main_doc.setText("点击下载查看" );
                        setclick(string);

                    }
                    map.put("DocID",recID);
                    HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETATTACHSBYDOCID, map, new IWebServiceCallBack() {
                        @Override
                        public void onSucced (SoapObject result) {

                            if(result != null){
                                String string = result.getProperty(0).toString();
                                if(!"404".equals(string)){
                                    WordBean jsonBean = JsonToBean.getJsonBean(string, WordBean.class);
                                    wordList = jsonBean.ds;
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,wordList.size()*DensityUtils.dip2px(OfficDetailActivity.this,35));
                                    lv_doc.setLayoutParams(params);

                                    setOrUpdateAdapter(wordList);
                                }
                            }
                        }

                        @Override
                        public void onFailure (String result) {
                            ToastUtils.showToast(OfficDetailActivity.this, "联网错误，请检查网络连接");
                        }
                    });
                }
            }

            @Override
            public void onFailure (String result) {
                ToastUtils.showToast(OfficDetailActivity.this, "联网错误，请检查网络连接");
            }
        });


    }

    private void setclick (final String string) {

        tv_main_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(string);
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }

    private void setOrUpdateAdapter (final List<WordBean.Word> wordList1) {
        adapter = new CommonAdapter<WordBean.Word>(wordList1) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getLayoutInflater()
                            .inflate(R.layout.list_item_word,
                                    parent, false);
                    vh = new ViewHolder(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder) view.getTag();
                }
                WordBean.Word word = wordList1.get(position);
                vh.tv_filename.setText(word.FileName);
                vh.iv_download.setVisibility(View.GONE);
                return view;
            }
        };
        lv_doc.setAdapter(adapter);
    }


    static class ViewHolder {
        @InjectView(R.id.tv_filename)
        TextView tv_filename;
        @InjectView(R.id.iv_download)
        ImageView iv_download;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    protected void getAutoRecord() {
        map.put("DocID", recID);
        map.put("userID", SysApplication.gainData(Const.USERID).toString());
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_AUDITRECORDDOC, map , new IWebServiceCallBack() {

            @Override
            public void onSucced(SoapObject result) {

                if(result != null){
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        approveRecordBean = JsonToBean.getJsonBean(string, ApproveRecordBean.class);
                        record = approveRecordBean.ds;
                        addTab();
                    }
                }else{
                    ToastUtils.showToast(OfficDetailActivity.this, "联网失败");
                }
            }

            @Override
            public void onFailure(String result) {
                ToastUtils.showToast(OfficDetailActivity.this, "联网错误，请检查网络连接");
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