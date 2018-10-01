package com.a7552_2c_2018.melliapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";
    EditText etName, etSurname, etEmail;
    UserInfo user;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = SingletonUser.getInstance().getUser();

        View v = inflater.inflate(R.layout.fragment_account, container, false);

        etName = v.findViewById(R.id.faEtName);
        etName.setText(user.getName());

        etSurname = v.findViewById(R.id.faEtSurname);
        etSurname.setText(user.getSurname());

        etEmail = v.findViewById(R.id.faEtEmail);
        etEmail.setText(user.getEmail());

        ProfilePictureView profilePicture;
        profilePicture = v.findViewById(R.id.AccProfilePicture);
        profilePicture.setProfileId(user.getFacebookID());

        Button btLogin = v.findViewById(R.id.logout_button);
        btLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                logOut();
            }
        });

        Button btSave = v.findViewById(R.id.faBtnSave);
        btSave.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                saveChanges();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }


    private void logOut() {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.af_title1))
                .setMessage(getString(R.string.af_title2))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginManager.getInstance().logOut();
                        getActivity().finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Authorization", SingletonUser.getInstance().getToken());
                return headers; }
        };
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest,REQUEST_TAG);
    }



}
