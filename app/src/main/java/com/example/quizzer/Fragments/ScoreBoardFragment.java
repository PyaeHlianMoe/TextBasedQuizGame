package com.example.quizzer.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizzer.Activities.LoginActivity;
import com.example.quizzer.Firestore;
import com.example.quizzer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
This framgment will be called when a user wants to see a ranking
 */

public class ScoreBoardFragment extends Fragment {
    private static final String TAG = "ScoreBoardFragment";

    private Map<String, Integer> map = new HashMap<String, Integer>();
    private ArrayList<Integer> scoreList = new ArrayList<>();
    private TextView textViewName, textViewScore, textViewFirstRankName, textViewFirstRankScore,textViewSecondRankName, textViewSecondRankScore, textViewThirdRankName, textViewThirdRankScore;
    private RelativeLayout layoutRank1, layoutRank2, layoutRank3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_score_board, container, false);

        getTop3();
        getLoginUserTotalScore();

        Toast.makeText(getActivity(), "Getting user information..." , Toast.LENGTH_LONG).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int totalScore = 0;

                for (int i = 0; i < scoreList.size(); i++) {
                    totalScore += scoreList.get(i);
                }

                initialiseResource(view);

                textViewName.setText(LoginActivity.username);
                textViewScore.setText("Score : " + totalScore);

                if (map.size() == 1) {
                    textViewFirstRankName.setText((map.keySet().toArray()[0]).toString());
                    textViewFirstRankScore.setText(map.get((map.keySet().toArray()[0]).toString()));

                    layoutRank2.setVisibility(view.GONE);
                    layoutRank3.setVisibility(view.GONE);

                } else if (map.size() == 2){
                    textViewFirstRankName.setText((map.keySet().toArray()[0]).toString());
                    textViewFirstRankScore.setText("Score : " + (map.get( (map.keySet().toArray())[0] )).toString());

                    textViewSecondRankName.setText((map.keySet().toArray()[1]).toString());
                    textViewSecondRankScore.setText("Score : " + (map.get( (map.keySet().toArray())[1] )).toString());

                    layoutRank3.setVisibility(view.GONE);
                } else if (map.size() == 3) {
                    textViewFirstRankName.setText((map.keySet().toArray()[0]).toString());
                    textViewFirstRankScore.setText("Score : " + (map.get( (map.keySet().toArray())[0] )).toString());

                    textViewSecondRankName.setText((map.keySet().toArray()[1]).toString());
                    textViewSecondRankScore.setText("Score : " + (map.get( (map.keySet().toArray())[1] )).toString());

                    textViewThirdRankName.setText((map.keySet().toArray()[2]).toString());
                    textViewThirdRankScore.setText("Score : " + (map.get( (map.keySet().toArray())[2] )).toString());
                } else {
                    Toast.makeText(getActivity(), "No top player at this moment", Toast.LENGTH_SHORT).show();
                }

            }
        }, 1500); // Delay 1.5 seconds

        return view;
    }

    private void initialiseResource(View view) {
        textViewName = view.findViewById(R.id.text_name);
        textViewScore = view.findViewById(R.id.text_score);
        textViewFirstRankName = view.findViewById(R.id.text_user_name_1);
        textViewFirstRankScore = view.findViewById(R.id.text_total_mark_1);
        textViewSecondRankName = view.findViewById(R.id.text_user_name_2);
        textViewSecondRankScore = view.findViewById(R.id.text_total_mark_2);
        textViewThirdRankName = view.findViewById(R.id.text_user_name_3);
        textViewThirdRankScore = view.findViewById(R.id.text_total_mark_3);

        layoutRank1 = view.findViewById(R.id.layout_ranking_1);
        layoutRank2 = view.findViewById(R.id.layout_ranking_2);
        layoutRank3 = view.findViewById(R.id.layout_ranking_3);
    }

    private void getTop3() {
        Firestore.db.collection("challenges").whereEqualTo("isComplete", true)
                .orderBy("mark", Query.Direction.DESCENDING).limit(3)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document :task.getResult()) {
                        map.put(document.getString("receiverId"), document.getLong("mark").intValue());
                        Log.d(TAG, "Receiver name => " + document.getString("receiverId") + " Mark => " + document.getLong("mark").intValue());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void getLoginUserTotalScore() {
        Firestore.db.collection("challenges").whereEqualTo("receiverId", LoginActivity.username)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        scoreList.add(document.getLong("mark").intValue());
                    }
                }
            }
        });
    }
}
