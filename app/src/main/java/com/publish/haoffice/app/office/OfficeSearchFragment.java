package com.publish.haoffice.app.office;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msystemlib.base.BaseFragment;
import com.publish.haoffice.R;
import com.publish.haoffice.app.Repair.BaseFragmentapp;


public class OfficeSearchFragment extends BaseFragmentapp {

    @Override
    public View initView () {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frg_office_search,null);

        return view;
    }

    @Override
    public void initData () {

    }
}
