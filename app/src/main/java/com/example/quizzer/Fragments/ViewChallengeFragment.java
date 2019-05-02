package com.example.quizzer.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.quizzer.Activities.LoginActivity;
import com.example.quizzer.Adapter.ChallengeAdapter;
import com.example.quizzer.Model.Challenge;
import com.example.quizzer.CommonActions;
import com.example.quizzer.Firestore;
import com.example.quizzer.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/*
This fragment will be called when a user want to view the challenges
 */

public class ViewChallengeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChallengeAdapter adapter;
    private List<Challenge> challengeList;
    private ProgressBar progressBar;
    String userId;
    private String id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_challenge, container, false);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userId = bundle.getString("USER_ID");
        }

        //userId= getIntent().getStringExtra("USER_ID"); //pass from loginActivity

        progressBar = view.findViewById(R.id.progressbar);

        recyclerView = view.findViewById(R.id.recyclerview_challenges);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        challengeList = new ArrayList<>();
        adapter = new ChallengeAdapter(getContext(), challengeList, userId);

        recyclerView.setAdapter(adapter);

        // Field to query from Firebase will be changed based on the log user type
        //CommonActions.getUserRole(userId);
        if (CommonActions.userType.equals("Lecturer")) {
            id = "creatorId";
        } else {
            id = "receiverId";
        }

        Firestore.db.collection("challenges")
                .whereEqualTo(id, LoginActivity.username).whereEqualTo("isComplete", false)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        progressBar.setVisibility(View.GONE);

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

        return view;
    }
}