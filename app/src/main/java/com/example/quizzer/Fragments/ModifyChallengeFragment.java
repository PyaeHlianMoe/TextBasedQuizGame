package com.example.quizzer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizzer.Activities.HomeActivity;
import com.example.quizzer.CommonActions;
import com.example.quizzer.Firestore;
import com.example.quizzer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

/*
This fragment will be called if the user want to modify a challenge
 */

public class ModifyChallengeFragment extends Fragment implements
        View.OnClickListener
{
    private static final String TAG = "ModifyChallengeFragment";
    private EditText editTextModule, editTextTopic, editTextReceiver, editTextDifficulty;
    private String challengeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modify_challenge, container, false);

        editTextModule = view.findViewById(R.id.edittext_module);
        editTextTopic = view.findViewById(R.id.edittext_topic);
        editTextReceiver = view.findViewById(R.id.edittext_receiver);
        editTextDifficulty = view.findViewById(R.id.edittext_difficulty);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            challengeId = bundle.getString("ID");
            editTextModule.setText(bundle.getString("MODULE"));
            editTextTopic.setText(bundle.getString("TOPIC"));
            editTextReceiver.setText(bundle.getString("RECEIVER"));
            editTextDifficulty.setText(bundle.getString("DIFFICULTY"));
        }

        view.findViewById(R.id.btn_update).setOnClickListener(this);
        view.findViewById(R.id.btn_delete).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_delete) {
            deleteQuiz();
        } else {
            if (!hasValidationErrors(editTextModule.getText().toString(), editTextTopic.getText().toString(), editTextReceiver.getText().toString(), editTextDifficulty.getText().toString())) {
                if (CommonActions.moduleList.contains(editTextModule.getText().toString()) && CommonActions.topicList.contains(editTextTopic.getText().toString())) {
                    if (CommonActions.userList.contains(editTextReceiver.getText().toString())) {
                        updateQuiz();
                    } else {
                        Toast.makeText(getActivity(), "Invalid challengee name. Please enter correct name!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Invalid Module or Topic. Please check!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void deleteQuiz() {
        Firestore.db.collection("challenges").document(challengeId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Quiz has been deleted successfully", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Unable to delete the quiz. Please check the log", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    }
                });
    }

    private void updateQuiz() {
        DocumentReference documentReference = Firestore.db.collection("challenges").document(challengeId);
        documentReference.update("module", editTextModule.getText().toString());
        documentReference.update("topic", editTextTopic.getText().toString());
        documentReference.update("receiverId", editTextReceiver.getText().toString());
        documentReference.update("difficulty", editTextDifficulty.getText().toString());

        Toast.makeText(getActivity(), "Quiz has been Updated", Toast.LENGTH_LONG).show();
        getActivity().finish();
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }

    private boolean hasValidationErrors(String module, String topic, String receiver, String difficulty) {

        if (module.isEmpty()) {
            editTextModule.setError("Module name is required");
            editTextModule.requestFocus();
            return true;
        }

        if (topic.isEmpty()) {
            editTextTopic.setError("Topic is required");
            editTextTopic.requestFocus();
            return true;
        }

        if (receiver.isEmpty()) {
            editTextReceiver.setError("Challengee name is required");
            editTextReceiver.requestFocus();
            return true;
        }

        if (difficulty.isEmpty()) {
            editTextDifficulty.setError("Difficulty level is required");
            editTextDifficulty.requestFocus();
            return true;
        }
        return false;
    }
}
