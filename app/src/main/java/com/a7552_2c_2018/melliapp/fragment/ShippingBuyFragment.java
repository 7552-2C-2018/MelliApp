package com.a7552_2c_2018.melliapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.BuyingActivity;
import com.a7552_2c_2018.melliapp.adapters.BuysAdapter;
import com.a7552_2c_2018.melliapp.adapters.ItemAdapter;
import com.a7552_2c_2018.melliapp.model.BuyItem;
import com.a7552_2c_2018.melliapp.model.PostItem;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ShippingBuyFragment extends Fragment {

    private static final String TAG = "ShippingBuyFragment";
    private RadioButton noShipping, takesShipping;
    private EditText tvStreet, tvCp, tvFloor, tvDept, tvCity;
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
        noShipping.setChecked(true);
        rlShipping = v.findViewById(R.id.fsbRlAddress);
        tvStreet = v.findViewById(R.id.fsbStreet);
        tvCp = v.findViewById(R.id.fsbPostalCode);
        tvFloor = v.findViewById(R.id.fsbFloor);
        tvDept = v.findViewById(R.id.fsbDep);
        tvCity = v.findViewById(R.id.fsbCity);
        rlShipping.setVisibility(View.GONE);

        noShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noShipping.isChecked()) {
                    rlShipping.setVisibility(View.GONE);
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
                    ((BuyingActivity)getActivity()).selectTab(1);
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
            if (tvStreet.getText().toString().isEmpty() || tvCp.getText().toString().isEmpty()
                    || tvCity.getText().toString().isEmpty()) {
                return false;
            } else {
                return true;
            }
        }
    }

}