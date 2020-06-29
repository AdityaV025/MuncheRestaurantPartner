package com.example.muncherestaurantpartner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Fragments.MenuFragment;
import Fragments.MyProfileFragment;
import Fragments.OrdersFragment;
import Fragments.SalesFragment;
import UI.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mCurrentUser = mAuth.getCurrentUser();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_orders:
                        selectedFragment = new OrdersFragment();
                        break;
                    case R.id.nav_menu:
                        selectedFragment = new MenuFragment();
                        break;
                    case R.id.nav_sales:
                        selectedFragment = new SalesFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new MyProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                        selectedFragment).commit();
                return true;
            };

    private void sendUserToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCurrentUser == null) {
            sendUserToLogin();
        }else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new OrdersFragment())
                    .commit();
        }
    }

}