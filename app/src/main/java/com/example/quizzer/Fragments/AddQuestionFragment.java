package com.example.quizzer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizzer.Activities.HomeActivity;
import com.example.quizzer.CommonActions;
import com.example.quizzer.Firestore;
import com.example.quizzer.R;

/*
This fragment will be called when a lecturer wants to add a new question
 */

public class AddQuestionFragment extends Fragment implements
        View.OnClickListener
{
    private static final String TAG = "AddQuestionFragment";
    private EditText editTextQuestion, editTextOption1, editTextOption2, editTextOption3, editTextOption4, editTextCorrectAns;
    private String module, topic, question, option1, option2, option3, option4, correctAns, difficulty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_question, container, false);

        editTextQuestion = view.findViewById(R.id.edittext_question);
        editTextOption1 = view.findViewById(R.id.edittext_option1);
        editTextOption2 = view.findViewById(R.id.edittext_option2);
        editTextOption3 = view.findViewById(R.id.edittext_option3);
        editTextOption4 = view.findViewById(R.id.edittext_option4);
        editTextCorrectAns = view.findViewById(R.id.edittext_correctAnswer);

        view.findViewById(R.id.btn_ToAddQuestion).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ToAddQuestion) {
            getInfo();

            Log.d(TAG, "Testing here " + CommonActions.moduleList.get(0));
            Log.d(TAG, topic);

            if (!hasValidationErrors(question, option1, option2, option3, option4, correctAns)) {
                if (CommonActions.moduleList.contains(module) && CommonActions.topicList.contains(topic)) {
                    Firestore firestore = new Firestore();
                    firestore.addNewQuestion(difficulty, correctAns, option1, option2, option3, option4, question, module, topic);
                    Toast.makeText(getActivity(), "New question has been added", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Invalid Module or Topic. Please check!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void getInfo() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            module = bundle.getString("MODULE");
            topic = bundle.getString("TOPIC");
            difficulty = bundle.getString("DIFFICULTY");
        }
        question = editTextQuestion.getText().toString().trim();
        option1 = editTextOption1.getText().toString().trim();
        option2 = editTextOption2.getText().toString().trim();
        option3 = editTextOption3.getText().toString().trim();
        option4 = editTextOption4.getText().toString().trim();
        correctAns = editTextCorrectAns.getText().toString().trim();
    }

    private boolean hasValidationErrors(String question, String option1, String option2, String option3, String option4, String correctAns) {

        if (question.isEmpty()) {
            editTextQuestion.setError("Question required");
            editTextQuestion.requestFocus();
            return true;
        }

        if (option1.isEmpty()) {
            editTextOption1.setError("Option 1 required");
            editTextOption1.requestFocus();
            return true;
        }

        if (option2.isEmpty()) {
            editTextOption2.setError("Option 2 required");
            editTextOption2.requestFocus();
            return true;
        }

        if (option3.isEmpty()) {
            editTextOption3.setError("Option 3 required");
            editTextOption3.requestFocus();
            return true;
        }

        if (option4.isEmpty()) {
            editTextOption4.setError("Option 4 required");
            editTextOption4.requestFocus();
            return true;
        }

        if (correctAns.isEmpty()) {
            editTextCorrectAns.setError("Correct answer required");
            editTextCorrectAns.requestFocus();
            return true;
        }
        return false;
    }
}
