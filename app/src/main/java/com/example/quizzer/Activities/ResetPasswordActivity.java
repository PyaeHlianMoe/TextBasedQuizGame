package com.example.quizzer.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizzer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/*
This activity will be called when user want to reset the password.
This will send an email to user with a link to reset password
 */

public class ResetPasswordActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    EditText userEmail;
    Button userPass;
    Button backButton;
    String email;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userEmail = findViewById(R.id.et_pw_email);
        userPass = findViewById(R.id.btn_reset_pw);
        backButton = findViewById(R.id.BackButton);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String proceed = userEmail.getText().toString().trim();
                if(TextUtils.isEmpty(proceed)){
                    Toast.makeText(ResetPasswordActivity.this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Sending reset link to your email...");
                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "The reset link has sent to your email.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, task.getException().getMessage() + ", please key in again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
