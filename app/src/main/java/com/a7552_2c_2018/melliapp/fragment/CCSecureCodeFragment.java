package com.a7552_2c_2018.melliapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;


import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.CheckOutActivity;
import com.a7552_2c_2018.melliapp.model.ActualBuy;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.CreditCardEditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CCSecureCodeFragment extends Fragment {


    @BindView(R.id.et_cvv)
    CreditCardEditText et_cvv;

    private TextView tv_cvv;

    private CheckOutActivity activity;

    public CCSecureCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ccsecure_code, container, false);
        ButterKnife.bind(this, view);

        activity = (CheckOutActivity) getActivity();

        et_cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (tv_cvv != null) {
                    if (TextUtils.isEmpty(editable.toString().trim()))
                        tv_cvv.setText(getString(R.string.card_cvv_sample));
                    else
                        tv_cvv.setText(editable.toString());

                } else
                    Log.d(TAG, "afterTextChanged: cvv null");

            }
        });

        et_cvv.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if(activity!=null)
                {
                    activity.nextClick();
                    return true;
                }

            }
            return false;
        });

        et_cvv.setOnBackButtonListener(() -> {
            if(activity!=null)
                activity.onBackPressed();
        });

        return view;

    }

    public void setCvv(TextView tv) {
        tv_cvv = tv;
    }

    public String getValue() {

        String getValue = "";

        if (et_cvv != null) {
            getValue = Objects.requireNonNull(et_cvv.getText()).toString().trim();
        }
        return getValue;
    }

}
