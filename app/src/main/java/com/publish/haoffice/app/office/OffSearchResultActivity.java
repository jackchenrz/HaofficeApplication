package com.publish.haoffice.app.office;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.common.adapter.CommonAdapter;
import com.msystemlib.http.HttpConn;
import com.msystemlib.http.IWebServiceCallBack;
import com.msystemlib.http.JsonToBean;
import com.msystemlib.utils.LogUtils;
import com.msystemlib.utils.SPUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.api.bean.office.DBDocListBean;
import com.publish.haoffice.app.Repair.SearchResultActivity;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ACER on 2016/7/1.
 */
public class OffSearchResultActivity extends BaseActivity {

    @InjectView(R.id.lv_autolist)
    ListView lv_autolist;
    @InjectView(R.id.tv_count)
    TextView tv_count;
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.tv_size)
    TextView tv_size;

    @InjectView(R.id.btn_first)
    Button btn_first;
    @InjectView(R.id.btn_pre)
    Button btn_pre;
    @InjectView(R.id.btn_next)
    Button btn_next;
    @InjectView(R.id.btn_last)
    Button btn_last;
    private SPUtils spUtils;
    private String officeUrl;
    private int falgSearch;
    private int pageSize = 20;
    private int pageindex = 1;
    private int pageMax = 1;
    private HashMap<String, Object> map;
    private int i;
    private CommonAdapter<DBDocListBean.Doc> adapter;
    private List<DBDocListBean.Doc> DBDocList;

    @Override
    public int bindLayout () {
        return R.layout.activity_search_result;
    }

    @Override
    public void initView (View view) {
        ButterKnife.inject(this);
        tv_title.setText("查询列表");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finishActivity(OffSearchResultActivity.this);
            }
        });
        tv_size.setVisibility(View.GONE);
        initClick();
    }

    @Override
    public void doBusiness (Context mContext) {
        spUtils = new SPUtils();
        officeUrl = "http://" + spUtils.getString(OffSearchResultActivity.this, Const.SERVICE_IP, "", Const.SP_OFFICE) + ":" + spUtils.getString(OffSearchResultActivity.this, Const.SERVICE_PORT, "", Const.SP_OFFICE) + Const.SERVICE_PAGE1;


        Intent intent = getIntent();
        falgSearch = intent.getIntExtra("falgSearch", 0);
        map = new HashMap<>();
        map.put("UserID",spUtils.getString(OffSearchResultActivity.this, Const.USERID, "", Const.SP_OFFICE));
        map.put("txtFileCodeText", intent.getStringExtra("fileCode"));
        map.put("txtRecTitleText", intent.getStringExtra("fileTitle"));
        map.put("txtRecTypeSelectedValue", "");
        map.put("txtstartDateValue", intent.getStringExtra("StartDate"));
        map.put("txtendDateValue", intent.getStringExtra("EndDate"));

        if(falgSearch == 0){
            getData(Const.OFFIC_SEARCHSENDDOC,pageindex,pageSize);
        }else if(falgSearch == 1){
            getData(Const.OFFIC_SEARCHRECDOC,pageindex,pageSize);
        }

    }

    private void getData (String methodName,int pageindex1,int pageSize1) {
        map.put("PageSize", pageSize1);
        map.put("PageIndex", pageindex1);

        HttpConn.callService(officeUrl, Const.SERVICE_NAMESPACE, methodName, map, new IWebServiceCallBack() {
            @Override
            public void onSucced (SoapObject result) {
                if(result != null){
                    String string = result.getProperty(0).toString();
                    String string1= result.getProperty(1).toString();
                    if(!"404".equals(string) && !"404".equals(string1)){
                        tv_size.setVisibility(View.VISIBLE);
                        i = Integer.parseInt(string1);
                        pageMax = i % pageSize == 0 ? i / pageSize : i / pageSize + 1;
                        tv_size.setText("共"+i+"条 第"+pageindex+"页/共" +pageMax+ "页");
                        DBDocListBean jsonBean = JsonToBean.getJsonBean(string, DBDocListBean.class);
                        DBDocList = jsonBean.ds;
                        tv_count.setVisibility(View.GONE);
                        setOrUpdateAdapter(DBDocList);
                    }else{
                        tv_size.setVisibility(View.GONE);
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText("暂无数据");
                    }
                }
            }

            @Override
            public void onFailure (String result) {
                tv_size.setVisibility(View.GONE);
                tv_count.setVisibility(View.VISIBLE);
                tv_count.setText("联网失败");
            }
        });
    }

    private void initClick () {

        btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                pageindex = 1;
                if(falgSearch == 0){
                    getData(Const.OFFIC_SEARCHSENDDOC,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.OFFIC_SEARCHRECDOC,pageindex,pageSize);
                }

            }
        });

        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(pageindex == 1){
                    pageindex = 1;
                }else{
                    pageindex --;
                }
                if(falgSearch == 0){
                    getData(Const.OFFIC_SEARCHSENDDOC,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.OFFIC_SEARCHRECDOC,pageindex,pageSize);
                }

            }
        });



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(pageindex == pageMax){
                    pageindex = pageMax;
                }else{
                    pageindex ++;
                }
                tv_size.setText("共"+i+"条 第"+pageindex+"页/共" +pageMax+ "页");
                if(falgSearch == 0){
                    getData(Const.OFFIC_SEARCHSENDDOC,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.OFFIC_SEARCHRECDOC,pageindex,pageSize);
                }

            }
        });

        btn_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                pageindex = pageMax;
                tv_size.setText("共"+i+"条 第"+pageindex+"页/共" +pageMax+ "页");
                if(falgSearch == 0){
                    getData(Const.OFFIC_SEARCHSENDDOC,pageindex,pageSize);
                }else if(falgSearch == 1){
                    getData(Const.OFFIC_SEARCHRECDOC,pageindex,pageSize);
                }

            }
        });

        lv_autolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                DBDocListBean.Doc doc = DBDocList.get(position);
                if("上级文电".equals(doc.DocType)){
                    Intent intent = new Intent(OffSearchResultActivity.this,
                            DocDetailActivity.class);
                    intent.putExtra("RecID", doc.RecID);
                    intent.putExtra("btnFlag", 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);
                }else if("段发公文".equals(doc.DocType)){
                    Intent intent = new Intent(OffSearchResultActivity.this,
                            OfficDetailActivity.class);
                    intent.putExtra("RecID", doc.RecID);
                    intent.putExtra("btnFlag", 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);
                }else{
                    Intent intent = new Intent(OffSearchResultActivity.this,
                            NoticeDetailActivity.class);
                    intent.putExtra("RecID", doc.RecID);
                    intent.putExtra("btnFlag", 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.base_slide_right_in,
                            R.anim.base_slide_remain);
                }

            }
        });

    }

    private void setOrUpdateAdapter (final List<DBDocListBean.Doc> dbDocList) {
        adapter = new CommonAdapter<DBDocListBean.Doc>(dbDocList) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view;
                ViewHolder vh;
                if (convertView == null) {
                    view = getLayoutInflater()
                            .inflate(R.layout.list_item_dbdoc,
                                    parent, false);
                    vh = new ViewHolder(view);
                    view.setTag(vh);
                } else {
                    view = convertView;
                    vh = (ViewHolder) view.getTag();
                }
                DBDocListBean.Doc Doc = dbDocList.get(position);
                vh.tv_name.setText(Doc.FileTitle);
                vh.tv_time.setText(Doc.CreateDate);
                vh.tv_describe.setText(Doc.FileCode);
                vh.tv_step.setText("当前环节：" + Doc.CurrentStepName);

                if("上级文电".equals(Doc.DocType)){
                    vh.tv_fileDZ.setText("局");
                    vh.iv_img.setBackgroundResource(R.drawable.round_bg);
                }else if("段发公文".equals(Doc.DocType)){
                    vh.tv_fileDZ.setText("段");
                    vh.iv_img.setBackgroundResource(R.drawable.round_bg1);
                }else{
                    vh.tv_fileDZ.setText("段");
                    vh.iv_img.setBackgroundResource(R.drawable.round_bg1);
                }
                return view;
            }
        };
        lv_autolist.setAdapter(adapter);

    }

    static class ViewHolder {
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_time)
        TextView tv_time;
        @InjectView(R.id.tv_step)
        TextView tv_step;
        @InjectView(R.id.tv_describe)
        TextView tv_describe;
        @InjectView(R.id.tv_fileDZ)
        TextView tv_fileDZ;
        @InjectView(R.id.iv_img)
        ImageView iv_img;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }


    @Override
    public void resume () {

    }

    @Override
    public void destroy () {

    }
}
