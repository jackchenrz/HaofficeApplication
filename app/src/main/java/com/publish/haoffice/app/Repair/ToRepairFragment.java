package com.publish.haoffice.app.Repair;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.msystemlib.utils.LogUtils;
import com.publish.haoffice.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ToRepairFragment extends BaseFragmentapp {
    @InjectView(R.id.rl_torepair)
    LinearLayout rl_torepair;
    @InjectView(R.id.rl_5Ttorepair)
    LinearLayout rl_5Ttorepair;
    @InjectView(R.id.rl_5Ttorepair_look)
    LinearLayout rl_5Ttorepair_look;
    @InjectView(R.id.rl_torepair_look)
    LinearLayout rl_torepair_look;
    private int flag5t;
    private String roleName;

    @Override
    public View initView () {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_torepair, null);
        ButterKnife.inject(this,view);
        init();

        return view;
    }

    private void init () {

        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
            roleName = bundle.getString("roleName");
            LogUtils.d("ckj",bundle.getString("roleName"));
        }

        if("动态车间".equals(roleName) || "动态维修".equals(roleName)){
            rl_torepair.setVisibility(View.GONE);
            rl_torepair_look.setVisibility(View.GONE);
        }



        rl_torepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent2 = new Intent(getActivity(), ToRepairListActivity.class);
                flag5t = 0;
                intent2.putExtra("flag5t", flag5t);
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
        rl_5Ttorepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent2 = new Intent(getActivity(), ToRepairListActivity.class);
                flag5t = 1;
                intent2.putExtra("flag5t", flag5t);
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });

        rl_torepair_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent2 = new Intent(getActivity(), ToRepairLookListActivity.class);
                flag5t = 0;
                intent2.putExtra("flag5t", flag5t);
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
        rl_5Ttorepair_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent2 = new Intent(getActivity(), ToRepairLookListActivity.class);
                flag5t = 1;
                intent2.putExtra("flag5t", flag5t);
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
    }

    @Override
    public void initData () {

    }
}
