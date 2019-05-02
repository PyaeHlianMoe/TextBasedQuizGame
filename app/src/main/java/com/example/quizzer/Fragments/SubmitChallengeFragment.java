package com.example.quizzer.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizzer.Activities.PlayChallengeActivity;
import com.example.quizzer.Model.Challenge;
import com.example.quizzer.Firestore;
import com.example.quizzer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/*
This fragment will be called when a user submit a challenge
 */

public class SubmitChallengeFragment extends Fragment implements
        View.OnClickListener
{
    private static final String TAG = "SubmitChallengeFragment";
    Challenge c;
    Integer score;
    //private String creatorId, module, topic;
    TextView tv_totalMark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_submit_challenge, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            c = (Challenge)bundle.getSerializable("CHALLENGE_OBJECT");
            score = bundle.getInt("SCORE");
        }

        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        view.findViewById(R.id.btn_exit).setOnClickListener(this);

        tv_totalMark = (TextView)view.findViewById(R.id.tv_totalMark);
        tv_totalMark.setText("Your score is " + String.valueOf(score));

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                submitChallenge();
                break;
            case R.id.btn_exit:
                getActivity().finish();
                break;
        }
    }


    private void submitChallenge() {
        c.setMark(score);
        c.setIsComplete(true);
        Firestore.db.collection("challenges").document(c.getId())
                .set(c)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Score Updated", Toast.LENGTH_LONG).show();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("CHALLENGE_OBJECT", c); //test
                        bundle.putInt("SCORE",score);
                        ShareChallengeFragment shareChallengeFragment = new ShareChallengeFragment();
                        shareChallengeFragment.setArguments(bundle);
                        PlayChallengeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, shareChallengeFragment).commit();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }




}
