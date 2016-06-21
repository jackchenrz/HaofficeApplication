package com.publish.haoffice.app.office;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.ApproveRecordBean;
import com.publish.haoffice.api.bean.office.DocDetailBean;
import com.publish.haoffice.api.bean.office.OfficDocDetailBean;
import com.publish.haoffice.api.bean.office.WordBean;
import com.publish.haoffice.api.utils.DensityUtils;
import com.publish.haoffice.application.SysApplication;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DocDetailActivity extends BaseActivity {

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
    @InjectView(R.id.tv_doc)
    TextView tv_doc;



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
    private DocDetailBean.DocDetail officdocDetail;


    @Override
    public int bindLayout () {
        return R.layout.activity_docdetail;
    }

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
    public void initView (View view) {
        ButterKnife.inject(this);
        tv_title.setText("详情页");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(DocDetailActivity.this);
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
        map.put("recID",recID);
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETDETAIL, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    String string = result.getProperty(0).toString();
                    if(!"404".equals(string)){
                        DocDetailBean jsonBean = JsonToBean.getJsonBean(string, DocDetailBean.class);
                        officdocDetail = jsonBean.ds.get(0);
                        tvTitle.setText("标        题：" + officdocDetail.RecTitle);
                        tvType.setText("公文种类：" + officdocDetail.RecType);
                        tv_docCode.setText("文电号：" + officdocDetail.FileCode );
                        tv_FileHJCD.setText("发文日期：" + officdocDetail.CreateDate );
                        tv_FileJMDJ.setText("归档号：" + officdocDetail.DocumentNo );
                        tv_doc.setText("附件：");
                    }
                    getAutoRecord();
                }else{
                    ToastUtils.showToast(DocDetailActivity.this, "联网失败");
                }
            }

            @Override
            public void onFailure (String result) {
                ToastUtils.showToast(DocDetailActivity.this, "联网失败");
            }
        });
    }

    protected void getAutoRecord() {
        map.put("DocID", recID);
        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_AUDITRECORD, map , new IWebServiceCallBack() {

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
                    ToastUtils.showToast(DocDetailActivity.this, "联网失败");
                }
            }

            @Override
            public void onFailure(String result) {
                ToastUtils.showToast(DocDetailActivity.this, "联网错误，请检查网络连接");
            }
        });
    }


//    protected void getWordData() {
//
//        map.put("DocID",recID);
//        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, Const.OFFIC_GETATTACHSBYDOCID, map, new IWebServiceCallBack() {
//            @Override
//            public void onSucced (SoapObject result) {
//
//                if(result != null){
//                    String string = result.getProperty(0).toString();
//                    if(!"404".equals(string)){
//                        WordBean jsonBean = JsonToBean.getJsonBean(string, WordBean.class);
//                        wordList = jsonBean.ds;
//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,wordList.size()* DensityUtils.dip2px(OfficDetailActivity.this,35));
//                        lv_doc.setLayoutParams(params);
//
//                        setOrUpdateAdapter(wordList);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure (String result) {
//                ToastUtils.showToast(OfficDetailActivity.this, "联网错误，请检查网络连接");
//            }
//        });
//
//
//    }

    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }
}
