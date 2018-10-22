package com.a7552_2c_2018.melliapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a7552_2c_2018.melliapp.R;


public class ConfirmBuyFragment extends Fragment {

    private static final String TAG = "PayingBuyFragment";

    public ConfirmBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_shipping_buys, container, false);

        /*
        etName = v.findViewById(R.id.faEtName);
        etName.setText(user.getName());

        etSurname = v.findViewById(R.id.faEtSurname);
        etSurname.setText(user.getSurname());

        EditText etEmail = v.findViewById(R.id.faEtEmail);
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
        */
        // Inflate the layout for this fragment
        return v;
    }

}