package com.a7552_2c_2018.melliapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankingActivity extends AppCompatActivity {

    private static final String TAG = "RankingActivity";
    private String buyId;
    private String postId;
    private String user = "";
    private boolean hasRating = false;

    @BindView(R.id.ratingBar)
    RatingBar rbRank;

    @BindView(R.id.raEtComent)
    EditText etComent;

    @BindView(R.id.raBtSend)
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.st_ranking));

        buyId = getIntent().getStringExtra("buyId");
        postId = getIntent().getStringExtra("postId");
        user = getIntent().getStringExtra("user");

        btnSend.setOnClickListener(v -> {
            if (rbRank.getRating() == 0.0){
                PopUpManager.showToastError(getApplicationContext(), getString(R.string.ra_error));
            } else {
                sendRanking();
            }
        });

        //getRanking();
    }

    private void sendRanking() {
        String REQUEST_TAG = "sendRanking";
        String url = getString(R.string.remote_rating);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                response -> {
                    Log.d(TAG, "Success");
                    etComent.setText("");
                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.ra_send));
                    setResult(RESULT_OK);
                    finish();
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
                Map<String, String> params = new HashMap<>();
                params.put("value", String.valueOf(rbRank.getRating()));
                String rol;
                if (user.equals("seller")){
                    rol = "Vendedor";
                } else {
                    rol = "Comprador";
                }
                params.put("rol", rol);
                if (!etComent.getText().toString().isEmpty()){
                    params.put("comment", etComent.getText().toString());
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());
                params.put("buyId", buyId);
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(stringRequest,REQUEST_TAG);
    }

    private void getRanking() {
        String REQUEST_TAG = "getRanking";
        String url = getString(R.string.remote_rating);
        url = url + buyId;
        JsonObjectRequest jsonObtRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::getRankResponse,
                error -> {
                    Log.d(TAG, "volley error check" + error.getMessage());
                    //OR
                    Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                    //OR
                    Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
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

    private void getRankResponse(JSONObject response) {
        Log.d(TAG, response.toString());
        try {
            hasRating = true;
            rbRank.setRating((float)response.get("value"));
            if (response.has("comment")){
                etComent.setText(response.getString("comment"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
