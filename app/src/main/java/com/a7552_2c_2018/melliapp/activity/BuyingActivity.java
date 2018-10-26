package com.a7552_2c_2018.melliapp.activity;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.adapters.CustomPageAdapter;

import java.util.Objects;

public class BuyingActivity extends AppCompatActivity {

    private String facebookId, pubDate;

    private FragmentPagerAdapter adapterViewPager;
    private ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        facebookId = getIntent().getStringExtra("facebookId");
        pubDate = getIntent().getStringExtra("pubDate");

        vpPager = findViewById(R.id.vpPager);
        adapterViewPager = new CustomPageAdapter(getSupportFragmentManager());
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


}
