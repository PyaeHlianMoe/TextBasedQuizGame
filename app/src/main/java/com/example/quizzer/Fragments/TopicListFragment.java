package com.example.quizzer.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.quizzer.Activities.HomeActivity;
import com.example.quizzer.Firestore;
import com.example.quizzer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/*
This fragment will be called when a user want to choose a topic
 */

public class TopicListFragment extends Fragment {

    private static final String TAG = "TopicListFragment";
    public static final String DATA_RECEIVE = "data_receive";
    private ArrayList<String> topicList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

        topicList.add("Topic List");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Log.d(TAG, "Bundle is not empty");
            Log.d(TAG, bundle.getString("MODULE"));
            queryTopicList(bundle.getString("MODULE"));
            initialiseUI(bundle.getString("MODULE"), view);
        } else {
            Log.d(TAG, "Bundle is empty");
        }
        return view;
    }

    private void initialiseUI(final String module, View view) {
        Spinner spinner = view.findViewById(R.id.spinner_TopicList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, topicList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Topic List")) {
                    // do nothing

                } else {
                    // on selected a spinner item
                    Bundle bundle = new Bundle();
                    bundle.putString("MODULE", module);
                    bundle.putString("TOPIC", parent.getItemAtPosition(position).toString());

                    if (LecturerFragment.actionType != null && !LecturerFragment.actionType.isEmpty() && LecturerFragment.actionType.equals("AddQuestion")) {
                        DifficultyFragment difficultyFragment = new DifficultyFragment();
                        difficultyFragment.setArguments(bundle);
                        HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, difficultyFragment).addToBackStack(null).commit();
                    } else {
                        ChallengeeFragment challengeeAndDifficultyFragment = new ChallengeeFragment();
                        challengeeAndDifficultyFragment.setArguments(bundle);
                        HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, challengeeAndDifficultyFragment).addToBackStack(null).commit();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void queryTopicList(String module) {
        // TODO: get the list of the document under "questions" from the Firestore and pass to the fragment
        Firestore.db.collection("topics")
                .whereEqualTo("module", module)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId());
                        topicList.add(document.getId());
                    }
                } else {
                    Log.d(TAG, "Failed to retrieve");
                }
            }
        });
    }
}
