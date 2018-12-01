package com.a7552_2c_2018.melliapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.BuyingActivity;
import com.a7552_2c_2018.melliapp.model.ActualBuy;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ShippingBuyFragment extends Fragment {

    private static final String TAG = "ShippingBuyFragment";

    @BindView(R.id.fsbRbOut)
    RadioButton noShipping;

    @BindView(R.id.fsbRbShips)
    RadioButton takesShipping;

    @BindView(R.id.fsbRlAddress)
    RelativeLayout rlShipping;

    @BindView(R.id.fsbStreet)
    EditText tvStreet;

    @BindView(R.id.fsbPostalCode)
    EditText tvCp;

    @BindView(R.id.fsbFloor)
    EditText tvFloor;

    @BindView(R.id.fsbDep)
    EditText tvDept;

    @BindView(R.id.fsbCity)
    EditText tvCity;

    @BindView(R.id.fsbCalculate)
    Button btCalculateCost;

    @BindView(R.id.fsbShipCost)
    TextView tvShipCost;

    @BindView(R.id.fsbBtnNext)
    ImageButton btNext;

    public ShippingBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_shipping_buys, container, false);

        ButterKnife.bind(this, v);

        rlShipping.setVisibility(View.GONE);
        noShipping.setOnClickListener(v12 -> {
            if (noShipping.isChecked()) {
                rlShipping.setVisibility(View.GONE);
                saveValues();
                ((BuyingActivity)Objects.requireNonNull(getActivity())).selectTab(1);

            }
        });

        takesShipping.setOnClickListener(v1 -> {
            if (takesShipping.isChecked()) {
                rlShipping.setVisibility(View.VISIBLE);
            }
        });

        btNext.setOnClickListener(view -> {
            if (validInput()){
                saveValues();
                ((BuyingActivity)Objects.requireNonNull(getActivity())).selectTab(1);
            } else {
                PopUpManager.showToastError(getApplicationContext(), getString(R.string.sbf_msg));
            }

        });

        btCalculateCost.setOnClickListener(view -> calculateCost(tvStreet.getText().toString(), tvCp.getText().toString(),
                tvCity.getText().toString()));

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
        SingletonUser.getInstance().setActualBuy(buy);
    }

    private void calculateCost(String street, String cp, String city) {
        //TODO: call backend
    }

}