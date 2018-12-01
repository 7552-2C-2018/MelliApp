package com.a7552_2c_2018.melliapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.CardFrontFragment;
import com.a7552_2c_2018.melliapp.activity.CheckOutActivity;
import com.a7552_2c_2018.melliapp.utils.CreditCardEditText;
import com.a7552_2c_2018.melliapp.utils.CreditCardExpiryTextWatcher;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CCValidityFragment extends Fragment {


    @BindView(R.id.et_validity)
    CreditCardEditText et_validity;

    private CheckOutActivity activity;

    public CCValidityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ccvalidity, container, false);
        ButterKnife.bind(this, view);

        activity = (CheckOutActivity) getActivity();
        CardFrontFragment cardFrontFragment = Objects.requireNonNull(activity).cardFrontFragment;


        TextView tv_validity = cardFrontFragment.getValidity();
        et_validity.addTextChangedListener(new CreditCardExpiryTextWatcher(et_validity, tv_validity));

        et_validity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (activity != null) {
                    activity.nextClick();
                    return true;
                }

            }
            return false;
        });

        et_validity.setOnBackButtonListener(() -> {
            if(activity!=null)
                activity.onBackPressed();
        });


        return view;
    }

    public String getValidity()
    {
        if(et_validity!=null)
            return Objects.requireNonNull(et_validity.getText()).toString().trim();

        return null;
    }

}
