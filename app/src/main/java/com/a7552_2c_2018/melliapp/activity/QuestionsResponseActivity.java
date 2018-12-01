package com.a7552_2c_2018.melliapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsResponseActivity extends AppCompatActivity {

    private static final String TAG = "QuestionsRspActivity";
    private String qstId;

    @BindView(R.id.aqrTvPreg)
    TextView tvPreg;

    @BindView(R.id.aqrEtResp)
    EditText etResp;

    @BindView(R.id.aqrBtResp)
    Button btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_response);

        ButterKnife.bind(this);

        btSend.setEnabled(false);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.st_questions);

        qstId = getIntent().getStringExtra("ID");
        getQuestion();

        btSend.setOnClickListener(v -> {
            String qst = etResp.getText().toString();
            if (qst.isEmpty()){
                PopUpManager.showToastError(getApplicationContext(), getString(R.string.ia_ask_error));
            } else {
                sendResponse(qst);
            }
        });
    }

    private void getQuestion() {
        String REQUEST_TAG = "getQuestions";
        String url = getString(R.string.remote_questions);
        url = url + qstId;
        JsonObjectRequest jsonObtRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::getQstResponse,
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

    private void getQstResponse(JSONObject response) {
        Log.d(TAG, response.toString());
        try {
            tvPreg.setText(response.getString("pregunta")); //TODO
            btSend.setEnabled(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(String msg) {
        String REQUEST_TAG = "sendResponse";
        String url = getString(R.string.remote_questions);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                url,
                response -> {
                    Log.d(TAG, "Success");
                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.qra_resp_ok));

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
                params.put("respuesta", msg);
                params.put("questionId", qstId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(stringRequest,REQUEST_TAG);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
