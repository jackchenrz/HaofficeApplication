package com.publish.haoffice.app.Repair;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.msystemlib.utils.LogUtils;
import com.publish.haoffice.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RepairFragment extends Fragment {

    @InjectView(R.id.rl_repair)
    RelativeLayout rl_repair;
    @InjectView(R.id.rl_5Trepair)
    RelativeLayout rl_5Trepair;
    @InjectView(R.id.layout_5Tinput)
    View layout_5Tinput;
    @InjectView(R.id.layout_input)
    View layout_input;
    @InjectView(R.id.iv_click)
    ImageView iv_click;
    @InjectView(R.id.iv_click1)
    ImageView iv_click1;

    private boolean isClick = true;
    private boolean isClick1 = false;
    private String roleName;
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_repair,container,false);
        ButterKnife.inject(this,view);

        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
            roleName = bundle.getString("roleName");
        }

        if("动态车间".equals(roleName)){
            rl_repair.setVisibility(View.GONE);
        }
        layout_input.setVisibility(View.GONE);
        layout_5Tinput.setVisibility(View.GONE);
        rl_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(isClick){
                    layout_input.setVisibility(View.GONE);
                    iv_click.setBackgroundResource(R.drawable.minus_index);
                }else{
                    layout_input.setVisibility(View.VISIBLE);
                    iv_click.setBackgroundResource(R.drawable.plus_index);
                }

                isClick = !isClick;
            }
        });
        rl_5Trepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(isClick1){
                    layout_5Tinput.setVisibility(View.GONE);

                    iv_click1.setBackgroundResource(R.drawable.minus_index);
                }else{
                    layout_5Tinput.setVisibility(View.VISIBLE);
                    iv_click1.setBackgroundResource(R.drawable.plus_index);
                }

                isClick1 = !isClick1;
            }
        });

        return view;
    }


}
