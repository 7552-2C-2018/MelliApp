package com.a7552_2c_2018.melliapp.activity;

import android.support.annotation.NonNull;
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
import com.a7552_2c_2018.melliapp.utils.PopUpManager;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dl = findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;
                switch(id)
                {
                    case R.id.home:
                        PopUpManager.showToastError(getApplicationContext(), "H");
                    case R.id.buys:
                        PopUpManager.showToastError(getApplicationContext(), "B");
                    case R.id.posts:
                        PopUpManager.showToastError(getApplicationContext(), "P");
                    case R.id.solds:
                        PopUpManager.showToastError(getApplicationContext(), "S");
                    case R.id.chats:
                        PopUpManager.showToastError(getApplicationContext(), "C");
                    case R.id.profile:
                        fragment = new AccountFragment();
                    default: break;
                }

                if (fragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, fragment);
                    transaction.commit();
                    dl.closeDrawers();
                    return true;
                }
                return false;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

}
