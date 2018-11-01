package com.a7552_2c_2018.melliapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.a7552_2c_2018.melliapp.fragment.ConfirmBuyFragment;
import com.a7552_2c_2018.melliapp.fragment.PayingBuyFragment;
import com.a7552_2c_2018.melliapp.fragment.ShippingBuyFragment;

public class CustomPageAdapter extends FragmentPagerAdapter {

    public CustomPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages.
    @Override
    public int getCount() {
        int NUM_ITEMS = 3;
        return NUM_ITEMS;
    }

    // Returns the fragment to display for a particular page.
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ShippingBuyFragment();
            case 1:
                return new PayingBuyFragment();
            case 2:
                return new ConfirmBuyFragment();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Envio";
            case 1:
                return "Pago";
            case 2:
                return "Confirmación";
            default:
                return null;
        }
    }

}

