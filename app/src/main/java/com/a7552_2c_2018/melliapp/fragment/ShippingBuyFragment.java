package com.a7552_2c_2018.melliapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.BuyingActivity;
import com.a7552_2c_2018.melliapp.model.ActualBuy;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;

import java.util.Objects;

import butterknife.BindView;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ShippingBuyFragment extends Fragment {

    private static final String TAG = "ShippingBuyFragment";

    @BindView(R.id.fsbRbOut) RadioButton noShipping;
    @BindView(R.id.fsbRbShips) RadioButton takesShipping;
    @BindView(R.id.fsbRlAddress) RelativeLayout rlShipping;
    @BindView(R.id.fsbStreet) EditText tvStreet;
    @BindView(R.id.fsbPostalCode) EditText tvCp;
    @BindView(R.id.fsbFloor) EditText tvFloor;
    @BindView(R.id.fsbDep) EditText tvDept;
    @BindView(R.id.fsbCity) EditText tvCity;
    @BindView(R.id.fsbCalculate) Button btCalculateCost;
    @BindView(R.id.fsbShipCost) EditText tvShipCost;

    public ShippingBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_shipping_buys, container, false);

        rlShipping.setVisibility(View.GONE);

        noShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noShipping.isChecked()) {
                    rlShipping.setVisibility(View.GONE);
                    ((BuyingActivity)Objects.requireNonNull(getActivity())).selectTab(1);

                }
            }
        });

        takesShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takesShipping.isChecked()) {
                    rlShipping.setVisibility(View.VISIBLE);
                }
            }
        });

        ImageButton b1 = v.findViewById(R.id.fsbBtnNext);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validInput()){
                    saveValues();
                    ((BuyingActivity)Objects.requireNonNull(getActivity())).selectTab(1);
                } else {
                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.sbf_msg));
                }

            }
        });

        btCalculateCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateCost(tvStreet.getText().toString(), tvCp.getText().toString(),
                        tvCity.getText().toString());
            }
        });

        return v;
    }

    private boolean validInput() {
        if (noShipping.isChecked()){
            return true;
        } else {
            return !tvStreet.getText().toString().isEmpty() && !tvCp.getText().toString().isEmpty()
                    && !tvCity.getText().toString().isEmpty();
        }
    }

    private void saveValues() {
        ActualBuy buy = SingletonUser.getInstance().getActualBuy();
        if (takesShipping.isChecked()){
            buy.setPaysShipping(true);
            buy.setStreet(tvStreet.getText().toString());
            buy.setCp(tvCp.getText().toString());
            buy.setCity(tvCity.getText().toString());
            if (!tvFloor.getText().toString().isEmpty()){
                buy.setFloor(tvFloor.getText().toString());
            }
            if (!tvDept.getText().toString().isEmpty()){
                buy.setFloor(tvDept.getText().toString());
            }
        } else {
            buy.setPaysShipping(false);
        }
    }

    private void calculateCost(String street, String cp, String city) {
        //TODO: call backend
    }

}