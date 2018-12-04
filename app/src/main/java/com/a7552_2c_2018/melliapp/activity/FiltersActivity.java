package com.a7552_2c_2018.melliapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.model.ActualFilters;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
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

    @BindView(R.id.faRbNew)
    RadioButton rbNew;

    @BindView(R.id.faRbUsed)
    RadioButton rbUsed;

    @BindView(R.id.faSpCateg)
    Spinner spCateg;

    @BindView(R.id.faRbShipYes)
    RadioButton rbShipYes;

    @BindView(R.id.faRbShipNo)
    RadioButton rbShipNo;

    @BindView(R.id.faTvDist)
    TextView tvDist;

    @BindView(R.id.faqSbDist)
    SeekBar sbDist;

    @BindView(R.id.faBtClean)
    Button btClean;

    @BindView(R.id.faBtApply)
    Button btApply;

    private RangeSeekBar rgSeekBar;

    @BindView(R.id.faEtPriceMin)
    EditText etPrinceMin;

    @BindView(R.id.faEtPriceMax)
    EditText etPrinceMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ButterKnife.bind(this);
        rgSeekBar = findViewById(R.id.rangeSeekbar);
        rgSeekBar.setRangeValues(0, 9999);
        etPrinceMin.setText(String.valueOf(0));
        etPrinceMax.setText(String.valueOf(9999));
        loadFilters();
        spCateg.setEnabled(false);
        getServerCategories();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.st_filters));

        rgSeekBar.setOnRangeSeekBarChangeListener((RangeSeekBar.OnRangeSeekBarChangeListener<Integer>) (bar, minValue, maxValue) -> {


        });

        rgSeekBar.setNotifyWhileDragging(true);

        rgSeekBar.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);

        sbDist.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                tvDist.setText(String.format(getString(R.string.fa_progress), String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btClean.setOnClickListener(v -> {
            rbNew.setChecked(false);
            rbUsed.setChecked(false);
            rgSeekBar.setSelectedMinValue(0);
            rgSeekBar.setSelectedMaxValue(9999);
            etPrinceMin.setText(String.valueOf(0));
            etPrinceMax.setText(String.valueOf(9999));
            spCateg.setSelection(0);
            rbShipYes.setChecked(false);
            rbShipNo.setChecked(false);
            sbDist.setProgress(100);
            tvDist.setText("");
            ActualFilters filters = SingletonUser.getInstance().getActualFilters();
            filters.setCategSelected(false);
            filters.setCondSelected(false);
            filters.setPriceSelected(false);
            filters.setDistSelected(false);
            filters.setShipSelected(false);
            SingletonUser.getInstance().setActualFilters(filters);
            finish();
            setResult(RESULT_OK);
        });

        btApply.setOnClickListener(v -> {

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
            if (rbShipYes.isChecked()){
                filters.setShipYes(true);
                filters.setShipSelected(true);
            }
            if (rbShipNo.isChecked()){
                filters.setShipNo(true);
                filters.setShipSelected(true);
            }
            if (sbDist.getProgress() < sbDist.getMax()){
                filters.setMaxDist(sbDist.getProgress());
                filters.setDistSelected(true);
            }
            SingletonUser.getInstance().setActualFilters(filters);
            finish();
            setResult(RESULT_OK);
            return;
        });

        etPrinceMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (etPrinceMin.isFocused()) { //<-- check if is focused
                    if (!etPrinceMin.getText().toString().isEmpty()){
                        rgSeekBar.setSelectedMinValue(Integer.valueOf(etPrinceMin.getText().toString()));
                        if (Integer.valueOf(etPrinceMin.getText().toString()) > Integer.valueOf(etPrinceMax.getText().toString())){
                            rgSeekBar.setSelectedMaxValue(rgSeekBar.getSelectedMinValue());
                            etPrinceMax.setText(etPrinceMin.getText().toString());
                        }
                    }
                }
            }
        });

        etPrinceMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (etPrinceMax.isFocused()) { //<-- check if is focused
                    if (!etPrinceMax.getText().toString().isEmpty()) {
                        rgSeekBar.setSelectedMaxValue(Integer.valueOf(etPrinceMax.getText().toString()));
                        if (Integer.valueOf(etPrinceMax.getText().toString()) < Integer.valueOf(etPrinceMin.getText().toString())) {
                            rgSeekBar.setSelectedMinValue(rgSeekBar.getSelectedMaxValue());
                            etPrinceMin.setText(etPrinceMax.getText().toString());
                        }
                    }
                }
            }
        });

        rgSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                etPrinceMin.setText(rgSeekBar.getSelectedMinValue().toString());
                etPrinceMax.setText(rgSeekBar.getSelectedMaxValue().toString());
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
        if (filters.isCategSelected()){
            spCateg.setSelection(getSelectedItem(filters.getCateg()));
        }
        if (filters.isShipSelected()) {
            if (filters.isShipYes()){
                rbShipYes.setChecked(true);
            }
            if (filters.isShipNo()){
                rbShipNo.setChecked(true);
            }
        }
        if (filters.isDistSelected()){
            sbDist.setProgress(filters.getMaxDist());
            sbDist.refreshDrawableState();
            tvDist.setText(String.format(getString(R.string.fa_progress), String.valueOf(filters.getMaxDist())));
        }

    }

    private int getSelectedItem(String categ) {
        @SuppressWarnings("unchecked") ArrayAdapter<String> adapter = (ArrayAdapter<String>) spCateg.getAdapter();
        int n = adapter.getCount();
        int m = 0;
        for (int i = 0; i < n; i++) {
            String aux = adapter.getItem(i);
            if (Objects.requireNonNull(aux).equals(categ)){
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
                this::getServerCategoriesResponse,
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
