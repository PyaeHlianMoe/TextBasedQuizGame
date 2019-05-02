package com.example.quizzer.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizzer.Activities.HomeActivity;
import com.example.quizzer.R;

/*
This fragment will be used when user select the difficulty
 */

public class DifficultyFragment extends Fragment implements
        View.OnClickListener
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_difficulty, container, false);

        view.findViewById(R.id.btn_Easy).setOnClickListener(this);
        view.findViewById(R.id.btn_Medium).setOnClickListener(this);
        view.findViewById(R.id.btn_Hard).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bundle.putString("MODULE", bundle.getString("MODULE"));
            bundle.putString("TOPIC", bundle.getString("TOPIC"));

        }

        if (v.getId() == R.id.btn_Easy) {
            bundle.putString("DIFFICULTY", "Easy");
        } else if (v.getId() == R.id.btn_Medium) {
            bundle.putString("DIFFICULTY", "Medium");
        } else {
            bundle.putString("DIFFICULTY", "Hard");
        }


        if (LecturerFragment.actionType != null && !LecturerFragment.actionType.isEmpty() && LecturerFragment.actionType.equals("AddQuestion")) {
            bundle.putString("CHALLENGEE", bundle.getString("CHALLENGEE"));
            AddQuestionFragment addQuestionFragment = new AddQuestionFragment();
            addQuestionFragment.setArguments(bundle);
            HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, addQuestionFragment).addToBackStack(null).commit();
        } else {
            CreateChallengeFragment createChallengeFragment = new CreateChallengeFragment();
            createChallengeFragment.setArguments(bundle);
            HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, createChallengeFragment).addToBackStack(null).commit();
        }

    }
}
