package com.a7552_2c_2018.melliapp.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.utils.FontTypeChange;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.a7552_2c_2018.melliapp.utils.CreditCardUtils.AMEX;
import static com.a7552_2c_2018.melliapp.utils.CreditCardUtils.DISCOVER;
import static com.a7552_2c_2018.melliapp.utils.CreditCardUtils.MASTERCARD;
import static com.a7552_2c_2018.melliapp.utils.CreditCardUtils.NONE;
import static com.a7552_2c_2018.melliapp.utils.CreditCardUtils.VISA;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardFrontFragment extends Fragment {


    @BindView(R.id.tv_card_number)
    TextView tvNumber;

    @BindView(R.id.tv_member_name)
    TextView tvName;

    @BindView(R.id.tv_validity)
    TextView tvValidity;

    @BindView(R.id.ivType)
    ImageView ivType;

    public CardFrontFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_card_front, container, false);
        ButterKnife.bind(this, view);
        FontTypeChange fontTypeChange = new FontTypeChange(getActivity());
        tvNumber.setTypeface(fontTypeChange.get_fontface(3));
        tvName.setTypeface(fontTypeChange.get_fontface(3));

        return view;
    }

    public TextView getNumber()
    {
        return tvNumber;
    }
    public TextView getName()
    {
        return tvName;
    }
    public TextView getValidity()
    {
        return tvValidity;
    }

    public ImageView getCardType()
    {
        return ivType;
    }


    public void setCardType(int type)
    {
        switch(type)
        {
            case VISA:
                ivType.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_visa));
                break;
            case MASTERCARD:
                ivType.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_mastercard));
                break;
            case AMEX:
                ivType.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_amex));
                break;
            case DISCOVER:
                ivType.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_discover));
                break;
            case NONE:
                ivType.setImageResource(android.R.color.transparent);
            break;

        }


    }


}
