package com.a7552_2c_2018.melliapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.ItemActivity;
import com.a7552_2c_2018.melliapp.activity.MainActivity;
import com.a7552_2c_2018.melliapp.activity.PostsActivity;
import com.a7552_2c_2018.melliapp.adapters.BuysAdapter;
import com.a7552_2c_2018.melliapp.adapters.ItemAdapter;
import com.a7552_2c_2018.melliapp.model.BuyItem;
import com.a7552_2c_2018.melliapp.model.PostItem;
import com.a7552_2c_2018.melliapp.model.UserInfo;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;


public class BuysFragment extends Fragment {

    private static final String TAG = "BuysFragment";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public BuysFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_buys, container, false);

        // Inflate the layout for this fragment

        recyclerView = v.findViewById(R.id.fbRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        //getBuys();
        mocking();

        final GestureDetector mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                try {
                    View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                    if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                        int position = recyclerView.getChildAdapterPosition(child);
                        /*
                        ItemAdapter aux = (ItemAdapter) recyclerView.getAdapter();
                        String fId = aux.getPostItem(position).getFacebookId();
                        String publ = aux.getPostItem(position).getPublDate();
                        Intent itemIntent = new Intent(getApplicationContext(), ItemActivity.class);
                        itemIntent.putExtra("facebookId", fId);
                        itemIntent.putExtra("pubDate", publ);
                        startActivity(itemIntent);
                        */
                        return true;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }
        });

        return v;
    }

    private void getBuys() {
        String REQUEST_TAG = "getBuys";
        String url = getString(R.string.remote_posts_all);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        getPostsResponse(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d(TAG, "volley error check" + error.getMessage());
                        //OR
                        Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                        //OR
                        Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                        //Or if nothing works than splitting is the only option
                        Log.d(TAG, "volley msg4 " +new String(error.networkResponse.data).split(":")[1]);

                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                    }
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
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest,REQUEST_TAG);
    }

    private void getPostsResponse(JSONArray response) {
        Log.d(TAG, response.toString());
        try {
            List<PostItem> input = new ArrayList<>();
            PostItem item;
            for (int i = 0; i < response.length(); ++i) {
                JSONObject jItem = response.getJSONObject(i);
                item = new PostItem();
                if (jItem.isNull("pictures")) {
                    item.setImage(getString(R.string.base64mock));
                } else {
                    JSONArray pictures = jItem.getJSONArray("pictures");
                    item.setImage(pictures.getString(0));
                }
                item.setPrice(jItem.getInt("price"));
                item.setDesc(jItem.getString("title"));
                JSONObject jIdItem = jItem.getJSONObject("_id");
                item.setFacebookId(jIdItem.getString("facebookId"));
                item.setPublDate(String.valueOf(jIdItem.getLong("publication_date")));
                input.add(item);
            }
            RecyclerView.Adapter mAdapter = new ItemAdapter(input);
            recyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mocking() {
        List<BuyItem> input = new ArrayList<>();
        BuyItem item;
        for (int i = 0; i < 4; ++i) {
            item = new BuyItem();
            item.setImage(getString(R.string.base64mock));
            item.setStatus(getString(R.string.mock_status));
            item.setTitle(getString(R.string.mock_title));
            input.add(item);
        }
        RecyclerView.Adapter mAdapter = new BuysAdapter(input);
        recyclerView.setAdapter(mAdapter);
    }
}