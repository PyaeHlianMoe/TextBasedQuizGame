package com.example.quizzer.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.quizzer.Activities.HomeActivity;
import com.example.quizzer.Interfaces.UserActionInterface;
import com.example.quizzer.R;

/*
This fragment will be called if the login user type is the lecturer.
This fragment will show all the features/ functions of the lecturer.
 */

public class LecturerFragment extends Fragment implements
        View.OnClickListener,
        UserActionInterface
{
    public static String actionType;
    private Button btnCreateQuiz, btnModifyQuiz, btnViewReport, btnAddQuestion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lecturer, container, false);

        btnCreateQuiz = view.findViewById(R.id.btn_CreateQuiz);
        btnModifyQuiz = view.findViewById(R.id.btn_ModifyQuiz);
        //btnViewReport = view.findViewById(R.id.btn_ViewReport);
        btnAddQuestion = view.findViewById(R.id.btn_AddQuestion);

        btnCreateQuiz.setOnClickListener(this);
        btnModifyQuiz.setOnClickListener(this);
        //btnViewReport.setOnClickListener(this);
        btnAddQuestion.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_CreateQuiz) {
            createQuizOrChallenge();
        } else if (v.getId() == R.id.btn_ModifyQuiz) {
            // TODO: do action to delete quiz
            modifyQuiz();
        }
//        else if (v.getId() == R.id.btn_ViewReport) {
//            // TODO: go to the analysis report
//            viewReport();
//        }
        else {
            addQuestion();
        }
    }

    @Override
    public void createQuizOrChallenge() {
        HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, new ModuleListFragment(), null).addToBackStack(null).commit();
    }

    private void modifyQuiz() {
        HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, new ViewChallengeFragment(), null).addToBackStack(null).commit();
    }

    private void viewReport() {

    }

    private void addQuestion() {
        actionType = "AddQuestion";
        HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, new ModuleListFragment(), null).addToBackStack(null).commit();
    }
}
