package com.a7552_2c_2018.melliapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.model.UserInfo;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("UnusedAssignment")
public class MainActivity extends AppCompatActivity  implements LocationListener {

    private static final String TAG = "MainActivity";
    private static final int RESULT_LOGIN_ACTIVITY = 1;
    private double latitude = 0, longitude = 0;
    private LocationManager locationManager;

    @BindView(R.id.profilePicture)
    ProfilePictureView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");

        ButterKnife.bind(this);

        // If MainActivity is reached without the user being logged in, redirect to the Login
        // Activity
        // For manual logout
        /*
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        */

        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");

        if (AccessToken.getCurrentAccessToken() == null) {
            Intent loginIntent = new Intent(MainActivity.this, FacebookLoginActivity.class);
            startActivityForResult(loginIntent, RESULT_LOGIN_ACTIVITY);
        } else {
            loadData();
            //getLocation();
        }
    }

    private void loadData() {
        TextView tvMsg = findViewById(R.id.testMsg);
        tvMsg.setText(R.string.validating);
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                (object, response) -> {
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
                        profilePicture.setProfileId(id);
                        TextView tvTitle = findViewById(R.id.tvHelloName);
                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.welcome_msg), name);
                        tvTitle.setText(text);
                        checkServer();
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                this::getServerLoginResponse,
                error -> {
                    Log.d(TAG, "volley error check" + error.getMessage());
                    //OR
                    Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                    //OR
                    Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                    //Or if nothing works than splitting is the only option
                    //Log.d(TAG, "volley msg4 " +new String(error.networkResponse.data).split(":")[1]);

                    //PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                    createUser();
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
        String token = "";
        try {
            token = response.getString("token");
            SingletonUser.getInstance().setToken(token);
            Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(loginIntent);
        } catch (JSONException e) {
            e.printStackTrace();
            PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
        }
    }

    private void createUser() {
        String REQUEST_TAG = "createUser";
        String url = getString(R.string.remote_register);
        final UserInfo user = SingletonUser.getInstance().getUser();
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                url,
                response -> {
                    Log.d(TAG, "Success");
                    getServerRegisterResponse(response);
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
        String token = "";
        JSONObject aux = null;
        try {
            aux = new JSONObject(response);
            token = aux.getString("token");
            SingletonUser.getInstance().setToken(token);
            Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(loginIntent);
        } catch (JSONException e) {
            e.printStackTrace();
            PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_LOGIN_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    loadData();
                    getLocation();
                    checkServer();
                }
                break;
            default:
                super.onActivityResult(requestCode,resultCode, data);
        }
    }

    private void getLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            /*
            Location location = Objects.requireNonNull(lm).getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            */
            Criteria criteria = new Criteria();
            String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

            //You can still do this if you like, you might get lucky:
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            else{
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
            Location location = Objects.requireNonNull(locationManager).getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        UserInfo user = SingletonUser.getInstance().getUser();
        user.setLatitude(latitude);
        user.setLongitude(longitude);
        SingletonUser.getInstance().setUser(user);
    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
