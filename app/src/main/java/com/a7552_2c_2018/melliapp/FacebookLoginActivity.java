package com.a7552_2c_2018.melliapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookLoginActivity extends AppCompatActivity {

    private static final String TAG = "FbLoginActivity";
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String USER_BIRTHDAY = "user_birthday";
    private static final String AUTH_TYPE = "rerequest";


    private CallbackManager mCallbackManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        mCallbackManager = CallbackManager.Factory.create();

        LoginButton mLoginButton = findViewById(R.id.login_button);

        // Set the initial permissions to request from the user while logging in
        mLoginButton.setReadPermissions(Arrays.asList(PUBLIC_PROFILE, EMAIL, USER_BIRTHDAY, USER_POSTS));

        mLoginButton.setAuthType(AUTH_TYPE);

        // Register a callback to respond to the user
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);
                UserInfo user;
                user = new UserInfo("123", "carlos", "furnari", "asd");
                //user.setPhotoURL(profilePicUrl);
                SingletonUser.getInstance().setUser(user);
                /*
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,picture.width(100).height(100)");
                request.setParameters(parameters);
                request.executeAsync();
                */
                finish();
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onError(FacebookException e) {
                // Handle exception
            }
        });
    }

}
