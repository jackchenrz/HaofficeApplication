package com.publish.haoffice.app.office;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msystemlib.base.BaseFragment;
import com.publish.haoffice.R;

/**
 * Created by ACER on 2016/6/15.
 */
public class OfficeNoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_office_no,container,false);
    }
}
