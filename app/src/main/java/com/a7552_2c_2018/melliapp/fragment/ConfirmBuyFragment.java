package com.a7552_2c_2018.melliapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a7552_2c_2018.melliapp.R;


public class ConfirmBuyFragment extends Fragment {

    private static final String TAG = "ConfirmBuyFragment";

    public ConfirmBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_confirm_buys, container, false);
    }

}