package com.a7552_2c_2018.melliapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.adapters.BuysAdapter;
import com.a7552_2c_2018.melliapp.adapters.QuestionsAdapter;
import com.a7552_2c_2018.melliapp.model.BuyItem;
import com.a7552_2c_2018.melliapp.model.Question;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

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

public class QuestionsActivity extends AppCompatActivity {

    private static final String TAG = "QuestionsActivity";
    private String Id;

    @BindView(R.id.aqRecycler) RecyclerView recyclerView;

    @BindView(R.id.aqEtQuestion) EditText etQuestion;
    @BindView(R.id.aqBtSendQuestion) Button btnAsk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.st_questions);

        Id = getIntent().getStringExtra("ID");

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        etQuestion.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qst = etQuestion.getText().toString();
                if (qst.isEmpty()){
                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.ia_ask_error));
                } else {
                    sendQuestion(qst);
                }
            }
        });

        //getQuestions();
        mocking();
    }

    private void sendQuestion(final String qst) {
        String REQUEST_TAG = "sendQuestion";
        String url = getString(R.string.remote_posts);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Success");
                        etQuestion.setText("");
                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.ia_ask_ok));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "volley error create " + error.getMessage());
                //OR
                Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                //OR
                Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                //Or if nothing works than splitting is the only option
                Log.d(TAG, "volley msg4 " + new String(error.networkResponse.data));

                PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID", Id);
                params.put("question", qst);
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

    private void getQuestions() {
        String REQUEST_TAG = "getQuestions";
        String url = getString(R.string.remote_buys);
        url = url + "seller=" + SingletonUser.getInstance().getUser().getFacebookID();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        getQuestionsResponse(response);
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

    private void getQuestionsResponse(JSONArray response) {
        Log.d(TAG, response.toString());
        try {
            List<Question> input = new ArrayList<>();
            Question item;
            for (int i = 0; i < response.length(); ++i) {
                JSONObject jItem = response.getJSONObject(i);
                item = new Question();
                /*
                if (jItem.isNull("pictures")) {
                    item.setImage(getString(R.string.base64default));
                } else {
                    JSONArray pictures = jItem.getJSONArray("pictures");
                    item.setImage(pictures.getString(0));
                }
                item.setStatus(jItem.getString("estado"));
                item.setTitle(jItem.getString("title"));
                input.add(item);
                */
            }
            RecyclerView.Adapter mAdapter = new QuestionsAdapter(input);
            recyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mocking() {
        List<Question> input = new ArrayList<>();
        Question item = new Question();
        item.setDate(Long.parseLong(getString(R.string.mock_qst_date_1)));
        item.setQuestion(getString(R.string.mock_qst_qst_1));
        item.setHasResponse(true);
        item.setRespDate(Long.parseLong(getString(R.string.mock_qst_respdate_1)));
        item.setResponse(getString(R.string.mock_qst_resp_1));
        input.add(item);
        item = new Question();
        item.setDate(Long.parseLong(getString(R.string.mock_qst_date_2)));
        item.setQuestion(getString(R.string.mock_qst_qst_2));
        item.setHasResponse(false);
        input.add(item);
        item = new Question();
        item.setDate(Long.parseLong(getString(R.string.mock_qst_date_3)));
        item.setQuestion(getString(R.string.mock_qst_qst_3));
        item.setHasResponse(true);
        item.setRespDate(Long.parseLong(getString(R.string.mock_qst_respdate_3)));
        item.setResponse(getString(R.string.mock_qst_resp_3));
        input.add(item);

        RecyclerView.Adapter mAdapter = new QuestionsAdapter(input);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
