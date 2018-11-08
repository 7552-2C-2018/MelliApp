package com.a7552_2c_2018.melliapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.a7552_2c_2018.melliapp.R;

import java.util.Objects;

public class QuestionsActivity extends AppCompatActivity {

    private static final String TAG = "QuestionsActivity";

    private String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Preguntas");

        Id = getIntent().getStringExtra("ID");
    }
}
