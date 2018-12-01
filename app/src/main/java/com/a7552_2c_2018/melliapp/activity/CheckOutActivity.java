package com.a7552_2c_2018.melliapp.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.fragment.CCNameFragment;
import com.a7552_2c_2018.melliapp.fragment.CCNumberFragment;
import com.a7552_2c_2018.melliapp.fragment.CCSecureCodeFragment;
import com.a7552_2c_2018.melliapp.fragment.CCValidityFragment;
import com.a7552_2c_2018.melliapp.utils.CreditCardUtils;
import com.a7552_2c_2018.melliapp.utils.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckOutActivity extends FragmentActivity implements FragmentManager.OnBackStackChangedListener {

    @BindView(R.id.btnNext)
    Button btnNext;

    public CardFrontFragment cardFrontFragment;
    private CardBackFragment cardBackFragment;

    //This is our viewPager
    private ViewPager viewPager;

    private CCNumberFragment numberFragment;
    private CCNameFragment nameFragment;
    private CCValidityFragment validityFragment;
    CCSecureCodeFragment secureCodeFragment;

    private int total_item;
    private boolean backTrack = false;

    private boolean mShowingBack = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        ButterKnife.bind(this);


        cardFrontFragment = new CardFrontFragment();
        cardBackFragment = new CardBackFragment();

        if (savedInstanceState == null) {
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, cardFrontFragment).commit();

        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }

        getFragmentManager().addOnBackStackChangedListener(this);

        //Initializing viewPager
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == total_item)
                    btnNext.setText(getString(R.string.coa_submit));
                else
                    btnNext.setText(getString(R.string.coa_next));

                Log.d("track", "onPageSelected: " + position);

                if (position == total_item) {
                    flipCard();
                    backTrack = true;
                } else if (position == total_item - 1 && backTrack) {
                    flipCard();
                    backTrack = false;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(view -> {
            int pos = viewPager.getCurrentItem();
            if (pos < total_item) {
                viewPager.setCurrentItem(pos + 1);
            } else {
                checkEntries();
            }

        });


    }

    private void checkEntries() {
        String cardName = nameFragment.getName();
        String cardNumber = numberFragment.getCardNumber();
        String cardValidity = validityFragment.getValidity();
        String cardCVV = secureCodeFragment.getValue();

        if (TextUtils.isEmpty(cardName)) {
            Toast.makeText(CheckOutActivity.this, "Ingrese un nombre valido", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardNumber) || !CreditCardUtils.isValid(cardNumber.replace(" ",""))) {
            Toast.makeText(CheckOutActivity.this, "Ingrese un número valido", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardValidity)||!CreditCardUtils.isValidDate(cardValidity)) {
            Toast.makeText(CheckOutActivity.this, "Verifique la fecha", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardCVV)|| cardCVV.length()<3) {
            Toast.makeText(CheckOutActivity.this, "Ingrese un código de seguridad válido", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(CheckOutActivity.this, "Datos ingresados correctos", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();

    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        numberFragment = new CCNumberFragment();
        nameFragment = new CCNameFragment();
        validityFragment = new CCValidityFragment();
        secureCodeFragment = new CCSecureCodeFragment();
        adapter.addFragment(numberFragment);
        adapter.addFragment(nameFragment);
        adapter.addFragment(validityFragment);
        adapter.addFragment(secureCodeFragment);

        total_item = adapter.getCount() - 1;
        viewPager.setAdapter(adapter);

    }

    private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }
        // Flip to the back.
        //setCustomAnimations(int enter, int exit, int popEnter, int popExit)

        mShowingBack = true;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.fragment_container, cardBackFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        int pos = viewPager.getCurrentItem();
        if (pos > 0) {
            viewPager.setCurrentItem(pos - 1);
        } else
            super.onBackPressed();
    }

    public void nextClick() {
        btnNext.performClick();
    }
}
