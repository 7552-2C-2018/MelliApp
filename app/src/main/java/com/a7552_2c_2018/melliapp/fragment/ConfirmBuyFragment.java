package com.a7552_2c_2018.melliapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ConfirmBuyFragment extends Fragment {

    private static final String TAG = "ConfirmBuyFragment";

    @BindView(R.id.fcbTvItem) TextView tvItemDesc;
    @BindView(R.id.fcbTvItemPrice) TextView tvItemPrice;
    @BindView(R.id.fcbTvShip) TextView tvShipDesc;
    @BindView(R.id.fcbTvShipPrice) TextView tvShipPrice;
    @BindView(R.id.fcbTvPay) TextView tvPayDesc;
    @BindView(R.id.fcbTvPayPrice) TextView tvPayPrice;
    @BindView(R.id.fcbBtnConf) Button btConfirm;

    public ConfirmBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_confirm_buys, container, false);

        ButterKnife.bind(this, v);

        return v;
    }

}