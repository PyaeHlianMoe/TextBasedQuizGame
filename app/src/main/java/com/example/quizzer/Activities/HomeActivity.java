package com.example.quizzer.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.design.widget.NavigationView;
import android.widget.Toast;

import com.example.quizzer.Fragments.LecturerFragment;
import com.example.quizzer.Fragments.ScoreBoardFragment;
import com.example.quizzer.Fragments.StudentFragment;
import com.example.quizzer.CommonActions;
import com.example.quizzer.R;
import com.google.firebase.auth.FirebaseAuth;

/*
This is the main activity of the application.
The layout/ view will be changed based on the login user type.
 */

public class HomeActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener{
    private static final String TAG = "HomeActivity";
    public static FragmentManager fragmentManager;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private Menu drawerMenu;
    private NavigationView navigation_view;

    ActionBarDrawerToggle actionBarDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigation_view = (NavigationView) findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (findViewById(R.id.FragmentContainer) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // Replace the fragment according to the login user type
            if (CommonActions.userType.equals("Lecturer")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new LecturerFragment()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new StudentFragment()).commit();
            }

        }

        drawerMenu = navigation_view.getMenu();
         for(int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_menu_drawer, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                drawerLayout.closeDrawers();
                if (CommonActions.userType.equals("Lecturer")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new LecturerFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new StudentFragment()).addToBackStack(null).commit();
                }
                break;
            case R.id.nav_scoreboard:
                drawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new ScoreBoardFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_logout:
                // Empty the actionType when logout so that it won't remain as static data
                LecturerFragment.actionType = "";

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return false;

    }



}
