package com.a7552_2c_2018.melliapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.CardFrontFragment;
import com.a7552_2c_2018.melliapp.activity.CheckOutActivity;
import com.a7552_2c_2018.melliapp.utils.CreditCardEditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CCNameFragment extends Fragment {


    @BindView(R.id.et_name)
    CreditCardEditText et_name;

    private TextView tv_Name;

    private CheckOutActivity activity;

    public CCNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ccname, container, false);
        ButterKnife.bind(this, view);

        activity = (CheckOutActivity) getActivity();
        CardFrontFragment cardFrontFragment = Objects.requireNonNull(activity).cardFrontFragment;

        tv_Name = cardFrontFragment.getName();

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(tv_Name!=null)
                {
                    if (TextUtils.isEmpty(editable.toString().trim()))
                        tv_Name.setText(getString(R.string.card_holders_name));
                    else
                        tv_Name.setText(editable.toString());

                }

            }
        });

        et_name.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if(activity!=null)
                {
                    activity.nextClick();
                    return true;
                }

            }
            return false;
        });


        et_name.setOnBackButtonListener(() -> {
            if(activity!=null)
                activity.onBackPressed();
        });

        return view;
}

    public String getName()
    {
        if(et_name!=null)
            return Objects.requireNonNull(et_name.getText()).toString().trim();

        return null;
    }


}
