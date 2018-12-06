package com.a7552_2c_2018.melliapp.activity;

import android.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.fragment.AccountFragment;
import com.a7552_2c_2018.melliapp.fragment.BuysFragment;
import com.a7552_2c_2018.melliapp.fragment.ChatsFragment;
import com.a7552_2c_2018.melliapp.fragment.MyPostsFragment;
import com.a7552_2c_2018.melliapp.fragment.PostsFragment;
import com.a7552_2c_2018.melliapp.fragment.QrFragment;
import com.a7552_2c_2018.melliapp.fragment.RatingsFragment;
import com.a7552_2c_2018.melliapp.fragment.SoldsFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("SpellCheckingInspection")
public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout dl;

    private ActionBarDrawerToggle t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Fragment fragment = new PostsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();

        NavigationView nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment1 = null;
            switch(id)
            {
                case R.id.home:
                    fragment1 = new PostsFragment();
                    break;
                case R.id.buys:
                    fragment1 = new BuysFragment();
                    break;
                case R.id.posts:
                    fragment1 = new MyPostsFragment();
                    break;
                case R.id.solds:
                    fragment1 = new SoldsFragment();
                    break;
                case R.id.ratings:
                    fragment1 = new RatingsFragment();
                    break;
                case R.id.chats:
                    fragment1 = new ChatsFragment();
                    break;
                case R.id.qr:
                    fragment1 = new QrFragment();
                    break;
                case R.id.profile:
                    fragment1 = new AccountFragment();
                default: break;
            }

            if (fragment1 != null) {
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.frame, fragment1);
                transaction1.commit();
                dl.closeDrawers();
                return true;
            }
            return false;
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.exit_title))
                .setMessage(getString(R.string.exit_qst))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> finishAffinity())
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

}
