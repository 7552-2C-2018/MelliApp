package com.a7552_2c_2018.melliapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.model.ActualFilters;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FiltersActivity extends AppCompatActivity {

    private static final String TAG = "FiltersActivity";

    @BindView(R.id.faRbNew) RadioButton rbNew;
    @BindView(R.id.faRbUsed) RadioButton rbUsed;
    @BindView(R.id.faSpCateg) Spinner spCateg;
    @BindView(R.id.faTvDist) TextView tvDist;
    @BindView(R.id.faqSbDist) SeekBar sbDist;
    @BindView(R.id.faBtClean) Button btClean;
    @BindView(R.id.faBtApply) Button btApply;
    RangeSeekBar rgSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ButterKnife.bind(this);
        rgSeekBar = findViewById(R.id.rangeSeekbar);
        rgSeekBar.setRangeValues(0, 9999);
        loadFilters();
        spCateg.setEnabled(false);
        getServerCategories();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.st_filters));

        rgSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {


            }
        });

        rgSeekBar.setNotifyWhileDragging(true);

        rgSeekBar.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);

        sbDist.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                tvDist.setText(String.valueOf(progress) + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbNew.setChecked(false);
                rbUsed.setChecked(false);
                rgSeekBar.setSelectedMinValue(0);
                rgSeekBar.setSelectedMaxValue(9999);
                spCateg.setSelection(0);
                sbDist.setProgress(100);
                tvDist.setText("");
                ActualFilters filters = SingletonUser.getInstance().getActualFilters();
                filters.setCategSelected(false);
                filters.setCondSelected(false);
                filters.setCondSelected(false);
                filters.setDistSelected(false);
                SingletonUser.getInstance().setActualFilters(filters);
            }
        });

        btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActualFilters filters = SingletonUser.getInstance().getActualFilters();

                if (rbNew.isChecked()){
                    filters.setOnlyNew(true);
                    filters.setCondSelected(true);
                }
                if (rbUsed.isChecked()){
                    filters.setOnlyUsed(true);
                    filters.setCondSelected(true);
                }
                if ((int)rgSeekBar.getSelectedMinValue() != 0){
                    filters.setMinPrice((int)rgSeekBar.getSelectedMinValue());
                    filters.setPriceSelected(true);
                }
                if ((int)rgSeekBar.getSelectedMaxValue() != 0){
                    filters.setMaxPrice((int)rgSeekBar.getSelectedMaxValue());
                    filters.setPriceSelected(true);
                }
                if (spCateg.getSelectedItemPosition() != 0){
                    filters.setCateg(spCateg.getSelectedItem().toString());
                    filters.setCategSelected(true);
                }
                if (sbDist.getProgress() < sbDist.getMax()){
                    filters.setMaxDist(sbDist.getProgress());
                    filters.setDistSelected(true);
                }
                SingletonUser.getInstance().setActualFilters(filters);
                onBackPressed();
            }
        });
    }

    private void loadFilters() {
        ActualFilters filters = SingletonUser.getInstance().getActualFilters();
        if (filters.isCondSelected()) {
            if (filters.isOnlyNew()){
                rbNew.setChecked(true);
            }
            if (filters.isOnlyUsed()){
                rbUsed.setChecked(true);
            }
        }
        if (filters.isPriceSelected()){
            rgSeekBar.setSelectedMinValue(filters.getMinPrice());
            rgSeekBar.setSelectedMaxValue(filters.getMaxPrice());
        }
        /*
        if (filters.isCategSelected()){
            spCateg.setSelection(getSelectedItem(filters.getCateg()));
        }
        */
        if (filters.isDistSelected()){
            sbDist.setProgress(filters.getMaxDist());
            sbDist.refreshDrawableState();
            tvDist.setText(String.valueOf(filters.getMaxDist()) + " km");
        }

    }

    private int getSelectedItem(String categ) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spCateg.getAdapter();
        int n = adapter.getCount();
        int m = 0;
        for (int i = 0; i < n; i++) {
            String aux = adapter.getItem(i);
            if (aux.equals(categ)){
                m = i;
            }
        }
        return m;
    }


    private void getServerCategories() {
        String REQUEST_TAG = "getCategories";
        String url = getString(R.string.remote_getCat);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        getServerCategoriesResponse(response);
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
                        //Log.d(TAG, "volley msg4 " +new String(error.networkResponse.data).split(":")[1]);

                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());

                return params;
            }
        };
        Log.d(TAG, "categories request " + jsonArrayRequest.toString());
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest,REQUEST_TAG);
    }

    private void getServerCategoriesResponse(JSONArray response) {
        Log.d(TAG, response.toString());
        String[] categories_array = null;
        try {
            categories_array = new String[response.length() + 1];
            categories_array[0] = "Todas";
            for (int i = 0; i < response.length(); ++i) {
                JSONObject item = response.getJSONObject(i);
                String cat = item.getString("name");
                categories_array[i+1] = cat;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCateg.setAdapter(adapter);
        spCateg.setEnabled(true);

        ActualFilters filters = SingletonUser.getInstance().getActualFilters();
        if (filters.isCategSelected()){
            spCateg.setSelection(getSelectedItem(filters.getCateg()));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
