package com.a7552_2c_2018.melliapp.fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.FiltersActivity;
import com.a7552_2c_2018.melliapp.activity.ItemActivity;
import com.a7552_2c_2018.melliapp.activity.MapActivity;
import com.a7552_2c_2018.melliapp.activity.PostsActivity;
import com.a7552_2c_2018.melliapp.adapters.ItemAdapter;
import com.a7552_2c_2018.melliapp.model.ActualFilters;
import com.a7552_2c_2018.melliapp.model.PostItem;
import com.a7552_2c_2018.melliapp.model.UserInfo;
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


public class PostsFragment extends Fragment {

    private static final String TAG = "PostsFragment";
    private static final int RESULT_FILTERS_ACTIVITY = 1;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fpRecycler)
    RecyclerView recyclerView;

    @BindView(R.id.fpSearching)
    RelativeLayout searching;

    @BindView(R.id.fpAbNew)
    FloatingActionButton fabNew;

    @BindView(R.id.fbEtSearch)
    EditText fabInput;

    @BindView(R.id.fpBtnSearch)
    ImageButton fabSearch;

    @BindView(R.id.fpBtnFilter)
    ImageButton fabFilter;

    @BindView(R.id.fbBtnMap)
    ImageButton fabMap;

    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_posts, container, false);

        ButterKnife.bind(this, v);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Comprame");

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        getPosts();

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

                        ItemAdapter aux = (ItemAdapter) recyclerView.getAdapter();
                        String Id = Objects.requireNonNull(aux).getPostItem(position).getId();
                        Intent itemIntent = new Intent(getApplicationContext(), ItemActivity.class);
                        itemIntent.putExtra("ID", Id);
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

        fabNew.setOnClickListener(v1 -> {
            Intent postIntent = new Intent(getApplicationContext(), PostsActivity.class);
            startActivity(postIntent);
        });

        fabSearch.setOnClickListener(v12 -> getPosts());

        fabSearch.getBackground().setColorFilter(0x00000000,PorterDuff.Mode.MULTIPLY);

        fabFilter.setOnClickListener(v13 -> {
            Intent filterIntent = new Intent(getApplicationContext(), FiltersActivity.class);
            startActivityForResult(filterIntent, RESULT_FILTERS_ACTIVITY);
        });

        fabFilter.getBackground().setColorFilter(0x00000000,PorterDuff.Mode.MULTIPLY);

        fabMap.setOnClickListener(v14 -> {
            Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(mapIntent);
        });
        fabMap.getBackground().setColorFilter(0x00000000,PorterDuff.Mode.MULTIPLY);

        fabInput.setOnTouchListener((v15, event) -> {

            v15.setFocusable(true);
            v15.setFocusableInTouchMode(true);
            return false;
        });

        swipeRefreshLayout.setOnRefreshListener(() -> getPosts());

        return v;
    }

    private void getPosts() {
        String REQUEST_TAG = "getPosts";
        String url = getString(R.string.remote_posts);
        /*
        String params = getFilterParams();
        url += params;
        */
        Log.d(TAG, "Get Post URL" + url);
        recyclerView.setVisibility(View.GONE);
        searching.setVisibility(View.VISIBLE);
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
                    Log.d(TAG, "volley msg4 " +new String(error.networkResponse.data).split(":")[1]);

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
                String search = fabInput.getText().toString();
                if (!search.isEmpty()){
                    params.put("search", search);
                }

                ActualFilters filters = SingletonUser.getInstance().getActualFilters();
                UserInfo user = SingletonUser.getInstance().getUser();
                if (filters.isCondSelected()) {
                    if (filters.isOnlyNew()){
                        params.put("estado", "nuevo");
                    }
                    if (filters.isOnlyUsed()){
                        params.put("estado", "usado");
                    }
                }
                if (filters.isPriceSelected()){
                    params.put("precioMinimo", String.valueOf(filters.getMinPrice()));
                    params.put("precioMaximo", String.valueOf(filters.getMaxPrice()));
                }
                if (filters.isCategSelected()){
                    params.put("categoria", filters.getCateg());
                }
                if (filters.isShipSelected()) {
                    if (filters.isShipYes()){
                        params.put("envio", "1");
                    }
                    if (filters.isShipNo()){
                        params.put("envio", "0");
                    }
                }
                if (filters.isDistSelected()){
                    params.put("distancia", String.valueOf(filters.getMaxDist()));
                    params.put("latitud", String.valueOf(user.getLatitude()));
                    params.put("longitud", String.valueOf(user.getLongitude()));
                }

                if (filters.anyFilterOn()){
                    fabFilter.getBackground().setColorFilter(0xFFFFFF00,PorterDuff.Mode.MULTIPLY);
                } else {
                    fabFilter.getBackground().setColorFilter(0x00000000,PorterDuff.Mode.MULTIPLY);
                }
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
                item.setId(jItem.getString("ID"));
                JSONArray jArray = jItem.getJSONArray("coordenates");
                item.setLongitude(jArray.getDouble(0));
                item.setLatitude(jArray.getDouble(1));
                input.add(item);
            }
            SingletonUser.getInstance().setItemList(input);
            searching.setVisibility(View.GONE);
            RecyclerView.Adapter mAdapter = new ItemAdapter(input);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getFilterParams() {
        String result = "?search=";
        String search = fabInput.getText().toString();
        result += search;

        ActualFilters filters = SingletonUser.getInstance().getActualFilters();
        UserInfo user = SingletonUser.getInstance().getUser();
        if (filters.isCondSelected()) {
            result += "&cond=";
            if (filters.isOnlyNew()) {
                result += "new";
            }
            if (filters.isOnlyUsed()) {
                result += "used";
            }
        }
        if (filters.isPriceSelected()) {
            result += "&minPrice=" + filters.getMinPrice();
            result += "&maxPrice=" + filters.getMaxPrice();
        }
        if (filters.isCategSelected()) {
            result += "&categ=" + filters.getCateg();
        }
        if (filters.isShipSelected()) {
            result += "&shipping=";
            if (filters.isShipYes()) {
                result += "yes";
            }
            if (filters.isShipNo()) {
                result += "no";
            }
        }
        if (filters.isDistSelected()) {
            result += "&distance=" + filters.getMaxDist();
            result += "&lat=" + String.valueOf(user.getLatitude());
            result += "&long=" + String.valueOf(user.getLongitude());
        }

        if (filters.anyFilterOn()) {
            fabFilter.getBackground().setColorFilter(0xFFFFFF00, PorterDuff.Mode.MULTIPLY);
        } else {
            fabFilter.getBackground().setColorFilter(0x00000000, PorterDuff.Mode.MULTIPLY);
        }
        return result;
    }

        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_FILTERS_ACTIVITY:
                getPosts();
                break;
            default:
                super.onActivityResult(requestCode,resultCode, data);
        }
    }
}
