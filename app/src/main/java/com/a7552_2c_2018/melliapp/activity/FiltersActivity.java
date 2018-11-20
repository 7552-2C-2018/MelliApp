package com.a7552_2c_2018.melliapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.a7552_2c_2018.melliapp.R;

import java.util.Objects;

public class FiltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.st_filters));
    }
}
