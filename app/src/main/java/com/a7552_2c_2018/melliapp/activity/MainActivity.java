package com.a7552_2c_2018.melliapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.model.UserInfo;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnusedAssignment")
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int RESULT_LOGIN_ACTIVITY = 1;
    private ProfilePictureView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If MainActivity is reached without the user being logged in, redirect to the Login
        // Activity
        // For manual logout
        /*
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        */

        if (AccessToken.getCurrentAccessToken() == null) {
            Intent loginIntent = new Intent(MainActivity.this, FacebookLoginActivity.class);
            startActivityForResult(loginIntent, RESULT_LOGIN_ACTIVITY);
        } else {
            loadData();
        }
    }

    private void loadData() {
        TextView tvMsg = findViewById(R.id.testMsg);
        tvMsg.setText(R.string.validating);
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @SuppressWarnings("SpellCheckingInspection")
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(TAG, "response: " + response.toString());

                        String id, name, surname, email;
                        String profilePicUrl;
                        try {
                            id = object.getString("id");
                            name = object.getString("first_name");
                            surname = object.getString("last_name");
                            email = object.getString("email");
                            profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            UserInfo user;
                            user = new UserInfo(id, name, surname, email);
                            user.setPhotoURL(profilePicUrl);
                            SingletonUser.getInstance().setUser(user);
                            profilePicture = findViewById(R.id.profilePicture);
                            profilePicture.setProfileId(id);
                            TextView tvTitle = findViewById(R.id.tvHelloName);
                            tvTitle.setText("¡Bienvenido " + name + "!");
                            checkServer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,picture.width(100).height(100)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkServer() {
        String REQUEST_TAG = "checkUser";
        String url = getString(R.string.remote_login);
        Log.d(TAG, "token " + AccessToken.getCurrentAccessToken().getToken());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getServerLoginResponse(response);
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
                params.put("token", AccessToken.getCurrentAccessToken().getToken());

                return params;
            }
        };
        Log.d(TAG, "login request " + jsonObjectRequest.toString());
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest,REQUEST_TAG);
    }

    private void getServerLoginResponse(JSONObject response) {
        Log.d(TAG, response.toString());
        Integer status = 0;
        String token = "";
        JSONObject data;
        try {
            status = response.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status == 200){
            try {
                data = response.getJSONObject("data");
                token = data.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SingletonUser.getInstance().setToken(token);
            Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(loginIntent);

        } else {
            //PopUpManager.showToastError(getApplicationContext(), getString(R.string.validation_error));
            createUser();
        }
    }

    private void createUser() {
        String REQUEST_TAG = "createUser";
        String url = getString(R.string.remote_register);
        final UserInfo user = SingletonUser.getInstance().getUser();
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Success");
                        getServerRegisterResponse(response);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstName", user.getName());
                params.put("lastName", user.getSurname());
                params.put("photoUrl", user.getPhotoURL());
                params.put("email", user.getEmail());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", AccessToken.getCurrentAccessToken().getToken());

                return params;
            }

        };

        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObjRequest,REQUEST_TAG);
    }

    private void getServerRegisterResponse(String response) {
        Log.d(TAG, "getServerRegisterResponse: " + response);
        Integer status = 0;
        String token;
        JSONObject data = null, data2 = null;
        try {
            data = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            status = data.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status == 200){
            try {
                data2 = data.getJSONObject("data");
                token = data2.getString("token");
                SingletonUser.getInstance().setToken(token);
                Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(loginIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUpManager.showToastError(getApplicationContext(), getString(R.string.validation_error));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_LOGIN_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    loadData();
                    checkServer();
                }
                break;
            default:
                super.onActivityResult(requestCode,resultCode, data);
        }
    }

}
