package com.a7552_2c_2018.melliapp.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.MainActivity;
import com.a7552_2c_2018.melliapp.model.UserInfo;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";

    @BindView(R.id.faEtName)
    EditText etName;

    @BindView(R.id.faEtSurname)
    EditText etSurname;

    @BindView(R.id.faEtEmail)
    EditText etEmail;

    @BindView(R.id.AccProfilePicture)
    ProfilePictureView profilePicture;

    @BindView(R.id.logout_button)
    Button btLogin;

    @BindView(R.id.faBtnSave)
    Button btSave;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        UserInfo user = SingletonUser.getInstance().getUser();

        View v = inflater.inflate(R.layout.fragment_account, container, false);

        ButterKnife.bind(this, v);

        ((AppCompatActivity)Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle("Mi Cuenta");

        etName.setText(user.getName());
        etSurname.setText(user.getSurname());
        etEmail.setText(user.getEmail());
        profilePicture.setProfileId(user.getFacebookID());
        btLogin.setOnClickListener(v12 -> logOut());
        btSave.setOnClickListener(v1 -> saveChanges());

        // Inflate the layout for this fragment
        return v;
    }


    private void logOut() {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.af_title1))
                .setMessage(getString(R.string.af_title2))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    LoginManager.getInstance().logOut();
                    Objects.requireNonNull(getActivity()).finishAffinity();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
    /*
    private void saveChanges(){
        String REQUEST_TAG = "updateUser";
        final String url = getString(R.string.remote_login);
        JSONObject data = new JSONObject();
        try {
            data.put("facebookId", user.getFacebookID());
            data.put("firstName", etName.getText());
            data.put("lastName", etSurname.getText());
            data.put("email", user.getEmail());
            Log.d(TAG, getString(R.string.send_server) + data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, getString(R.string.json_error) + e.toString());
            PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.msg_save_ok));
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d(TAG, "error: " + error.toString());
                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> headers = new HashMap<>();
                headers.put("Authorization", SingletonUser.getInstance().getToken());
                return headers; }
        };
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest,REQUEST_TAG);
    }
    */
    private void saveChanges() {
        String REQUEST_TAG = "saveChanges";
        //String url = getString(R.string.remote_register);
        String url = getString(R.string.remote_users);
        final UserInfo user = SingletonUser.getInstance().getUser();
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,
                url,
                response -> {
                    Log.d(TAG, "Success");
                    PopUpManager.showToastError(getApplicationContext(), "Datos Guardados");
                }, error -> {
                    Log.d(TAG, "volley error create " + error.getMessage());
                    //OR
                    Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                    //OR
                    Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                    //Or if nothing works than splitting is the only option
                    //Log.d(TAG, "volley msg4 " + new String(error.networkResponse.data));

                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("firstName", etName.getText().toString());
                params.put("lastName", etSurname.getText().toString());
                params.put("photoUrl", user.getPhotoURL());
                params.put("email", user.getEmail());
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

        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObjRequest,REQUEST_TAG);
        user.setName(etName.getText().toString());
        user.setSurname(etSurname.getText().toString());
        SingletonUser.getInstance().setUser(user);

    }
}
