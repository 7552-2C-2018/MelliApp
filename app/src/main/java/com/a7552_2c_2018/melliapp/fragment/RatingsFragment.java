package com.a7552_2c_2018.melliapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.adapters.ActivitiesAdapter;
import com.a7552_2c_2018.melliapp.adapters.BuysAdapter;
import com.a7552_2c_2018.melliapp.model.BuyItem;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;


public class RatingsFragment extends Fragment {

    private static final String TAG = "RatingsFragment";

    @BindView(R.id.frtRvRec)
    RecyclerView rvRec;

    @BindView(R.id.frtRvSend)
    RecyclerView rvEnv;

    public RatingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_ratings, container, false);

        // Inflate the layout for this fragment

        ButterKnife.bind(this, v);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(getString(R.string.st_rantings));

        rvEnv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerEnv = new GridLayoutManager(getApplicationContext(), 1);
        rvEnv.setLayoutManager(layoutManagerEnv);

        rvRec.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerRec = new GridLayoutManager(getApplicationContext(), 1);
        rvRec.setLayoutManager(layoutManagerRec);

        getRatingsSend();
        getRatingsReceived();

        final GestureDetector mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        return v;
    }

    private void getRatingsReceived() {
        String REQUEST_TAG = "getRaitingsRecived";
        String url = getString(R.string.remote_rating_reci);
        JsonArrayRequest jsonObtRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::getRatRecResponse,
                error -> {
                    Log.d(TAG, "volley error check" + error.getMessage());
                    //OR
                    Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                    //OR
                    Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                    //Or if nothing works than splitting is the only option
                    //Log.d(TAG, "volley msg4 " +new String(error.networkResponse.data).split(":")[1]);

                    //PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());
                return params;
            }
        };
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObtRequest,REQUEST_TAG);
    }

    private void getRatRecResponse(JSONArray response) {
        Log.d(TAG, response.toString());
        try {
            List<String> input = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                JSONObject jItem = response.getJSONObject(i);
                String item = "Puntaje: " + String.valueOf(jItem.getDouble("value"));
                if (jItem.has("comment")) {
                    item = item + " Comentario: " + jItem.getString("comment");
                }
                input.add(item);
            }
            RecyclerView.Adapter mAdapter = new ActivitiesAdapter(input);
            rvRec.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getRatingsSend() {
        String REQUEST_TAG = "getRatingsSend";
        String url = getString(R.string.remote_rating_send);
        JsonArrayRequest jsonObtRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::getRatSendResponse,
                error -> {
                    Log.d(TAG, "volley error check" + error.getMessage());
                    //OR
                    Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                    //OR
                    Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                    //Or if nothing works than splitting is the only option
                    //Log.d(TAG, "volley msg4 " +new String(error.networkResponse.data).split(":")[1]);

                    //PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());
                return params;
            }
        };
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObtRequest,REQUEST_TAG);
    }

    private void getRatSendResponse(JSONArray response) {
        Log.d(TAG, response.toString());
        try {
            List<String> input = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                JSONObject jItem = response.getJSONObject(i);
                String item = "Puntaje: " + String.valueOf(jItem.getDouble("value"));
                if (jItem.has("comment")) {
                    item = item + " Comentario: " + jItem.getString("comment");
                }
                input.add(item);
            }
            RecyclerView.Adapter mAdapter = new ActivitiesAdapter(input);
            rvEnv.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}