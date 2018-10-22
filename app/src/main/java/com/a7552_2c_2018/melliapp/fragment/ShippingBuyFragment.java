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

    public ShippingBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_shipping_buys, container, false);

        ImageButton b1 = v.findViewById(R.id.fsbBtnNext);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BuyingActivity)getActivity()).selectTab(1);
            }
        });
        /*
        etName = v.findViewById(R.id.faEtName);
        etName.setText(user.getName());

        etSurname = v.findViewById(R.id.faEtSurname);
        etSurname.setText(user.getSurname());

        EditText etEmail = v.findViewById(R.id.faEtEmail);
        etEmail.setText(user.getEmail());

        ProfilePictureView profilePicture;
        profilePicture = v.findViewById(R.id.AccProfilePicture);
        profilePicture.setProfileId(user.getFacebookID());

        Button btLogin = v.findViewById(R.id.logout_button);
        btLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                logOut();
            }
        });

        Button btSave = v.findViewById(R.id.faBtnSave);
        btSave.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                saveChanges();

            }
        });
        */
        // Inflate the layout for this fragment
        return v;
    }

}