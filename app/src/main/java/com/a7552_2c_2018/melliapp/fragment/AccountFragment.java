package com.a7552_2c_2018.melliapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.MainActivity;
import com.a7552_2c_2018.melliapp.model.UserInfo;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AccountFragment extends Fragment {


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

        UserInfo user = SingletonUser.getInstance().getUser();

        View v = inflater.inflate(R.layout.fragment_account, container, false);

        EditText etName = v.findViewById(R.id.faEtName);
        etName.setText(user.getName());

        EditText etSurname = v.findViewById(R.id.faEtSurname);
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

        // Inflate the layout for this fragment
        return v;
    }


    private void logOut() {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Cerrar Sesión")
                .setMessage("Desea cerrar su sesión ?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginManager.getInstance().logOut();
                        getActivity().finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void saveChanges(){
        // TODO: AGREGAR PUT AL BACKEND

        PopUpManager.showToastError(getApplicationContext(), getString(R.string.msg_save_ok));
    }

}
