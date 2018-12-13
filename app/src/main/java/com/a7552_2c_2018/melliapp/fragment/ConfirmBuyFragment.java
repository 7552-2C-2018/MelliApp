package com.a7552_2c_2018.melliapp.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.CheckOutActivity;
import com.a7552_2c_2018.melliapp.activity.HomeActivity;
import com.a7552_2c_2018.melliapp.model.ActualBuy;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.CreditCardUtils;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ConfirmBuyFragment extends Fragment {

    private static final String TAG = "ConfirmBuyFragment";

    @BindView(R.id.fcbTvItem)
    TextView tvItemDesc;

    @BindView(R.id.fcbTvItemPrice)
    TextView tvItemPrice;

    @BindView(R.id.fcbTvShip)
    TextView tvShipDesc;

    @BindView(R.id.fcbTvShipPrice)
    TextView tvShipPrice;

    @BindView(R.id.fcbTvPay)
    TextView tvPayDesc;

    @BindView(R.id.fcbTvPayPrice)
    TextView tvPayPrice;

    @BindView(R.id.fcbBtnConf)
    Button btConfirm;

    private int total;

    public ConfirmBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_confirm_buys, container, false);

        ButterKnife.bind(this, v);

        ActualBuy buy = SingletonUser.getInstance().getActualBuy();
        total = buy.getTotal();
        tvItemDesc.setText(buy.getTitle().substring(0,Math.min(buy.getTitle().length(),33)));
        Resources res = getResources();
        tvItemPrice.setText(String.format(res.getString(R.string.price_holder), buy.getPrice()));
        if (buy.isPaysShipping()){
            tvShipDesc.setText(getString(R.string.cbf_takes));
            tvShipPrice.setText(String.format(res.getString(R.string.price_holder), buy.getShippingPrice()));
        } else {
            tvShipDesc.setText(getString(R.string.cbf_out));
            tvShipPrice.setText(String.format(res.getString(R.string.price_holder), 0));
        }
        if (buy.isPaysWithCard()){
            tvPayDesc.setText(String.format(res.getString(R.string.cbf_tc), buy.getCardNumber().substring(12,15)));
        } else {
            tvPayDesc.setText(getString(R.string.cbf_cash));
        }
        tvPayPrice.setText(String.format(res.getString(R.string.price_holder), total));

        btConfirm.setOnClickListener(v1 -> {if (checkEntries()) {callBackend();}});
        return v;
    }

    private void callBackend(){
        String REQUEST_TAG = "confirmBuy";
        String url = getString(R.string.remote_buys);
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                url,
                response -> {
                    Log.d(TAG, "Success");
                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.cbf_confirm));
                    Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(homeIntent);
                }, error -> {
                    Log.d(TAG, "volley error create " + error.getMessage());
                    //OR
                    Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                    //OR
                    Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                    //Or if nothing works than splitting is the only option
                    Log.d(TAG, "volley msg4 " + new String(error.networkResponse.data));

                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                ActualBuy buy = SingletonUser.getInstance().getActualBuy();
                Map<String, String> params = new HashMap<>();
                params.put("price", String.valueOf(total));
                if (buy.isPaysShipping()){
                    params.put("street", buy.getStreet());
                    params.put("cp", buy.getCp());
                    params.put("floor", buy.getFloor());
                    params.put("dept", buy.getDept());
                    params.put("city", "CABA");
                }
                if (buy.isPaysWithCard()){
                    params.put("cardNumber", buy.getCardNumber());
                    params.put("cardBank", "Frances");
                    params.put("cardDate", buy.getCardDate());
                    params.put("cardName", buy.getCardName());
                    params.put("cardCVV", String.valueOf(buy.getCardCVV()));
                    params.put("paymentMethod", "Credito");
                } else {
                    params.put("paymentMethod", "Efectivo");
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                ActualBuy buy = SingletonUser.getInstance().getActualBuy();
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());
                params.put("postId", buy.getId());
                return params;
            }

        };

        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObjRequest,REQUEST_TAG);
    }

    private boolean checkEntries() {

        ActualBuy buy = SingletonUser.getInstance().getActualBuy();

        if (!buy.isPaysWithCard()) {
            return true;
        }

        String cardName = buy.getCardName();
        String cardNumber = buy.getCardNumber();
        String cardValidity = buy.getCardDate();
        String cardCVV = String.valueOf(buy.getCardCVV());

        if (TextUtils.isEmpty(cardName)) {
            PopUpManager.showToastError(getApplicationContext(), "Ingrese un nombre valido");
            return false;
        } else if (TextUtils.isEmpty(cardNumber) || !CreditCardUtils.isValid(cardNumber.replace(" ",""))) {
            PopUpManager.showToastError(getApplicationContext(), "Ingrese un número valido");
            return false;
        } else if (TextUtils.isEmpty(cardValidity)||!CreditCardUtils.isValidDate(cardValidity)) {
            PopUpManager.showToastError(getApplicationContext(), "Verifique la fecha");
            return false;
        } else if (TextUtils.isEmpty(cardCVV)|| cardCVV.length()<3) {
            PopUpManager.showToastError(getApplicationContext(), "Ingrese un código de seguridad válido");
            return false;
        } else {
            return true;
        }
    }
}