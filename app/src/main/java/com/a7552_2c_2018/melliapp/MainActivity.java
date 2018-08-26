package com.a7552_2c_2018.melliapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    String BASE_URI = "https://api.chucknorris.io/jokes/random";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getTestText();
    }

    public void getTestText() {

        String url = BASE_URI;
        String REQUEST_TAG = "getJoke";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showResponse(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.v(TAG, "error: " + error.toString());
                        PopUpManager.showToastError(getApplicationContext(),"Error en la aplicación");
                        finish();
                    }
                }
        );
        // Adding JsonObject request to request queue
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest,REQUEST_TAG);
    }

    private void showResponse(JSONObject response) {
        String text = "";
        try {
            text = response.getString("value");
            TextView tvText = findViewById(R.id.testMsg);
            tvText.setText(text);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v(TAG, "error: " + e.toString());
            PopUpManager.showToastError(getApplicationContext(),"Error en la aplicación");
            finish();
        }
    }
}
