package com.a7552_2c_2018.melliapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.PostsActivity;
import com.a7552_2c_2018.melliapp.adapters.ItemAdapter;
import com.a7552_2c_2018.melliapp.model.PostItem;
import com.a7552_2c_2018.melliapp.model.UserInfo;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class PostsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        UserInfo user = SingletonUser.getInstance().getUser();

        View v = inflater.inflate(R.layout.fragment_posts, container, false);

        // Inflate the layout for this fragment

        recyclerView =  v.findViewById(R.id.fpRecycler);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        List<PostItem> input = new ArrayList<>();
        PostItem item;
        for (int i=0; i<6; i++) {
            item = new PostItem();
            item.setImage(getString(R.string.base64mock));
            item.setPrice(800);
            item.setDesc("Remera Selección Argentina Original Local a la calle");
            input.add(item);
        }
        mAdapter = new ItemAdapter(input);
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fabNew = v.findViewById(R.id.fpAbNew);
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(getApplicationContext(), PostsActivity.class);
                startActivity(postIntent);
            }
        });
        return v;
    }

}
