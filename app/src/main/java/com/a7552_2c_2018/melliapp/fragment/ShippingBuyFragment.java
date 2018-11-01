package com.a7552_2c_2018.melliapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.BuyingActivity;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;

import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ShippingBuyFragment extends Fragment {

    private static final String TAG = "ShippingBuyFragment";
    private RadioButton noShipping, takesShipping;
    private EditText tvStreet;
    private EditText tvCp;
    private EditText tvDept;
    private EditText tvCity;
    private RelativeLayout rlShipping;

    public ShippingBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_shipping_buys, container, false);

        noShipping = v.findViewById(R.id.fsbRbOut);
        takesShipping = v.findViewById(R.id.fsbRbShips);
        rlShipping = v.findViewById(R.id.fsbRlAddress);
        tvStreet = v.findViewById(R.id.fsbStreet);
        tvCp = v.findViewById(R.id.fsbPostalCode);
        EditText tvFloor = v.findViewById(R.id.fsbFloor);
        tvDept = v.findViewById(R.id.fsbDep);
        tvCity = v.findViewById(R.id.fsbCity);
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
                    ((BuyingActivity)Objects.requireNonNull(getActivity())).selectTab(1);
                } else {
                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.sbf_msg));
                }

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

}