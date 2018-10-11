package com.a7552_2c_2018.melliapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.a7552_2c_2018.melliapp.fragment.PostsFragment;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;

import java.util.Objects;

@SuppressWarnings("SpellCheckingInspection")
public class HomeActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dl = findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Fragment fragment = new PostsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();

        NavigationView nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;
                switch(id)
                {
                    case R.id.home:
                        //PopUpManager.showToastError(getApplicationContext(), "Home");
                        fragment = new PostsFragment();
                        break;
                    case R.id.buys:
                        PopUpManager.showToastError(getApplicationContext(), "Buys");
                        break;
                    case R.id.posts:
                        PopUpManager.showToastError(getApplicationContext(), "Posts");
                        break;
                    case R.id.solds:
                        PopUpManager.showToastError(getApplicationContext(), "Solds");
                        break;
                    case R.id.chats:
                        PopUpManager.showToastError(getApplicationContext(), "Chats");
                        break;
                    case R.id.qr:
                        PopUpManager.showToastError(getApplicationContext(), "QR");
                        break;
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Salir")
                .setMessage("Desea cerrar la aplicación ?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}
