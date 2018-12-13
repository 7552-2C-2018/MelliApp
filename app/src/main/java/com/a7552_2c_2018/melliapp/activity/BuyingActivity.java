package com.a7552_2c_2018.melliapp.activity;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.adapters.CustomPageAdapter;
import com.a7552_2c_2018.melliapp.model.ActualBuy;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyingActivity extends AppCompatActivity {

    @BindView(R.id.vpPager)
    ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Comprando");

        ActualBuy buy = new ActualBuy();
        buy.setId(getIntent().getStringExtra("ID"));
        buy.setTitle(getIntent().getStringExtra("title"));
        buy.setPrice(getIntent().getIntExtra("price", 0));
        SingletonUser.getInstance().setActualBuy(buy);
        FragmentPagerAdapter adapterViewPager = new CustomPageAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

    public void selectTab(int position) {
        vpPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        if (vpPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            vpPager.setCurrentItem(vpPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
