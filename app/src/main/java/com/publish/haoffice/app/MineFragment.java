package com.publish.haoffice.app;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.msystemlib.utils.AlertUtils;
import com.msystemlib.utils.ThreadUtils;
import com.publish.haoffice.R;
import com.publish.haoffice.api.utils.DialogUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MineFragment extends Fragment implements View.OnClickListener {
    @InjectView(R.id.rl_checkversion)
    RelativeLayout rlCheckversion;
    @InjectView(R.id.rl_about)
    RelativeLayout rlAbout;
    @InjectView(R.id.rl_logout)
    RelativeLayout rlLogout;

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            loadingDialog.dismiss();
            AlertUtils.dialog1(getActivity(), "温馨提示", "当前版本已是最新版本！",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }, null);
        };
    };
    private Dialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine,container,false);

        ButterKnife.inject(this,view);
        rlCheckversion.setOnClickListener(this);
		rlAbout.setOnClickListener(this);
        rlLogout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.rl_checkversion:
                loadingDialog = DialogUtils.createLoadingDialog(getActivity(), "正在检查，请稍后...");
                loadingDialog.show();
                ThreadUtils.runInBackground(new Runnable() {

                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        handler.sendEmptyMessage(100);
                    }
                });
                break;
		case R.id.rl_about:
			Intent intent1 = new Intent(getActivity(),AboutActivity.class);
			startActivity(intent1);
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
            case R.id.rl_logout:
                AlertUtils.dialog1(getActivity(), "退出", "是否退出当前账号?",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent2);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                    }
                },new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
        }

    }
}
