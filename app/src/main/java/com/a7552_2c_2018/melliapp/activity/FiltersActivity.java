package com.a7552_2c_2018.melliapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.a7552_2c_2018.melliapp.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FiltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.st_filters));

        RangeSeekBar seekBar = findViewById(R.id.rangeSeekbar);
        seekBar.setRangeValues(0, 99999);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {


            }
        });

        seekBar.setNotifyWhileDragging(true);

        seekBar.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);
    }
}
