package com.a7552_2c_2018.melliapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.BuyingActivity;
import com.a7552_2c_2018.melliapp.activity.CheckOutActivity;
import com.a7552_2c_2018.melliapp.model.ActualBuy;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class PayingBuyFragment extends Fragment {

    private static final String TAG = "PayingBuyFragment";
    private static final int RESULT_CHECK_OUT_ACTIVITY = 1;


    @BindView(R.id.fpbRbCash)
    RadioButton cash;

    @BindView(R.id.fpbRbTc)
    RadioButton creditCard;

    @BindView(R.id.fpbBtBack)
    ImageButton btBack;

    @BindView(R.id.fpbBtNext)
    ImageButton btNext;

    public PayingBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_paying_buys, container, false);

        ButterKnife.bind(this, v);


        cash.setOnClickListener(v12 -> {
            if (cash.isChecked()) {
                ActualBuy buy = SingletonUser.getInstance().getActualBuy();
                buy.setPaysWithCard(false);
                SingletonUser.getInstance().setActualBuy(buy);
                ((BuyingActivity)Objects.requireNonNull(getActivity())).selectTab(2);
            }
        });

        creditCard.setOnClickListener(v1 -> {
            if (creditCard.isChecked()) {
                ActualBuy buy = SingletonUser.getInstance().getActualBuy();
                buy.setPaysWithCard(true);
                SingletonUser.getInstance().setActualBuy(buy);
                Intent cardIntent = new Intent(getApplicationContext(), CheckOutActivity.class);
                startActivityForResult(cardIntent, RESULT_CHECK_OUT_ACTIVITY);
            }
        });

        btBack.setOnClickListener(view -> ((BuyingActivity)Objects.requireNonNull(getActivity())).selectTab(0));

        btNext.setOnClickListener(view -> {
            ((BuyingActivity)Objects.requireNonNull(getActivity())).selectTab(2);
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_CHECK_OUT_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    ((BuyingActivity)Objects.requireNonNull(getActivity())).selectTab(2);
                }
                break;
            default:
                super.onActivityResult(requestCode,resultCode, data);
        }
    }


}