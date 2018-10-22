package com.a7552_2c_2018.melliapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.BuyingActivity;
import com.a7552_2c_2018.melliapp.activity.CheckOutActivity;

import static com.facebook.FacebookSdk.getApplicationContext;


public class PayingBuyFragment extends Fragment {

    private static final String TAG = "PayingBuyFragment";
    private RadioButton cash, creditCard;

    public PayingBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_paying_buys, container, false);

        cash = v.findViewById(R.id.fpbRbCash);
        creditCard = v.findViewById(R.id.fpbRbTc);

        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (creditCard.isChecked()) {
                    Intent cardIntent = new Intent(getApplicationContext(), CheckOutActivity.class);
                    startActivity(cardIntent);
                }
            }
        });

        ImageButton b1 = v.findViewById(R.id.fpbBtBack);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BuyingActivity)getActivity()).selectTab(0);
            }
        });

        ImageButton b2 = v.findViewById(R.id.fpbBtNext);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BuyingActivity)getActivity()).selectTab(2);
            }
        });

        return v;


    }

}