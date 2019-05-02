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
This fragment will be called when a user needs to select a module
 */

public class ModuleListFragment extends Fragment {

    private static final String TAG = "ModuleListFragment";
    private ArrayList<String> moduleList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_module_list, container, false);

        moduleList.add("Module List");

        queryModuleList();
        initialiseUI(view);

        return view;
    }

    private void initialiseUI(View view) {
        Spinner spinner = view.findViewById(R.id.spinner_ModuleList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, moduleList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Module List")) {
                    // do nothing

                } else {
                    // on selected a spinner item
                    Bundle bundle = new Bundle();
                    bundle.putString("MODULE", parent.getItemAtPosition(position).toString());
                    TopicListFragment topicListFragment = new TopicListFragment();
                    topicListFragment.setArguments(bundle);
                    HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, topicListFragment).addToBackStack(null).commit();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void queryModuleList() {
        // TODO: get the list of the document under "questions" from the firestore and pass to the fragment
        Firestore.db.collection("modules")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId());
                                moduleList.add(document.getId());
                            }
                        } else {
                            Log.d(TAG, "Failed to retrieve");
                        }
                    }
                });
    }
}
