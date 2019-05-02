package com.example.quizzer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizzer.Activities.HomeActivity;
import com.example.quizzer.Activities.LoginActivity;
import com.example.quizzer.Model.Challenge;
import com.example.quizzer.CommonActions;
import com.example.quizzer.Firestore;
import com.example.quizzer.R;
import com.example.quizzer.mail.SendMail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

/*
This fragment will be called after user choose a difficulty.
This fragment is the final page before creating a challenge/ quiz. A page that will display a summary information
 */

public class CreateChallengeFragment extends Fragment implements
        View.OnClickListener
{
    private static final String TAG = "CreateChallengeFragment";
    private String creatorId, module, topic, receiverId, difficulty;
    private TextView textViewModule, textViewTopic, textViewReceiverId, textViewDifficulty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_challenge, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            module = bundle.getString("MODULE");
            topic = bundle.getString("TOPIC");
            receiverId = bundle.getString("CHALLENGEE");
            difficulty = bundle.getString("DIFFICULTY");
        } else {
            Log.d(TAG, "Bundle is empty");
        }

        textViewModule = view.findViewById(R.id.txtView_Module);
        textViewTopic = view.findViewById(R.id.txtView_Topic);
        textViewReceiverId = view.findViewById(R.id.txtView_Challengee);
        textViewDifficulty = view.findViewById(R.id.txtView_Difficulty);

        textViewModule.setText(module);
        textViewTopic.setText(topic);
        textViewReceiverId.setText(receiverId);
        textViewDifficulty.setText(difficulty);

        view.findViewById(R.id.btn_StartNow).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_StartNow:
                // Get the recipient email address
                CommonActions.getUserEmail(receiverId);

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
                        saveChallenge();
                    }
                }, 2000); // Delay 2 seconds
                break;
        }
    }
    private void saveChallenge(){

        creatorId = LoginActivity.username;

        //final String receiverId = editTextReceiver.getText().toString().trim();
        //final String difficulty = editTextDifficulty.getText().toString().trim();
        final String type;

        /*
         * It would be better to hard-code the type of a challenge based on the user type.
         *       userType == "Lecturer" -> type is "Quiz"
         *       userType == "Student" -> type is "Challenge"
         */
        CommonActions.getUserRole(LoginActivity.username);
        if (CommonActions.userType.equals("Lecturer")) {
            type = "Quiz";
        } else {
            type = "Challenge";
        }

        if (CommonActions.userList.contains(receiverId)) {

            Challenge challenge = new Challenge(creatorId, receiverId, module, topic, difficulty, type, 0, false);
            Firestore.db.collection("challenges").add(challenge)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getActivity(), "Challenge has been Added", Toast.LENGTH_LONG).show();
                            sendEmail();
                            getActivity().finish();
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Invalid challengee name. Please enter correct name!", Toast.LENGTH_LONG).show();
        }
    }


    private void sendEmail() {
        Log.d(TAG, "Receiver email address => " + CommonActions.userEmail);
        String email = CommonActions.userEmail;
        String subject = "You have been challenged by " + creatorId;
        String message = "Challenge Details\nChallenger : " + creatorId + "\nModule : " + module + "\nTopic : " + topic + "\nDifficulty : " + difficulty;

        //Creating SendMail object
        SendMail sm = new SendMail(getActivity(), email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }
}
