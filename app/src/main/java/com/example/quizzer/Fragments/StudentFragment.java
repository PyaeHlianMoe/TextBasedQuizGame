package com.example.quizzer.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.quizzer.Activities.HomeActivity;
import com.example.quizzer.Activities.LoginActivity;
import com.example.quizzer.Adapter.CompleteChallengeAdapter;
import com.example.quizzer.Firestore;
import com.example.quizzer.Interfaces.UserActionInterface;
import com.example.quizzer.Model.Challenge;
import com.example.quizzer.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/*
This fragment will be called if the login user type is the student.
This fragment will show all the features/ functions of the student.
 */

public class StudentFragment extends Fragment implements
            View.OnClickListener,
            UserActionInterface
{
    private static final String TAG = "StudentFragment";
    private Button createButton, viewChallengesButton, viewRankingButton;
    private RecyclerView recyclerView;
    private CompleteChallengeAdapter adapter;
    private List<Challenge> challengeList;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student, container, false);

        createButton = view.findViewById(R.id.btn_createChallenge);
        viewChallengesButton = view.findViewById(R.id.btn_viewChallenges);
        viewRankingButton = view.findViewById(R.id.btn_viewRanking);

        createButton.setOnClickListener(this);
        viewChallengesButton.setOnClickListener(this);
        viewRankingButton.setOnClickListener(this);

        getHistory(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_createChallenge) {
            createQuizOrChallenge();
        } else if (v.getId() == R.id.btn_viewChallenges) {
            viewChallenge();
        } else if (v.getId() == R.id.btn_viewRanking){
            viewRanking();
        }
    }

    @Override
    public void createQuizOrChallenge() {
        HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, new ModuleListFragment(), null).addToBackStack(null).commit();
    }

    public void viewChallenge() {
        Bundle bundle = new Bundle();
        bundle.putString("USER_ID", LoginActivity.username);
        ViewChallengeFragment viewChallengeFragment = new ViewChallengeFragment();
        viewChallengeFragment.setArguments(bundle);
        HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, viewChallengeFragment).addToBackStack(null).commit();
    }

    public void viewRanking() {
        HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, new ScoreBoardFragment(), null).addToBackStack(null).commit();
    }

    public void getHistory(View view) {
        recyclerView = view.findViewById(R.id.recyclerview_challenges);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        challengeList = new ArrayList<>();
        adapter = new CompleteChallengeAdapter(getContext(), challengeList, LoginActivity.username );

        recyclerView.setAdapter(adapter);

        Firestore.db.collection("challenges")
                .whereEqualTo("receiverId", LoginActivity.username).whereEqualTo("isComplete", true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //progressBar.setVisibility(View.GONE);
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d : list){
                                Challenge c = d.toObject(Challenge.class);
                                c.setId(d.getId());
                                challengeList.add(c);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
