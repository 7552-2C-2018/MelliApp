package com.a7552_2c_2018.melliapp.fragment;

import android.content.Intent;
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
import com.a7552_2c_2018.melliapp.activity.ItemActivity;
import com.a7552_2c_2018.melliapp.activity.ItemSoldActivity;
import com.a7552_2c_2018.melliapp.adapters.BuysAdapter;
import com.a7552_2c_2018.melliapp.model.BuyItem;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

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


public class MyPostsFragment extends Fragment {

    private static final String TAG = "MyPostsFragment";
    @BindView(R.id.fmpRecycler)
    RecyclerView recyclerView;

    public MyPostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_myposts, container, false);

        // Inflate the layout for this fragment

        ButterKnife.bind(this, v);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Mis Publicaciones");

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        getMyPosts();
        //mocking();

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

                        BuysAdapter aux = (BuysAdapter) recyclerView.getAdapter();
                        String postId = Objects.requireNonNull(aux).getBuyItem(position).getPostId();
                        Intent itemIntent = new Intent(getApplicationContext(), ItemActivity.class);
                        itemIntent.putExtra("ID", postId);
                        startActivity(itemIntent);
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

    private void getMyPosts() {
        String REQUEST_TAG = "getMyPosts";
        String url = getString(R.string.remote_posts);
        url = url + "user=" + SingletonUser.getInstance().getUser().getFacebookID();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::getPostsResponse,
                error -> {
                    Log.d(TAG, "volley error check" + error.getMessage());
                    //OR
                    Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                    //OR
                    Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                    //Or if nothing works than splitting is the only option

                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
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
            List<BuyItem> input = new ArrayList<>();
            BuyItem item;
            for (int i = 0; i < response.length(); ++i) {
                JSONObject jItem = response.getJSONObject(i);
                item = new BuyItem();
                if (jItem.isNull("pictures")) {
                    item.setImage(getString(R.string.base64default));
                } else {
                    JSONArray pictures = jItem.getJSONArray("pictures");
                    item.setImage(pictures.getString(0));
                }
                //item.setStatus(jItem.getString("estado"));
                item.setPostId(jItem.getString("ID"));
                item.setStatus("Activa");
                item.setTitle(jItem.getString("title"));
                input.add(item);
            }
            RecyclerView.Adapter mAdapter = new BuysAdapter(input);
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