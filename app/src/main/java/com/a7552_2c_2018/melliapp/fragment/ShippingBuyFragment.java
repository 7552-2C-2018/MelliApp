package com.a7552_2c_2018.melliapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.a7552_2c_2018.melliapp.model.ActualFilters;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
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
            if (noShipping.isChecked()) {
                saveValues();
                ((BuyingActivity)Objects.requireNonNull(getActivity())).selectTab(1);
            } else {
                if (takesShipping.isChecked()){
                    if (validInput()){
                        saveValues();
                        ((BuyingActivity)Objects.requireNonNull(getActivity())).selectTab(1);
                    } else {
                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.sbf_msg));
                    }
                } else {
                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.sbf_msg2));
                }
            }
        });

        btCalculateCost.setOnClickListener(view -> calculateCost(tvStreet.getText().toString(), tvCp.getText().toString(),
                tvCity.getText().toString()));

        ActualBuy buy = SingletonUser.getInstance().getActualBuy();
        if (buy.getPrice() < 50) {
            noShipping.setVisibility(View.INVISIBLE);
        }

        return v;
    }

    private boolean validInput() {
        return !tvStreet.getText().toString().isEmpty() && !tvCp.getText().toString().isEmpty()
                    && !tvCity.getText().toString().isEmpty();
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
                buy.setDept(tvDept.getText().toString());
            }
        } else {
            buy.setPaysShipping(false);
        }
        SingletonUser.getInstance().setActualBuy(buy);
    }

    private void calculateCost(String street, String cp, String city) {
        String REQUEST_TAG = "getScore";
        String url = getString(R.string.remote_estimate);
        JsonObjectRequest jsonObtRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::getShippingResponse,
                error -> {
                    Log.d(TAG, "volley error check" + error.getMessage());
                    //OR
                    Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                    //OR
                    Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                    //Or if nothing works than splitting is the only option
                    //Log.d(TAG, "volley msg4 " +new String(error.networkResponse.data).split(":")[1]);

                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                ActualBuy buy = SingletonUser.getInstance().getActualBuy();
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());
                params.put("postId", buy.getId());
                params.put("street", street);
                params.put("cp", cp);
                params.put("city", "CABA");
                return params;
            }
        };
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObtRequest,REQUEST_TAG);
    }

    private void getShippingResponse(JSONObject response) {
        Log.d(TAG, response.toString());
        try {
            int price = (int) response.getDouble("ShipmentCost");
            String text = String.format(getString(R.string.price_holder), price);
            tvShipCost.setText(text);
            ActualBuy buy = SingletonUser.getInstance().getActualBuy();
            buy.setShippingPrice(price);
            SingletonUser.getInstance().setActualBuy(buy);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}