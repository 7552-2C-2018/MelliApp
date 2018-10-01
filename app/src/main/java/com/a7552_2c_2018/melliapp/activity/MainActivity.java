package com.a7552_2c_2018.melliapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.model.UserInfo;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


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
            makeValidations();
        }
    }

    private void makeValidations() {
        TextView tvMsg = findViewById(R.id.testMsg);
        tvMsg.setText(R.string.validating);
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(TAG, "response: " + response.toString());

                        String id, name, surname, email = null;
                        String profilePicUrl = null;
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
                            loginServer();
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

    private void loginServer() {
        String REQUEST_TAG = "sendUser";
        //String url = R.string.local_server + "/login";
        //String url = "http://127.0.0.1:5000/login";
        String url = getString(R.string.remote_login);
        JSONObject data = new JSONObject();
        UserInfo user = SingletonUser.getInstance().getUser();
        try {
            data.put("facebookId", user.getFacebookID());
            data.put("firstName", user.getName());
            data.put("lastName", user.getSurname());
            data.put("photoUrl", user.getPhotoURL());
            data.put("email", user.getEmail());
            data.put("token", AccessToken.getCurrentAccessToken().getToken());
            Log.d(TAG, getString(R.string.send_server) + data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, getString(R.string.json_error) + e.toString());
            PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getServerLoginResponse(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d(TAG, "error: " + error.toString());
                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                    }
                }
        );
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest,REQUEST_TAG);
    }

    private void getServerLoginResponse(JSONObject response) {
        Log.d(TAG, response.toString());
        Integer status = 0;
        String token = "";
        try {
            status = response.getInt("status");
            token = response.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //if (status == 200){
            SingletonUser.getInstance().setToken(token);
            Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(loginIntent);

        //} else {
        //    PopUpManager.showToastError(getApplicationContext(), getString(R.string.validation_error));
        //}
    }


    @SuppressWarnings("SpellCheckingInspection")
    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_LOGIN_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    makeValidations();
                }
                break;
            default:
                super.onActivityResult(requestCode,resultCode, data);
        }
    }

}
