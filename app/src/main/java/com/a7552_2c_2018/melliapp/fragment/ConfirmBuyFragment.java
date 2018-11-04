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
import com.a7552_2c_2018.melliapp.model.ActualBuy;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;

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

        ActualBuy buy = SingletonUser.getInstance().getActualBuy();
        int total = buy.getPrice();

        tvItemDesc.setText(buy.getTitle());
        tvItemPrice.setText("$ " + buy.getPrice());
        if (buy.isPaysShipping()){
            tvShipDesc.setText(getString(R.string.cbf_takes));
            tvShipPrice.setText("$ " + buy.getShippingPrice());
            total =+ buy.getShippingPrice();
        } else {
            tvShipDesc.setText(getString(R.string.cbf_out));
            tvShipPrice.setText("$ 0");
        }
        if (buy.isPaysWithCard()){
            tvPayDesc.setText(getString(R.string.cbf_tc) + buy.getCardNumber().substring(12,15));
        } else {
            tvPayDesc.setText(getString(R.string.cbf_cash));
        }
        tvPayPrice.setText("$ " + total);


        return v;
    }

}