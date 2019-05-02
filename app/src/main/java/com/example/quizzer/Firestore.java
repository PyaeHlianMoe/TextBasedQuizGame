package com.example.quizzer;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.quizzer.Interfaces.DBInterface;
import com.example.quizzer.Interfaces.FirestoreInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
This class will be used for Firestore related activities
 */

public class Firestore implements DBInterface {

    private static final String TAG = "Firestore";
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String personId;

    @Override
    public void addNewUser(final String Name, String Email){

        final Map<String, Object> user = new HashMap<>();
        personId = mAuth.getCurrentUser().getUid();

        user.put("userId", personId);
        user.put("userName", Name);
        user.put("email", Email);
        // default account type is student
        user.put("type", "Student");

        db.collection("users").
                whereEqualTo("id",user.get("id"))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot.isEmpty()){
                        db.collection("users")
                                .document(Name)
                                .set(user);
                    }
                }
            }
        });
    }

    @Override
    public void addNewQuestion(String level, String answer, String option1, String option2, String option3, String option4, String title, String module, String topic){

        final  Map<String, Object> question = new HashMap<>();

        question.put("difficulty",level);
        question.put("correct_answer", answer);
        question.put("option1", option1);
        question.put("option2", option2);
        question.put("option3", option3);
        question.put("option4", option4);
        question.put("question", title);
        question.put("topic", topic);
        question.put("module", module);

        db.collection("questions").
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    final int count = querySnapshot.size() + 1;
//                    final DocumentReference documentReference = db.collection("users").document();
                    db.collection("questions")
                            .document(String.valueOf(count))
                            .set(question);
                }
            }
        });
    }

    @Override
    public void updateQuestion(final String id, final String level, final String answer, final String option1, final String option2, final String option3, final String option4, final String title){

        db = FirebaseFirestore.getInstance();

        db.collection("questions")
                .whereEqualTo("questionId",id)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                DocumentReference documentReference = db.collection("questions").document(id);
                documentReference.update("difficulty",level);
                documentReference.update("correctAnswer",answer);
                documentReference.update("option1", option1);
                documentReference.update("option2", option2);
                documentReference.update("option3", option3);
                documentReference.update("option4", option4);
                documentReference.update("Question", title);
            }
        });
    }

    @Override
    public void queryUserRole(String username,final FirestoreInterface myCallBack) {
        db.collection("users").document(username)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> values = new ArrayList<>();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    values.add(document.getString("type"));
                    Log.d(TAG, "User role => " + document.getString("type"));
                }
                else {
                    Log.d(TAG, "Something went wrong => " + task.getException());

                }
                myCallBack.onCallback(values);
            }
        });
    }

    @Override
    public void queryUserEmail(String username, final FirestoreInterface myCallBack) {
        db.collection("users").document(username)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> values = new ArrayList<>();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    values.add(document.getString("email"));
                    Log.d(TAG, "User email => " + document.getString("email"));
                } else {
                    Log.d(TAG, "Something went wrong => " + task.getException());
                }
                myCallBack.onCallback(values);
            }
        });
    }
}
