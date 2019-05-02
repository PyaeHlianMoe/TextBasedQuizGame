package com.example.quizzer;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.quizzer.Activities.HomeActivity;
import com.example.quizzer.Activities.LoginActivity;
import com.example.quizzer.Activities.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/*
This is the main activity that will have the splash UI feature
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout mDotLayout;
    private ProgressBar progressBar;
    private Button loginPageButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    static boolean isUserLogged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFirebase();

        if(mAuth.getCurrentUser() != null){
            setContentView(R.layout.splash_screen);
            RelativeLayout movingBackground = findViewById(R.id.moving_background);
            AnimationDrawable animationDrawable = (AnimationDrawable) movingBackground.getBackground();
            animationDrawable.setEnterFadeDuration(1500);
            animationDrawable.setExitFadeDuration(1500);
            animationDrawable.start();

            startWithSplashLogin(mAuth.getCurrentUser().getUid());
        } else {
            startWithoutSplashLogin();
        }
    }

    private void initFirebase() {

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    //show splash screen for user alr registered and login
    private void startWithSplashLogin(String userId){
        CollectionReference userReference = db.collection("users");
        Query query = userReference.whereEqualTo("id",userId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot queryResult = task.getResult();
                    if(!queryResult.isEmpty()){
                        isUserLogged = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        },4000);
                    } else {
                        isUserLogged = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                                startActivity(intent);
                            }
                        }, 2000);
                    }
                } else {
                    isUserLogged = false;
                    startWithoutSplashLogin();
                }

            }
        });
    }

    //screen for re-login & new user
    private void startWithoutSplashLogin(){
        setContentView(R.layout.activity_main);
        initView();
        loginPageButton.setOnClickListener(this);
    }

    private void initView(){
        //Setting LOGIN button
        loginPageButton = findViewById(R.id.btn_start);

        //Setting progressBar and making it invisible
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        //Setting view pager
        ViewPager mSlideViewPager = findViewById(R.id.view_pager);
        mDotLayout = findViewById(R.id.dots_layout);
        SliderAdapter sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                progressBar.setVisibility(View.VISIBLE);
                login();
                break;
        }
    }

    public void login(){
        progressBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void addDotsIndicator(int position) {

        TextView[] mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i=0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            if(Build.VERSION.SDK_INT > 24)
            {
                mDots[i].setText(Html.fromHtml("&#8226",Html.FROM_HTML_MODE_COMPACT));
            } else {
                mDots[i].setText(Html.fromHtml("&#8226"));
            }
            mDots[i].setTextSize(35);
            mDots[i].setPadding(4,1,4,1);
            mDots[i].setTextColor(ContextCompat.getColor(this,R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);
        }

        mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
