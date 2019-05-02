package com.example.quizzer.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizzer.CommonActions;
import com.example.quizzer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/*
This activity will be called when user login to the application
When login, it will also pull
1) user type
2) module list
3) topic list
4) user list
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static String username;


    private static final String TAG = "LoginActivity";

    Button loginSignupButton, loginButton, resetPassword;
    EditText usernameText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.loginUserName);
        passwordText = findViewById(R.id.loginPass);

        loginSignupButton = findViewById(R.id.btn_signUp_member);
        loginSignupButton.setOnClickListener(this);

        loginButton = findViewById(R.id.btn_logIn);
        loginButton.setOnClickListener(this);

        resetPassword = findViewById(R.id.button_forgetPassword);
        resetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_signUp_member) {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.button_forgetPassword){
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        } else {
            loginUser(v);
        }

    }

    public void loginUser(View v) {
        if (usernameText.getText().toString().equals("") || passwordText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Blank is not allowed", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(usernameText.getText().toString(), passwordText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "SignInWithEmail: success");

                                // Trim the email domain from the user input
                                int index = usernameText.getText().toString().indexOf('@');
                                username = usernameText.getText().toString().substring(0, index);

                                CommonActions.getModuleList();
                                CommonActions.getTopicList();
                                CommonActions.getUserList();
                                CommonActions.getUserRole(username);

                                /*
                                * Added a delay because
                                *       1) Asynchronous nature of the Firesbase
                                *       2) Need to determine the UserType before routing to next Activity
                                * Recommended delay is between 3-5 seconds
                                 */
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.w(TAG, "User type => " + CommonActions.userType );
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 5000); // Delay 3 seconds
                            } else {
                                Log.w(TAG, "signInWithEmail: Failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
