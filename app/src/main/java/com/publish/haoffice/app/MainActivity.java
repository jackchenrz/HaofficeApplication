package com.publish.haoffice.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msystemlib.base.BaseActivity;
import com.msystemlib.common.adapter.GirdViewAdapter;
import com.msystemlib.utils.ToastUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.Const;
import com.publish.haoffice.application.SysApplication;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.gv_items)
    GridView gvItems;
    @InjectView(R.id.ll_back)
    LinearLayout ll_back;

    private GirdViewAdapter adapter;
    private String[] names =  new String[] { "电子公文", "报修管理","施工管理","生产交班"};
    private int[] imageIds = new int[] { R.drawable.main_office, R.drawable.main_repair,
            R.drawable.main_construct, R.drawable.main_produce};
    @Override
    public int bindLayout () {
        return R.layout.activity_main;
    }

    @Override
    public void initView (View view) {

        ButterKnife.inject(this);
        ll_back.setVisibility(View.GONE);
        adapter = new GirdViewAdapter(names) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = null;
                if (convertView == null) {
                    view = View.inflate(MainActivity.this, R.layout.gird_item_home,
                            null);
                } else {
                    view = convertView;
                }
                TextView name = (TextView) view
                        .findViewById(R.id.tv_name_grid_item);
                name.setText(names[position]);
                name.setTextColor(MainActivity.this.getResources().getColor(
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
                SysApplication.assignData(Const.SYSTEM_FLAG,"0");
                jump2Activity(MainActivity.this,LoginActivity.class,null,false);
                break;
            case 1: // 报修管理
                SysApplication.assignData(Const.SYSTEM_FLAG,"1");
                jump2Activity(MainActivity.this,LoginActivity.class,null,false);
                break;
            case 2: // 施工管理
                ToastUtils.showToast(MainActivity.this,"该功能尚未开发，敬请期待");
                break;
		    case 3: // 生产交班
                ToastUtils.showToast(MainActivity.this,"该功能尚未开发，敬请期待");
			break;

        }
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出应用",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                mApplication.removeAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
