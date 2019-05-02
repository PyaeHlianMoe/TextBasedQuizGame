package com.example.quizzer.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizzer.Firestore;
import com.example.quizzer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/*
The activity will be called when user perform a signup.
It will help to add the information into the Firebase database.
 */

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "SignupActivity";

    Button btn_already_member;
    Button btn_signup;
    EditText et_username;
    EditText et_email;
    EditText et_password;
    EditText et_rePassword;

    FirebaseFirestore db;
    Map<String, Object> user;
    String personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btn_already_member = findViewById(R.id.btn_Already_Member);
        btn_signup = findViewById(R.id.btn_SignUp);
        et_username = findViewById(R.id.regUserName);
        et_email = findViewById(R.id.regEmail);
        et_password = findViewById(R.id.regPassword);
        et_rePassword = findViewById(R.id.regRePassword);

        btn_already_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterUser();
            }
        });
    }

    //make sure all edittext are correctly filled
    private boolean validate(){
        boolean valid = true;

        String name = et_username.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String reEnterPassword = et_rePassword.getText().toString();

        if(name.isEmpty()){
            et_username.setError("Username cannot be empty");
            valid = false;
        } else {
            et_username.setError(null);
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Please enter a valid email");
            valid = false;
        } else {
            et_email.setError(null);
        }

        if(password.isEmpty() || password.length() < 6){
            et_password.setError("Password must contain minimum 6 alphanumeric characters");
            valid = false;
        } else {
            et_password.setError(null);
        }

        if(reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || !(reEnterPassword.equals(password))){
            et_rePassword.setError("Password does not match");
            valid = false;
        } else {
            et_rePassword.setError(null);
        }

        return valid;
    }

    private void onRegisterUser(){
        String name = et_username.getText().toString();
        if(!validate()){

        } else {
            signUp(getUserEmail(),getUserPassword());
        }
    }

    private String getUserDisplayName(){ return et_username.getText().toString().trim();}
    private String getUserEmail(){ return et_email.getText().toString().trim();}
    private String getUserPassword(){ return et_password.getText().toString().trim();}

    private void signUp(String email, String password){
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            personId = mAuth.getCurrentUser().getUid();
                            Firestore firestore = new Firestore();
                            firestore.addNewUser(getUserDisplayName(), getUserEmail());
                            /*
                            Here, we will redirect to LoginActivity.class instead of HomeActivity.class.
                            If we keep using HomeActivity.class, it won't be able to login all the time because
                            the Data has not been added to Firestore due to async nature of Firebase API in Java.
                             */
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    private void onAuthSuccess(FirebaseUser user){
//        UserModel user = buildNewUser();
//
//
//    }

//    private UserModel buildNewUser(){
//        return new UserModel(getUserDisplayName(),getUserEmail());
//    }

}
