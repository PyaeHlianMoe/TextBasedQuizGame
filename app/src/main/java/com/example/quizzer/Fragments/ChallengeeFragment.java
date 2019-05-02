package com.example.quizzer.Fragments;

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
import com.example.quizzer.R;

/*
This fragment will be called when a user needs to select who they want to challenge to
 */

public class ChallengeeFragment extends Fragment implements
        View.OnClickListener
{

    private static final String TAG = "ChallengeeFragment";
    private EditText editTextReceiver;
    private String module, topic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_challengee, container, false);

        editTextReceiver = view.findViewById(R.id.edittext_receiver);
        view.findViewById(R.id.btn_Challengee).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Challengee) {
            getChallengee(v);
        }
    }

    private void getChallengee(View v) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            module = bundle.getString("MODULE");
            topic = bundle.getString("TOPIC");
        } else {
            Log.d(TAG, "Bundle is empty");
        }

        if (!hasValidationErrors(editTextReceiver.getText().toString().trim())) {
            if (CommonActions.userList.contains(editTextReceiver.getText().toString().trim())) {
                bundle.putString("MODULE", module);
                bundle.putString("TOPIC", topic);
                bundle.putString("CHALLENGEE", editTextReceiver.getText().toString().trim());
                DifficultyFragment difficultyFragment = new DifficultyFragment();
                difficultyFragment.setArguments(bundle);
                HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, difficultyFragment).addToBackStack(null).commit();
            } else {
                Toast.makeText(getActivity(), "Invalid challengee name! Please enter a correct name", Toast.LENGTH_LONG).show();
            }

        }
    }

    private boolean hasValidationErrors(String receiver) {

        if (receiver.isEmpty()) {
            editTextReceiver.setError("Name required");
            editTextReceiver.requestFocus();
            return true;
        }
        return false;
    }
}
