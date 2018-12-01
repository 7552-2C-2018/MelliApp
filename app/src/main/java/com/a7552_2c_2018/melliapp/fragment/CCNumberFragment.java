package com.a7552_2c_2018.melliapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.CardFrontFragment;
import com.a7552_2c_2018.melliapp.activity.CheckOutActivity;
import com.a7552_2c_2018.melliapp.utils.CreditCardEditText;
import com.a7552_2c_2018.melliapp.utils.CreditCardFormattingTextWatcher;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class CCNumberFragment extends Fragment {


    @BindView(R.id.et_number)
    CreditCardEditText et_number;

    private CheckOutActivity activity;
    private CardFrontFragment cardFrontFragment;

    public CCNumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ccnumber, container, false);
        ButterKnife.bind(this, view);

        activity = (CheckOutActivity) getActivity();
        cardFrontFragment = Objects.requireNonNull(activity).cardFrontFragment;

        TextView tv_number = cardFrontFragment.getNumber();

        //Do your stuff
        et_number.addTextChangedListener(new CreditCardFormattingTextWatcher(et_number, tv_number,cardFrontFragment.getCardType(), type -> {
            Log.d("Card", "setCardType: "+type);

            cardFrontFragment.setCardType(type);
        }));

        et_number.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if(activity!=null)
                {
                    activity.nextClick();
                    return true;
                }

            }
            return false;
        });

        et_number.setOnBackButtonListener(() -> {
            if(activity!=null)
                activity.onBackPressed();
        });

        return view;
    }

    public String getCardNumber()
    {
        if(et_number!=null)
            return Objects.requireNonNull(et_number.getText()).toString().trim();

        return null;
    }



}
