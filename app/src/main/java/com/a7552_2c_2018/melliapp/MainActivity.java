package com.a7552_2c_2018.melliapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;


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
        /* For manual logout
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        */

        if (AccessToken.getCurrentAccessToken() == null) {
            Intent loginIntent = new Intent(MainActivity.this, FacebookLoginActivity.class);
            startActivityForResult(loginIntent, RESULT_LOGIN_ACTIVITY);
        } 
        makeValidations();
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
        String url = R.string.test_uri + "";

        JSONObject data = new JSONObject();
        UserInfo user = SingletonUser.getInstance().getUser();
        try {
            data.put("facebookId", user.getFacebookID());
            data.put("firstName", user.getName());
            data.put("lastName", user.getSurname());
            data.put("photoUrl", user.getPhotoURL());
            data.put("email", user.getEmail());
            data.put("token", AccessToken.getCurrentAccessToken().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
            PopUpManager.showToastError(getApplicationContext(), "Agregar error");
        }

        final String requestBody = data.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // tomar token de acceso
                        getServerLoginResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PopUpManager.showToastError(getApplicationContext(), "Agregar error");
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    System.out.println(uee.toString());
                    return null;
                }
            }
        };
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(stringRequest,REQUEST_TAG);
    }

    private void getServerLoginResponse(String response) {
        //
    }


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
