package com.a7552_2c_2018.melliapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.HomeActivity;
import com.a7552_2c_2018.melliapp.activity.ItemActivity;
import com.a7552_2c_2018.melliapp.model.ActualBuy;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ConfirmBuyFragment extends Fragment {

    private static final String TAG = "ConfirmBuyFragment";

    @BindView(R.id.fcbTvItem) TextView tvItemDesc;
    @BindView(R.id.fcbTvItemPrice) TextView tvItemPrice;
    @BindView(R.id.fcbTvShip) TextView tvShipDesc;
    @BindView(R.id.fcbTvShipPrice) TextView tvShipPrice;
    @BindView(R.id.fcbTvPay) TextView tvPayDesc;
    @BindView(R.id.fcbTvPayPrice) TextView tvPayPrice;
    @BindView(R.id.fcbBtnConf) Button btConfirm;

    int total;

    public ConfirmBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_confirm_buys, container, false);

        ButterKnife.bind(this, v);

        ActualBuy buy = SingletonUser.getInstance().getActualBuy();
        total = buy.getPrice();

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

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackend();
            }
        });
        return v;
    }

    private void callBackend(){
        String REQUEST_TAG = "confirmBuy";
        String url = getString(R.string.remote_posts);
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Success");
                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.cbf_confirm));
                        Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(homeIntent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "volley error create " + error.getMessage());
                //OR
                Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                //OR
                Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                //Or if nothing works than splitting is the only option
                Log.d(TAG, "volley msg4 " + new String(error.networkResponse.data));

                PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                ActualBuy buy = SingletonUser.getInstance().getActualBuy();
                Map<String, String> params = new HashMap<>();
                params.put("publicationId", buy.getId());
                params.put("price", String.valueOf(total));
                if (buy.isPaysShipping()){
                    params.put("street", buy.getStreet());
                    params.put("cp", buy.getCp());
                    params.put("floor", buy.getFloor());
                    params.put("dept", buy.getDept());
                    params.put("city", buy.getCity());
                }
                if (buy.isPaysWithCard()){
                    params.put("cardNumber", buy.getCardNumber());
                    params.put("cardDate", buy.getCardDate());
                    params.put("cardName", buy.getCardName());
                    params.put("cardCVV", String.valueOf(buy.getCardCVV()));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());
                return params;
            }

        };

        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObjRequest,REQUEST_TAG);
    }
}